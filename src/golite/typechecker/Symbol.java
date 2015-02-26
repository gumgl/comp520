package golite.typechecker;

public abstract class Symbol {
	
	String id;
	
	public Symbol() {
		super();
	}
	
	public Symbol(String identifier) {
		id = identifier;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "<" + id + ">";
	}

}
