package golite;

import golite.node.AArrayAccessExp;
import golite.node.AVariableExp;
import golite.node.PExp;
import golite.node.TId;
import golite.typechecker.ArrayType;
import golite.typechecker.Type;

public class ArrayCopyHelperFunctionManager extends JSHelperFunctionManager<ArrayType> {

	private static final PExp arrayAccessExp =
			new AArrayAccessExp(new AVariableExp(new TId("a")), new AVariableExp(new TId("i")));

	public ArrayCopyHelperFunctionManager(String prefix, JSGenerator out) {
		super(prefix, out);
	}

	protected boolean areEquivalentContainedTypes(Type t1, Type t2) {
		t1 = t1.getUnderlying();
		t2 = t2.getUnderlying();

		if (t1 instanceof ArrayType) {
			if (t2 instanceof ArrayType) {
				return areEquivalent((ArrayType) t1, (ArrayType) t2);
			}

			return false;
		}

		return t1.isIdentical(t2);
	}

	protected boolean areEquivalent(ArrayType t1, ArrayType t2) {
		if (t1 == t2)
			return true;

		return areEquivalentContainedTypes(t1.getType(), t2.getType());
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
