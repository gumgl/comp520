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
