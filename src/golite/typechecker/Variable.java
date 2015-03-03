package golite.typechecker;

public class Variable implements Symbol {

	private Type type;
	private String identifier;

	public Variable(String identifier, Type type) {
		this.identifier = identifier;
		this.type = type;
	}

	public String getId() {
		return identifier;
	}

	public void setId(String id) {
		identifier = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTypeInfo() {
		return type.getRepresentation();
	}

}
