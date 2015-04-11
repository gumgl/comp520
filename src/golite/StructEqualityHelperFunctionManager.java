package golite;

import golite.node.AFieldAccessExp;
import golite.node.AVariableExp;
import golite.node.TId;
import golite.typechecker.StructType;
import golite.typechecker.Variable;

public class StructEqualityHelperFunctionManager extends JSHelperFunctionManager<StructType> {

	private static final AFieldAccessExp fieldAccessLeftExp =
			new AFieldAccessExp(new AVariableExp(new TId("a")), null);
	private static final AFieldAccessExp fieldAccessRightExp =
			new AFieldAccessExp(new AVariableExp(new TId("b")), null);

	public StructEqualityHelperFunctionManager(String prefix, JSGenerator out) {
		super(prefix, 2, out);
	}

	@Override
	protected void printFunction(StructType type) {
		for (Variable field : type.getFields()) {
			out.startl();
			out.p("if (");

			fieldAccessLeftExp.setId(new TId(field.getId()));
			fieldAccessRightExp.setId(new TId(field.getId()));
			out.printInequalityTest(field.getType(), fieldAccessLeftExp, fieldAccessRightExp);

			out.p(") return false;");
			out.endl();
		}
		out.pln("return true;");
	}

}
