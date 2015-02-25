package golite.typechecker;

import java.util.ArrayList;

public class SymbolTable {

	SymbolTable parent = null;
	ArrayList<Symbol> symbols = new ArrayList<Symbol>();
	
	
	public SymbolTable(SymbolTable pParent) {
		parent = pParent;
	}
	
	public void addSymbol(Symbol symbol) {
		symbols.add(symbol);
	}
	
	public SymbolTable pushScope() {
		return new SymbolTable(this);
	}
	
	public SymbolTable popScope() {
		return this.parent;
	}
}