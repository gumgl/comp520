package golite.typechecker;

import java.util.ArrayList;

public class StructType extends Type {

	// Here we have an array of Variable because when typechecking, we need to lookup the name of a specific field
	ArrayList<Variable> fields = new ArrayList<Variable>();

	public StructType() {
		super();
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

	public boolean isIdentical(Type other) {
		if (this == other)
			return true;

		if (!(other instanceof StructType))
			return false;

		StructType otherStructType = (StructType)other;

		if (otherStructType.fields.size() != fields.size())
			return false;

		// Struct fields are ordered
		for (int i=0; i < fields.size(); i++) {
			Variable myField = fields.get(i);
			Variable otherField = otherStructType.fields.get(i);

			if (!myField.getId().equals(otherField.getId()))
				return false;

			if (!myField.getType().isIdentical(otherField.getType()))
				return false;
		}

		return true;
	}

	public String getRepresentation() {
		StringBuilder builder = new StringBuilder();
		builder.append("struct { ");
		for (Variable field : fields) {
			builder.append(field.getId());
			builder.append(" ");
			builder.append(field.getType().getRepresentation());
			builder.append("; ");
		}
		builder.append("}");
		return builder.toString();
	}

}
