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

import golite.node.* ;
import golite.analysis.* ;
import golite.typechecker.*;


public class TypeChecker extends DepthFirstAdapter {
	
	public HashMap<Node,Type> types = new HashMap<Node,Type>(); // Mapping a Type for every Node
	public SymbolTable symbols = new SymbolTable(null);
	public boolean success = true;
	PrintWriter stdout;
	PrintWriter stderr;
	
	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("one");
		list.add("two");
		list.add("three");
		list.add("four");
		System.out.println(collectionToString(list, ",", "or"));
	}
	
	public TypeChecker(PrintWriter out, PrintWriter err) {
		stdout = out;
		stderr = err;
	}
	static private String collectionToString(Collection collection, String separator, String finalWord) {
		StringBuilder sb = new StringBuilder();
		
		int i = 1;
		int size = collection.size();
		for (Object obj : collection) {
			sb.append(obj.toString());
			if (i < size)
				sb.append(separator);
			else if (size > 1)
				sb.append(finalWord);
			i++;
		}
		return sb.toString();
	}
	private void error(Node node, String message) {
		Token token = Main.getFirstToken(node);
		stderr.println("Type Error at ["+token.getLine()+","+token.getPos()+"]: " + message + ".");
		success = false;
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
	private Type getType(Node node) {
		return types.get(node);
	}
	private void setType(Node node, Type type) {
		types.put(node, type);
	}
	private void preDeclare() {
		BuiltInType boolType = new BuiltInType("bool");
		BuiltInType intType = new BuiltInType("int");
		BuiltInType float64Type = new BuiltInType("float64");
		BuiltInType runeType = new BuiltInType("rune");
		BuiltInType stringType = new BuiltInType("string");
	
		Variable trueVar = new Variable("true", boolType);
		Variable falseVar = new Variable("false", boolType);
	
		BuiltInType.builtIns.add(boolType);
		BuiltInType.builtIns.add(intType);
		BuiltInType.builtIns.add(float64Type);
		BuiltInType.builtIns.add(runeType);
		BuiltInType.builtIns.add(stringType);
		
		BuiltInType.typeCastTypes.add(boolType);
		BuiltInType.typeCastTypes.add(intType);
		BuiltInType.typeCastTypes.add(float64Type);
		BuiltInType.typeCastTypes.add(runeType);
		
		symbols.addSymbols(BuiltInType.builtIns);
		symbols.addSymbol(trueVar);
		symbols.addSymbol(falseVar);
		
		symbols.newScope(); // Shadow them
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
	public void outStart(Start node)
	{
		defaultOut(node);
	}
	
	public void outAProgram(AProgram node)
	{
		if (node.getPackageName().getText() != "main")
			error(node.getPackageName(), "The only package allowed is main");
		defaultOut(node);
	}
	
	public void outAVariableDeclaration(AVariableDeclaration node)
	{
		// Nothing to do here, checks are done in individual VariableSpec
		defaultOut(node);
	}
	
	public void outATypeDeclaration(ATypeDeclaration node)
	{
		defaultOut(node);
	}
	
	public void outAFunctionDeclaration(AFunctionDeclaration node)
	{
		defaultOut(node);
	}
	
	public void outATypedVariableSpec(ATypedVariableSpec node)
	{
		if (node.getId().size() != node.getExp().size())
			error(node, "Number of variables and values do not match");
		
		Type type = getType(node.getTypeExp());
		
		for (int i=0; i<node.getId().size(); i++) {
			TId id = node.getId().get(i);
			PExp value = node.getExp().get(i);
			Symbol symbol = symbols.getInScope(id.getText());
			
			if (symbol != null) // Symbol already declared
				errorSymbolDeclared(id, symbol);
			else if (getType(value) != type) // Value's type is not the same as the declared type
				errorSymbolType(value, getType(value), type);
			else
				symbols.addSymbol(new Variable(id.getText(), type));
		}
		defaultOut(node);
	}
	
	// Basically the same as TypedVariableSpec but with type inference instead of typecheck
	public void outAUntypedVariableSpec(AUntypedVariableSpec node)
	{
		if (node.getId().size() != node.getExp().size())
			error(node, "Number of variables and values do not match");
		
		for (int i=0; i<node.getId().size(); i++) {
			TId id = node.getId().get(i);
			PExp value = node.getExp().get(i);
			Symbol symbol = symbols.getInScope(id.getText());
			Type type = getType(value);
			
			if (symbol != null) // Symbol already declared
				errorSymbolDeclared(id, symbol);
			else
				symbols.addSymbol(new Variable(id.getText(), type));
		}
		defaultOut(node);
	}
	
	public void outATypeSpec(ATypeSpec node)
	{
		defaultOut(node);
	}
	
	public void outAFuncParam(AFuncParam node)
	{
		defaultOut(node);
	}
	
	public void outAAliasTypeExp(AAliasTypeExp node)
	{
		String id = node.getId().getText();
		Symbol symbol = symbols.get(id);
		if (symbol == null)
			errorSymbolNotFound(node, id);
		else if ( !(symbol instanceof Type))
			errorSymbolClass(node, symbol, Type.class);
		else
			setType(node, (Type) symbol);
		
		defaultOut(node);
	}
	
	public void outAIntTypeExp(AIntTypeExp node)
	{
		setType(node, (Type)symbols.get("int"));
		defaultOut(node);
	}
	
	public void outAFloat64TypeExp(AFloat64TypeExp node)
	{
		defaultOut(node);
	}
	
	public void outABoolTypeExp(ABoolTypeExp node)
	{
		defaultOut(node);
	}
	
	public void outARuneTypeExp(ARuneTypeExp node)
	{
		defaultOut(node);
	}
	
	public void outAStringTypeExp(AStringTypeExp node)
	{
		defaultOut(node);
	}
	
	public void outAStructTypeExp(AStructTypeExp node)
	{
		defaultOut(node);
	}
	
	public void outASliceTypeExp(ASliceTypeExp node)
	{
		defaultOut(node);
	}
	
	public void outAArrayTypeExp(AArrayTypeExp node)
	{
		defaultOut(node);
	}
	
	public void outAFieldDec(AFieldDec node)
	{
		defaultOut(node);
	}
	
	public void outAExpressionStm(AExpressionStm node)
	{
		defaultOut(node);
	}
	
	public void outAAssignStm(AAssignStm node)
	{
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
	
	public void outAShortVariableDecStm(AShortVariableDecStm node)
	{
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
		defaultOut(node);
	}
	
	public void outAArrayAccessExp(AArrayAccessExp node)
	{
		defaultOut(node);
	}
	
	public void outAFieldAccessExp(AFieldAccessExp node)
	{
		defaultOut(node);
	}
	
	public void outALitIntExp(ALitIntExp node)
	{
		defaultOut(node);
	}
	
	public void outALitFloatExp(ALitFloatExp node)
	{
		defaultOut(node);
	}
	
	public void outALitHexExp(ALitHexExp node)
	{
		defaultOut(node);
	}
	
	public void outALitOctalExp(ALitOctalExp node)
	{
		defaultOut(node);
	}
	
	public void outALitInterpretedExp(ALitInterpretedExp node)
	{
		defaultOut(node);
	}
	
	public void outALitRawExp(ALitRawExp node)
	{
		defaultOut(node);
	}
	
	public void outALitRuneExp(ALitRuneExp node)
	{
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
		allowed.add(StructType.class);
		
		Type from = getType(node.getExp());
		Type to = getType(node.getTypeExp());
		
		Type underlyingFrom = from.getUnderlying();
		Type underlyingTo = to.getUnderlying();

		if (underlyingFrom == null) // If it cannot be reduced to a built-in type
			errorSymbolClasses(node.getExp(), from, allowed);
		else if (underlyingTo == null)
			errorSymbolClasses(node.getTypeExp(), from, allowed);
		else if ( !BuiltInType.typeCastTypes.contains(underlyingFrom) // Check if it's a type we can typecast 
				|| !BuiltInType.typeCastTypes.contains(underlyingTo)
				|| underlyingFrom != underlyingTo) // Check if they are the same type
			errorTypeCast(node, underlyingFrom, underlyingTo);
		else
			setType(node, to);
		
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
