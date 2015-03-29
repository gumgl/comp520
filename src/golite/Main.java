package golite;

import golite.lexer.Lexer;
import golite.lexer.LexerException;
import golite.node.EOF;
import golite.node.Node;
import golite.node.Token;
import golite.parser.ParserException;
import golite.typechecker.SymbolTableLogger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.PushbackReader;

public class Main {
	/** Container class for compilation switches */
	public static class Options {
		// Debugging: dumping scope info
		public static enum SymbolTableLogLevel {
			NOTHING,
			LAST_FRAME,
			ALL
		}

		public SymbolTableLogLevel symbolTableLogLevel = SymbolTableLogLevel.NOTHING;

		public boolean prettyPrint;
		public boolean prettyPrintTyped;
		public boolean dumpToks;
		public boolean displayAST;

		String path;
	}

	public static final int EXIT_SUCCESS = 0;
	public static final int EXIT_INVALID_SOURCE = 1;
	public static final int EXIT_INTERNAL_ERROR = 2;

	public static void main(String[] args) {
		Options options = null;

		try {
			options = getCLIOptions(args);
		} catch (IllegalArgumentException e) {
			System.err.print("Error: "+e.getMessage()+"\n\n");
			printUsage();
			System.exit(EXIT_INTERNAL_ERROR);
		}

		try {
			processSource(options);
		} catch (golite.lexer.LexerException e) {
			handleSourceCodeError("Lexer", e);
		} catch (golite.parser.ParserException e) {
			handleSourceCodeError("Parser", e);
		} catch (GoLiteWeedingException e) {
			handleSourceCodeError("Weeding", e);
		} catch (GoLiteTypeException e) {
			handleSourceCodeError("Type", e);
		} catch (FileNotFoundException e) {
			handleSourceCodeError("File \"" + options.path + "\" not found.");
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
	public static Options getCLIOptions(String[] args) {
		Options options = new Options();
		int i;

		// Read options
		for (i=0; i < args.length && args[i].startsWith("-"); i++) {
			switch (args[i]) {
			case "-dumpsymtab":
				options.symbolTableLogLevel = Options.SymbolTableLogLevel.LAST_FRAME;
				break;
			case "-dumpsymtaball":
				options.symbolTableLogLevel = Options.SymbolTableLogLevel.ALL;
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

		options.path = args[i];

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

	/** Source processing */
	public static void processSource(Options options)
			throws LexerException, IOException, ParserException {

		String pathBase;

		if (options.path.endsWith(".go")) {
			pathBase = options.path.substring(0, options.path.length() - 3);
		} else {
			pathBase = options.path;
		}

		/* Print HTML representation of tokens */
		if (options.dumpToks) {
			tokenPrintHtml(options.path, pathBase+".tokens.html");
		}

		// Build AST
		Compiler compiler = new Compiler(options.path);
		Node ast = compiler.getUnvalidatedAST();

		// Display AST in a JTree
		if (options.displayAST) {
			ASTDisplay display = new ASTDisplay();
			ast.apply(display);
			while (display.frame != null && display.frame.isDisplayable())
				try {Thread.sleep(250);} catch (InterruptedException e) {}
		}

		// Do the weeding
		compiler.validateAST();

		// Pretty print the AST
		if (options.prettyPrint) {
			printOutput(ast, new PrettyPrinter(), pathBase+".pretty.go");
		}

		runLoggedTypeCheck(compiler, pathBase+".symtab", options.symbolTableLogLevel);

		if (options.prettyPrintTyped) {
			printOutput(ast, new TypedPrettyPrinter(compiler.getTypeMap()), pathBase+".pptype.go");
		}

		printOutput(ast, new JSGenerator(compiler.getTypeMap()), pathBase+".js");
	}

	/** Generate an HTML token representation */
	public static void tokenPrintHtml(String inputPath, String outputPath) throws LexerException, IOException {

		Lexer lexer = new ConservingGoLexer(new PushbackReader(new FileReader(inputPath), 1024));
		PrintWriter htmlFile = new PrintWriter(new PrintWriter(outputPath), true);

		try {
			int count=0; // To prevent some infinite loops

			htmlFile
				.append("<!DOCTYPE html><html><head><title>")
				.append(inputPath)
				.append("</title><style>")
				.append(".tok.even{background-color:LightSkyBlue}")
				.append(".tok.odd{background-color:LightSalmon}")
				.append("</style></head><body><pre>");

			while (!(lexer.peek() instanceof EOF)) {
				count ++;
				Token token = lexer.next();

				htmlFile
					.append("<span class=\"tok ")
					.append(count % 2 == 0 ? "even" : "odd")
					.append("\" title=\"")
					.append(token.getClass().getSimpleName())
					.append("\">")
					.append(token.getText().replaceAll(" ", "&#32;").replaceAll("(\r\n|\n)", "&#x23CE;$1"))
					.append("</span>");
			}
			htmlFile.append("</pre></body></html>");
		} finally {
			htmlFile.close();
		}
	}

	/** Print output for the program
	 *
	 * @param ast The root node of the program
	 * @param printer The printer to use
	 * @param path The path of the file to print to
	 */
	public static void printOutput(Node ast, PrintingASTAdapter printer, String path)
			throws FileNotFoundException {

		PrintWriter prettyFile = new PrintWriter(new PrintWriter(path), true);
		try {
			printer.setOutputWriter(prettyFile);
			ast.apply(printer);
		} finally {
			prettyFile.close();
		}
	}

	public static void runLoggedTypeCheck(Compiler compiler, String logFilePath,
			Options.SymbolTableLogLevel level) throws ParserException, LexerException, IOException {

		SymbolTableLogger logger = getSymbolTableLogger(level);

		PrintWriter logFile = null;
		if (logger != null) {
			logFile = new PrintWriter(new PrintWriter(logFilePath), true);
			logger.setOutputWriter(logFile);
		}

		try {
			compiler.typeCheck(logger);
		} finally {
			if (logFile != null) {
				logFile.close();
			}
		}
	}

	public static SymbolTableLogger getSymbolTableLogger(Options.SymbolTableLogLevel level) {

		switch (level) {
		case NOTHING:
			return null;

		case ALL:
			return new SymbolTableLogger(true);

		case LAST_FRAME:
			return new SymbolTableLogger(false);

		default:
			throw new IllegalArgumentException(
					"Bad symbol table logging level " + level);
		}
	}
}
