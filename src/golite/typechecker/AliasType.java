/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

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
