package golite.typechecker;

import java.util.ArrayList;

public class Function implements Symbol {

	private String identifier;
	protected Type returnType;
	protected ArrayList<Type> argumentTypes = new ArrayList<Type>();
	
	public Function(String identifier) {
		this.identifier = identifier;
	}
	
	public String getId() {
		return identifier;
	}

	public void setId(String id) {
		identifier = id;
	}

	public void addArgument(Type argument) {
		argumentTypes.add(argument);
	}

	public void addArguments(int count, Type argument) {
		for (int i=0; i<count; i++) {
			argumentTypes.add(argument);
		}
	}

	public ArrayList<Type> getArguments() {
		return argumentTypes;
	}

	public Type getReturnType() {
		assert returnType != null;
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public String getTypeInfo() {
		StringBuilder builder = new StringBuilder();
		builder.append("func : (");

		boolean first = true;
		for (Type t : argumentTypes) {
			if (first) {
				first = false;
			} else {
				builder.append(" * ");
			}
			builder.append(t.getRepresentation());
		}
		if (!(returnType instanceof VoidType)) {
			builder.append(" -> ");
			builder.append(returnType.getRepresentation());
		}
		builder.append(")");
		return builder.toString();
	}

}
