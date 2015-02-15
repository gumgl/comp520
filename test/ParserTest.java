import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import org.junit.Test;

import golite.lexer.Lexer;
import golite.lexer.LexerException;
import golite.node.Node;
import golite.parser.Parser;
import golite.parser.ParserException;

public class ParserTest {
	@Test
	public void testPackageDeclaration() throws ParserException, LexerException, IOException {
		assertEquals("Program(test)", serializeAST("package test;\n"));
	}

	@Test
	public void testVariableDeclaration() throws ParserException, LexerException, IOException {
		assertEquals(
			"Program(test,VariableDeclaration(UntypedVariableSpec(x,LitIntExp(1))))",
			serializeAST("package test; var x = 1;")
		);

		assertEquals(
			"Program(test,VariableDeclaration(TypedVariableSpec(x,IntTypeExp())))",
			serializeAST("package test; var x int;")
		);

		assertEquals(
			"Program(test,VariableDeclaration(TypedVariableSpec(x,AliasTypeExp(Point))))",
			serializeAST("package test; var x Point;")
		);

		assertEquals(
			"Program(test,VariableDeclaration(UntypedVariableSpec(x,y,LitIntExp(1),LitIntExp(2))))",
			serializeAST("package test; var x, y = 1, 2;")
		);

		assertEquals(
			"Program(test,VariableDeclaration(TypedVariableSpec(x,y,IntTypeExp(),LitIntExp(1),LitIntExp(2))))",
			serializeAST("package test; var x, y int = 1, 2;")
		);

		assertEquals(
			"Program(test,VariableDeclaration(UntypedVariableSpec(x,y,LitIntExp(1),LitIntExp(2)),"+
				"TypedVariableSpec(a,ArrayTypeExp(5,Float64TypeExp()))))",
			serializeAST("package test; var (x, y = 1, 2; a [5]float64;);")
		);
	}

	@Test
	public void testTypeDeclaration() throws ParserException, LexerException, IOException {
		assertEquals(
			"Program(test,TypeDeclaration(TypeSpec(num,IntTypeExp())))",
			serializeAST("package test; type num int;")
		);

		assertEquals(
			"Program(test,TypeDeclaration(TypeSpec(num,IntTypeExp()),TypeSpec(Polar,AliasTypeExp(Point))))",
			serializeAST("package test; type (num int; Polar Point;);")
		);
	}

	@Test
	public void testFunctionDeclaration() throws ParserException, LexerException, IOException {
		assertEquals(
			"Program(test,FunctionDeclaration(f,FuncParam(a,b,IntTypeExp()),FuncParam(c,ArrayTypeExp(3,Float64TypeExp()))))",
			serializeAST("package test; func f(a, b int, c [3]float64) {}")
		);
	}

	@Test
	public void testSimpleStm() throws ParserException, LexerException, IOException {
		assertEquals(
			"Program(test,FunctionDeclaration(f,EmptyStm()))",
			serializeAST("package test; func f() {;}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,ExpressionStm(LitIntExp(1))))",
			serializeAST("package test; func f() {1;}")
		);

		/* LValues should be accepted here, but aren't yet */
		assertEquals(
			"Program(test,FunctionDeclaration(f,AssignStm(a,b,c,LitIntExp(1),LitIntExp(2),LitIntExp(3))))",
			serializeAST("package test; func f() {a, b, c = 1, 2, 3;}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,OpAssignStm(IdLvalue(a),MinusAssignOp(),LitIntExp(1))))",
			serializeAST("package test; func f() {a -= 1;}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,VariableDecStm(UntypedVariableSpec(a,LitIntExp(1)))))",
			serializeAST("package test; func f() {a := 1;}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,IncDecStm(IdLvalue(a),DecPostfixOp())))",
			serializeAST("package test; func f() {a--;}")
		);
	}

	@Test
	public void testNonSimpleStatements() throws ParserException, LexerException, IOException {
		assertEquals(
			"Program(test,FunctionDeclaration(f,VariableDecStm(UntypedVariableSpec(a,LitIntExp(1)))))",
			serializeAST("package test; func f() {var a = 1;}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,TypeDecStm(TypeSpec(a,IntTypeExp()))))",
			serializeAST("package test; func f() {type a int;}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,PrintStm(SimplePrintOp(),LitIntExp(1))))",
			serializeAST("package test; func f() {print(1);}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,BreakStm(),ContinueStm(),ReturnStm()))",
			serializeAST("package test; func f() {break;continue;return;}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,SwitchStm("
			+ "VariableDecStm(UntypedVariableSpec(x,LitIntExp(1))),"
			+ "VariableExp(x),"
			+ "ConditionalSwitchClause(LitIntExp(1),LitIntExp(2),EmptyStm()),"
			+ "DefaultSwitchClause(EmptyStm()))))",
			serializeAST("package test; func f() {switch x := 1; x {case 1, 2: ; default: ;}}")
		);
	}

	@Test
	public void testIfStatement() throws ParserException, LexerException, IOException {
		assertEquals(
			"Program(test,FunctionDeclaration(f,IfStm("
			+ "VariableDecStm(UntypedVariableSpec(x,LitIntExp(1))),"
			+ "VariableExp(x),"
			+ "EmptyStm())))",
			serializeAST("package test; func f() {if x := 1; x {;}}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,IfStm("
			+ "VariableDecStm(UntypedVariableSpec(x,LitIntExp(1))),"
			+ "VariableExp(x),"
			+ "EmptyStm(),"
			+ "EmptyStm())))",
			serializeAST("package test; func f() {if x := 1; x {;} else {;}}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,IfStm("
			+ "VariableDecStm(UntypedVariableSpec(x,LitIntExp(1))),"
			+ "VariableExp(x),"
			+ "EmptyStm(),"
			+ "IfStm(VariableExp(y),EmptyStm(),EmptyStm()))))",
			serializeAST("package test; func f() {if x := 1; x {;} else if y {;} else {;}}")
		);
	}

	@Test
	public void testForStatement() throws ParserException, LexerException, IOException {
		assertEquals(
			"Program(test,FunctionDeclaration(f,ForStm("
			+ "VariableDecStm(UntypedVariableSpec(x,LitIntExp(1))),"
			+ "VariableExp(x),"
			+ "IncDecStm(IdLvalue(x),IncPostfixOp()),"
			+ "EmptyStm())))",
			serializeAST("package test; func f() {for x := 1; x; x++; {;}}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,ForStm(VariableExp(x),EmptyStm())))",
			serializeAST("package test; func f() {for x {;}}")
		);

		assertEquals(
			"Program(test,FunctionDeclaration(f,ForStm(EmptyStm())))",
			serializeAST("package test; func f() {for {;}}")
		);
	}

	private Node getAST(String input) throws ParserException, LexerException, IOException {
		Lexer lexer = new Lexer(new PushbackReader(new StringReader(input), 1024));
		Parser parser = new Parser(lexer);
		return parser.parse();
	}

	private String serializeAST(Node ast) {
		ASTSerializer serializer = new ASTSerializer();
		ast.apply(serializer);
		return serializer.getSerialized();
	}

	private String serializeAST(String s) throws ParserException, LexerException, IOException {
		return serializeAST(getAST(s));
	}
}
