package golite;

import golite.node.* ; 
import golite.analysis.* ; 

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.System;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class CGenerator extends DepthFirstAdapter {
	static String indent = "\t";
	StringBuilder prepend = new StringBuilder("");
	PrintWriter output;
	public HashMap<Node,TypeChecker.Type> types;
	public Hashtable<String,TypeChecker.Type> symbols;

	public CGenerator(PrintWriter writer, HashMap<Node,TypeChecker.Type> intypes, Hashtable<String,TypeChecker.Type> insymbols) {
		types = intypes;
		output = writer;
		symbols = insymbols;
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

	/*
    @Override
	public void inAAstGrammar(AAstGrammar node)
    {
        pln("#include <stdio.h>");
        pln("int main(void) {");
        shift();
    }
    @Override
	public void outAAstGrammar(AAstGrammar node)
    {
    	pln("return 0;");
    	unshift();
        pln("}");
    }

    @Override
    public void inAIntAstDec(AIntAstDec node)
    {
    	pln("int " + node.getId().getText() + ";");
    }
    @Override
    public void inAFloatAstDec(AFloatAstDec node)
    {
    	pln("float " + node.getId().getText() + ";");
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
    	p("if (");
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }
        p(") {");
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
        	pln("} else {");
        	shift();
            List<PAstStm> copy = new ArrayList<PAstStm>(node.getFalse());
            for(PAstStm e : copy)
            {
                e.apply(this);
            }
            unshift();
        }
        pln("}");
        outAIfAstStm(node);
    }

    @Override
    public void caseAWhileAstStm(AWhileAstStm node)
    {
        inAWhileAstStm(node);
        startl();
        p("while (");
        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }
        p(") {");
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
        pln("}");
        outAWhileAstStm(node);
    }
    @Override
    public void inAReadAstStm(AReadAstStm node)
    {
    	String id = node.getId().getText();
    	if (symbols.get(id) == TypeChecker.Type.Int)
    		pln("scanf(\"%d\", &" + id + ");");
    	else if (symbols.get(id) == TypeChecker.Type.Float)
    		pln("scanf(\"%f\", &" + id + ");");
    }

    @Override
    public void inAPrintAstStm(APrintAstStm node)
    {
    	startl();
    	if (types.get(node.getAstExp()) == TypeChecker.Type.Int)
    		p("printf(\"%d\\n\", ");
    	else if (types.get(node.getAstExp()) == TypeChecker.Type.Float)
    		p("printf(\"%f\\n\", ");
    }
    @Override
    public void outAPrintAstStm(APrintAstStm node)
    {
        p(");");
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
    */
}
