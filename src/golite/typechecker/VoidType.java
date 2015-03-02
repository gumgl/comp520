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

	public String toString() {
		return "(no value)";
	}

}
