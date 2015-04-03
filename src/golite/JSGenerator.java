package golite;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import golite.node.* ;
import golite.typechecker.*;

public class JSGenerator extends PrintingASTAdapter {
	protected Map<Node, Type> types;
	protected Map<Type,Integer> copyHelperFunctions = new HashMap<Type,Integer>();
	protected Map<StructType,Integer> structInitializerHelperFunctions = new HashMap<StructType,Integer>();

	public JSGenerator(PrintWriter writer, Map<Node, Type> types) {
		super(writer);
		this.types = types;
		this.indent = "  ";
	}

	public JSGenerator(Map<Node, Type> types) {
		this(null, types);
	}

	/* ------------------- Value initialization helpers ------------------- */
	/**
	 * Transpile variable specs into a var declaration. Sample output:
	 *
	 * <pre>
	 *     //var (
	 *     //    x1, x2 int
	 *     //    y1, y2 = 42, 43
	 *     //    z1, z2 int = 1, 2
	 *     //)
	 *     var
	 *       x1 = 0,
	 *       x2 = 0,
	 *       y1 = 42,
	 *       y2 = 43,
	 *       z1 = 1,
	 *       z2 = 2;
	 * </pre>
	 *
	 * See also:
	 * https://golang.org/ref/spec#The_zero_value
	 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/var
	 */
	private void printVariableDeclaration(LinkedList<PVariableSpec> specs) {
		int specCount = specs.size();

		if (specCount == 1) {
			PVariableSpec first = specs.getFirst();

			// If there is a single variable being declared then print it on
			// a single line
			if ((first instanceof ATypedVariableSpec
						&& ((ATypedVariableSpec) first).getId().size() == 1)
					|| (first instanceof AUntypedVariableSpec
						&& ((AUntypedVariableSpec) first).getId().size() == 1)) {

				p("var ");
				specs.getFirst().apply(this);
				p(";");
				return;
			}
		}

		// Print one variable declaration per line
		p("var");
		endl();
		shift();

		int specsPrinted = 0;
		for (PVariableSpec spec : specs) {
			specsPrinted++;

			startl();
			spec.apply(this);

			if (specsPrinted != specCount) {
				p(",");
				endl();
			}
		}

		p(";");
		unshift();
	}

	/**
	 * Print an expression that reflects GoLite's pass by reference semantics
	 */
	private void printFreshCopy(PExp value, Type valueType) {
		valueType = valueType.getUnderlying();

		// Builtin types are already value type in JavaScript; slice types are
		// references in GoLite
		if (valueType instanceof BuiltInType || valueType instanceof SliceType) {
			value.apply(this);
		} else if (valueType instanceof ArrayType) {
			Type typeArrayContains = ((ArrayType)valueType).getType();

			if (typeArrayContains instanceof BuiltInType
					|| typeArrayContains instanceof SliceType) {
				value.apply(this);
				p(".slice()");
			} else {
				p(getCopyHelperFunction(valueType));
				p("(");
				value.apply(this);
				p(")");
			}
		} else if (valueType instanceof StructType) {
			p(getCopyHelperFunction(valueType));
			p("(");
			value.apply(this);
			p(")");
		} else {
			throw new IllegalArgumentException("unexpected type: "+valueType);
		}
	}

	/** Convenience variant */
	private void printNewCopy(PExp value) {
		printFreshCopy(value, types.get(value));
	}

