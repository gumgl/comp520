/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

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
