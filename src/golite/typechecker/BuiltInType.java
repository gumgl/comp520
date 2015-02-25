package golite.typechecker;

import java.util.Set;
import java.util.HashSet;

public class BuiltInType extends Type {
	
	// For the built-ins, we add compatible types at run-time for each built-in type
	Set<Type> compatible = new HashSet<Type>();
	
	public BuiltInType(String identifier) {
		super(identifier);
	}

	@Override
	public boolean TypeCastCheck(Type that) {
		return compatible.contains(that);
	}
	
	public void addCompatible(Type that) {
		compatible.add(that);
	}

}
