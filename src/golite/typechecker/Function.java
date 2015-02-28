package golite.typechecker;

import java.util.ArrayList;

public class Function extends Symbol {

	protected Type returnType;
	protected ArrayList<Type> argumentTypes = new ArrayList<Type>();
	
	public Function(String identifier) {
		super(identifier);
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

}