	private void printDefaultValue(Type type) {
		type = type.getUnderlying();

		if (type instanceof BuiltInType) {
			switch (type.getRepresentation()) {
			case "int":
			case "rune":
				// FIXME: what is the best thing to do with runes?
				p("0");
				break;
			case "float64":
				// Technically this is the same as the int case,
				// but it's more intuitive this way
				p("0.0");
				break;
			case "bool":
				p("false");
				break;
			case "string":
				p("\"\"");
				break;
			default:
				throw new IllegalArgumentException("Unexpected builtin type "+type.getRepresentation());
			}
		} else if (type instanceof SliceType) {
			p("null");
		} else if (type instanceof ArrayType) {
			int arraySize = ((ArrayType) type).getSize();
			Type typeArrayContains = ((ArrayType) type).getType();

			p("[");
			// FIXME: this is wildly inefficient
			for (int i=0; i < arraySize; i++) {
				printDefaultValue(typeArrayContains);
				if (i < arraySize - 1) {
					p(", ");
				}
			}
			p("]");
		} else if (type instanceof StructType) {
			p(getStructInitializerHelperFunction((StructType) type));
			p("()");
		} else {
			throw new IllegalArgumentException("unexpected type: "+type);
		}
	}

	private String getStructInitializerHelperFunction(StructType type) {
		Integer helperIndex = structInitializerHelperFunctions.get(type);

		if (helperIndex == null) {
			helperIndex = structInitializerHelperFunctions.size();
			structInitializerHelperFunctions.put(type, helperIndex);
		}

		return "golite$initializeStruct"+helperIndex;
	}

	protected String getCopyHelperFunction(Type t) {
		Integer helperIndex = copyHelperFunctions.get(t);

		if (helperIndex == null) {
			helperIndex = copyHelperFunctions.size();
			copyHelperFunctions.put(t, helperIndex);
		}

		return "golite$copy"+helperIndex;
	}

	private void printTypeCoerced(PExp exp, Type targetType) {
		Type sourceType = types.get(exp).getUnderlying();
		targetType = targetType.getUnderlying();

		if (!sourceType.isIdentical(targetType)) {
			if (targetType.getRepresentation().equals("bool")) {
				p("(");
				exp.apply(this);
				p("!==0)");
				return;
			}

			if (sourceType.getRepresentation().equals("bool")
					|| sourceType.getRepresentation().equals("float64")) {
				p("(");
				exp.apply(this);
				p("|0)");
				return;
			}
		}

		exp.apply(this);
	}

	/* --------------------- Toplevel declarations --------------------- */

	@Override
	public void caseAProgram(AProgram node) {
		inAProgram(node);
		pln("/* Generated from GoLite source, package "+node.getPackageName().getText()+" */");
		pln("(function () {");
		shift();
		pln("'use strict';");

		printConsecutiveLines(node.getDeclarations());

		printShims();
		pln("main();");
		pln("if(golite$printbuffer.length>0)console.log(golite$printbuffer);");
		unshift();

		pln("})();");
		outAProgram(node);
	}

	private void printShims() {
		startl();
		p("var golite$printbuffer='';");
		p("function golite$print(){var a=arguments,b=[],i=0,o;for(o=a[0];i<a.length;o=a[++i]){b.push(o)};golite$printbuffer+=b.join(' ')}");
		p("function golite$println(){golite$print.apply(null,arguments);console.log(golite$printbuffer);golite$printbuffer=''}");

		for (Entry<StructType,Integer> entry : structInitializerHelperFunctions.entrySet()) {
			p("function golite$initializeStruct");
			p(entry.getValue().toString());
			p("(a){return{");

			boolean first = true;
			for (Variable field : entry.getKey().getFields()) {
				if (first) {
					first = false;
				} else {
					p(",");
				}

				p(field.getId());
				p(":");
				printDefaultValue(field.getType());
			}

			p("}}");
		}

		// Initialize helper expressions for code generation
		PExp arrayAccessExp =
				new AArrayAccessExp(new AVariableExp(new TId("a")), new AVariableExp(new TId("i")));
		AFieldAccessExp fieldAccessExp =
				new AFieldAccessExp(new AVariableExp(new TId("a")), null);

		for (Entry<Type,Integer> entry : copyHelperFunctions.entrySet()) {
			p("function golite$copy");
			p(entry.getValue().toString());
			p("(a)");

			Type entryType = entry.getKey();
			if (entryType instanceof ArrayType) {
				p("{var i,o=[];for(i=0;i<a.length;i++)o.push(");
				printFreshCopy(arrayAccessExp, ((ArrayType) entryType).getType());
				p(");return o}");
			} else if (entryType instanceof StructType) {
				p("{return{");

				boolean first = true;
				for (Variable field : ((StructType) entryType).getFields()) {
					if (first) {
						first = false;
					} else {
						p(",");
					}

					p(field.getId());
					p(":");

					// Access the field in the original and copy it
					fieldAccessExp.setId(new TId(field.getId()));
					printFreshCopy(fieldAccessExp, field.getType());
				}
				p("}}");
			} else {
				throw new IllegalArgumentException("unexpected type to copy: "+entryType.getClass());
			}
		}

		endl();
	}

