package golite;

import golite.node.*;
import golite.typechecker.*;

import java.io.PrintWriter;
import java.util.HashMap;

public class TypedPrettyPrinter extends PrettyPrinter {
	protected HashMap<Node, Type> types;

	public TypedPrettyPrinter(PrintWriter writer, HashMap<Node, Type> types) {
		super(writer);
		this.types = types;
	}

	protected void printExpType(PExp exp) {
		Type type = types.get(exp);
		
		p(" /* ");
		p(type == null ? "(null)" : type.toString());
		p(" */");
	}

	public void defaultOut(Node node) {
		super.defaultOut(node);
		if (node instanceof PExp) {
			printExpType((PExp)node);
		}
	}

	public void inAFunctionDeclaration(AFunctionDeclaration node) {
//		Function signature = (Function)types.get(node);
		
		startl();
		p("/* ");
		p("This should describe the function I guess?");
		p(" */");
		endl();
		super.inAFunctionDeclaration(node);
	}
}
