/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

import golite.node.* ; 
import golite.analysis.* ; 

import java.lang.System;

public class CustomASTPrinter extends DepthFirstAdapter {
	StringBuilder indent = new StringBuilder("");

	/*public ASTPrinter() {
		super()
	}*/

	public void defaultIn(@SuppressWarnings("unused") Node node)
	{
	    // Do nothing
		System.out.println(indent + node.getClass().getSimpleName() + ": " + node.toString());
		indent.append("   |");
	}

	public void defaultOut(@SuppressWarnings("unused") Node node)
	{
	    // Do nothing
		indent.delete(0, 4);
		//System.out.println(indent + "<" + node.getClass().getName());
	}

}
