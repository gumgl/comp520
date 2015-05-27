/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

import golite.node.AArrayAccessExp;
import golite.node.AVariableExp;
import golite.node.PExp;
import golite.node.TId;
import golite.typechecker.ArrayType;

public class ArrayEqualityHelperFunctionManager extends ArrayHelperFunctionManager {

	private static final PExp arrayAccessLeftExp =
			new AArrayAccessExp(new AVariableExp(new TId("a")), new AVariableExp(new TId("i")));
	private static final PExp arrayAccessRightExp =
			new AArrayAccessExp(new AVariableExp(new TId("b")), new AVariableExp(new TId("i")));

	public ArrayEqualityHelperFunctionManager(String prefix, JSGenerator out) {
		super(prefix, 2, out);
	}

	@Override
	protected void printFunction(ArrayType type) {
		out.pln("for (var i = 0; i < a.length; i++)");

		out.startl();
		out.p("  if (");
		out.printInequalityTest(type, arrayAccessLeftExp, arrayAccessRightExp);
		out.p(")");
		out.endl();

		out.pln("    return false;");
		out.pln("return true;");
	}
}
