package golite;

import java.io.PrintWriter;
import java.util.List;

import golite.node.*;


public class PrettyPrinter extends PrintingASTAdapter {
	public PrettyPrinter(PrintWriter writer) {
		super(writer);
	}

	public PrettyPrinter() {
		this(null);
	}

	private void printConsecutiveLines(List<? extends Node> nodes) {
		for (Node e : nodes) {
			startl();
			e.apply(this);
			endl();
		}
	}

	private void printList(List<? extends Node> nodes) {
		boolean first = true;
		for (Node e : nodes) {
			if (first) {
				first = false;
			} else {
				p(", ");
			}
			e.apply(this);
		}
	}

	private void printSpecs(List<? extends Node> specs) {
		if (specs.size() == 1) {
			specs.get(0).apply(this);
		} else {
			p("(");
			endl();
			shift();
			printConsecutiveLines(specs);
			unshift();
			startl();
			p(")");
		}
	}

	@Override
	public void caseAProgram(AProgram node)
	{
		inAProgram(node);
		pln("/*The content is generated by pretty printer.*/");
		if(node.getPackageName() != null)
		{
			pln("package " + node.getPackageName().getText());
		}
		printConsecutiveLines(node.getDeclarations());
		outAProgram(node);
	}

	@Override
	public void caseAVariableDeclaration(AVariableDeclaration node)
	{
		inAVariableDeclaration(node);
		p("var ");
		printSpecs(node.getVariableSpec());
		outAVariableDeclaration(node);
	}

	@Override
	public void caseATypeDeclaration(ATypeDeclaration node)
	{
		inATypeDeclaration(node);
		p("type ");
		printSpecs(node.getTypeSpec());
		outATypeDeclaration(node);
	}

	@Override
	public void caseAFunctionDeclaration(AFunctionDeclaration node)
	{
		inAFunctionDeclaration(node);
		p("func ");
		p(node.getId().getText());
		p(" (");
		printList(node.getFuncParam());
		p(")");
		if(node.getReturnType() != null)
		{
			p(" ");
			node.getReturnType().apply(this);
		}
		p(" {");
		endl();
		shift();
		printConsecutiveLines(node.getStm());
		unshift();
		startl();
		p("}");
		outAFunctionDeclaration(node);
	}

	@Override
	public void caseATypedVariableSpec(ATypedVariableSpec node)
	{
		inATypedVariableSpec(node);
		printList(node.getId());
		p(" ");
		if(node.getTypeExp() != null)
		{
			node.getTypeExp().apply(this);
		}
		if (!node.getExp().isEmpty()) {
			p(" = ");
			printList(node.getExp());
		}
		outATypedVariableSpec(node);
	}

	@Override
	public void caseAUntypedVariableSpec(AUntypedVariableSpec node)
	{
		inAUntypedVariableSpec(node);
		printList(node.getId());
		p(" = ");
		printList(node.getExp());
		outAUntypedVariableSpec(node);
	}

	@Override
	public void caseATypeSpec(ATypeSpec node)
	{
		inATypeSpec(node);
		if(node.getId() != null)
		{
			p(node.getId().getText());
		}
		p(" ");
		if(node.getTypeExp() != null)
		{
			node.getTypeExp().apply(this);
		}
		outATypeSpec(node);
	}

	@Override
	public void caseAFuncParam(AFuncParam node)
	{
		inAFuncParam(node);
		printList(node.getId());
		p(" ");
		if(node.getTypeExp() != null)
		{
			node.getTypeExp().apply(this);
		}
		outAFuncParam(node);
	}

	@Override
	public void caseAAliasTypeExp(AAliasTypeExp node)
	{
		inAAliasTypeExp(node);
		if(node.getId() != null)
		{
			p(node.getId().getText());
		}
		outAAliasTypeExp(node);
	}

	@Override
	public void caseAIntTypeExp(AIntTypeExp node)
	{
		inAIntTypeExp(node);
		p("int");
		outAIntTypeExp(node);
	}

	@Override
	public void caseAFloat64TypeExp(AFloat64TypeExp node)
	{
		inAFloat64TypeExp(node);
		p("float64");
		outAFloat64TypeExp(node);
	}

	@Override
	public void caseABoolTypeExp(ABoolTypeExp node)
	{
		inABoolTypeExp(node);
		p("bool");
		outABoolTypeExp(node);
	}

	@Override
	public void caseARuneTypeExp(ARuneTypeExp node)
	{
		inARuneTypeExp(node);
		p("rune");
		outARuneTypeExp(node);
	}

