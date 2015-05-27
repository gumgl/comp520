/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

import golite.typechecker.StructType;
import golite.typechecker.Variable;

public class StructInitializerHelperFunctionManager extends JSHelperFunctionManager<StructType> {

	public StructInitializerHelperFunctionManager(String prefix, JSGenerator out) {
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
			out.printDefaultValue(field.getType());
		}

		out.endl();

		out.unshift();
		out.pln("}");
	}
}
