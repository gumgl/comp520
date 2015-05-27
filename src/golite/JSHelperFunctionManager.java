/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import golite.typechecker.Type;

public abstract class JSHelperFunctionManager<T extends Type> {
	protected String functionNamePrefix;
	protected int arity;
	protected JSGenerator out;

	/** Map the type to the helper function ID */
	protected Map<T, Integer> functionId = new HashMap<T, Integer>();

	/** Store an example of a type for which the helper function whose ID is
	 * the index is appropriate */
	protected List<T> exampleType = new ArrayList<T>();
	private Deque<Integer> printQueue;


	public JSHelperFunctionManager(String prefix, int arity, JSGenerator out) {
		this.functionNamePrefix = prefix;
		this.arity = arity;
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
		if (printQueue != null) {
			printQueue.push(i);
		}

		return i;
	}

	/** Decide whether two types are the same for the purpose of the helper
	 * function */
	protected boolean areEquivalent(T t1, T t2) {
		return t1.isIdentical(t2);
	}

	public void printFunctions() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (char c='a'; c < 'a'+arity; c++) {
			if (first)
				first = false;
			else
				sb.append(", ");
			sb.append(c);
		}
		String arguments = sb.toString();

		printQueue = new LinkedList<Integer>();
		for (int i = 0; i < exampleType.size(); i++) {
			printQueue.push(i);
		}

		while (!printQueue.isEmpty()) {
			int i = printQueue.pop();
			T type = exampleType.get(i);

			out.pln("function "+functionNamePrefix+i+"("+arguments+") {");
			out.shift();
			printFunction(type);
			out.unshift();
			out.pln("}");
		}
	}

	public boolean moreToPrint() {
		return printQueue == null || !printQueue.isEmpty();
	}

	/** Write the helper function for the given type */
	protected abstract void printFunction(T type);
}
