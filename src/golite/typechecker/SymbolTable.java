package golite.typechecker;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.Deque;
import java.util.Map;

public class SymbolTable {
	Deque<Map<String, Symbol>> scopes = new ArrayDeque<Map<String, Symbol>>();
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
		scopes.peek().put(symbol.getId(), symbol);
	}

	public void addSymbols(Collection<? extends Symbol> symbols) {
		Map<String, Symbol> scope = scopes.peek();
		for (Symbol s : symbols) {
			scope.put(s.getId(), s);
		}
	}

	public Collection<Symbol> getSymbols() {
		return scopes.peek().values();
	}

	public void addScope() {
		if (logger != null)
			logger.logScopeEntry();
		scopes.push(new HashMap<String, Symbol>());
	}

	public void dropScope() {
		if (logger != null)
			logger.logScopeExit();
		scopes.pop();
	}

	public Symbol getLastSymbol() {
		Map<String, Symbol> scope = scopes.peek();
		return scope.values().iterator().next();
	}

	// Used when an identifier is referenced
	public Symbol get(String id) {
		for (Map<String, Symbol> scope : scopes) {
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

	public Symbol getInScope(Map<String, Symbol> scope, String id) {
		return scope.get(id);
	}
}
