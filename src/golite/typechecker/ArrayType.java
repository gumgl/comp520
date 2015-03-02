package golite.typechecker;

public class ArrayType extends Type {
	
	Type type;
	int size;

	public ArrayType(Type pType, int pSize) {
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
	public boolean isIdentical(Type other) {
		if (this == other)
			return true;

		if (!(other instanceof ArrayType))
			return false;

		ArrayType otherArrayType = (ArrayType)other;
		return size == otherArrayType.getSize() && type.isIdentical(otherArrayType.getType());
	}

	public String toString() {
		return "["+size+"]"+type;
	}

}
