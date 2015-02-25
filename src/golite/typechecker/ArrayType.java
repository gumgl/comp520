package golite.typechecker;

public class ArrayType extends Type {
	
	Type type;
	int size;

	public ArrayType(String identifier, Type pType, int pSize) {
		super(identifier);
		type = pType;
		size = pSize;
	}

	@Override
	public boolean TypeCastCheck(Type that) {
		// Cannot typecast an array
		return false;
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

}