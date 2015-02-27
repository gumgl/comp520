package golite.typechecker;

public class BuiltInType extends Type {

	public BuiltInType(String identifier) {
		super(identifier);
	}

	@Override
	public BuiltInType getUnderlying() {
		return this;
	}

}
