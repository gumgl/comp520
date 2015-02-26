package golite.typechecker;

import java.util.ArrayList;

public class StructType extends Type {
	
	// Here we have an array of Variable because when typechecking, we need to lookup the name of a specific field
	ArrayList<Variable> fields;

	public StructType() {
		super();
	}
	public StructType(String identifier) {
		super(identifier);
	}

	public void addField(Variable field) {
		fields.add(field);
	}
	
	public Variable getField(String id) {
		for (Variable field : fields)
			if (field.getId().equals(id))
				return field;
		
		return null; // Not found
	}

	@Override
	public BuiltInType getUnderlying() {
		return null;
	}

}
