package golite.typechecker;

public class VoidType extends Type {

	public VoidType() {
	}

	public boolean isIdentical(Type other) {
		return other instanceof VoidType;
	}

	@Override
	public Type getUnderlying() {
		return null;
	}

	public String getRepresentation() {
		return "<void (internal type)>";
	}

	public String toString() {
		return ((Object)this).toString();
	}

}
