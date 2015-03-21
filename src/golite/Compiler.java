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
	public static void processSource(CLIOptions options) throws LexerException, IOException, ParserException {
		Node ast;
		PositionHelper positionHelper;

		/* Print HTML representation of tokens */
		if (options.dumpToks) {
			tokenPrintHtml(options.fullPath, options.basePath+".tokens.html");
		}

		/* Build AST */
		ast = getParsedAST(options.fullPath);
		positionHelper = new PositionHelper(ast);

		// Do the weeding
		GoLiteWeeder.weed(ast, positionHelper);

		// Pretty print AST
		if (options.prettyPrint) {
			prettyPrint(ast, new PrettyPrinter(), options.basePath+".pretty.go");
		}

		// Display AST in a JTree
		if (options.displayAST) {
			ASTDisplay display = new ASTDisplay();
			ast.apply(display);
		}

		HashMap<Node, Type> types = typeCheck(ast, options.basePath+".symtab", positionHelper,
				getSymbolTableLogger(options.dumpSymbolTable));

		if (options.prettyPrintTyped) {
			prettyPrint(ast, new TypedPrettyPrinter(types), options.basePath+".pptype.go");
		}

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
			CLIOptions.ScopeInfoDumpLevel infoDumpLevel) {

		switch (infoDumpLevel) {
		case NOTHING:
			return null;

		case ALL:
			return new SymbolTableLogger(true);

		case LAST_FRAME:
			return new SymbolTableLogger(false);

		default:
			throw new IllegalArgumentException("Bad scope logging value "+infoDumpLevel);	
		}
	}
}
