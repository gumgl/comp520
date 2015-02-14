
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import golite.node.* ;
import golite.analysis.* ;


public class TypeChecker extends DepthFirstAdapter {
	public enum Type {Null, Int, Float, Poly};
	
	public Hashtable<String,Type> symbols = new Hashtable<String,Type>();
	public HashMap<Node,Type> types = new HashMap<Node,Type>(); // to tag every node
	PrintWriter stdout;
	PrintWriter stderr;
	public boolean success = true;
	
	public TypeChecker(PrintWriter out, PrintWriter err) {
		stdout = out;
		stderr = err;
	}
	/*
	private void error(Node node, String message) {
		Token token = Main.getFirstToken(node);
		stderr.println("INVALID (type error) at ["+token.getLine()+","+token.getPos()+"]: " + message);
		success = false;
	}
    public void defaultIn(Node node)
    {
        types.put(node, Type.Null);
    }
    
    public void inAIntAstDec(AIntAstDec node)
    {
        defaultIn(node);
    	String id = node.getId().getText();
    	if (symbols.containsKey(id))
    		error(node, "[declaration] "+id+" already declared");
    	else {
    		symbols.put(id, Type.Int);
    		stdout.println(id + ": " + "int");
    	}
    }

    public void inAFloatAstDec(AFloatAstDec node)
    {
        defaultIn(node);
    	String id = node.getId().getText();
    	if (symbols.containsKey(id))
    		error(node,"[declaration] "+id+" already declared");
    	else {
    		symbols.put(id, Type.Float);
    		stdout.println(id + ": " + "float");
    	}
    }

    public void outAAssignAstStm(AAssignAstStm node)
    {
    	String id = node.getVariable().getText();
    	if (!symbols.containsKey(id))
    		error(node,"[assignment] variable " + id + " does not exist");
    	else if (types.get(node.getValue()) == Type.Float && symbols.get(id) == Type.Int )
    		error(node,"[assignment] cannot put float in " + id + " (of type int)");
    	
        defaultOut(node);
    }

    public void outAIfAstStm(AIfAstStm node)
    {
    	if (! types.containsKey(node.getCondition()) || types.get(node.getCondition()) != Type.Int)
    		error(node,"[if] condition is not of type int");
    	
        defaultOut(node);
    }

    public void outAWhileAstStm(AWhileAstStm node)
    {
    	if (! types.containsKey(node.getCondition()) || types.get(node.getCondition()) != Type.Int)
    		error(node,"[while] condition is not of type int");
    	
        defaultOut(node);
    }

    public void outAReadAstStm(AReadAstStm node)
    {
    	String id = node.getId().getText();
    	if (!symbols.containsKey(id))
    		error(node,"[read] variable " + id + " does not exist");
    	else
    		types.put(node, symbols.get(id));
        defaultOut(node);
    }

    public void outAPrintAstStm(APrintAstStm node)
    {
    	types.put(node,types.get(node.getAstExp()));
        defaultOut(node);
    }

    public void outAAdditionAstExp(AAdditionAstExp node)
    {
    	Type left = types.get(node.getLeft());
    	Type right = types.get(node.getRight());
    	if (left == Type.Float || right == Type.Float)
        	types.put(node,Type.Float);
    	else
    		types.put(node,Type.Int);
        defaultOut(node);
    }

    public void outASubtractionAstExp(ASubtractionAstExp node)
    {
    	Type left = types.get(node.getLeft());
    	Type right = types.get(node.getRight());
    	if (left == Type.Float || right == Type.Float)
        	types.put(node,Type.Float);
    	else
    		types.put(node,Type.Int);
        defaultOut(node);
    }

    public void outAMultiplicationAstExp(AMultiplicationAstExp node)
    {
    	Type left = types.get(node.getLeft());
    	Type right = types.get(node.getRight());
    	if (left == Type.Float || right == Type.Float)
        	types.put(node,Type.Float);
    	else
    		types.put(node,Type.Int);
        defaultOut(node);
    }

    public void outADivisionAstExp(ADivisionAstExp node)
    {
    	Type left = types.get(node.getLeft());
    	Type right = types.get(node.getRight());
    	if (left == Type.Float || right == Type.Float)
        	types.put(node,Type.Float);
    	else
    		types.put(node,Type.Int);
        defaultOut(node);
    }

    public void outANegationAstExp(ANegationAstExp node)
    {
    	types.put(node, types.get(node.getAstExp()));
        defaultOut(node);
    }

    public void outAVariableAstExp(AVariableAstExp node)
    {
    	String id = node.getId().getText();
    	if (!symbols.containsKey(id))
    		error(node,"[expression] variable " + id + " does not exist");
    	else
    		types.put(node, symbols.get(id));
        defaultOut(node);
    }

    public void outAIntLitAstExp(AIntLitAstExp node)
    {
    	types.put(node, Type.Int);
        defaultOut(node);
    }

    public void outAFloatLitAstExp(AFloatLitAstExp node)
    {
    	types.put(node, Type.Float);
        defaultOut(node);
    }*/
}
