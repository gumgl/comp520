package golite.typechecker;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;

public class SymbolTable {

	SymbolTable parent = null;
	ArrayList<Symbol> table = new ArrayList<Symbol>();
	
	
	public SymbolTable(SymbolTable pParent) {
		parent = pParent;
	}
	
	public void addSymbol(Symbol symbol) {
		table.add(symbol);
	}
	
	public void addSymbols(Collection<? extends Symbol> symbols) {
		table.addAll(symbols);
	}
	
	public SymbolTable newScope() {
		return new SymbolTable(this);
	}
	
	public SymbolTable popScope() {
		return this.parent;
	}
	
	public Symbol getSymbol(String id) {
		SymbolTable scope = this;
		while (scope != null) { // Search up through all scopes
			for (Symbol symbol : scope.table) // Search in current scope
				if (symbol.id == id)
					return symbol;
			scope = scope.popScope(); // Go up one level
		}
		return null; // Not found
	}
}
