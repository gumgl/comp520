
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
