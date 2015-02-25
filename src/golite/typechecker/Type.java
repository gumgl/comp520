package golite.typechecker;

public abstract class Type extends Symbol {
	
	public Type(String identifier) {
		super(identifier);
	}
	
	public boolean equals(Type that) {
		return (this == that); 
	}
	
	/* var (a A; b B)
	 * A.TypeCastCheck(B) IFF B can be typecast to A [i.e. if a = A(b) is legal]
	 */
	public abstract boolean TypeCastCheck(Type that);

}
