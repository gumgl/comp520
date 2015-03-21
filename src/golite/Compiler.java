package golite;

import golite.lexer.Lexer;
import golite.lexer.LexerException;
import golite.node.Node;
import golite.node.Token;
import golite.parser.Parser;
import golite.parser.ParserException;
import golite.typechecker.SymbolTableLogger;
import golite.typechecker.Type;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.util.HashMap;

/**
 * Control the flow of execution of a program
 */
public class Compiler {

	public static enum CompilationStage {
		LEXING,
		PARSING,
		WEEDING,
		TYPE_CHECKING,
		CODE_GEN
	}

	/** Container class for compilation switches */
	public static class Options {
		// Debugging: dumping scope info
		public static enum SymbolTableLogLevel {
			NOTHING,
			LAST_FRAME,
			ALL
		}

		// By default we don't assume any particular stage
		public CompilationStage executeStage = null;

		public SymbolTableLogLevel symbolTableLogLevel = SymbolTableLogLevel.NOTHING;

		public boolean prettyPrint;
		public boolean prettyPrintTyped;
		public boolean dumpToks;
		public boolean displayAST;

		String path;
	}

	public static void processSource(String path)
			throws LexerException, IOException, ParserException {

		processSource(path, null);
	}

	public static void processSource(String path, CompilationStage stage)
			throws LexerException, IOException, ParserException {

		Options opts = new Options();
		opts.path = path;
		opts.executeStage = stage;
		processSource(opts);
	}

	public static void processSource(Options options) throws LexerException, IOException, ParserException {
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

		/* Build AST */
		Node ast = getParsedAST(options.path);

		// End of stages: Lexing and parsing
		// For now, we'll also parse the file, even if the options say to
		// just do the lexing
		if (options.executeStage == CompilationStage.LEXING
				|| options.executeStage == CompilationStage.PARSING)
			return;

		// Initialize a helper class to map the position of nodes in the AST
		PositionHelper positionHelper = new PositionHelper(ast);

		// Do the weeding
		GoLiteWeeder.weed(ast, positionHelper);

		// End of stage: weeding
		if (options.executeStage == CompilationStage.WEEDING)
			return;

		// Pretty print AST
		if (options.prettyPrint) {
			prettyPrint(ast, new PrettyPrinter(), pathBase+".pretty.go");
		}

		// Display AST in a JTree
		if (options.displayAST) {
			ASTDisplay display = new ASTDisplay();
			ast.apply(display);
		}

		HashMap<Node, Type> types = typeCheck(ast, pathBase+".symtab", positionHelper,
				getSymbolTableLogger(options.symbolTableLogLevel));

		if (options.prettyPrintTyped) {
			prettyPrint(ast, new TypedPrettyPrinter(types), pathBase+".pptype.go");
		}

		// End of stage: type checking
		if (options.executeStage == CompilationStage.TYPE_CHECKING)
			return;

		// TODO: code generation
	}

	public static Node getParsedAST(String filename) throws ParserException, LexerException, IOException {
		Lexer lexer = new GoLexer(new PushbackReader(new FileReader(filename), 1024));
		Parser parser = new Parser(lexer);
		Node ast = parser.parse();

		return ast;
	}

	/** Generate an HTML token representation */
	public static void tokenPrintHtml(String inputPath, String path) throws LexerException, IOException {

		Lexer lexer = new ConservingGoLexer(new PushbackReader(new FileReader(inputPath), 1024));
		PrintWriter htmlFile = new PrintWriter(new PrintWriter(path), true);

		try {
			int count=0; // To prevent some infinite loops

			htmlFile
				.append("<!DOCTYPE html><html><head><title>")
				.append(path)
				.append("</title><style>")
				.append(".tok.even{background-color:LightSkyBlue}")
				.append(".tok.odd{background-color:LightSalmon}")
				.append("</style></head><body><pre>");

			while (lexer.peek() != null && count < 2000) {
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

	/** Pretty print the program
	 *
	 * @param ast The root node of the program
	 * @param printer The pretty printer to use
	 * @param path The path of the file to print to
	 */
	public static void prettyPrint(Node ast, PrettyPrinter printer, String path) throws FileNotFoundException {
		PrintWriter prettyFile = new PrintWriter(new PrintWriter(path), true);
		try {
			printer.setOutputWriter(prettyFile);
			ast.apply(printer);
		} finally {
			prettyFile.close();
		}
	}

	/** Type analysis */
	public static HashMap<Node, Type> typeCheck(Node ast, String path, PositionHelper positionHelper,
			SymbolTableLogger logger) throws FileNotFoundException {

		TypeChecker checker = new TypeChecker(positionHelper, logger);
		PrintWriter logFile = null;

		if (logger != null) {
			logFile = new PrintWriter(new PrintWriter(path), true);
			logger.setOutputWriter(logFile);
		}

		try {
			ast.apply(checker);
		} finally {
			if (logFile != null) {
				logFile.close();
			}
		}

		return checker.types;
	}

	public static SymbolTableLogger getSymbolTableLogger(
			Options.SymbolTableLogLevel level) {

		switch (level) {
		case NOTHING:
			return null;

		case ALL:
			return new SymbolTableLogger(true);

		case LAST_FRAME:
			return new SymbolTableLogger(false);

		default:
			throw new IllegalArgumentException("Bad symbol table logging level "+level);
		}
	}
}
