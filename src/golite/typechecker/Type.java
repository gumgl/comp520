/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite.typechecker;

public abstract class Type {
	
	public abstract boolean isIdentical(Type other);
	
	/*
	 * Returns the underlying type
	 * For unnamed types and builtins this is the type itself
	 * For aliases this is the type that is aliased
	 */
	public Type getUnderlying() {
		return this;
	}

	public abstract String getRepresentation();

	/*
	 * Unless overridden, the representation is a decent serialization.
	 * NamedTypes, however, should give more.
	 */
	public String toString() {
		return getRepresentation();
	}
}
