package golite;

import java.io.PrintWriter;
import java.util.Map;

import golite.node.* ;
import golite.typechecker.*;

public class JSGenerator extends PrintingASTAdapter {
	protected Map<Node, Type> types;

	public JSGenerator(PrintWriter writer, Map<Node, Type> types) {
		super(writer);
		this.types = types;
		this.indent = "  ";
	}

	public JSGenerator(Map<Node, Type> types) {
		this(null, types);
	}
}
