package golite;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import golite.node.* ;
import golite.analysis.* ;
import golite.typechecker.*;


public class TypeChecker extends DepthFirstAdapter {

	public HashMap<Node,Type> types = new HashMap<Node,Type>(); // Mapping a Type for every Node
	public SymbolTable symbolTable = new SymbolTable(null);
	PrintWriter stdout;
	PositionHelper positionHelper;

	// Builtin types
	final BuiltInType boolType = new BuiltInType("bool");
	final BuiltInType intType = new BuiltInType("int");
	final BuiltInType floatType = new BuiltInType("float64");
	final BuiltInType runeType = new BuiltInType("rune");
	final BuiltInType stringType = new BuiltInType("string");

	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("one");
		list.add("two");
		list.add("three");
		/*list.add("four");*/
		System.out.println(collectionToString(list, ", ", " and "));
	}

	public TypeChecker(PrintWriter out, PositionHelper positionHelper) {
		stdout = out;
		this.positionHelper = positionHelper;

		preDeclareBooleans();
	}

	static private String collectionToString(Collection collection, String separator, String finalWord) {
		StringBuilder sb = new StringBuilder();

		int i = 1;
		int size = collection.size();
		for (Object obj : collection) {
			if (i > 1)
				if (size > 1 && i == size)
					sb.append(finalWord);
				else
					sb.append(separator);
			sb.append(obj.toString());

			i++;
		}
		return sb.toString();
	}
	private void error(Node node, String message) {
		throw new GoLiteTypeException(positionHelper.lineAndPos(node)+": " + message);
	}

	// When a symbol is already declared
	private void errorSymbolDeclared(Node node, Symbol found) {
		error(node, "\"" + found + "\" already declared in current scope");
	}
	// When a symbol is not found in the symbol table
	private void errorSymbolNotFound(Node node, String id) {
		error(node, "Symbol \"" + id + "\" not found");
	}
	// e.g. When we expect the symbol to be a function instead of a variable
	private void errorSymbolClass(Node node, Symbol found, Class<? extends Symbol> expected) {
		error(node, "Expected " + found + " to be a " + expected.getSimpleName());
	}
	// e.g. When we expect the symbol to be a function instead of a variable
	private void errorSymbolClasses(Node node, Symbol found, Collection<Class<? extends Symbol> > expected) {
		error (node, "Expected " + found + " to be a " + collectionToString(expected, ",", "or"));
	}
	// When we expect the symbol to be a certain type
	private void errorSymbolType(Node node, Symbol found, Symbol expected) {
		error(node, "Expected a " + expected + " instead of a " + found);
	}
	private void errorTypeCast(Node node, Type from, Type to) {
		error(node, "Cannot typecast " + from + " to "+  to);
	}

	private void ensureUndeclared(Node node, String id) {
		Symbol symbol = symbolTable.getInScope(id);
		if (symbol != null)
			errorSymbolDeclared(node, symbol);
	}

	private void ensureUndeclared(TId id) {
		ensureUndeclared(id, id.getText());
	}

	private Type getType(Node node) {
		Type t = types.get(node);

		if (t == null) {
			throw new IllegalArgumentException(node.getClass().getSimpleName()+" with no type at "+positionHelper.lineAndPos(node));
		}

		return t;
	}
	private void setType(Node node, Type type) {
		types.put(node, type);
	}

	private void preDeclareBooleans() {
		symbolTable.addSymbol(new Variable("true", boolType));
		symbolTable.addSymbol(new Variable("false", boolType));

		// Shadow them
		symbolTable = symbolTable.newScope();
	}

	private boolean canBeCast(Type type) {
		return type instanceof BuiltInType && type != stringType;
	}

	public void defaultIn(@SuppressWarnings("unused") Node node)
	{
		// Do nothing
	}
	public void defaultOut(@SuppressWarnings("unused") Node node)
	{
		// Do nothing
	}
	/* ********* Top-level declarations **************** */

	// We don't need to do anything for variable declarations, checks are
	// done in each individual VariableSpec

	public void outATypeDeclaration(ATypeDeclaration node)
	{
		defaultOut(node);
	}

	@Override
	public void caseAFunctionDeclaration(AFunctionDeclaration node) {
		inAFunctionDeclaration(node);

		TId id = node.getId();

		ensureUndeclared(id);

		Function funcSignature = new Function(id.getText());
		PTypeExp returnType = node.getReturnType();

		if (returnType == null)  {
			funcSignature.setReturnType(new VoidType());
		} else {
			returnType.apply(this);
			funcSignature.setReturnType(getType(returnType));
		}

		// Add the symbol before checking the function body to support recursion
		symbolTable.addSymbol(funcSignature);

		symbolTable = symbolTable.newScope();
		for (PFuncParam param : node.getFuncParam()) {
			param.apply(this);
			funcSignature.addArguments(((AFuncParam)param).getId().size(), getType(param));
		}

		for (Node stm : node.getStm())
			stm.apply(this);

		symbolTable = symbolTable.popScope();

		outAFunctionDeclaration(node);
	}


	public void outATypedVariableSpec(ATypedVariableSpec node)
	{
		Type type = getType(node.getTypeExp());

		// The weeder ensures that either there are no assignments or
		// the number of variables matches the number of expressions
		boolean hasAssignments = (node.getExp().size() != 0);

		if (hasAssignments)
			assert node.getExp().size() == node.getId().size();

		for (int i=0; i<node.getId().size(); i++) {
			TId id = node.getId().get(i);

			ensureUndeclared(id);

			if (hasAssignments) {
				PExp value = node.getExp().get(i);

				// Value's type is not the same as the declared type
				if (!Type.Similar(getType(value), type))
					errorSymbolType(value, getType(value), type);
			}

			symbolTable.addSymbol(new Variable(id.getText(), type));
		}


		defaultOut(node);
	}
	// Basically the same as TypedVariableSpec but with type inference instead of typecheck
	public void outAUntypedVariableSpec(AUntypedVariableSpec node)
	{
		assert node.getId().size() == node.getExp().size();

		for (int i=0; i<node.getId().size(); i++) {
			TId id = node.getId().get(i);
			PExp value = node.getExp().get(i);
			Type type = getType(value);

			ensureUndeclared(id);
			symbolTable.addSymbol(new Variable(id.getText(), type));
		}
		defaultOut(node);
	}

	public void outATypeSpec(ATypeSpec node)
	{
		TId id = node.getId();
		ensureUndeclared(id);
		Type type = new AliasType(id.getText(), getType(node.getTypeExp()));
		symbolTable.addSymbol(type);
		defaultOut(node);
	}

	public void outAFuncParam(AFuncParam node)
	{
		Type type = getType(node.getTypeExp());

		for (TId id : node.getId()) {
			ensureUndeclared(id);
			symbolTable.addSymbol(new Variable(id.getText(), type));
		}

		setType(node, type);
		defaultOut(node);
	}
	public void outAAliasTypeExp(AAliasTypeExp node)
	{
		TId id = node.getId();
		Symbol symbol = symbolTable.get(id.getText());
		if (symbol == null)
			errorSymbolNotFound(id, id.getText());
		else if ( !(symbol instanceof Type))
			errorSymbolClass(node, symbol, Type.class);
		else
			setType(node, (Type) symbol);
		
		defaultOut(node);
	}
	public void outAIntTypeExp(AIntTypeExp node)
	{
		setType(node, intType);
		defaultOut(node);
	}
	public void outAFloat64TypeExp(AFloat64TypeExp node)
	{
		setType(node, floatType);
		defaultOut(node);
	}
	public void outABoolTypeExp(ABoolTypeExp node)
	{
		setType(node, boolType);
		defaultOut(node);
	}
	public void outARuneTypeExp(ARuneTypeExp node)
	{
		setType(node, runeType);
		defaultOut(node);
	}
	public void outAStringTypeExp(AStringTypeExp node)
	{
		setType(node, stringType);
		defaultOut(node);
	}
	@Override
	public void inAStructTypeExp(AStructTypeExp node)
	{
		defaultIn(node);
		// Create a new scope in which we declare fields as Variables
		symbolTable = symbolTable.newScope();
	}

	public void outAStructTypeExp(AStructTypeExp node)
	{
		StructType structType = new StructType();

		for (Symbol symbol : symbolTable.getSymbols()) {
			if (!(symbol instanceof Variable))
				errorSymbolClass(node, symbol, Variable.class);
			structType.addField((Variable)symbol);
		}

		symbolTable = symbolTable.popScope();
		symbolTable.addSymbol(structType);

		setType(node, structType);

		defaultOut(node);
	}
	public void outASliceTypeExp(ASliceTypeExp node)
	{
		SliceType sliceType = new SliceType();
		sliceType.setType(getType(node.getTypeExp()));
		symbolTable.addSymbol(sliceType);
		defaultOut(node);
	}
	public void outAArrayTypeExp(AArrayTypeExp node)
	{
		ArrayType arrayType = new ArrayType();
		arrayType.setType(getType(node.getTypeExp()));
		symbolTable.addSymbol(arrayType);
		defaultOut(node);
	}
	public void outAFieldDec(AFieldDec node)
	{
		Type type = getType(node.getTypeExp());

		for (TId id : node.getId()) {
			ensureUndeclared(id);
			symbolTable.addSymbol(new Variable(id.getText(), type));
		}
		defaultOut(node);
	}
	public void outAExpressionStm(AExpressionStm node)
	{
		defaultOut(node);
	}
	public void outAAssignStm(AAssignStm node)
	{
		assert node.getLvalue().size() == node.getExp().size();

		for (int i=0; i<node.getLvalue().size(); i++) {
			Type lType = getType(node.getLvalue().get(i));
			PExp value = node.getExp().get(i);
			Type vType = getType(value);

			if ( ! Type.Similar(lType, vType)) // Value's type is not the same as the declared type
				errorSymbolType(value, vType, lType);
		}
		defaultOut(node);
	}
	public void outAOpAssignStm(AOpAssignStm node)
	{
		defaultOut(node);
	}
	public void outAIncDecStm(AIncDecStm node)
	{
		defaultOut(node);
	}

	// Override case so it won't recurse on the IDs
	// FIXME: does this get the scoping right?
	@Override
	public void caseAShortVariableDecStm(AShortVariableDecStm node) {
		inAShortVariableDecStm(node);
		for (PExp exp : node.getExp()) {
			exp.apply(this);
		}
		outAShortVariableDecStm(node);
	}

	public void outAShortVariableDecStm(AShortVariableDecStm node)
	{
		assert node.getIds().size() == node.getExp().size();
		boolean hasNewVariable = false;

		// Ids are expressions here, but guaranteed by the weeder to be
		// variables
		for (int i=0; i < node.getIds().size(); i++) {
			String id = ((AVariableExp)node.getIds().get(i)).getId().getText();
			PExp exp = node.getExp().get(i);
			Symbol symbol = symbolTable.getInScope(id);

			if (symbol == null) {
				hasNewVariable = true;
				symbolTable.addSymbol(new Variable(id, getType(exp)));
			} else if (!(symbol instanceof Variable)) {
				errorSymbolClass(node.getIds().get(i), symbol, Variable.class);
			} else if (!Type.Similar(((Variable)symbol).getType(), getType(exp))) {
				errorSymbolType(exp, symbol, new Variable(id, getType(exp)));
			}
		}

		if (!hasNewVariable)
			error(node, "No new variable on left side of :=");

		defaultOut(node);
	}
	public void outAVariableDecStm(AVariableDecStm node)
	{
		defaultOut(node);
	}
	public void outATypeDecStm(ATypeDecStm node)
	{
		defaultOut(node);
	}
	public void outAPrintStm(APrintStm node)
	{
		defaultOut(node);
	}
	public void outAReturnStm(AReturnStm node)
	{
		defaultOut(node);
	}
	public void outAIfStm(AIfStm node)
	{
		defaultOut(node);
	}
	public void outASwitchStm(ASwitchStm node)
	{
		defaultOut(node);
	}
	public void outAForStm(AForStm node)
	{
		defaultOut(node);
	}
	public void outABreakStm(ABreakStm node)
	{
		defaultOut(node);
	}
	public void outAContinueStm(AContinueStm node)
	{
		defaultOut(node);
	}
	public void outAConditionalSwitchClause(AConditionalSwitchClause node)
	{
		defaultOut(node);
	}
	public void outADefaultSwitchClause(ADefaultSwitchClause node)
	{
		defaultOut(node);
	}
	public void outAFallthroughStm(AFallthroughStm node)
	{
		defaultOut(node);
	}
	public void outASimplePrintOp(ASimplePrintOp node)
	{
		defaultOut(node);
	}
	public void outALinePrintOp(ALinePrintOp node)
	{
		defaultOut(node);
	}
	public void outAIncPostfixOp(AIncPostfixOp node)
	{
		defaultOut(node);
	}
	public void outADecPostfixOp(ADecPostfixOp node)
	{
		defaultOut(node);
	}
	public void outAPlusAssignOp(APlusAssignOp node)
	{
		defaultOut(node);
	}
	public void outAMinusAssignOp(AMinusAssignOp node)
	{
		defaultOut(node);
	}
	public void outAStarAssignOp(AStarAssignOp node)
	{
		defaultOut(node);
	}
	public void outASlashAssignOp(ASlashAssignOp node)
	{
		defaultOut(node);
	}
	public void outAPercentAssignOp(APercentAssignOp node)
	{
		defaultOut(node);
	}
	public void outAAmpAssignOp(AAmpAssignOp node)
	{
		defaultOut(node);
	}
	public void outAPipeAssignOp(APipeAssignOp node)
	{
		defaultOut(node);
	}
	public void outACaretAssignOp(ACaretAssignOp node)
	{
		defaultOut(node);
	}
	public void outALshiftAssignOp(ALshiftAssignOp node)
	{
		defaultOut(node);
	}
	public void outARshiftAssignOp(ARshiftAssignOp node)
	{
		defaultOut(node);
	}
	public void outAAmpCaretAssignOp(AAmpCaretAssignOp node)
	{
		defaultOut(node);
	}
	public void outALogicalOrBinaryOp(ALogicalOrBinaryOp node)
	{
		defaultOut(node);
	}
	public void outALogicalAndBinaryOp(ALogicalAndBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAEqBinaryOp(AEqBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAGtBinaryOp(AGtBinaryOp node)
	{
		defaultOut(node);
	}
	public void outALtBinaryOp(ALtBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAGtEqBinaryOp(AGtEqBinaryOp node)
	{
		defaultOut(node);
	}
	public void outALtEqBinaryOp(ALtEqBinaryOp node)
	{
		defaultOut(node);
	}
	public void outANotEqBinaryOp(ANotEqBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAPlusBinaryOp(APlusBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAMinusBinaryOp(AMinusBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAStarBinaryOp(AStarBinaryOp node)
	{
		defaultOut(node);
	}
	public void outASlashBinaryOp(ASlashBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAPercentBinaryOp(APercentBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAAmpBinaryOp(AAmpBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAPipeBinaryOp(APipeBinaryOp node)
	{
		defaultOut(node);
	}
	public void outACaretBinaryOp(ACaretBinaryOp node)
	{
		defaultOut(node);
	}
	public void outALshiftBinaryOp(ALshiftBinaryOp node)
	{
		defaultOut(node);
	}
	public void outARshiftBinaryOp(ARshiftBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAAmpCaretBinaryOp(AAmpCaretBinaryOp node)
	{
		defaultOut(node);
	}
	public void outAPlusUnaryOp(APlusUnaryOp node)
	{
		defaultOut(node);
	}
	public void outAMinusUnaryOp(AMinusUnaryOp node)
	{
		defaultOut(node);
	}
	public void outAExclamationUnaryOp(AExclamationUnaryOp node)
	{
		defaultOut(node);
	}
	public void outACaretUnaryOp(ACaretUnaryOp node)
	{
		defaultOut(node);
	}
	public void outAVariableExp(AVariableExp node)
	{
		TId id = node.getId();
		Symbol symbol = symbolTable.get(id.getText());
		
		if (symbol == null)
			errorSymbolNotFound(id, id.getText());
		else if ( ! (symbol instanceof Variable))
			errorSymbolClass(id, symbol, Variable.class);
		else
			setType(node, ((Variable)symbol).getType());
		defaultOut(node);
	}
	public void outAArrayAccessExp(AArrayAccessExp node)
	{
		defaultOut(node);
	}
	public void inAFieldAccessExp(AFieldAccessExp node)
	{
		defaultIn(node);
		TId id = node.getId();
		Symbol symbol = symbolTable.get(id.getText());
		if (symbol == null)
			errorSymbolNotFound(id, id.getText());
	}
	public void outAFieldAccessExp(AFieldAccessExp node)
	{
		
		defaultOut(node);
	}
	public void outALitIntExp(ALitIntExp node)
	{
		setType(node, intType);
		defaultOut(node);
	}
	public void outALitFloatExp(ALitFloatExp node)
	{
		setType(node, floatType);
		defaultOut(node);
	}
	public void outALitHexExp(ALitHexExp node)
	{
		setType(node, intType);
		defaultOut(node);
	}
	public void outALitOctalExp(ALitOctalExp node)
	{
		setType(node, intType);
		defaultOut(node);
	}
	public void outALitInterpretedExp(ALitInterpretedExp node)
	{
		setType(node, stringType);
		defaultOut(node);
	}
	public void outALitRawExp(ALitRawExp node)
	{
		setType(node, stringType);
		defaultOut(node);
	}
	public void outALitRuneExp(ALitRuneExp node)
	{
		setType(node, runeType);
		defaultOut(node);
	}
	public void outAFunctionCallExp(AFunctionCallExp node)
	{
		String identifier = node.getId().getText();
		Symbol functorSymbol = symbolTable.get(identifier);

		if (functorSymbol == null)
			errorSymbolNotFound(node.getId(), identifier);

		List<PExp> exps = node.getExp();

		if (functorSymbol instanceof Type) {
			if (exps.size() != 1)
				error(node, "Expected an expression to typecast but saw a list of expressions");

			processTypeCast(node, (Type)functorSymbol, exps.get(0));
			setType(node, (Type)functorSymbol);
		} else if (functorSymbol instanceof Function) {
			Function func = (Function)functorSymbol;

			if (exps.size() != func.getArguments().size())
				error(node, "Wrong number of arguments");

			for (int i=0; i<exps.size(); i++) {
				PExp exp = exps.get(i);
				Type expType = getType(exp);
				Type argType = func.getArguments().get(i);
				if (!Type.Similar(expType, argType))
					errorSymbolType(exp, expType, argType);
			}

			setType(node, func.getReturnType());
		} else {
			if (exps.size() != 1)
				errorSymbolClass(node, functorSymbol, Function.class);

			Collection<Class<? extends Symbol>> expected = new ArrayList<Class<? extends Symbol>>();
			expected.add(Function.class);
			expected.add(Type.class);
			errorSymbolClasses(node, functorSymbol, expected);
		}
		defaultOut(node);
	}
	public void outABaseTypeCastExp(ABaseTypeCastExp node)
	{
		processTypeCast(node, getType(node.getTypeExp()), node.getExp());
		defaultOut(node);
	}

	private void processTypeCast(Node node, Type to, PExp exp) {
		Type from = getType(exp);

		if (canBeCast(from.getUnderlying()) && canBeCast(to.getUnderlying()))
			setType(node, to);
		else
			errorTypeCast(node, from, to);
	}

	public void outAAppendExp(AAppendExp node)
	{
		defaultOut(node);
	}
	public void outABinaryExp(ABinaryExp node)
	{
		defaultOut(node);
	}
	public void outAUnaryExp(AUnaryExp node)
	{
		defaultOut(node);
	}
}
