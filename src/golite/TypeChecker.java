package golite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import golite.node.* ;
import golite.analysis.* ;
import golite.typechecker.*;


public class TypeChecker extends DepthFirstAdapter {

	public HashMap<Node,Type> types = new HashMap<Node,Type>(); // Mapping a Type for every Node
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

	public TypeChecker(PositionHelper positionHelper, SymbolTableLogger logger) {
		this.positionHelper = positionHelper;
		symbolTable = new SymbolTable(logger);
	}

	public TypeChecker(PositionHelper positionHelper) {
		this(positionHelper, null);
	}

	static private String collectionToString(Collection<?> collection, String separator, String finalWord) {
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
		error(node, "\"" + found.getId() + "\" already declared in current scope");
	}
	// When a symbol is not found in the symbol table
	private void errorSymbolNotFound(Node node, String id) {
		error(node, "Symbol \"" + id + "\" not found");
	}
	// e.g. When we expect the symbol to be a function instead of a variable
	private void errorSymbolClass(Node node, Symbol found, Class<? extends Symbol> expected) {
		error(node, "Expected " + found.getId() + " to be a " + expected.getSimpleName());
	}
	// e.g. When we expect the symbol to be a function instead of a variable
	private void errorSymbolClasses(Node node, Symbol found, Collection<Class<? extends Symbol> > expected) {
		error (node, "Expected " + found.getId() + " to be a " + collectionToString(expected, ",", "or"));
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
		return underlying instanceof BuiltInType
				|| underlying instanceof ArrayType
				|| underlying instanceof StructType;
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

			if (isBlankId(id)) continue;
			
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
			symbolTable.addSymbol(new Variable(id.getText(), type));
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
		int arraySize = Integer.parseInt(node.getLitInt().getText());

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

		// Ids are expressions here, but guaranteed by the weeder to be
		// variables
		for (int i=0; i < node.getIds().size(); i++) {
			AVariableExp idExp = (AVariableExp)node.getIds().get(i);
			
			if (isBlankId(idExp.getId()))
				continue;
			
			String id = idExp.getId().getText();
			
			PExp exp = node.getExp().get(i);
			Symbol symbol = symbolTable.getInScope(id);
			Type varType;

			if (symbol == null) {
				hasNewVariable = true;
				varType = getType(exp);
				symbolTable.addSymbol(new Variable(id, varType));
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
		Type returnedType;

		// check exp has same type as enclosing function returnType

		if (node.getExp()!=null){
			returnedType = getType(node.getExp());
		} else {
			returnedType = voidType;
		}

		if (!returnedType.isIdentical(functionReturnType)) {
			errorSymbolType(node.getExp(), returnedType, functionReturnType);
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
	public void outASwitchStm(ASwitchStm node)
	{
		boolean hasExp = false;
		Type expType = null;
		
		if (node.getExp()!=null){ //switch has an expr
			hasExp = true;
			PExp valueExp = node.getExp();
			expType = getType (valueExp);
		}	
			//if switch has expr, checks if cases e1, e2, . . . , en have same type as valueExp
			//else, checks if cases e1, e2, . . . , en are well-typed and have type bool
		for (int i=0; i<node.getSwitchClause().size();i++){
			Node valueSwitch = node.getSwitchClause().get(i);
			Type switchType = getType(valueSwitch);
			if (!hasExp) {
				if(!isBooleanType(switchType)){ //case type is not boolType
					errorSymbolType(valueSwitch,switchType, boolType);
				}
			} else { 
				if(!expType.isIdentical(switchType)){ //case type is not expType
					errorSymbolType(valueSwitch,switchType, expType);
				}
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
	public void outAConditionalSwitchClause(AConditionalSwitchClause node)
	{
		PExp first = node.getExp().get(0);
		Type firstType = getType(first);
		if (isBooleanType(firstType)){
			//all the rest cases should have boolType			
			for (int i=1; i<node.getExp().size(); i++){
				PExp valueExp = node.getExp().get(i);
				Type expType = getType(valueExp);
				if (!isBooleanType (expType)){
					errorSymbolType(valueExp, expType, "boolean type");
				}
			}
		setType(node,boolType);
		} else { 
			//check all the rest have the same type as expType
		for (int i=0; i<node.getExp().size(); i++){
			PExp valueExp = node.getExp().get(i);
			Type expType = getType(valueExp);
			if (!expType.isIdentical(firstType)){
				errorSymbolType(valueExp, expType, "boolean type");
			}
		}
		setType(node,firstType);
		}
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

	/* ******************** Expressions ******************** */
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
		Type t = getType(node.getArray());

		// Per the GoLite spec, we check that the index *is* an int, rather
		// than checking if its underlying type is
		if (!intType.isIdentical(getType(node.getIndex()))) {
			errorSymbolType(node.getIndex(), getType(node.getIndex()), intType);
		}

		if (!(t instanceof ArrayType || t instanceof SliceType))
			errorSymbolType(node.getArray(), t, "an array or slice");

		setType(node, t);
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
				if (!expType.isIdentical(argType))
					errorSymbolType(exp, expType, argType);
			}

			setType(node, func.getReturnType());
		} else {
			if (exps.size() != 1)
				errorSymbolClass(node, functorSymbol, Function.class);

			Collection<Class<? extends Symbol>> expected = new ArrayList<Class<? extends Symbol>>();
			expected.add(Function.class);
			expected.add(AliasType.class);
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

		if (canBeCast(from) && canBeCast(to))
			setType(node, to);
		else
			errorTypeCast(node, from, to);
	}

	public void outAAppendExp(AAppendExp node)
	{
		Symbol slice = symbolTable.get(node.getId().getText());

		if (slice == null)
			errorSymbolNotFound(node.getId(), node.getId().getText());

		if (!(slice instanceof Variable))
			errorSymbolClass(node.getId(), slice, Variable.class);

		Type sliceType = ((Variable)slice).getType();
		Type expType = getType(node.getExp());

		if (!(sliceType instanceof SliceType && expType.isIdentical(((SliceType)sliceType).getType())))
			errorSymbolType(node.getId(), sliceType, new SliceType(expType));

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
			if (!isBooleanType(leftType))
				errorSymbolType(left, leftType, "boolean type");
			if (!isBooleanType(rightType))
				errorSymbolType(left, rightType, "boolean type");

			setType(node, boolType);

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
