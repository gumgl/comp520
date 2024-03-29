/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import golite.node.* ;
import golite.analysis.* ;
import golite.typechecker.*;


public class TypeChecker extends DepthFirstAdapter {

	/** Map types for expression nodes, etc. */
	public HashMap<Node, Type> types = new HashMap<Node, Type>();

	/** Map variables to the number of nested nodes */
	public HashMap<TId, Integer> occurrenceCounts = new HashMap<TId, Integer>();

	public SymbolTable symbolTable;
	PositionHelper positionHelper;

	// Builtin types
	final BuiltInType boolType = new BuiltInType("bool");
	final BuiltInType intType = new BuiltInType("int");
	final BuiltInType floatType = new BuiltInType("float64");
	final BuiltInType runeType = new BuiltInType("rune");
	final BuiltInType stringType = new BuiltInType("string");
	final VoidType voidType = new VoidType();

	private Type functionReturnType;
	private Type switchType;

	public TypeChecker(PositionHelper positionHelper, SymbolTableLogger logger) {
		this.positionHelper = positionHelper;
		symbolTable = new SymbolTable(logger);
	}

	public TypeChecker(PositionHelper positionHelper) {
		this(positionHelper, null);
	}

	private Variable getVariable(TId id) {
		Symbol symbol = symbolTable.get(id.getText());

		if (symbol == null)
			errorSymbolNotFound(id, id.getText());
		else if (!(symbol instanceof Variable))
			errorSymbolClass(id, symbol, Variable.class);

		occurrenceCounts.put(id, symbolTable.countShadows(id.getText()));
		return (Variable) symbol;
	}

	private void addVariable(TId id, Type type) {
		symbolTable.addSymbol(new Variable(id.getText(), type));
		occurrenceCounts.put(id, symbolTable.countShadows(id.getText()));
	}

	static private String getSimpleNames(
			Collection<Class<? extends Symbol>> symbolClasses, String separator,
			String finalWord) {
		StringBuilder sb = new StringBuilder();

		int i = 1;
		int size = symbolClasses.size();
		for (Class<? extends Symbol> cls : symbolClasses) {
			if (i > 1)
				if (size > 1 && i == size)
					sb.append(finalWord);
				else
					sb.append(separator);
			sb.append(cls.getSimpleName());

			i++;
		}
		return sb.toString();
	}
	private void error(Node node, String message) {
		throw new GoLiteTypeException(positionHelper.lineAndPos(node)+": " + message);
	}

	// When a symbol is already declared
	private void errorSymbolDeclared(Node node, Symbol found) {
		error(node, "\"" + found.getId() + "\" already declared in current scope");
	}
	// When a symbol is not found in the symbol table
	private void errorSymbolNotFound(Node node, String id) {
		error(node, "Symbol \"" + id + "\" not found");
	}
	// e.g. When we expect the symbol to be a function instead of a variable
	@SafeVarargs
	final private void errorSymbolClass(Node node, Symbol found,
			Class<? extends Symbol>... expected) {
		String expectedString;

		if (expected.length == 1)
			expectedString = expected[0].getSimpleName();
		else
			expectedString = getSimpleNames(Arrays.asList(expected), ", ", " or ");

		error(node, "Expected " + found.getId() + " to be a " + expectedString);
	}
	// When we expect the symbol to be a certain type
	private void errorSymbolType(Node node, Type found, Type expected) {
		error(node, "Expected type " + expected.getRepresentation() + " instead of " + found.getRepresentation());
	}
	private void errorSymbolType(Node node, Type found, String expected) {
		error(node, "Expected " + expected + " instead of " + found.getRepresentation());
	}
	private void errorTypeCast(Node node, Type from, Type to) {
		error(node, "Cannot typecast " + from.getRepresentation() + " to "+  to.getRepresentation());
	}
	private void errorVoidFunctionAsValue(Node node) {
		if (node instanceof AFunctionCallExp) {
			PExp functorExp = ((AFunctionCallExp)node).getFunctor();
			Symbol funcSymbol = null;
			if (functorExp instanceof AVariableExp) {
				funcSymbol = symbolTable.get(((AVariableExp)functorExp).getId().getText());
			}
			if (funcSymbol instanceof Function) {
				Function func = (Function) funcSymbol;
				String funcRep = func.getId() + (func.getArguments().size()>0 ? "(...)" : "()");
				error(node, funcRep+" used as value");
			}
		} else {
			error(node, "function with no return used as value");
		}
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
			throw new RuntimeException(node.getClass().getSimpleName()
					+ " with no type at " + positionHelper.lineAndPos(node));
		}

