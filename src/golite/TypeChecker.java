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
		return type.getUnderlying() instanceof BuiltInType;
	}

	private boolean isBooleanType(Type type) {
		return type.getUnderlying() == boolType;
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

		ensureUndeclared(id);

		Function funcSignature = new Function(id.getText());
		PTypeExp returnType = node.getReturnType();

		if (returnType == null)  {
			funcSignature.setReturnType(voidType);
		} else {
			returnType.apply(this);
			funcSignature.setReturnType(getType(returnType));
		}

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
			Type vType = getType(value);

			if ( ! lType.isIdentical(vType)) // Value's type is not the same as the declared type
				errorSymbolType(value, vType, lType);
		}
		defaultOut(node);
	}
	public void outAOpAssignStm(AOpAssignStm node)
	{
			Type lType = getType(node.getLvalue());
			PExp valueExp = node.getExp();
			Type vType = getType(valueExp);
			//PAssignOp valueOp = node.getAssignOp();
			//Type opType = getType(valueOp);
			//valueOp accept (lType,vType) and returns vType
			
		defaultOut(node);
	}

	public void outAIncDecStm(AIncDecStm node)
	{
		PExp value = node.getExp();
		Type vType = getType(value);
		//PPostfixOp node.getPostfixOp()
		if (!isNumericType(vType)) {
			errorSymbolType(value, vType, "a numeric type"); //found vType, expected int/float
		}
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
			Node idExp = node.getIds().get(i);
			String id = ((AVariableExp)idExp).getId().getText();

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
	public void outAPrintStm(APrintStm node)
	{
		assert node.getExp().size()>=0;
		for (int i=0; i<node.getExp().size(); i++) {
			PExp value = node.getExp().get(i);
			Type vType = getType(value);
		}
		defaultOut(node);
	}
	public void outAReturnStm(AReturnStm node)
	{
		if (node.getExp()!=null){
			//check exp
			PExp valueExp = node.getExp();
			Type expType = getType(valueExp);
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
	public void outAIfStm(AIfStm node)
	{
		if (node.getStm()!=null){
			//check stm
			PStm valueStm = node.getStm();
			Type stmType = getType(valueStm);
		}
		
		PExp valueExp = node.getExp();
		Type expType = getType(valueExp);
		if (!isBooleanType(expType)){ //exp is not bool type
			errorSymbolType(valueExp, expType, boolType);
		}
		
		if (node.getIfBlock().size()==0){
			
		} else {
			for (int i=0; i<node.getIfBlock().size();i++){
				PStm valueStm = node.getIfBlock().get(i);
				Type stmType = getType(valueStm);
			}
		}
		if (node.getElseBlock().size()==0){
			
		} else {
			for (int j=0; j<node.getElseBlock().size();j++){
				PStm valueStm = node.getIfBlock().get(j);
				Type stmType = getType(valueStm);
			}
		}
		defaultOut(node);
	}
	public void outASwitchStm(ASwitchStm node)
	{
		assert node.getSwitchClause().size()>=0;
		if (node.getStm()!=null){
			PStm valueStm = node.getStm();
			Type stmType = getType (valueStm);
		}
		if (node.getExp()!=null){
			PExp valueExp = node.getExp();
			Type expType = getType (valueExp);
		}
		//SwitchClause
		if (node.getSwitchClause().size()==0){
			
		} else {
			for (int i=0; i<node.getSwitchClause().size();i++){
				PSwitchClause valueSwitch = node.getSwitchClause().get(i);
				Type switchType = getType(valueSwitch);
			}
		}
		defaultOut(node);
	}
	public void outAForStm(AForStm node)
	{
		assert node.getStm().size()>=0;
		//[init]:stm? exp? [post]:stm? stm*
		if (node.getInit()!=null){
			PStm valueStm = node.getInit();			
		}
		
		if (node.getExp()!=null){
			PExp valueExp = node.getExp();
			Type expType = getType(valueExp);
			if (!isBooleanType(expType)){//expected bool but found expType
				errorSymbolType(valueExp,expType,boolType);
			}
		}
		if (node.getPost()!=null){
			PStm valuePost = node.getPost();
			Type postType = getType(valuePost);
		}
		
		if (node.getStm().size()==0){
			
		} else {
			for (int i=0; i<node.getStm().size();i++){
				PStm valueStm = node.getStm().get(i);
				Type stmType = getType(valueStm);
			}
		}
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
		assert node.getExp().size()>0;
		assert node.getStm().size()>=0;
		//exp+ stm* fallthrough_stm?
		for (int i=0; i<node.getExp().size(); i++){
			PExp valueExp = node.getExp().get(i);
			Type expType = getType(valueExp);
			
		}
		if (node.getStm().size()==0){
			
		} else {
			
		}
		defaultOut(node);
	}
	public void outADefaultSwitchClause(ADefaultSwitchClause node)
	{
		assert node.getStm().size()>=0;
		//{default} stm* fallthrough_stm?
		if (node.getStm().size()==0){
		
		} else {
			for (int i=0; i<node.getStm().size(); i++){
				PStm valueStm = node.getStm().get(i);
				Type stmType = getType(valueStm);
			}
		}

		if (node.getFallthroughStm()!=null){
		}
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
		Type t = getType(node.getExp());

		if (!(t instanceof StructType))
			errorSymbolType(node.getExp(), t, "a struct with a field named "+node.getId().getText());

		Variable field = ((StructType)t).getField(node.getId().getText());

		if (field == null)
			error(node.getId(), "Field "+node.getId().getText()+" is not defined for type "+t);

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
				error(left, "Expected boolean type but got "+leftType);
			if (!isBooleanType(rightType))
				error(left, "Expected boolean type but got "+leftType);

			setType(node, boolType);

			operatorFound = true;
		} else {
			if (!leftType.isIdentical(rightType))
				error(node, "Mismatched types "+leftType+" and "+rightType);

			if (op instanceof AEqBinaryOp || op instanceof ANotEqBinaryOp) {
				if (!isComparableType(leftType))
					error(left, "Expected comparable type but got "+leftType);

				setType(node, boolType);
				operatorFound = true;
			} else if (op instanceof ALtEqBinaryOp || op instanceof ALtBinaryOp
					|| op instanceof AGtEqBinaryOp || op instanceof AGtBinaryOp) {

				if (!isOrderedType(leftType))
					error(left, "Expected ordered type but got "+leftType);

				setType(node, boolType);
				operatorFound = true;
			} else if (op instanceof APlusBinaryOp) {
				if (!(isNumericType(leftType) || leftType.getUnderlying() == stringType))
					error(left, "Expected numeric or string type but got "+leftType);
				setType(node, leftType);
				operatorFound = true;
			} else if (op instanceof AMinusBinaryOp || op instanceof AStarBinaryOp
					|| op instanceof ASlashBinaryOp || op instanceof APercentBinaryOp) {

				if (!isNumericType(leftType))
					error(left, "Expected numeric type but got "+leftType);
				setType(node, leftType);
				operatorFound = true;
			} else if (op instanceof APipeBinaryOp || op instanceof AAmpBinaryOp
					|| op instanceof ALshiftBinaryOp || op instanceof ARshiftBinaryOp
					|| op instanceof AAmpCaretBinaryOp || op instanceof ACaretBinaryOp) {

				if (!isIntegerType(leftType))
					error(left, "Expected integer type but got "+leftType);

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
				error(exp, "Expected numeric type but got "+expType);
		} else if (op instanceof AExclamationUnaryOp) {
			if (!isBooleanType(expType))
				error(exp, "Expected boolean type but got "+expType);
		} else if (op instanceof ACaretUnaryOp) {
			if (!isIntegerType(expType))
				error(exp, "Expected integer type but got "+expType);
		} else {
			throw new IllegalArgumentException("Bad unary operator: "+op);
		}

		setType(node, expType);
		defaultOut(node);
	}
}