	@Override
	public void caseAVariableDeclaration(AVariableDeclaration node)
	{
		inAVariableDeclaration(node);
		printVariableDeclaration(node.getVariableSpec());
		outAVariableDeclaration(node);
	}

	@Override
	public void caseATypedVariableSpec(ATypedVariableSpec node)
	{
		inATypedVariableSpec(node);

		if (node.getExp().isEmpty()) {
			printUninitializedVariables(node.getId(), types.get(node.getTypeExp()));
		} else {
			printInitializedVariables(node.getId(), node.getExp());
		}

		outATypedVariableSpec(node);
	}

	@Override
	public void caseAUntypedVariableSpec(AUntypedVariableSpec node)
	{
		inAUntypedVariableSpec(node);
		printInitializedVariables(node.getId(), node.getExp());
		outAUntypedVariableSpec(node);
	}

	private void printUninitializedVariables(List<TId> variables, Type varType) {
		Iterator<TId> variableIterator = variables.iterator();
		while (variableIterator.hasNext()) {
			TId variable = variableIterator.next();
			p(variable.getText());
			p(" = ");
			printDefaultValue(varType);

			if (variableIterator.hasNext()) {
				p(",");
				endl();
				startl();
			}
		}
	}

	private void printInitializedVariables(List<TId> variables, List<PExp> values) {
		assert variables.size() == values.size();

		Iterator<PExp> valueIterator = values.iterator();
		for (TId variable : variables) {
			PExp value = valueIterator.next();
			p(variable.getText());
			p(" = ");
			printNewCopy(value);

			if (valueIterator.hasNext()) {
				p(",");
				endl();
				startl();
			}
		}
	}

	@Override
	public void caseATypeDeclaration(ATypeDeclaration node) {
		inATypeDeclaration(node);
		// Don't recurse
		outATypeDeclaration(node);
	}

