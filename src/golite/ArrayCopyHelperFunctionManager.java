package golite;

import golite.node.AArrayAccessExp;
import golite.node.AVariableExp;
import golite.node.PExp;
import golite.node.TId;
import golite.typechecker.ArrayType;

public class ArrayCopyHelperFunctionManager extends ArrayHelperFunctionManager {

	private static final PExp arrayAccessExp =
			new AArrayAccessExp(new AVariableExp(new TId("a")), new AVariableExp(new TId("i")));

	public ArrayCopyHelperFunctionManager(String prefix, JSGenerator out) {
		super(prefix, 2, out);
	}

	@Override
	protected void printFunction(ArrayType type) {
		out.pln("var i, o=[];");

		out.startl();
		out.p("for(i=0; i < a.length; i++) o.push(");
		out.printFreshCopy(arrayAccessExp, type.getType());
		out.p(");");
		out.endl();

		out.pln("return o;");
	}
}
