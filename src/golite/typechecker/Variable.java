/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite.typechecker;

public class Variable implements Symbol {

	private Type type;
	private String identifier;

	public Variable(String identifier, Type type) {
		this.identifier = identifier;
		this.type = type;
	}

	public String getId() {
		return identifier;
	}

	public void setId(String id) {
		identifier = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTypeInfo() {
		return type.getRepresentation();
	}

}
