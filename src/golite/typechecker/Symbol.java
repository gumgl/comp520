package golite.typechecker;

public interface Symbol {

	public String getId();

	public void setId(String id);

	/**
	 * Get the symbol's type information. This might be either its type
	 * or its type signature
	 */
	public String getTypeInfo();

}