	@Override
	public void caseAStringTypeExp(AStringTypeExp node)
	{
		inAStringTypeExp(node);
		p("string");
		outAStringTypeExp(node);
	}

	@Override
	public void caseAStructTypeExp(AStructTypeExp node)
	{
		inAStructTypeExp(node);
		p("struct {");
		endl();
		shift();
		printConsecutiveLines(node.getFieldDec());
		unshift();
		startl();
		p("}");
		outAStructTypeExp(node);
	}

	@Override
	public void caseASliceTypeExp(ASliceTypeExp node)
	{
		inASliceTypeExp(node);
		p("[]");
		if(node.getTypeExp() != null)
		{
			node.getTypeExp().apply(this);
		}
		outASliceTypeExp(node);
	}

	@Override
	public void caseAArrayTypeExp(AArrayTypeExp node)
	{
		inAArrayTypeExp(node);
		p("[");
		if(node.getInteger() != null)
		{
			node.getInteger().apply(this);
		}
		p("]");
		if(node.getTypeExp() != null)
		{
			node.getTypeExp().apply(this);
		}
		outAArrayTypeExp(node);
	}

	@Override
	public void caseAFieldDec(AFieldDec node)
	{
		inAFieldDec(node);
		printList(node.getId());
		p(" ");
		if(node.getTypeExp() != null)
		{
			node.getTypeExp().apply(this);
		}
		outAFieldDec(node);
	}

	@Override
	public void caseAExpressionStm(AExpressionStm node)
	{
		inAExpressionStm(node);
		if(node.getExp() != null)
		{
			node.getExp().apply(this);
		}
		outAExpressionStm(node);
	}

	@Override
	public void caseAAssignStm(AAssignStm node)
	{
		inAAssignStm(node);
		printList(node.getLvalue());
		p(" = ");
		printList(node.getExp());
		outAAssignStm(node);
	}

	@Override
	public void caseAOpAssignStm(AOpAssignStm node)
	{
		inAOpAssignStm(node);
		if(node.getLvalue() != null)
		{
			node.getLvalue().apply(this);
		}
		p(" ");
		if(node.getAssignOp() != null)
		{
			node.getAssignOp().apply(this);
		}
		p(" ");
		if(node.getExp() != null)
		{
			node.getExp().apply(this);
		}
		outAOpAssignStm(node);
	}

	@Override
	public void caseAIncDecStm(AIncDecStm node)
	{
		inAIncDecStm(node);
		if(node.getExp() != null)
		{
			node.getExp().apply(this);
		}
		if(node.getPostfixOp() != null)
		{
			node.getPostfixOp().apply(this);
		}
		outAIncDecStm(node);
	}

	@Override
	public void caseAShortVariableDecStm(AShortVariableDecStm node)
	{
		inAShortVariableDecStm(node);
		printList(node.getIds());
		p(" := ");
		printList(node.getExp());
		outAShortVariableDecStm(node);
	}

	@Override
	public void caseAVariableDecStm(AVariableDecStm node)
	{
		inAVariableDecStm(node);
		p("var ");
		printSpecs(node.getVariableSpec());
		outAVariableDecStm(node);
	}

	@Override
	public void caseATypeDecStm(ATypeDecStm node)
	{
		inATypeDecStm(node);
		p("type ");
		printSpecs(node.getTypeSpec());
		outATypeDecStm(node);
	}

	@Override
	public void caseAPrintStm(APrintStm node)
	{
		inAPrintStm(node);
		p("print");
		if(node.getPrintOp() != null)
		{
			node.getPrintOp().apply(this);
		}
		p("(");
		printList(node.getExp());
		p(")");
		outAPrintStm(node);
	}

	@Override
	public void caseAReturnStm(AReturnStm node)
	{
		inAReturnStm(node);
		p("return");
		if(node.getExp() != null)
		{
			p(" ");
			node.getExp().apply(this);
		}
		outAReturnStm(node);
	}
	
	@Override
	public void caseABlockStm(ABlockStm node) {
		inABlockStm(node);
		p("{");
		endl();
		shift();
		printConsecutiveLines(node.getStm());
		unshift();
		startl();
		p("}");
		outABlockStm(node);
	}

