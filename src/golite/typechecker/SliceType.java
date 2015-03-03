package golite.typechecker;

public class SliceType extends Type {

	protected Type type;

	public SliceType(Type pType) {
		type = pType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public boolean isIdentical(Type other) {
		if (this == other)
			return true;

		return other instanceof SliceType && type.isIdentical(((SliceType)other).getType());
	}

	public String getRepresentation() {
		return "[]"+type.getRepresentation();
	}

}
