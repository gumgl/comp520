/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

import golite.lexer.Lexer;
import golite.lexer.LexerException;
import golite.node.Node;
import golite.node.TId;
import golite.parser.Parser;
import golite.parser.ParserException;
import golite.typechecker.SymbolTableLogger;
import golite.typechecker.Type;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.HashMap;

/**
 * High-level functions to compile a source file
 */
public class Compiler {

	String sourcePath;
	private Node ast;

	// Helper class which maps nodes in the AST to the source code
	private PositionHelper positionHelper;

	private boolean weeded = false;
	HashMap<Node, Type> types;
	HashMap<TId, Integer> occurrenceCounts;

	public Compiler(String path) {
		this.sourcePath = path;
	}

	public void parseSource() throws ParserException, LexerException, IOException {
		if (ast == null) {
			Lexer lexer = new GoLexer(new PushbackReader(new FileReader(sourcePath), 1024));
			Parser parser = new Parser(lexer);
			ast = parser.parse();
		}
	}

	public Node getUnvalidatedAST() throws ParserException, LexerException, IOException {
		parseSource();
		return ast;
	}

	protected void initializePositionHelper() throws ParserException, LexerException, IOException {
		parseSource();
		if (positionHelper == null)
			positionHelper = new PositionHelper(ast);
	}

	public PositionHelper getPositionHelper() throws ParserException, LexerException, IOException {
		initializePositionHelper();
		return positionHelper;
	}

	public void validateAST() throws ParserException, LexerException, IOException {
		parseSource();
		initializePositionHelper();

		if (!weeded) {
			GoLiteWeeder.weed(ast, positionHelper);
			weeded = true;
		}
	}

	/** Get the AST after having run the weeder on it */
	public Node getValidatedAST() throws ParserException, LexerException, IOException {
		validateAST();
		return ast;
	}

	public void typeCheck(SymbolTableLogger logger) throws ParserException, LexerException, IOException {
		if (types == null) {
			initializePositionHelper();
			validateAST();

			TypeChecker checker = new TypeChecker(positionHelper, logger);
			ast.apply(checker);
			types = checker.types;
			occurrenceCounts = checker.occurrenceCounts;
		}
	}

	public void typeCheck() throws ParserException, LexerException, IOException {
		typeCheck(null);
	}

	public HashMap<Node, Type> getTypeMap() throws ParserException, LexerException, IOException {
		typeCheck();
		return types;
	}

	public HashMap<TId, Integer> getOccurrenceCounts() throws ParserException, LexerException, IOException {
		typeCheck();
		return occurrenceCounts;
	}
}
