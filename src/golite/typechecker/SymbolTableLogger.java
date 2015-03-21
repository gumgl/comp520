package golite.typechecker;

import java.io.PrintWriter;

public class SymbolTableLogger {

	protected SymbolTable table;
	protected PrintWriter out;
	protected boolean verbose;

	public SymbolTableLogger(PrintWriter out, boolean verbose) {
		this.out = out;
		this.verbose = verbose;
	}

	public SymbolTableLogger(PrintWriter out) {
		this(out, false);
	}

	public SymbolTableLogger(boolean verbose) {
		this(null, verbose);
	}

	public SymbolTableLogger() {
		this(null, false);
	}

	// Getters and setters

	public void setOutputWriter(PrintWriter out) {
		this.out = out;
	}

	public PrintWriter getOutputWriter() {
		return out;
	}

	public void setTable(SymbolTable table) {
		this.table = table;
	}

	protected void dumpEntry(Symbol symbol) {
		out.println(symbol.getClass().getSimpleName()+"\t"+symbol.getId()+"\t"+symbol.getTypeInfo());
	}

	public void logScopeEntry() {
		out.println("# Opened scope");
	}

	public void logScopeExit() {
		if (verbose) {
			for (Symbol s : table.getSymbols())
				dumpEntry(s);
		} else {
			dumpEntry(table.getLastSymbol());
		}
		out.println("# Closed scope");
	}

}
