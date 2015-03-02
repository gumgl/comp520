package golite.typechecker;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Deque;

public class SymbolTable {

	Deque<List<Symbol>> scopes = new ArrayDeque<List<Symbol>>();
	
	public SymbolTable() {
		addScope();
	}
	
	public void addSymbol(Symbol symbol) {
		scopes.peek().add(symbol);
	}

	public void addSymbols(Collection<? extends Symbol> symbols) {
		scopes.peek().addAll(symbols);
	}
	
	public List<Symbol> getSymbols() {
		return scopes.peek();
	}
	
	private List<Symbol> newScope() {
		return new ArrayList<Symbol>();
	}
	
	public void addScope() {
		scopes.push(newScope());
	}
	
	public void dropScope() {
		scopes.pop();
	}
	
	// Used when an identifier is referenced
	public Symbol get(String id) {
		Iterator<List<Symbol>> iter = scopes.descendingIterator();
		while (iter.hasNext()) {
			Symbol found = getInScope(iter.next(), id);

			if (found != null)
				return found;
		}
		return null; // Not found
	}
	
	// Used when a new identifier is declared
	public Symbol getInScope(String id) {
		return getInScope(scopes.peek(), id);
	}
	
	public Symbol getInScope(List<Symbol> scope, String id) {
		for (Symbol symbol : scope) // Search in current scope
			if (symbol.getId().equals(id))
				return symbol;
		
		return null; // Not found
	}
}
