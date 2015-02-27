package golite.typechecker;

import java.util.ArrayList;

public class Function extends Symbol {

	Type returnType;
	ArrayList<Type> argumentTypes;
	
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

	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

}
