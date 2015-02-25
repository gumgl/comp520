package golite.typechecker;

public class Variable extends Symbol {

	Type type;

	public Variable(String identifier) {
		super(identifier);
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}

}