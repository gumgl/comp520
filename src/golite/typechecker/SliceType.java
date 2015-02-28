package golite.typechecker;

public class SliceType extends Type {

	Type type;
	
	public SliceType() {
		super();
	}
	
	public SliceType(String identifier, Type pType) {
		super(identifier);
		type = pType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public BuiltInType getUnderlying() {
		return null;
	}
	
}
