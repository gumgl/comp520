package golite.typechecker;

import java.util.Set;
import java.util.HashSet;

public class BuiltInType extends Type {
	
	static public HashSet<BuiltInType> builtIns = new HashSet<BuiltInType>();
	static public HashSet<BuiltInType> typeCastTypes = new HashSet<BuiltInType>();
	
	public BuiltInType(String identifier) {
		super(identifier);
	}

	@Override
	public BuiltInType getUnderlying() {
		return this;
	}

}
