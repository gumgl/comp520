package golite;

import golite.node.AFieldAccessExp;
import golite.node.AVariableExp;
import golite.node.TId;
import golite.typechecker.StructType;
import golite.typechecker.Variable;

public class StructCopyHelperFunctionManager extends JSHelperFunctionManager<StructType> {

	private static final AFieldAccessExp fieldAccessExp =
			new AFieldAccessExp(new AVariableExp(new TId("a")), null);

	public StructCopyHelperFunctionManager(String prefix, JSGenerator out) {
		super(prefix, 0, out);
	}

	@Override
	protected void printFunction(StructType type) {
		out.pln("return {");

		out.shift();
		out.startl();

		boolean first = true;
		for (Variable field : type.getFields()) {
			if (first) {
				first = false;
			} else {
				out.p(",");
				out.endl();
				out.startl();
			}

			out.p(field.getId());
			out.p(": ");

			// Access the field in the original and copy it
			fieldAccessExp.setId(new TId(field.getId()));
			out.printFreshCopy(fieldAccessExp, field.getType());
		}

		out.endl();
		out.unshift();
		out.pln("}");
	}
}