		return t;
	}

	private void setType(Node node, Type type) {
		assert node instanceof PExp || node instanceof PFuncParam
			|| node instanceof PTypeExp || node instanceof AOpAssignStm;
		types.put(node, type);
	}

	private boolean canBeCast(Type type) {
		Type underlying = type.getUnderlying();
		return underlying instanceof BuiltInType && underlying != stringType;
	}

	private boolean isNumericType(Type type) {
		Type underlying = type.getUnderlying();
		return underlying == intType || underlying == floatType || underlying == runeType;
	}

	private boolean isIntegerType(Type type) {
		Type underlying = type.getUnderlying();
		return underlying == intType || underlying == runeType;
	}

	private boolean isOrderedType(Type type) {
		Type underlying = type.getUnderlying();
		return underlying instanceof BuiltInType && underlying != boolType;
	}

	private boolean isComparableType(Type type) {
		Type underlying = type.getUnderlying();

		if (underlying instanceof BuiltInType)
			return true;

		if (underlying instanceof ArrayType)
			return isComparableType(((ArrayType)underlying).getType());

		if (underlying instanceof StructType) {
			for (Variable v : ((StructType)underlying).getFields()) {
				if (!isComparableType(v.getType()))
					return false;
			}
			return true;
		}

		return false;
	}

	private boolean isBooleanType(Type type) {
		return type.getUnderlying() == boolType;
	}

	private boolean isBlankId(TId id) {
		return id.getText().equals("_");
	}

	
	public void defaultIn(Node node)
	{
		// Do nothing
	}
	public void defaultOut(Node node)
	{
		// Do nothing
	}

	@Override
	public void inStart(Start node) {
		symbolTable.addSymbol(new Variable("true", boolType));
		symbolTable.addSymbol(new Variable("false", boolType));

		// Shadow them
		symbolTable.addScope();
	}

	@Override
	public void outStart(Start node) {
		symbolTable.dropScope();
		defaultOut(node);
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

		if (isBlankId(id))
			return;

		ensureUndeclared(id);

		Function funcSignature = new Function(id.getText());
		PTypeExp returnTypeExp = node.getReturnType();

		// Expose the function return type to nested statements
		if (returnTypeExp == null)  {
			functionReturnType = voidType;
		} else {
			returnTypeExp.apply(this);
			functionReturnType = getType(returnTypeExp);
		}

		funcSignature.setReturnType(functionReturnType);

		// Add the symbol before checking the function body to support recursion
		symbolTable.addSymbol(funcSignature);
		occurrenceCounts.put(id, symbolTable.countShadows(id.getText()));

		symbolTable.addScope();
		for (PFuncParam param : node.getFuncParam()) {
			param.apply(this);
			funcSignature.addArguments(((AFuncParam)param).getId().size(), getType(param));
		}

		for (Node stm : node.getStm())
			stm.apply(this);

		symbolTable.dropScope();

		functionReturnType = null;

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

			if (isBlankId(id)) continue;

			ensureUndeclared(id);

			if (hasAssignments) {
				PExp value = node.getExp().get(i);

				// Value's type is not the same as the declared type
				if (!type.isIdentical(getType(value)))
					errorSymbolType(value, getType(value), type);
			}

			addVariable(id, type);
		}


		defaultOut(node);
	}
	// Basically the same as TypedVariableSpec but with type inference instead of typecheck
	public void outAUntypedVariableSpec(AUntypedVariableSpec node)
	{
		assert node.getId().size() == node.getExp().size();

		for (int i=0; i<node.getId().size(); i++) {
			TId id = node.getId().get(i);

			if (isBlankId(id)) continue;
			
			PExp value = node.getExp().get(i);
			Type type = getType(value);

			if (type.isIdentical(voidType))
				errorVoidFunctionAsValue(value);

			ensureUndeclared(id);
			addVariable(id, type);
		}
		defaultOut(node);
	}

	public void outATypeSpec(ATypeSpec node)
	{
		TId id = node.getId();
		if (isBlankId(id)) return;
		ensureUndeclared(id);
		AliasType type = new AliasType(id.getText(), getType(node.getTypeExp()));
		symbolTable.addSymbol(type);
		defaultOut(node);
	}

	public void outAFuncParam(AFuncParam node)
	{
		Type type = getType(node.getTypeExp());

		for (TId id : node.getId()) {
			ensureUndeclared(id);
			addVariable(id, type);
		}

		setType(node, type);
		defaultOut(node);
	}

	/* ****************** Type expressions ****************** */
	public void outAAliasTypeExp(AAliasTypeExp node)
	{
		TId id = node.getId();
		Symbol symbol = symbolTable.get(id.getText());
		if (symbol == null)
			errorSymbolNotFound(id, id.getText());
		else if ( !(symbol instanceof Type))
			errorSymbolClass(node, symbol, AliasType.class);
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
		symbolTable.addScope();
	}

	public void outAStructTypeExp(AStructTypeExp node)
	{
		StructType structType = new StructType();

		for (Symbol symbol : symbolTable.getSymbols()) {
			if (!(symbol instanceof Variable))
				errorSymbolClass(node, symbol, Variable.class);
			structType.addField((Variable)symbol);
		}

		symbolTable.dropScope();
		setType(node, structType);

		defaultOut(node);
	}
	public void outASliceTypeExp(ASliceTypeExp node)
	{
		SliceType sliceType = new SliceType(getType(node.getTypeExp()));
		setType(node, sliceType);
		defaultOut(node);
	}
	public void outAArrayTypeExp(AArrayTypeExp node)
	{
		Type typeOfArray = getType(node.getTypeExp());
		
		// FIXME: this is absolutely not what we should be doing here
		PInteger integer = node.getInteger();
		int arraySize;
		
		if (integer instanceof ALitIntInteger) {
			arraySize = Integer.parseInt(((ALitIntInteger) integer).getLitInt().getText());
		} else if (integer instanceof ALitHexInteger) {
			String base16Number = ((ALitHexInteger) integer).getLitHex().getText().substring(2);
			arraySize = Integer.parseInt(base16Number, 16);
		} else if (integer instanceof ALitOctalInteger) {
			String base8Number = (((ALitOctalInteger) integer).getLitOctal()).getText().substring(2);
			arraySize = Integer.parseInt(base8Number, 8);
		} else {
			throw new RuntimeException("unrecognized integer type: "+integer.getClass());
		}
		
		ArrayType arrayType = new ArrayType(typeOfArray, arraySize);
		setType(node, arrayType);
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

	/* ******************** Statements ******************** */
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
			
			if (value instanceof AVariableExp && isBlankId(((AVariableExp)value).getId())) {
				continue;
			}
			
			Type vType = getType(value);

			if ( ! lType.isIdentical(vType)) // Value's type is not the same as the declared type
				errorSymbolType(value, vType, lType);
		}
		defaultOut(node);
	}
	public void outAOpAssignStm(AOpAssignStm node)
	{
		boolean operatorFound = false;

		Node left = node.getLvalue();
		Node right = node.getExp();
		Type leftType = getType(left);
		Type rightType = getType(right);
		PAssignOp op = node.getAssignOp();

		if (!leftType.isIdentical(rightType))
			error(node, "Mismatched types "+leftType.getRepresentation()+" and "+rightType.getRepresentation());

		if (op instanceof APlusAssignOp) {
			if (!(isNumericType(leftType) || leftType.getUnderlying() == stringType))
				errorSymbolType(left, leftType, "numeric or string type");
			setType(node, leftType);
			operatorFound = true;
		} else if (op instanceof AMinusAssignOp || op instanceof AStarAssignOp
				|| op instanceof ASlashAssignOp || op instanceof APercentAssignOp) {

			if (!isNumericType(leftType))
				errorSymbolType(left, leftType, "numeric type");
			setType(node, leftType);
			operatorFound = true;
		} else if (op instanceof APipeAssignOp || op instanceof AAmpAssignOp
				|| op instanceof ALshiftAssignOp || op instanceof ARshiftAssignOp
				|| op instanceof AAmpCaretAssignOp || op instanceof ACaretAssignOp) {

			if (!isIntegerType(leftType))
				errorSymbolType(left, leftType, "integer type");

			setType(node, leftType);
			operatorFound = true;
		}

		if (!operatorFound)
			throw new IllegalArgumentException("Bad operator: "+op);
		
		defaultOut(node);
	}

	public void outAIncDecStm(AIncDecStm node)
	{
		PExp value = node.getExp();
		Type vType = getType(value);
		if (!isNumericType(vType)) {
			errorSymbolType(value, vType, "a numeric type"); //found vType, expected int/float
		}
		defaultOut(node);
	}

	// Override case so it won't recurse on the IDs
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

		PriorityQueue<String> namesSeen = new PriorityQueue<String>();

		// Ids are expressions here, but guaranteed by the weeder to be
		// variables
		for (int i=0; i < node.getIds().size(); i++) {
			AVariableExp idExp = (AVariableExp)node.getIds().get(i);
			
			if (isBlankId(idExp.getId()))
				continue;
			
			String id = idExp.getId().getText();

			// Ensure that the id is not a repeat
			if (namesSeen.contains(id))
				error(idExp, id+" repeated on left side of :=");
			namesSeen.add(id);

			// See if the id is previously declared and typecheck accordingly
			PExp exp = node.getExp().get(i);
			Symbol symbol = symbolTable.getInScope(id);
			Type varType;

			if (symbol == null) {
				hasNewVariable = true;
				varType = getType(exp);

				if (varType.isIdentical(voidType))
					errorVoidFunctionAsValue(exp);

				addVariable(idExp.getId(), varType);
			} else {
				if (!(symbol instanceof Variable))
					errorSymbolClass(node.getIds().get(i), symbol, Variable.class);

				varType = ((Variable)symbol).getType();

				if (!varType.isIdentical(getType(exp)))
					errorSymbolType(exp, getType(exp), varType);
			}

			setType(idExp, varType);
		}

		if (!hasNewVariable)
			error(node, "No new variable on left side of :=");

		defaultOut(node);
	}
	/* We don't need to do anything for variable and type declaration statements
	 * except for short variable declarations: in the other cases, all the work
	 * is done in the type or variable spec */
	public void outAVariableDecStm(AVariableDecStm node) {
		defaultOut(node);
	}
	public void outATypeDecStm(ATypeDecStm node) {
		defaultOut(node);
	}
	/*
	 * Per https://mycourses2.mcgill.ca/d2l/le/161312/discussions/threads/261614/View,
	 * only basic types are allowed in print/println statements
	 */
	public void outAPrintStm(APrintStm node)
	{
		for (Node e : node.getExp()) {
			if (!(getType(e).getUnderlying() instanceof BuiltInType)) {
				errorSymbolType(e, getType(e), "a basic type");
			}
		}
		defaultOut(node);
	}
	public void outAReturnStm(AReturnStm node)
	{
		Node returnedTypeExp = node.getExp();
		Type returnedType;

		// check exp has same type as enclosing function returnType

		if (returnedTypeExp != null){
			returnedType = getType(node.getExp());
		} else {
			returnedType = voidType;
		}

		if (!returnedType.isIdentical(functionReturnType)) {
			if (returnedTypeExp == null) {
				error(node, "Expected a value of type "
						+ functionReturnType.getRepresentation()
						+ " to be returned");
			} else {
				errorSymbolType(returnedTypeExp, returnedType, functionReturnType);
			}
		}

		defaultOut(node);
	}
	public void inABlockStm(ABlockStm node)
	{
		defaultIn(node);
		symbolTable.addScope();
	}
	public void outABlockStm(ABlockStm node)
	{
		symbolTable.dropScope();
		defaultOut(node);
	}
	public void caseAIfStm(AIfStm node)
	{
		inAIfStm(node);

		symbolTable.addScope();
		
		if (node.getStm() != null)
			node.getStm().apply(this);

		// Ensure exp is a bool
		PExp valueExp = node.getExp();
		
		valueExp.apply(this);
		
		Type expType = getType(valueExp);
		if (!isBooleanType(expType)){ //exp is not bool type
			errorSymbolType(valueExp, expType, boolType);
		}
		
		if (node.getIfBlock() != null) {
			symbolTable.addScope();
			for (PStm stm : node.getIfBlock())
				stm.apply(this);
			symbolTable.dropScope();
		}

		if (node.getElseBlock() != null) {
			symbolTable.addScope();
			for (PStm stm : node.getElseBlock())
				stm.apply(this);
			symbolTable.dropScope();
		}
		symbolTable.dropScope();

		outAIfStm(node);
	}

	@Override
	public void caseASwitchStm(ASwitchStm node) {
		inASwitchStm(node);
		symbolTable.addScope();

		if (node.getStm() != null) {
			node.getStm().apply(this);
		}

		// Handle nested switches: hold on to the previous type and
		// restore it at the end
		Type oldSwitchType = switchType;

		Node switchExp = node.getExp();
		if (switchExp == null) {
			switchType = boolType;
		} else {
			switchExp.apply(this);
			switchType = getType(switchExp);

			if (voidType.isIdentical(switchType)) {
				errorVoidFunctionAsValue(switchExp);
			}
		}

		for (Node clause : node.getSwitchClause()) {
			clause.apply(this);
		}

		switchType = oldSwitchType;
		symbolTable.dropScope();
		outASwitchStm(node);
	}

	/* Switch clauses: each opens its own scope. Non-default ("conditional")
	 * switch clauses also enforce other type invariants.
	 */
	@Override
	public void inASwitchClause(ASwitchClause node) {
		defaultIn(node);
		symbolTable.addScope();
	}

	@Override
	public void outASwitchClause(ASwitchClause node)
	{
		symbolTable.dropScope();
		defaultOut(node);
	}

	@Override
	public void outAConditionalSwitchCase(AConditionalSwitchCase node)
	{
		for (PExp caseExp : node.getExp()) {
			Type caseType = getType(caseExp);

			if (!caseType.isIdentical(switchType)) {
				errorSymbolType(caseExp, caseType, switchType);
			}
		}
		defaultOut(node);
	}

	@Override
	public void inAForStm(AForStm node)
	{
		defaultIn(node);
		symbolTable.addScope();
	}
	public void outAForStm(AForStm node)
	{
		if (node.getExp()==null){
			//An infinite for loop type checks if its body type checks
			//The body opens a new scope in the symbol table.
		} else {
			PExp valueExp = node.getExp();
			Type expType = getType(valueExp);
			if (!isBooleanType(expType)){//expected bool but found expType
				errorSymbolType(valueExp,expType,boolType);
			}
		}
		symbolTable.dropScope();
		defaultOut(node);
	}
	public void outABreakStm(ABreakStm node)//trivially well-typed
	{
		defaultOut(node);
	}
	public void outAContinueStm(AContinueStm node) //trivially well-typed
	{
		defaultOut(node);
	}

	/* ******************** Expressions ******************** */
	public void outAVariableExp(AVariableExp node)
	{
		Variable var = getVariable(node.getId());
		setType(node, var.getType());
		defaultOut(node);
	}
	public void outAArrayAccessExp(AArrayAccessExp node)
	{
		// Per the GoLite spec, we check that the index *is* an int, rather
		// than checking if its underlying type is
		if (!intType.isIdentical(getType(node.getIndex()))) {
			errorSymbolType(node.getIndex(), getType(node.getIndex()), intType);
		}

		Type underlyingTypeOfArray, typeArrayContains;
		underlyingTypeOfArray = getType(node.getArray()).getUnderlying();
		typeArrayContains = null;

		if (underlyingTypeOfArray instanceof ArrayType) {
			typeArrayContains = ((ArrayType)underlyingTypeOfArray).getType();
		} else if (underlyingTypeOfArray instanceof SliceType) {
			typeArrayContains = ((SliceType)underlyingTypeOfArray).getType();
		} else {
			errorSymbolType(node.getArray(), underlyingTypeOfArray, "an array or slice");
		}

		setType(node, typeArrayContains);
		defaultOut(node);
	}
	public void outAFieldAccessExp(AFieldAccessExp node)
	{
		Type t = getType(node.getExp()).getUnderlying();

		if (!(t instanceof StructType))
			errorSymbolType(node.getExp(), t, "a struct with a field named "+node.getId().getText());

		Variable field = ((StructType)t).getField(node.getId().getText());

		if (field == null)
			error(node.getId(), "Field "+node.getId().getText()+" is not defined for type "+t.getRepresentation());

		setType(node, field.getType());
		defaultOut(node);
	}
	@Override
	public void outAIntegerExp(AIntegerExp node)
	{
		setType(node, intType);
		defaultOut(node);
	}
	@Override
	public void outARuneExp(ARuneExp node) {
		setType(node, runeType);
		defaultOut(node);
	}
	@Override
	public void outAStringExp(AStringExp node) {
		setType(node, stringType);
		defaultOut(node);
	}
	@Override
	public void outAFloatExp(AFloatExp node)
	{
		setType(node, floatType);
		defaultOut(node);
	}

	/* Override the function case to prevent it from checking the functor; since
	 * the functor is a variable expression, the normal check tests whether it
	 * references a regular variable, but here of course it should be either a
	 * function or a type alias. The check is performed in outAFunctionCallExp.
	 */
	public void caseAFunctionCallExp(AFunctionCallExp node) {
		inAFunctionCallExp(node);
		for (PExp argument : node.getExp()) {
			argument.apply(this);
		}
		outAFunctionCallExp(node);
	}
	public void outAFunctionCallExp(AFunctionCallExp node)
	{
		PExp functorExp = node.getFunctor();

		if (!(functorExp instanceof AVariableExp)) {
			error(functorExp, "Function is not a variable");
		}

		TId functorId = ((AVariableExp)functorExp).getId();
		String identifier = functorId.getText();
		Symbol functorSymbol = symbolTable.get(identifier);

		if (functorSymbol == null)
			errorSymbolNotFound(functorId, identifier);

		occurrenceCounts.put(functorId, symbolTable.countShadows(identifier));

		List<PExp> exps = node.getExp();

		if (functorSymbol instanceof Type) {
			// First case: this is a type cast to an alias type
			// Invariants: there should be one argument, the node type should
			// be the type cast to.
			// The processTypeCast helper does the other work

			if (exps.size() != 1)
				error(node, "Expected an expression to typecast but saw a list of expressions");

			processTypeCast(node, (Type)functorSymbol, exps.get(0));
			setType(node, (Type)functorSymbol);
			setType(node.getFunctor(), (Type)functorSymbol);
		} else if (functorSymbol instanceof Function) {
			// Second case: this is a function call
			// Invariants: the number of arguments should match the function
			// definition, the type of each argument should match the
			// definition, the node type should be the return type (possibly void)

			Function func = (Function)functorSymbol;

			if (exps.size() != func.getArguments().size())
				error(node, "Wrong number of arguments");

			for (int i=0; i<exps.size(); i++) {
				PExp exp = exps.get(i);
				Type expType = getType(exp);
				Type argType = func.getArguments().get(i);
				if (!expType.isIdentical(argType))
					errorSymbolType(exp, expType, argType);
			}

			setType(node, func.getReturnType());
		} else {
			if (exps.size() != 1)
				errorSymbolClass(node, functorSymbol, Function.class);
			else
				errorSymbolClass(node, functorSymbol, Function.class, AliasType.class);
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

		if (canBeCast(from) && canBeCast(to))
			setType(node, to);
		else
			errorTypeCast(node, from, to);
	}

	public void outAAppendExp(AAppendExp node)
	{
		Variable slice = getVariable(node.getId());

		Type sliceType = slice.getType();
		Type underlyingSliceType = sliceType.getUnderlying();
		Type expType = getType(node.getExp());

		if (!(underlyingSliceType instanceof SliceType
				&& expType.isIdentical(((SliceType)underlyingSliceType).getType())))
			errorSymbolType(node.getId(), underlyingSliceType, new SliceType(expType));

		setType(node, sliceType);
		defaultOut(node);
	}
	public void outABinaryExp(ABinaryExp node)
	{
		boolean operatorFound = false;

		Node left = node.getLeft();
		Node right = node.getRight();
		Type leftType = getType(left);
		Type rightType = getType(right);
		PBinaryOp op = node.getBinaryOp();

		if (op instanceof ALogicalOrBinaryOp || op instanceof ALogicalAndBinaryOp) {
			// Enforce the type invariants by checking that the left argument
			// is a boolean and then ensuring that the right argument is of
			// the same type
			if (!isBooleanType(leftType))
				errorSymbolType(left, leftType, "boolean type");
			if (!leftType.isIdentical(rightType))
				errorSymbolType(right, rightType, leftType);

			// Return the specific boolean type of the expression
			setType(node, leftType);

			operatorFound = true;
		} else {
			if (!leftType.isIdentical(rightType))
				error(node, "Mismatched types "+leftType.getRepresentation()+" and "+rightType.getRepresentation());

			if (op instanceof AEqBinaryOp || op instanceof ANotEqBinaryOp) {
				if (!isComparableType(leftType))
					errorSymbolType(left, leftType, "comparable type");

				setType(node, boolType);
				operatorFound = true;
			} else if (op instanceof ALtEqBinaryOp || op instanceof ALtBinaryOp
					|| op instanceof AGtEqBinaryOp || op instanceof AGtBinaryOp) {

				if (!isOrderedType(leftType))
					errorSymbolType(left, leftType, "ordered type");

				setType(node, boolType);
				operatorFound = true;
			} else if (op instanceof APlusBinaryOp) {
				if (!(isNumericType(leftType) || leftType.getUnderlying() == stringType))
					errorSymbolType(left, leftType, "numeric or string type");
				setType(node, leftType);
				operatorFound = true;
			} else if (op instanceof AMinusBinaryOp || op instanceof AStarBinaryOp
					|| op instanceof ASlashBinaryOp || op instanceof APercentBinaryOp) {

				if (!isNumericType(leftType))
					errorSymbolType(left, leftType, "numeric type");
				setType(node, leftType);
				operatorFound = true;
			} else if (op instanceof APipeBinaryOp || op instanceof AAmpBinaryOp
					|| op instanceof ALshiftBinaryOp || op instanceof ARshiftBinaryOp
					|| op instanceof AAmpCaretBinaryOp || op instanceof ACaretBinaryOp) {

				if (!isIntegerType(leftType))
					errorSymbolType(left, leftType, "integer type");

				setType(node, leftType);
				operatorFound = true;
			}
		}

		if (!operatorFound)
			throw new IllegalArgumentException("Bad binary operator: "+op);

		defaultOut(node);
	}

	public void outAUnaryExp(AUnaryExp node)
	{
		Node exp = node.getExp();
		Type expType = getType(exp);
		PUnaryOp op = node.getUnaryOp();

		if (op instanceof APlusUnaryOp || op instanceof AMinusUnaryOp) {
			if (!isNumericType(expType))
				errorSymbolType(exp, expType, "numeric type");
		} else if (op instanceof AExclamationUnaryOp) {
			if (!isBooleanType(expType))
				errorSymbolType(exp, expType, "boolean type");
		} else if (op instanceof ACaretUnaryOp) {
			if (!isIntegerType(expType))
				errorSymbolType(exp, expType, "integer type");
		} else {
			throw new IllegalArgumentException("Bad unary operator: "+op);
		}

		setType(node, expType);
		defaultOut(node);
	}
}
