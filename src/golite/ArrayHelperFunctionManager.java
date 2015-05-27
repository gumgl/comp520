/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

import golite.typechecker.ArrayType;
import golite.typechecker.Type;

public abstract class ArrayHelperFunctionManager extends JSHelperFunctionManager<ArrayType> {

	public ArrayHelperFunctionManager(String prefix, int arity, JSGenerator out) {
		super(prefix, arity, out);
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
}
