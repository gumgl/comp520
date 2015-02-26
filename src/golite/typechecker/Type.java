package golite.typechecker;

public abstract class Type extends Symbol {
	
	public Type() {
		super();
	}
	public Type(String identifier) {
		super(identifier);
	}
	
	public static boolean Similar(Type t1, Type t2) {
		if (t1 == null && t2 == null)
			return true;
		else if (t1 == null || t2 == null)
			return false;
		else
			return (t1 == t2);
	}
	
	/*
	 * Returns the underlying type (in the case of aliases or built-ins
	 * Return null in any other case
	 */
	public abstract BuiltInType getUnderlying();

}
