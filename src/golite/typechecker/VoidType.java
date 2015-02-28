package golite.typechecker;

public class VoidType extends Type {

	public VoidType() {
	}

	public VoidType(String identifier) {
		super(identifier);
	}

	@Override
	public BuiltInType getUnderlying() {
		return null;
	}

}
