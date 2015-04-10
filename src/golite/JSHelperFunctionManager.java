package golite;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import golite.typechecker.Type;

public abstract class JSHelperFunctionManager<T extends Type> {
	protected String functionNamePrefix;
	JSGenerator out;

	/** Map the type to the helper function ID */
	protected Map<T, Integer> functionId = new HashMap<T, Integer>();

	/** Store an example of a type for which the helper function whose ID is
	 * the index is appropriate */
	protected List<T> exampleType = new LinkedList<T>();


	public JSHelperFunctionManager(String prefix, JSGenerator out) {
		this.functionNamePrefix = prefix;
		this.out = out;
	}

	public String getFunctionName(T type) {
		return functionNamePrefix + getFunctionId(type);
	}

	/** Get the ID of helper functions for this type. This lookup is O(n) for
	 * the number of registered types because it iterates through them to see
	 * if an applicable type has already been added. This could be improved by
	 * using hashes or something. */
	protected int getFunctionId(T type) {
		// Try a lookup by object identity
		Integer helperIndex = functionId.get(type);
		if (helperIndex != null) {
			return helperIndex;
		}

		// Iterate through looking for an identical type
		for (Entry<T, Integer> registered : functionId.entrySet()) {
			if (areEquivalent(registered.getKey(), type)) {
				functionId.put(type, registered.getValue());
				return registered.getValue();
			}
		}

		// Add the type
		int i = exampleType.size();

		functionId.put(type, i);
		exampleType.add(type);

		return i;
	}

	/** Decide whether two types are the same for the purpose of the helper
	 * function */
	protected boolean areEquivalent(T t1, T t2) {
		return t1.isIdentical(t2);
	}

	public void printFunctions() {
		int i = 0;
		for (T type : exampleType) {
			out.pln("function "+functionNamePrefix+i+"(a) {");
			out.shift();
			printFunction(type);
			out.unshift();
			out.pln("}");

			i++;
		}
	}

	/** Write the helper function for the given type */
	protected abstract void printFunction(T type);
}