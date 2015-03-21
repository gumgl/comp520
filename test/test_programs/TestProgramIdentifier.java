package test_programs;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;

import golite.Compiler;

public class TestProgramIdentifier extends SimpleFileVisitor<Path> {
	protected Collection<Object[]> identifiedParams = new ArrayList<Object[]>();

	protected int lastCheckedDirectory = -1;
	protected int directoryLevel = 0;

	protected boolean isValid = true;
	protected int validityDeterminingLevel = -1;

	protected Compiler.CompilationStage executedStage = null;
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

		identifiedParams.add(new Object[] {
				file.toString(), isValid, executedStage});
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {

		directoryLevel++;
		return FileVisitResult.CONTINUE;
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
			executedStage = null;
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
			checkPathSegment(directoryLevel-i, uncheckedDirs.get(i));
		}
	}

	protected void checkPathSegment(int level, Path dir) {
		switch (dir.getFileName().toString()) {
		case "valid":
			setValidity(level, dir, true);
			break;

		case "invalid":
			setValidity(level, dir, false);
			break;

		case "lexer":
		case "scanner":
			setStage(level, dir, Compiler.CompilationStage.LEXING);
			break;

		case "parser":
		case "syntax":
			setStage(level, dir, Compiler.CompilationStage.PARSING);
			break;

		case "weeding":
			setStage(level, dir, Compiler.CompilationStage.WEEDING);
			break;

		case "type":
		case "types":
			setStage(level, dir, Compiler.CompilationStage.TYPE_CHECKING);
			break;

		case "code_gen":
		case "semantic":
			setStage(level, dir, Compiler.CompilationStage.CODE_GEN);
			break;
		}
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
			executedStage = stage;
		}
	}
}
