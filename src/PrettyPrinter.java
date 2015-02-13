
import golite.node.* ;
import golite.analysis.* ;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.System; 
import java.util.ArrayList;
import java.util.List;

public class PrettyPrinter extends DepthFirstAdapter {
	static String indent = "\t";
	StringBuilder prepend = new StringBuilder("");
	PrintWriter output;
	
	public PrettyPrinter(PrintWriter writer) {
		output = writer;
	}
	
	private void p(String text) {
		output.print(text);
	}
	private void pln(String text) {
		startl();
		p(text);
		endl();
	}
	private void shift() {
		prepend.append(indent);
	}
	private void unshift() {
        prepend.delete(0, indent.length());
	}
	private void startl() {
		p(prepend.toString());
	}
	private void endl() {
		p("\n");
	}

    @Override
	public void inAAstGrammar(AAstGrammar node)
    {
        pln("# This prettyprint is automatically generated");
    }

    @Override
    public void inAIntAstDec(AIntAstDec node)
    {
    	pln("var " + node.getId().getText() + ": int;");
    }
    @Override
    public void inAFloatAstDec(AFloatAstDec node)
    {
    	pln("var " + node.getId().getText() + ": float;");
    }
    
    @Override
    public void inAAssignAstStm(AAssignAstStm node)
    {
    	startl();
    	p(node.getVariable().getText() + " = ");
    	// we print out the value
    }
    @Override
    public void outAAssignAstStm(AAssignAstStm node)
    {
    	// we printed out the value
    	p(";");
    	endl();
    }
    
    @Override
    public void caseAIfAstStm(AIfAstStm node)
    {
        inAIfAstStm(node);
        startl();
    	p("if ");
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }
        p(" then");
        endl();
        shift();
        {
            List<PAstStm> copy = new ArrayList<PAstStm>(node.getTrue());
            for(PAstStm e : copy)
            {
                e.apply(this);
            }
        }
        unshift();
        if (!node.getFalse().isEmpty()) {
        	pln("else");
        	shift();
            List<PAstStm> copy = new ArrayList<PAstStm>(node.getFalse());
            for(PAstStm e : copy)
            {
                e.apply(this);
            }
            unshift();
        }
        pln("endif");
        outAIfAstStm(node);
    }
    
    @Override
    public void caseAWhileAstStm(AWhileAstStm node)
    {
        inAWhileAstStm(node);
        startl();
        p("while ");
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }
        p(" do");
        endl();
        shift();
        {
            List<PAstStm> copy = new ArrayList<PAstStm>(node.getBody());
            for(PAstStm e : copy)
            {
                e.apply(this);
            }
        }
        unshift();
        pln("done");
        outAWhileAstStm(node);
    }
    @Override
    public void inAReadAstStm(AReadAstStm node)
    {
    	pln("read " + node.getId().getText() + ";");
    }
    
    @Override
    public void inAPrintAstStm(APrintAstStm node)
    {
    	startl();
    	p("print ");
    }
    @Override
    public void outAPrintAstStm(APrintAstStm node)
    {
        p(";");
        endl();
    }
    
    @Override
    public void caseAAdditionAstExp(AAdditionAstExp node)
    {
        inAAdditionAstExp(node);
        p("(");
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        p(" + ");
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        p(")");
        outAAdditionAstExp(node);
    }
    
    @Override
    public void caseASubtractionAstExp(ASubtractionAstExp node)
    {
        inASubtractionAstExp(node);
        p("(");
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        p(" - ");
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        p(")");
        outASubtractionAstExp(node);
    }
    
    @Override
    public void caseAMultiplicationAstExp(AMultiplicationAstExp node)
    {
        inAMultiplicationAstExp(node);
        p("(");
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        p(" * ");
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        p(")");
        outAMultiplicationAstExp(node);
    }
    
    @Override
    public void caseADivisionAstExp(ADivisionAstExp node)
    {
        inADivisionAstExp(node);
        p("(");
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        p(" / ");
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        p(")");
        outADivisionAstExp(node);
    }
    
    @Override
    public void caseANegationAstExp(ANegationAstExp node)
    {
        inANegationAstExp(node);
        p("-(");
        if(node.getAstExp() != null)
        {
            node.getAstExp().apply(this);
        }
        p(")");
        outANegationAstExp(node);
    }
    
    @Override
    public void inAVariableAstExp(AVariableAstExp node)
    {
    	p(node.getId().getText());
    }
    
    @Override
    public void inAIntLitAstExp(AIntLitAstExp node)
    {
    	p(node.getIntLit().getText());
    }
    
    @Override
    public void inAFloatLitAstExp(AFloatLitAstExp node)
    {
    	p(node.getFloatLit().getText());
    }

}