	@Override
	public void caseAIfStm(AIfStm node)
	{
		inAIfStm(node);
		p("if ");
		if(node.getStm() != null)
		{
			node.getStm().apply(this);
			p("; ");
		}
		if(node.getExp() != null)
		{
			node.getExp().apply(this);
			p(" ");
		}
		p("{");
		endl();
		shift();
		printConsecutiveLines(node.getIfBlock());
		unshift();
		startl();
		p("}");
		if ( ! node.getElseBlock().isEmpty()) {
			List<PStm> elseBlock = node.getElseBlock();
			p(" else ");
			if (elseBlock.size() == 1 && elseBlock.get(0) instanceof AIfStm) { // Elseif
				elseBlock.get(0).apply(this);
			} else {
				p("{");
				endl();
				shift();
				printConsecutiveLines(elseBlock);
				unshift();
				startl();
				p("}");
			}
		}
		outAIfStm(node);
	}

	@Override
	public void caseASwitchStm(ASwitchStm node)
	{
		inASwitchStm(node);
		p("switch ");
		if(node.getStm() != null)
		{
			node.getStm().apply(this);
			p("; ");
		}
		if(node.getExp() != null)
		{
			node.getExp().apply(this);
			p(" ");
		}
		p("{");
		endl();
		{
			for(PSwitchClause e : node.getSwitchClause())
			{
				e.apply(this);
			}
		}
		startl();
		p("}");
		outASwitchStm(node);
	}

	@Override
	public void caseAForStm(AForStm node)
	{
		inAForStm(node);
		boolean whileLoop = (node.getInit() == null && node.getPost() == null);

		p("for ");

		if(node.getInit() != null)
		{
			node.getInit().apply(this);
		}
		if (!whileLoop) p("; ");
		if(node.getExp() != null)
		{
			node.getExp().apply(this);
		}
		if (!whileLoop) p("; ");
		if(node.getPost() != null)
		{
			node.getPost().apply(this);
		}

		p(" {");
		endl();
		shift();
		printConsecutiveLines(node.getStm());
		unshift();
		startl();
		p("}");
		outAForStm(node);
	}

	@Override
	public void caseABreakStm(ABreakStm node)
	{
		inABreakStm(node);
		p("break");
		outABreakStm(node);
	}

	@Override
	public void caseAContinueStm(AContinueStm node)
	{
		inAContinueStm(node);
		p("continue");
		outAContinueStm(node);
	}

	@Override
	public void caseAConditionalSwitchClause(AConditionalSwitchClause node)
	{
		inAConditionalSwitchClause(node);
		startl();
		p("case ");
		printList(node.getExp());
		p(":");
		endl();
		shift();
		printConsecutiveLines(node.getStm());
		if(node.getFallthroughStm() != null)
		{
			startl();
			node.getFallthroughStm().apply(this);
			endl();
		}
		unshift();
		outAConditionalSwitchClause(node);
	}

	@Override
	public void caseADefaultSwitchClause(ADefaultSwitchClause node)
	{
		inADefaultSwitchClause(node);
		startl();
		p("default:");
		endl();
		shift();
		printConsecutiveLines(node.getStm());
		if(node.getFallthroughStm() != null)
		{
			startl();
			node.getFallthroughStm().apply(this);
			endl();
		}
		unshift();
		outADefaultSwitchClause(node);
	}

	@Override
	public void caseAFallthroughStm(AFallthroughStm node)
	{
		inAFallthroughStm(node);
		p("fallthrough");
		outAFallthroughStm(node);
	}

	@Override
	public void caseASimplePrintOp(ASimplePrintOp node)
	{
		inASimplePrintOp(node);
		outASimplePrintOp(node);
	}

	@Override
	public void caseALinePrintOp(ALinePrintOp node)
	{
		inALinePrintOp(node);
		p("ln");
		outALinePrintOp(node);
	}
	//postfix_op:
	public void caseAIncPostfixOp(AIncPostfixOp node)
	{
		p("++");
	}

	public void caseADecPostfixOp(ADecPostfixOp node)
	{
		p("--");
	}

	//assign_op:
	public void caseAPlusAssignOp(APlusAssignOp node)
	{
		p("+=");
	}

	public void caseAMinusAssignOp(AMinusAssignOp node)
	{
		p("-=");
	}

	public void caseAStarAssignOp(AStarAssignOp node)
	{
		p("*=");
	}

	public void caseASlashAssignOp(ASlashAssignOp node)
	{
		p("/=");
	}

	public void caseAPercentAssignOp(APercentAssignOp node)
	{
		p("%=");
	}

	public void caseAAmpAssignOp(AAmpAssignOp node)
	{
		p("&=");
	}

	public void caseAPipeAssignOp(APipeAssignOp node)
	{
		p("|=");
	}

