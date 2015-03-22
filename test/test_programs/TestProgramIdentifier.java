package test_programs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import golite.Compiler;

public class TestProgramIdentifier extends SimpleFileVisitor<Path> {
	protected Collection<Object[]> identifiedParams = new ArrayList<Object[]>();

	protected Hashtable<String,Compiler.CompilationStage> stageConfig =
			new Hashtable<String,Compiler.CompilationStage>();

	protected Path configFilename = FileSystems.getDefault().getPath("test_configuration.txt");

	protected int lastCheckedDirectory = -1;
	protected int directoryLevel = 0;

	protected boolean isValid = true;
	protected int validityDeterminingLevel = -1;

	protected Compiler.CompilationStage directoryStage = null;
	protected int stageDeterminingLevel = -1;

	public static Collection<Object[]> findPrograms(String pathRoot) throws IOException {
		Path root = FileSystems.getDefault().getPath(pathRoot);
		TestProgramIdentifier programLocator = new TestProgramIdentifier();
		Files.walkFileTree(root, programLocator);
		return programLocator.identifiedParams;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
		if (!(attr.isRegularFile() && file.toString().endsWith(".go")))
			return FileVisitResult.CONTINUE;

		if (lastCheckedDirectory != directoryLevel)
			checkDirectories(file.getParent());

		Compiler.CompilationStage pathStage = stageConfig.get(file.normalize().toString());

		if (pathStage == null)
			pathStage = directoryStage;

		identifiedParams.add(new Object[] {
				file.toString(), isValid, pathStage});

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {

		directoryLevel++;

		File localConfig = dir.resolve(configFilename).toFile();

		if (localConfig.exists()) {
			loadConfiguration(dir, localConfig);
		}

		return FileVisitResult.CONTINUE;
	}

	public void loadConfiguration(Path dir, File configFile) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(configFile));
			for (String line=reader.readLine(); line != null; line=reader.readLine()) {
				if (line.length() == 0)
					continue;

				String[] components = line.split("\\s+");
				if (components.length != 2) {
					System.err.printf("Could not interpret config line: %s\n", line);
					continue;
				}

				Compiler.CompilationStage configStage = getCompilationStage(components[1]);
				if (configStage == null) {
					System.err.printf("Unrecognized execution stage %s\n", components[1]);
					continue;
				}

				String configTarget =
						dir.resolve(dir.getFileSystem().getPath(components[0]))
							.normalize()
							.toString();

				if (stageConfig.contains(configTarget)) {
					System.err.printf("Ignoring duplicate configuration for %s\n", configTarget);
					continue;
				}

				stageConfig.put(configTarget, configStage);
			}
		} catch (Exception e) {
			System.err.printf("Failed to read configuration at %s:\n", configFile.getPath());
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {

		if (lastCheckedDirectory == directoryLevel)
			lastCheckedDirectory--;

		if (directoryLevel == validityDeterminingLevel) {
			validityDeterminingLevel = -1;
			isValid = true;
		}

		if (directoryLevel == stageDeterminingLevel) {
			stageDeterminingLevel = -1;
			directoryStage = null;
		}

		directoryLevel--;
		return FileVisitResult.CONTINUE;
	}

	protected void checkDirectories(Path dir) {
		int levelsToCheck = directoryLevel;

		if (lastCheckedDirectory > 0)
			levelsToCheck -= lastCheckedDirectory;

		lastCheckedDirectory = directoryLevel;

		ArrayList<Path> uncheckedDirs = new ArrayList<Path>(levelsToCheck);
		for (int i=0; i < levelsToCheck; i++) {
			uncheckedDirs.add(dir);
			dir = dir.getParent();
		}
		for (int i=levelsToCheck-1; i >= 0; i--) {
			checkFinalPathSegment(directoryLevel-i, uncheckedDirs.get(i));
		}
	}

	protected void checkFinalPathSegment(int level, Path dir) {
		String segment = dir.getFileName().toString();

		if (segment.equals("valid")) {
			setValidity(level, dir, true);
		} else if (segment.equals("invalid")) {
			setValidity(level, dir, false);
		} else {
			Compiler.CompilationStage stage = getCompilationStage(segment);
			if (stage != null)
				setStage(level, dir, stage);
		}
	}

	public Compiler.CompilationStage getCompilationStage(String stage) {
		switch (stage) {
		case "lexer":
		case "scanner":
			return Compiler.CompilationStage.LEXING;

		case "parser":
		case "syntax":
			return Compiler.CompilationStage.PARSING;

		case "weeding":
			return Compiler.CompilationStage.WEEDING;

		case "type":
		case "types":
			return Compiler.CompilationStage.TYPE_CHECKING;

		case "code_gen":
		case "semantic":
			return Compiler.CompilationStage.CODE_GEN;
		}

		return null;
	}

	protected void setValidity(int level, Path dir, boolean validity) {
		if (validityDeterminingLevel >= 0 && validityDeterminingLevel != level) {
			System.err.printf("Ignoring duplicate validity indicator at %s\n", dir.toString());
		} else {
			validityDeterminingLevel = level;
			this.isValid = validity;
		}
	}

	protected void setStage(int level, Path dir, Compiler.CompilationStage stage) {
		if (stageDeterminingLevel >= 0 && stageDeterminingLevel != level) {
			System.err.printf("Ignoring duplicate stage indicator at %s\n", dir.toString());
		} else {
			stageDeterminingLevel = level;
			directoryStage = stage;
		}
	}
}
