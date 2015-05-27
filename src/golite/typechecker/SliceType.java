/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

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
