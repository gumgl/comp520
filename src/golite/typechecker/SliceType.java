package golite.typechecker;

public class SliceType extends Type {

	Type type;
	int size;
	
	public SliceType() {
		super();
	}
	
	public SliceType(String identifier, Type pType, int pSize) {
		super(identifier);
		type = pType;
		size = pSize;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public BuiltInType getUnderlying() {
		return null;
	}
	
}
