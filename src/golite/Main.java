package golite;

import golite.parser.* ;
import golite.typechecker.SymbolTableLogger;
import golite.typechecker.Type;
import golite.lexer.* ;
import golite.node.* ;

import java.io.* ;
import java.util.HashMap;

public class Main {
	public static void main(String[] args) {
		CLIOptions options = null;
		Node ast;
		PositionHelper positionHelper;

		try {
			options = getCLIOptions(args);
		} catch (IllegalArgumentException e) {
			System.err.print("Error: "+e.getMessage()+"\n\n");
			printUsage();
			System.exit(1);
		}

		try {
			/* Print HTML representation of tokens */
			if (options.dumpToks) {
				tokenPrintHtml(options.fullPath, options.basePath);
			}

			/* Build AST */
			ast = getParsedAST(options.fullPath);
			positionHelper = new PositionHelper(ast);

			// Do the weeding
			GoLiteWeeder.weed(ast, positionHelper);

			// Pretty print AST
			if (options.prettyPrint) {
				prettyPrint(ast, options.basePath);
			}

			// Display AST in a JTree
			if (options.displayAST) {
				ASTDisplay display = new ASTDisplay();
				ast.apply(display);
			}

			HashMap<Node, Type> types = typeCheck(ast, options.basePath, positionHelper, options.dumpSymbolTable);

			if (options.prettyPrintTyped) {
				prettyPrintTyped(ast, options.basePath, types);
			}

			// TODO: code generation

		} catch (golite.lexer.LexerException e) {
			handleError("Lexer", e);
		} catch (golite.parser.ParserException e) {
			handleError("Parser", e);
		} catch (GoLiteWeedingException e) {
			handleError("Weeding", e);
		} catch (GoLiteTypeException e) {
			handleError("Type", e);
		} catch (FileNotFoundException e) {
			handleError("File \"" + options.fullPath + "\" not found.");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			System.exit(2);
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

	public static void handleError(String errorType, Exception e) {
		System.err.println(errorType+" error at "+e.getMessage());
		System.exit(1);
	}

	public static void handleError(String message) {
		System.err.println("Error: "+message);
		System.exit(1);
	}

	public static Node getParsedAST(String filename) throws ParserException, LexerException, IOException {
		Lexer lexer = new GoLexer(new PushbackReader(new FileReader(filename), 1024));
		Parser parser = new Parser(lexer);
		Node ast = parser.parse();

		return ast;
	}

	/*
	 * Generates a .html file to see tokens
	 */
	public static void tokenPrintHtml(String inputPath, String outputPathBase) throws LexerException, IOException {
		Lexer lexer = new ConservingGoLexer(new PushbackReader(new FileReader(inputPath), 1024));
		PrintWriter fileHtml = new PrintWriter(new PrintWriter(outputPathBase+".tokens.html"), true);

		try {
			StringBuilder html = new StringBuilder("<html><body><pre>");
			int count=0; // To prevent some infinite loops
			while (lexer.peek() != null && count < 2000) {
				count ++;
				Token token = lexer.next();
				String color = count % 2 == 0 ? "LightSkyBlue" : "LightSalmon";

				html.append("<span style=\"background-color:"+color+"\" title=\"" + token.getClass().getSimpleName() + "\">" + token.getText().replaceAll(" ", "&#32;").replaceAll("(\r\n|\n)", "&#x23CE;$1") + "</span>");
			}
			html.append("</pre></body></html>");
			fileHtml.write(html.toString());
		} finally {
			fileHtml.close();
		}
	}

	public static void tokenPrintText(Lexer lexer) throws LexerException, IOException {
		int count=0; // To prevent some infinite loops
        while (lexer.peek() != null && count < 2000) {
        	count ++;
        	Token token = lexer.next();
        	System.out.println(token.getLine() + ": " + token.getClass().getSimpleName() + "(" + token.getText() + ")" );
        }
	}

	public static void prettyPrint(Node ast, String path) throws FileNotFoundException {
		// Pretty Print
		PrintWriter filePretty = new PrintWriter(new PrintWriter(path+".pretty.go"), true);
		PrettyPrinter pretty = new PrettyPrinter(filePretty) ;
		ast.apply(pretty);
		filePretty.close();
	}

	public static void prettyPrintTyped(Node ast, String path, HashMap<Node, Type> types) throws FileNotFoundException {
		// Pretty Print
		PrintWriter filePretty = new PrintWriter(new PrintWriter(path+".pptype.go"), true);
		PrettyPrinter pretty = new TypedPrettyPrinter(filePretty, types);
		ast.apply(pretty);
		filePretty.close();
	}

	public static HashMap<Node, Type> typeCheck(Node ast, String path, PositionHelper positionHelper,
			CLIOptions.ScopeInfoDumpLevel infoDumpLevel) throws FileNotFoundException {

		SymbolTableLogger logger = null;
		boolean logVerbose = false;
		PrintWriter logFile = null;

		switch (infoDumpLevel) {
		case NOTHING:
			break;

		case ALL:
			logVerbose = true;
			// fallthrough

		case LAST_FRAME:
			logFile = new PrintWriter(new PrintWriter(path+".symtab"), true);
			logger = new SymbolTableLogger(logFile, logVerbose);
			break;

		default:
			throw new IllegalArgumentException("Bad scope logging value "+infoDumpLevel);	
		}

		TypeChecker typechecker = new TypeChecker(positionHelper, logger);

		try {
			ast.apply(typechecker);
		} finally {
			if (logFile != null) {
				logFile.close();
			}
		}
		return typechecker.types;
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
