package golite.typechecker;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Deque;

public class SymbolTable {

	Deque<List<Symbol>> scopes = new ArrayDeque<List<Symbol>>();
	protected SymbolTableLogger logger;
	
	public SymbolTable(SymbolTableLogger logger) {
		// We don't need to log the opening of the zeroth scope; it should never close
		addScope();

		if (logger != null) {
			this.logger = logger;
			logger.setTable(this);
		}
	}
	
	public SymbolTable() {
		this(null);
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

	public void addScope() {
		if (logger != null)
			logger.logScopeEntry();
		scopes.push(new ArrayList<Symbol>());
	}

	public void dropScope() {
		if (logger != null)
			logger.logScopeExit();
		scopes.pop();
	}
	
	public Symbol getLastSymbol() {
		List<Symbol> scope = scopes.peek();
		return scope.get(scope.size()-1);
	}

	// Used when an identifier is referenced
	public Symbol get(String id) {
		for (List<Symbol> scope : scopes) {
			Symbol found = getInScope(scope, id);

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
