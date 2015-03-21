package golite;

import java.io.FileNotFoundException;

public class Main {
	public static final int EXIT_SUCCESS = 0;
	public static final int EXIT_INVALID_SOURCE = 1;
	public static final int EXIT_INTERNAL_ERROR = 2;

	public static void main(String[] args) {
		CLIOptions options = null;

		try {
			options = getCLIOptions(args);
		} catch (IllegalArgumentException e) {
			System.err.print("Error: "+e.getMessage()+"\n\n");
			printUsage();
			System.exit(EXIT_INTERNAL_ERROR);
		}

		try {
			Compiler.processSource(options);
		} catch (golite.lexer.LexerException e) {
			handleSourceCodeError("Lexer", e);
		} catch (golite.parser.ParserException e) {
			handleSourceCodeError("Parser", e);
		} catch (GoLiteWeedingException e) {
			handleSourceCodeError("Weeding", e);
		} catch (GoLiteTypeException e) {
			handleSourceCodeError("Type", e);
		} catch (FileNotFoundException e) {
			handleSourceCodeError("File \"" + options.fullPath + "\" not found.");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			System.exit(EXIT_INTERNAL_ERROR);
		}
	}

	/* Command-line interface */

	/**
	 * Parse the command-line arguments
	 *
	 * Corner case: this code assumes that the filename doesn't start
	 * with a `-` */
	public static CLIOptions getCLIOptions(String[] args) {
		CLIOptions options = new CLIOptions();
		int i;

		// Read options
		for (i=0; i < args.length && args[i].startsWith("-"); i++) {
			switch (args[i]) {
			case "-dumpsymtab":
				options.dumpSymbolTable = CLIOptions.ScopeInfoDumpLevel.LAST_FRAME;
				break;
			case "-dumpsymtaball":
				options.dumpSymbolTable = CLIOptions.ScopeInfoDumpLevel.ALL;
				break;
			case "-pprint":
				options.prettyPrint = true;
				break;
			case "-pptype":
				options.prettyPrintTyped = true;
				break;
			case "-dumptoks":
				options.dumpToks = true;
				break;
			case "-displayast":
				options.displayAST = true;
				break;
			default:
				throw new IllegalArgumentException("Unexpected option "+args[i]);
			}
		}

		if (i == args.length) {
			throw new IllegalArgumentException("No input file specified");
		}

		if (i != args.length - 1) {
			throw new IllegalArgumentException("Exactly one input file should be specified");
		}

		// Read filename
		options.fullPath = args[i];
		if (options.fullPath.endsWith(".go")) {
			options.basePath = options.fullPath.substring(0, options.fullPath.length() - 3);
		} else {
			options.basePath = options.fullPath;
		}

		return options;
	}

	public static void printUsage() {
		System.err.println(
				"Usage:\n"
				+ "    golite [options] inputFile\n\n"
				+ "Options:\n"
				+ "    Where [filename] is the base component of inputFile...\n"
				+ "    -pprint: Write a pretty printed file to [filename].pretty.go\n"
				+ "    -pptype: Write a pretty printed file with type information to\n"
				+ "        [filename].pptype.go\n"
				+ "    -dumpsymtab: Dump the topmost frame of each scope to [filename].symtab\n"
				+ "    -dumpsymtaball: Dump all frames to [filename].symtab\n"
				+ "    -dumptoks: Write a tokenized representation of the input to\n"
				+ "        [filename].tokens.html\n"
				+ "    -displayast: Display the abstract syntax tree in a graphical\n"
				+ "        interface"
		);
	}

	public static void handleSourceCodeError(String errorType, Exception e) {
		System.err.println(errorType+" error at "+e.getMessage());
		System.exit(EXIT_INVALID_SOURCE);
	}

	public static void handleSourceCodeError(String message) {
		System.err.println("Error: "+message);
		System.exit(EXIT_INVALID_SOURCE);
	}
}

/** Container class for command-line interface options */
class CLIOptions {
	// Debugging: dumping scope info
	public static enum ScopeInfoDumpLevel {
		NOTHING,
		LAST_FRAME,
		ALL
	}

	public ScopeInfoDumpLevel dumpSymbolTable = ScopeInfoDumpLevel.NOTHING;
	public boolean prettyPrint;
	public boolean prettyPrintTyped;
	public boolean dumpToks;
	public boolean displayAST;
	public String fullPath;
	public String basePath;
}
