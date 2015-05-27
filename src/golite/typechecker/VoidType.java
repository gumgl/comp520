/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

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
		return "void";//((Object)this).toString();
	}

}