	/* --------------------- Function declarations --------------------- */
	@Override
	public void caseAFunctionDeclaration(AFunctionDeclaration node)
	{
		inAFunctionDeclaration(node);
		p("function ");
		p(node.getId().getText());

		p("(");
		printList(node.getFuncParam());
		p(")");

		// We ignore the return type
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
	public void caseAFuncParam(AFuncParam node)
	{
		inAFuncParam(node);
		printList(node.getId());

		// Ignore the function type
		outAFuncParam(node);
	}

	/* ------------------- Scoping and control structure ------------------- */

	/**
	 * Open a block scope. Since JS doesn't actually have those, use a
	 * self-executing function
	 */
	protected void openBlock() {
		p("(function () {");
		endl();
		shift();
	}

	/** Close a block scope */
	protected void closeBlock() {
		unshift();
		startl();
		p("})();");
	}

	@Override
	public void caseABlockStm(ABlockStm node) {
		inABlockStm(node);
		openBlock();
		printConsecutiveLines(node.getStm());
		closeBlock();
		outABlockStm(node);
	}

	@Override
	public void caseAIfStm(AIfStm node)
	{
		inAIfStm(node);

		PStm initStm = node.getStm();
		if (initStm != null) {
			openBlock();
			startl();
			initStm.apply(this);
			endl();
		}

		p("if (");
		node.getExp().apply(this);
		p(") {");
		endl();
		shift();

		printConsecutiveLines(node.getIfBlock());

		unshift();
		startl();
		p("}");

		if (!node.getElseBlock().isEmpty()) {
			List<PStm> elseBlock = node.getElseBlock();
			p(" else ");

			// Handle the else-if construct
			if (elseBlock.size() == 1 && elseBlock.get(0) instanceof AIfStm
					&& ((AIfStm) elseBlock.get(0)).getStm() == null) {
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

		if (initStm != null) {
			closeBlock();
		}

		outAIfStm(node);
	}

	@Override
	public void caseAForStm(AForStm node) {
		inAForStm(node);
		p("for (");

		if(node.getInit() != null) {
			node.getInit().apply(this);
		}

		p(";");

		if(node.getExp() != null) {
			node.getExp().apply(this);
		}

		p("; ");

		if(node.getPost() != null) {
			node.getPost().apply(this);
		}

		p(") {");
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
		p("break;");
		outABreakStm(node);
	}

	@Override
	public void caseAContinueStm(AContinueStm node)
	{
		inAContinueStm(node);
		p("continue;");
		outAContinueStm(node);
	}

	@Override
	public void caseAReturnStm(AReturnStm node)
	{
		inAReturnStm(node);
		p("return");
		if(node.getExp() != null) {
			p(" ");
			node.getExp().apply(this);
		}
		p(";");
		outAReturnStm(node);
	}

	//TODO
//	@Override
//	public void caseASwitchStm(ASwitchStm node)
//	{
//		inASwitchStm(node);
//		p("switch ");
//		if(node.getStm() != null)
//		{
//			node.getStm().apply(this);
//			p("; ");
//		}
//		if(node.getExp() != null)
//		{
//			node.getExp().apply(this);
//			p(" ");
//		}
//		p("{");
//		endl();
//		{
//			for(PSwitchClause e : node.getSwitchClause())
//			{
//				e.apply(this);
//			}
//		}
//		startl();
//		p("}");
//		outASwitchStm(node);
//	}
//
//	@Override
//	public void caseAConditionalSwitchClause(AConditionalSwitchClause node)
//	{
//		inAConditionalSwitchClause(node);
//		startl();
//		p("case ");
//		printList(node.getExp());
//		p(":");
//		endl();
//		shift();
//		printConsecutiveLines(node.getStm());
//		if(node.getFallthroughStm() != null)
//		{
//			startl();
//			node.getFallthroughStm().apply(this);
//			endl();
//		}
//		unshift();
//		outAConditionalSwitchClause(node);
//	}
//
//	@Override
//	public void caseADefaultSwitchClause(ADefaultSwitchClause node)
//	{
//		inADefaultSwitchClause(node);
//		startl();
//		p("default:");
//		endl();
//		shift();
//		printConsecutiveLines(node.getStm());
//		if(node.getFallthroughStm() != null)
//		{
//			startl();
//			node.getFallthroughStm().apply(this);
//			endl();
//		}
//		unshift();
//		outADefaultSwitchClause(node);
//	}

	/* --------------------- Statements --------------------- */
	@Override
	public void caseAExpressionStm(AExpressionStm node)
	{
		inAExpressionStm(node);
		node.getExp().apply(this);
		p(";");
		outAExpressionStm(node);
	}

	@Override
	public void caseAAssignStm(AAssignStm node)
	{
		inAAssignStm(node);

		List<PExp> variables = node.getLvalue();
		List<PExp> values = node.getExp();

		assert variables.size() == values.size();

		Iterator<PExp> valueIterator = values.iterator();
		for (PExp variable : variables) {
			PExp value = valueIterator.next();

			variable.apply(this);
			p(" = ");
			printNewCopy(value);
			p(";");

			if (valueIterator.hasNext()) {
				endl();
				startl();
			}
		}

		outAAssignStm(node);
	}

	@Override
	public void caseAVariableDecStm(AVariableDecStm node)
	{
		inAVariableDecStm(node);
		printVariableDeclaration(node.getVariableSpec());
		outAVariableDecStm(node);
	}

	@Override
	public void caseAShortVariableDecStm(AShortVariableDecStm node)
	{
		inAShortVariableDecStm(node);

		// Extract a list of ID tokens for the helper method
		int size = node.getIds().size();
		List<TId> ids = new ArrayList<TId>(size);
		for (PExp var : node.getIds()) {
			ids.add(((AVariableExp) var).getId());
		}

		// If there is only one variable then print it on a single line
		if (size == 1) {
			p("var ");
		} else {
			p("var");
			endl();
			shift();
			startl();
		}

		printInitializedVariables(ids, node.getExp());
		p(";");

		if (size != 1) {
			unshift();
		}

		outAShortVariableDecStm(node);
	}

	@Override
	public void caseAOpAssignStm(AOpAssignStm node)
	{
		inAOpAssignStm(node);
		node.getLvalue().apply(this);
		p(" ");
		node.getAssignOp().apply(this);
		p(" ");
		node.getExp().apply(this);
		p(";");
		outAOpAssignStm(node);
	}

	@Override
	public void caseATypeDecStm(ATypeDecStm node) {
		inATypeDecStm(node);
		// Don't recurse
		outATypeDecStm(node);
	}

	@Override
	public void caseAIncDecStm(AIncDecStm node)
	{
		inAIncDecStm(node);
		node.getExp().apply(this);
		node.getPostfixOp().apply(this);
		p(";");
		outAIncDecStm(node);
	}

	@Override
	public void caseAPrintStm(APrintStm node) {
		inAPrintStm(node);

		// This will require helper shims since console.log always
		// appends a line
		if (node.getPrintOp() instanceof ALinePrintOp) {
			p("golite$println");
		} else {
			p("golite$print");
		}

		p("(");
		printList(node.getExp());
		p(");");
		outAPrintStm(node);
	}

	/* --------------------- Expressions --------------------- */
	public void caseAVariableExp(AVariableExp node)
	{
		inAVariableExp(node);
		p(node.getId().getText());
		outAVariableExp(node);
	}

	@Override
	public void caseAArrayAccessExp(AArrayAccessExp node)
	{
		inAArrayAccessExp(node);
		node.getArray().apply(this);
		p("[");
		node.getIndex().apply(this);
		p("]");
		outAArrayAccessExp(node);
	}

	@Override
	public void caseAFieldAccessExp(AFieldAccessExp node)
	{
		inAFieldAccessExp(node);
		node.getExp().apply(this);
		p(".");
		p(node.getId().getText());
		outAFieldAccessExp(node);
	}

	@Override
	public void caseAFunctionCallExp(AFunctionCallExp node)
	{
		inAFunctionCallExp(node);

		// FIXME
		Type functorType = types.get(node.getFunctor());

		if (functorType instanceof Type) {
			printTypeCoerced(node.getExp().get(0), functorType);
		} else {
			node.getFunctor().apply(this);
			p("(");

			boolean first = true;
			for (PExp exp : node.getExp()) {
				if (first) {
					first = false;
				} else {
					p(", ");
				}
				printNewCopy(exp);
			}

			p(")");
		}

		outAFunctionCallExp(node);
	}

	@Override
	public void caseABaseTypeCastExp(ABaseTypeCastExp node)
	{
		inABaseTypeCastExp(node);
		printTypeCoerced(node.getExp(), types.get(node.getTypeExp()));
		outABaseTypeCastExp(node);
	}

	/**
	 * This is a cryptic one-liner, but I assume this is faster than calling
	 * a helper function. Given
	 *
	 *     append(s, v)
	 *
	 * Output
	 *
	 *     (s===null?[v]:(s.push(v),s))
	 */
	@Override
	public void caseAAppendExp(AAppendExp node)
	{
		inAAppendExp(node);

		String variableId = node.getId().getText();

		p("(");
		p(variableId);
		p("===null?[");
		node.getExp().apply(this);
		p("]:(");
		p(variableId);
		p(".push(");
		node.getExp().apply(this);
		p("),");
		p(variableId);
		p("))");

		outAAppendExp(node);
	}

	@Override
	public void caseABinaryExp(ABinaryExp node)
	{
		inABinaryExp(node);

		if (node.getLeft() instanceof ABinaryExp) {
			p("(");
			node.getLeft().apply(this);
			p(")");
		} else {
			node.getLeft().apply(this);
		}

		p(" ");
		node.getBinaryOp().apply(this);
		p(" ");

		if (node.getRight() instanceof ABinaryExp) {
			p("(");
			node.getRight().apply(this);
			p(")");
		} else {
			node.getRight().apply(this);
		}

		outABinaryExp(node);
	}

	@Override
	public void caseAUnaryExp(AUnaryExp node) {
		inAUnaryExp(node);
		node.getUnaryOp().apply(this);
		node.getExp().apply(this);
		outAUnaryExp(node);
	}

	@Override
	public void caseTId(TId node) {
		p(node.getText());
	}

	/* --------------------- Literals --------------------- */

	// Runes: I think printing these as numbers makes most sense, but
	// I'm not positive

	@Override
	public void outARegularRuneCharacter(ARegularRuneCharacter node) {
		p(((int)node.getRegularRuneCharacter().getText().charAt(0))+"");
		defaultOut(node);
	}

	@Override
	public void outAEscapeRuneCharacter(AEscapeRuneCharacter node) {
		switch (node.getCharEscape().getText().charAt(1)) {
		case 'a':
			// bell
			p("7");
			break;

		case 'b':
			p(((int)'\b')+"");
			break;

		case 'f':
			p(((int)'\f')+"");
			break;

		case 'n':
			p(((int)'\n')+"");
			break;

		case 'r':
			p(((int)'\r')+"");
			break;

		case 't':
			p(((int)'\t')+"");
			break;

		case 'v':
			// vertical tab
			p("11");
			break;

		case '\\':
			p(((int)'\\')+"");
			break;

		default:
			throw new IllegalArgumentException("unexpected escape sequence: "
					+ node.getCharEscape().getText());
		}
		defaultOut(node);
	}

	@Override
	public void outASqRuneCharacter(ASqRuneCharacter node) {
		p(((int)'\'')+"");
		defaultOut(node);
	}

	@Override
	public void caseAFloatExp(AFloatExp node)
	{
		inAFloatExp(node);
		p(node.getLitFloat().getText());
		outAFloatExp(node);
	}

	@Override
	public void caseALitIntInteger(ALitIntInteger node)
	{
		inALitIntInteger(node);
		p(node.getLitInt().getText());
		outALitIntInteger(node);
	}

	@Override
	public void caseALitHexInteger(ALitHexInteger node)
	{
		inALitHexInteger(node);
		p(node.getLitHex().getText());
		outALitHexInteger(node);
	}

	@Override
	public void caseALitOctalInteger(ALitOctalInteger node)
	{
		inALitOctalInteger(node);
		p(node.getLitOctal().getText());
		outALitOctalInteger(node);
	}

	@Override
	public void outARawStringLiteral(ARawStringLiteral node) {
		p("'");
		if (node.getRawStringContents() != null) {
			// TODO
		}
		p("'");
		defaultOut(node);
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

	/* --------------------- Operators --------------------- */
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
	@Override
	public void caseANotEqBinaryOp(ANotEqBinaryOp node)
	{
		p("!=");
	}

	@Override
	public void caseAMinusBinaryOp(AMinusBinaryOp node)
	{
		p("-");
	}

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
}
