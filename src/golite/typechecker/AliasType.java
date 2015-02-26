package golite.typechecker;

public class AliasType extends Type {
	
	Type type;

	public AliasType(String identifier, Type pType) {
		super(identifier);
		type = pType;
	}

	@Override
	public BuiltInType getUnderlying() {
		return type.getUnderlying();
	}

}
