package golite.typechecker;

import java.util.ArrayList;

public class StructType extends Type {
	
	// Here we have an array of Variable because when typechecking, we need to lookup the name of a specific field
	ArrayList<Variable> fields;

	public StructType(String identifier) {
		super(identifier);
	}
	
	@Override
	public boolean TypeCastCheck(Type that) {
		if (fields.size() == 1) { // If struct is really just an alias
			return fields.get(0).getType().TypeCastCheck(that); // Check if that type can typecast
		} else
			return false;
	}

	public void addField(Variable field) {
		fields.add(field);
	}

}
