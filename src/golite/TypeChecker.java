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

		preDeclare();
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
	private void preDeclare() {
		BuiltInType boolType = new BuiltInType("bool");
		BuiltInType intType = new BuiltInType("int");
		BuiltInType floatType = new BuiltInType("float64");
		BuiltInType runeType = new BuiltInType("rune");
		BuiltInType stringType = new BuiltInType("string");
	
		Variable trueVar = new Variable("true", boolType);
		Variable falseVar = new Variable("false", boolType);
	
		BuiltInType.builtIns.add(boolType);
		BuiltInType.builtIns.add(intType);
		BuiltInType.builtIns.add(floatType);
		BuiltInType.builtIns.add(runeType);
		BuiltInType.builtIns.add(stringType);
		
		BuiltInType.typeCastTypes.add(boolType);
		BuiltInType.typeCastTypes.add(intType);
		BuiltInType.typeCastTypes.add(floatType);
		BuiltInType.typeCastTypes.add(runeType);
		
		symbolTable.addSymbols(BuiltInType.builtIns);
		symbolTable.addSymbol(trueVar);
		symbolTable.addSymbol(falseVar);
		
		symbolTable.newScope(); // Shadow them
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

		if (returnType != null) {
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
		Type type = getType(node.getTypeExp());

		// Verify if ID is available
		TId id = node.getId();

		ensureUndeclared(id);

		type.setId(id.getText());

		// Add to scope
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
		setType(node, (Type)symbolTable.get("int"));
		defaultOut(node);
	}
	public void outAFloat64TypeExp(AFloat64TypeExp node)
	{
		setType(node, (Type)symbolTable.get("float64"));
		defaultOut(node);
	}
	public void outABoolTypeExp(ABoolTypeExp node)
	{
		setType(node, (Type)symbolTable.get("bool"));
		defaultOut(node);
	}
	public void outARuneTypeExp(ARuneTypeExp node)
	{
		setType(node, (Type)symbolTable.get("rune"));
		defaultOut(node);
	}
	public void outAStringTypeExp(AStringTypeExp node)
	{
		setType(node, (Type)symbolTable.get("string"));
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
		setType(node, (Type)symbolTable.get("int"));
		defaultOut(node);
	}
	public void outALitFloatExp(ALitFloatExp node)
	{
		setType(node, (Type)symbolTable.get("float64"));
		defaultOut(node);
	}
	public void outALitHexExp(ALitHexExp node)
	{
		setType(node, (Type)symbolTable.get("int"));
		defaultOut(node);
	}
	public void outALitOctalExp(ALitOctalExp node)
	{
		setType(node, (Type)symbolTable.get("int"));
		defaultOut(node);
	}
	public void outALitInterpretedExp(ALitInterpretedExp node)
	{
		setType(node, (Type)symbolTable.get("string"));
		defaultOut(node);
	}
	public void outALitRawExp(ALitRawExp node)
	{
		setType(node, (Type)symbolTable.get("string"));
		defaultOut(node);
	}
	public void outALitRuneExp(ALitRuneExp node)
	{
		setType(node, (Type)symbolTable.get("rune"));
		defaultOut(node);
	}
	public void outAFunctionCallExp(AFunctionCallExp node)
	{
		defaultOut(node);
	}
	public void outABaseTypeCastExp(ABaseTypeCastExp node)
	{
		Collection<Class<? extends Symbol>> allowed = new ArrayList<Class<? extends Symbol>>();
		allowed.add(BuiltInType.class);
		allowed.add(AliasType.class);
		
		Type from = getType(node.getExp());
		Type to = getType(node.getTypeExp());
		
		Type underlyingFrom = from.getUnderlying();
		Type underlyingTo = to.getUnderlying();

		if (BuiltInType.typeCastTypes.contains(underlyingFrom) // Check if it's a type we can typecast 
			&& BuiltInType.typeCastTypes.contains(underlyingTo)
			&& Type.Similar(underlyingFrom, underlyingTo)) // Check if they are the same type
			setType(node, to);
		else
			errorTypeCast(node, underlyingFrom, underlyingTo);
		
		defaultOut(node);
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
