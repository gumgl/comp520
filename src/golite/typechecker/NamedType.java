/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite.typechecker;

public abstract class NamedType extends Type {

	protected String identifier;

	public NamedType(String identifier) {
		this.identifier = identifier;
	}

	public String getId() {
		return identifier;
	}

	public void setId(String id) {
		identifier = id;
	}

	@Override
	public boolean isIdentical(Type other) {
		return this == other;
	}

	public String getRepresentation() {
		return identifier;
	}

	public String toString() {
		return this.getClass().getSimpleName()+"<"+identifier+">";
	}

}
