/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

import java.io.PrintWriter;
import java.util.List;

import golite.analysis.DepthFirstAdapter;
import golite.node.Node;

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
	
	protected void printConsecutiveLines(List<? extends Node> nodes) {
		for (Node e : nodes) {
			startl();
			e.apply(this);
			endl();
		}
	}
	
	protected void printList(List<? extends Node> nodes) {
		boolean first = true;
		for (Node e : nodes) {
			if (first) {
				first = false;
			} else {
				p(", ");
			}
			e.apply(this);
		}
	}
}
