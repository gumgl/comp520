package golite.typechecker;

public abstract class NamedType extends Type implements Symbol {

	protected String identifier;

	public NamedType(String identifier) {
		this.identifier = identifier;
	}

	public String getId() {
		return identifier;
	}

	public void setId(String id) {
		identifier = id;
	}

	@Override
	public boolean isIdentical(Type other) {
		return this == other;
	}

	public String toString() {
		return this.getClass().getSimpleName()+"<"+identifier+">";
	}

}
