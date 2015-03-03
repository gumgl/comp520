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
