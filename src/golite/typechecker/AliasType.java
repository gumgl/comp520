package golite.typechecker;

public class AliasType extends NamedType implements Symbol {
	
	Type type;

	public AliasType(String identifier, Type pType) {
		super(identifier);
		type = pType;
	}

	@Override
	public Type getUnderlying() {
		return type.getUnderlying();
	}

	@Override
	public String getTypeInfo() {
		return type.getRepresentation();
	}

}