	public void caseACaretAssignOp(ACaretAssignOp node)
	{
		p("^=");
	}

	public void caseALshiftAssignOp(ALshiftAssignOp node)
	{
		p("<<=");
	}

	public void caseARshiftAssignOp(ARshiftAssignOp node)
	{
		p(">>=");
	}

	public void caseAAmpCaretAssignOp(AAmpCaretAssignOp node)
	{
		p("&^=");
	}

	//binary_op
	public void caseALogicalOrBinaryOp(ALogicalOrBinaryOp node)
	{
		p("||");
	}

	public void caseALogicalAndBinaryOp(ALogicalAndBinaryOp node)
	{
		p("&&");
	}

	public void caseAEqBinaryOp(AEqBinaryOp node)
	{
		p("==");
	}

	public void caseAGtBinaryOp(AGtBinaryOp node)
	{
		p(">");
	}

	public void caseALtBinaryOp(ALtBinaryOp node)
	{
		p("<");
	}

	public void caseAGtEqBinaryOp(AGtEqBinaryOp node)
	{
		p(">=");
	}

	public void caseALtEqBinaryOp(ALtEqBinaryOp node)
	{
		p("<=");
	}

	public void caseAPlusBinaryOp(APlusBinaryOp node)
	{
		p("+");
	}

	public void caseAMinusAssignOp(AMinusBinaryOp node)
	{
		p("-=");
	}

	public void caseAStarBinaryOp(AStarBinaryOp node)
	{
		p("*");
	}

	public void caseASlashBinaryOp(ASlashBinaryOp node)
	{
		p("/");
	}

	public void caseAPercentBinaryOp(APercentBinaryOp node)
	{
		p("%");
	}

	public void caseAAmpBinaryOp(AAmpBinaryOp node)
	{
		p("&");
	}

	public void caseAPipeBinaryOp (APipeBinaryOp node)
	{
		p("|");
	}

	public void caseACaretBinaryOp(ACaretBinaryOp node)
	{
		p("^");
	}

	public void caseALshiftBinaryOp(ALshiftBinaryOp node)
	{
		p("<<");
	}

	public void caseARshiftBinaryOp(ARshiftBinaryOp node)
	{
		p(">>");
	}

	public void caseAAmpCaretBinaryOp(AAmpCaretBinaryOp node)
	{
		p("&^");
	}

	//unary_op
	public void caseAPlusUnaryOp(APlusUnaryOp node)
	{
		p("+");
	}

	public void caseAMinusUnaryOp(AMinusUnaryOp node)
	{
		p("-");
	}

	public void caseAExclamationUnaryOp(AExclamationUnaryOp node)
	{
		p("!");
	}

	public void caseACaretUnaryOp(ACaretUnaryOp node)
	{
		p("^");
	}

	//exp:
	public void caseAVariableExp(AVariableExp node)
	{
		inAVariableExp(node);
		p(node.getId().getText());
		outAVariableExp(node);
	}


	@Override
	public void caseANotEqBinaryOp(ANotEqBinaryOp node)
	{
		inANotEqBinaryOp(node);
		p("!=");
		outANotEqBinaryOp(node);
	}


	@Override
	public void caseAMinusBinaryOp(AMinusBinaryOp node)
	{
		inAMinusBinaryOp(node);
		p("-");
		outAMinusBinaryOp(node);
	}

	@Override
	public void caseAArrayAccessExp(AArrayAccessExp node)
	{
		inAArrayAccessExp(node);
		if(node.getArray() != null)
		{
			node.getArray().apply(this);
		}
		p("[");
		if(node.getIndex() != null)
		{
			node.getIndex().apply(this);
		}
		p("]");
		outAArrayAccessExp(node);
	}

	@Override
	public void caseAFieldAccessExp(AFieldAccessExp node)
	{
		inAFieldAccessExp(node);
		if(node.getExp() != null)
		{
			node.getExp().apply(this);
		}
		p(".");
		if(node.getId() != null)
		{
			p(node.getId().getText());
		}
		outAFieldAccessExp(node);
	}

	@Override
	public void inARuneExp(ARuneExp node) {
		defaultIn(node);
		p("'");
	}

	@Override
	public void outARuneExp(ARuneExp node) {
		p("'");
		defaultOut(node);
	}

	@Override
	public void outAEscapeRuneCharacter(AEscapeRuneCharacter node) {
		p(node.getCharEscape().getText());
		defaultOut(node);
	}

