/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

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

	public String getRepresentation() {
		return "["+size+"]"+type.getRepresentation();
	}

}
