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

	public TypedPrettyPrinter(HashMap<Node, Type> types) {
		this(null, types);
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
}