	@Override
	public void outARegularRuneCharacter(ARegularRuneCharacter node) {
		p(node.getRegularRuneCharacter().getText());
		defaultOut(node);
	}

	@Override
	public void outASqRuneCharacter(ASqRuneCharacter node) {
		p(node.getCharEscapeSq().getText());
		defaultOut(node);
	}

	@Override
	public void caseALitIntInteger(ALitIntInteger node)
	{
		inALitIntInteger(node);
		if(node.getLitInt() != null)
		{
			p(node.getLitInt().getText());
		}
		outALitIntInteger(node);
	}

	@Override
	public void caseAFloatExp(AFloatExp node)
	{
		inAFloatExp(node);
		if(node.getLitFloat() != null)
		{
			p(node.getLitFloat().getText());
		}
		outAFloatExp(node);
	}

	@Override
	public void caseALitHexInteger(ALitHexInteger node)
	{
		inALitHexInteger(node);
		if(node.getLitHex() != null)
		{
			p(node.getLitHex().getText());
		}
		outALitHexInteger(node);
	}

	@Override
	public void caseALitOctalInteger(ALitOctalInteger node)
	{
		inALitOctalInteger(node);
		if(node.getLitOctal() != null)
		{
			p(node.getLitOctal().getText());
		}
		outALitOctalInteger(node);
	}

	@Override
	public void inAInterpretedStringLiteral(AInterpretedStringLiteral node) {
		defaultIn(node);
		p("\"");
	}

	@Override
	public void outAInterpretedStringLiteral(AInterpretedStringLiteral node) {
		p("\"");
		defaultOut(node);
	}

	@Override
	public void outADqInterpretedStringPart(ADqInterpretedStringPart node) {
		p(node.getCharEscapeDq().getText());
		defaultOut(node);
	}

	@Override
	public void outAEscapeInterpretedStringPart(
			AEscapeInterpretedStringPart node) {
		p(node.getCharEscape().getText());
		defaultOut(node);
	}

	@Override
	public void outARegularInterpretedStringPart(
			ARegularInterpretedStringPart node) {
		p(node.getInterpretedStringChars().getText());
		defaultOut(node);
	}

	@Override
	public void outARawStringLiteral(ARawStringLiteral node) {
		p("`");
		if (node.getRawStringContents() != null)
			p(node.getRawStringContents().getText());
		p("`");
		defaultOut(node);
	}

	@Override
	public void caseAFunctionCallExp(AFunctionCallExp node)
	{
		inAFunctionCallExp(node);
		if(node.getFunctor() != null)
		{
			node.getFunctor().apply(this);
		}
		p("(");
		printList(node.getExp());
		p(")");
		outAFunctionCallExp(node);
	}

	@Override
	public void caseABaseTypeCastExp(ABaseTypeCastExp node)
	{
		inABaseTypeCastExp(node);
		if(node.getTypeExp() != null)
		{
			node.getTypeExp().apply(this);
		}
		p("(");
		if(node.getExp() != null)
		{
			node.getExp().apply(this);
		}
		p(")");
		outABaseTypeCastExp(node);
	}

	@Override
	public void caseAAppendExp(AAppendExp node)
	{
		inAAppendExp(node);
		p("append(");
		if(node.getId() != null)
		{
			p(node.getId().getText());
		}
		p(", ");
		if(node.getExp() != null)
		{
			node.getExp().apply(this);
		}
		p(")");
		outAAppendExp(node);
	}

	@Override
	public void caseABinaryExp(ABinaryExp node)
	{
		inABinaryExp(node);
		if(node.getLeft() != null)
		{
			if (node.getLeft() instanceof ABinaryExp) {
				p("(");
				node.getLeft().apply(this);
				p(")");
			} else {
				node.getLeft().apply(this);
			}
		}
		if(node.getBinaryOp() != null)
		{
			p(" ");
			node.getBinaryOp().apply(this);
			p(" ");
		}
		if(node.getRight() != null)
		{
			if (node.getRight() instanceof ABinaryExp) {
				p("(");
				node.getRight().apply(this);
				p(")");
			} else {
				node.getRight().apply(this);
			}
		}
		outABinaryExp(node);
	}

	@Override
	public void caseAUnaryExp(AUnaryExp node)
	{
		inAUnaryExp(node);
		if(node.getUnaryOp() != null)
		{
			node.getUnaryOp().apply(this);
		}
		if(node.getExp() != null)
		{
			node.getExp().apply(this);
		}
		outAUnaryExp(node);
	}

	@Override
	public void caseTId(TId node) {
		p(node.getText());
	}
}
