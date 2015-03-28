package golite;

import java.io.PrintWriter;

import golite.analysis.DepthFirstAdapter;

public class PrintingASTAdapter extends DepthFirstAdapter {
	protected String indent = "\t";
	protected StringBuilder prepend = new StringBuilder("");
	protected PrintWriter output;

	public PrintingASTAdapter(PrintWriter writer) {
		output = writer;
	}

	public PrintingASTAdapter() {
		this(null);
	}

	/* Getters and setters */
	public PrintWriter getOutputWriter() {
		return output;
	}

	public void setOutputWriter(PrintWriter printer) {
		output = printer;
	}

	/* Output formatting utilities */
	protected void p(String text) {
		output.print(text);
	}
	protected void pln(String text) {
		startl();
		p(text);
		endl();
	}
	protected void shift() {
		prepend.append(indent);
	}
	protected void unshift() {
		prepend.delete(0, indent.length());
	}
	protected void startl() {
		p(prepend.toString());
	}
	protected void endl() {
		p("\n");
	}
}
