import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import golite.analysis.DepthFirstAdapter;
import golite.node.*;


public class PrettyPrinter2 extends DepthFirstAdapter {
	static String indent = "\t";
	StringBuilder prepend = new StringBuilder("");
	PrintWriter output;

	public PrettyPrinter2(PrintWriter writer) {
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
    public void caseStart(Start node)
    {
        inStart(node);
        node.getPProgram().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }

    @Override
    public void caseAProgram(AProgram node)
    {
        inAProgram(node);
		pln("/*The content is generated by pretty printer.*/");
        if(node.getPackageName() != null)
        {
        	pln("package " + node.getPackageName().getText());
            node.getPackageName().apply(this);
        }
        {
            List<PDeclaration> copy = new ArrayList<PDeclaration>(node.getDeclarations());
            for(PDeclaration e : copy)
            {
                e.apply(this);
            }
        }
        outAProgram(node);
    }

    @Override
    public void caseAVariableDeclaration(AVariableDeclaration node)
    {
        inAVariableDeclaration(node);
        startl();
        p("var ");
        {
            List<PVariableSpec> copy = new ArrayList<PVariableSpec>(node.getVariableSpec());
            for(PVariableSpec e : copy)
            {
                e.apply(this);
            }
        }
        endl();
        outAVariableDeclaration(node);
    }

    @Override
    public void caseATypeDeclaration(ATypeDeclaration node)
    {
        inATypeDeclaration(node);
        {
            List<PTypeSpec> copy = new ArrayList<PTypeSpec>(node.getTypeSpec());
            for(PTypeSpec e : copy)
            {
                e.apply(this);
            }
        }
        outATypeDeclaration(node);
    }

    @Override
    public void caseAFunctionDeclaration(AFunctionDeclaration node)
    {
        inAFunctionDeclaration(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        {
            List<PFuncParam> copy = new ArrayList<PFuncParam>(node.getFuncParam());
            for(PFuncParam e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getReturnType() != null)
        {
            node.getReturnType().apply(this);
        }
        {
            List<PStm> copy = new ArrayList<PStm>(node.getStm());
            for(PStm e : copy)
            {
                e.apply(this);
            }
        }
        outAFunctionDeclaration(node);
    }

    @Override
    public void caseAEmptyDeclaration(AEmptyDeclaration node)
    {
        inAEmptyDeclaration(node);
        outAEmptyDeclaration(node);
    }

    @Override
    public void caseATypedVariableSpec(ATypedVariableSpec node)
    {
        inATypedVariableSpec(node);
        {
            List<TId> copy = new ArrayList<TId>(node.getId());
            boolean first = true;
            for(TId e : copy)
            {
            	p(e.getText());
            	if (!first)
            		p(", ");
            	first = false;
                e.apply(this);
            }
        }
        p(" ");
        if(node.getTypeExp() != null)
        {
            node.getTypeExp().apply(this);
        }
        {
            List<PExp> copy = new ArrayList<PExp>(node.getExp());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        outATypedVariableSpec(node);
    }

    @Override
    public void caseAUntypedVariableSpec(AUntypedVariableSpec node)
    {
        inAUntypedVariableSpec(node);
        {
            List<TId> copy = new ArrayList<TId>(node.getId());
            for(TId e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PExp> copy = new ArrayList<PExp>(node.getExp());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        outAUntypedVariableSpec(node);
    }

    @Override
    public void caseATypeSpec(ATypeSpec node)
    {
        inATypeSpec(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getTypeExp() != null)
        {
            node.getTypeExp().apply(this);
        }
        outATypeSpec(node);
    }

    @Override
    public void caseAFuncParam(AFuncParam node)
    {
        inAFuncParam(node);
        {
            List<TId> copy = new ArrayList<TId>(node.getId());
            for(TId e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getTypeExp() != null)
        {
            node.getTypeExp().apply(this);
        }
        outAFuncParam(node);
    }

    @Override
    public void caseAAliasTypeExp(AAliasTypeExp node)
    {
        inAAliasTypeExp(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAAliasTypeExp(node);
    }

    @Override
    public void caseAIntTypeExp(AIntTypeExp node)
    {
        inAIntTypeExp(node);
        p("int");
        outAIntTypeExp(node);
    }

    @Override
    public void caseAFloat64TypeExp(AFloat64TypeExp node)
    {
        inAFloat64TypeExp(node);
        p("float64");
        outAFloat64TypeExp(node);
    }

    @Override
    public void caseABoolTypeExp(ABoolTypeExp node)
    {
        inABoolTypeExp(node);
        p("bool");
        outABoolTypeExp(node);
    }

    @Override
    public void caseARuneTypeExp(ARuneTypeExp node)
    {
        inARuneTypeExp(node);
        p("rune");
        outARuneTypeExp(node);
    }

    @Override
    public void caseAStringTypeExp(AStringTypeExp node)
    {
        inAStringTypeExp(node);
        p("string");
        outAStringTypeExp(node);
    }

    @Override
    public void caseAStructTypeExp(AStructTypeExp node)
    {
        inAStructTypeExp(node);
        {
            List<PFieldDec> copy = new ArrayList<PFieldDec>(node.getFieldDec());
            for(PFieldDec e : copy)
            {
                e.apply(this);
            }
        }
        outAStructTypeExp(node);
    }

    @Override
    public void caseASliceTypeExp(ASliceTypeExp node)
    {
        inASliceTypeExp(node);
        if(node.getTypeExp() != null)
        {
            node.getTypeExp().apply(this);
        }
        outASliceTypeExp(node);
    }

    @Override
    public void caseAArrayTypeExp(AArrayTypeExp node)
    {
        inAArrayTypeExp(node);
        if(node.getLitInt() != null)
        {
            node.getLitInt().apply(this);
        }
        if(node.getTypeExp() != null)
        {
            node.getTypeExp().apply(this);
        }
        outAArrayTypeExp(node);
    }

    @Override
    public void caseAFieldDec(AFieldDec node)
    {
        inAFieldDec(node);
        {
            List<TId> copy = new ArrayList<TId>(node.getId());
            for(TId e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getTypeExp() != null)
        {
            node.getTypeExp().apply(this);
        }
        outAFieldDec(node);
    }

    @Override
    public void caseAEmptyStm(AEmptyStm node)
    {
        inAEmptyStm(node);
        outAEmptyStm(node);
    }

    @Override
    public void caseAExpressionStm(AExpressionStm node)
    {
        inAExpressionStm(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAExpressionStm(node);
    }

    @Override
    public void caseAAssignStm(AAssignStm node)
    {
        inAAssignStm(node);
        {
            List<PExp> copy = new ArrayList<PExp>(node.getLvalue());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PExp> copy = new ArrayList<PExp>(node.getExp());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        outAAssignStm(node);
    }

    @Override
    public void caseAOpAssignStm(AOpAssignStm node)
    {
        inAOpAssignStm(node);
        if(node.getLvalue() != null)
        {
            node.getLvalue().apply(this);
        }
        if(node.getAssignOp() != null)
        {
            node.getAssignOp().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAOpAssignStm(node);
    }

    @Override
    public void caseAIncDecStm(AIncDecStm node)
    {
        inAIncDecStm(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        if(node.getPostfixOp() != null)
        {
            node.getPostfixOp().apply(this);
        }
        outAIncDecStm(node);
    }

    @Override
    public void caseAShortVariableDecStm(AShortVariableDecStm node)
    {
        inAShortVariableDecStm(node);
        {
            List<PExp> copy = new ArrayList<PExp>(node.getIds());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PExp> copy = new ArrayList<PExp>(node.getExp());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        outAShortVariableDecStm(node);
    }

    @Override
    public void caseAVariableDecStm(AVariableDecStm node)
    {
        inAVariableDecStm(node);
        {
            List<PVariableSpec> copy = new ArrayList<PVariableSpec>(node.getVariableSpec());
            for(PVariableSpec e : copy)
            {
                e.apply(this);
            }
        }
        outAVariableDecStm(node);
    }

    @Override
    public void caseATypeDecStm(ATypeDecStm node)
    {
        inATypeDecStm(node);
        {
            List<PTypeSpec> copy = new ArrayList<PTypeSpec>(node.getTypeSpec());
            for(PTypeSpec e : copy)
            {
                e.apply(this);
            }
        }
        outATypeDecStm(node);
    }

    @Override
    public void caseAPrintStm(APrintStm node)
    {
        inAPrintStm(node);
        if(node.getPrintOp() != null)
        {
            node.getPrintOp().apply(this);
        }
        {
            List<PExp> copy = new ArrayList<PExp>(node.getExp());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        outAPrintStm(node);
    }

    @Override
    public void caseAReturnStm(AReturnStm node)
    {
        inAReturnStm(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAReturnStm(node);
    }

    @Override
    public void caseAIfStm(AIfStm node)
    {
        inAIfStm(node);
        if(node.getStm() != null)
        {
            node.getStm().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        {
            List<PStm> copy = new ArrayList<PStm>(node.getIfBlock());
            for(PStm e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PStm> copy = new ArrayList<PStm>(node.getElseBlock());
            for(PStm e : copy)
            {
                e.apply(this);
            }
        }
        outAIfStm(node);
    }

    @Override
    public void caseASwitchStm(ASwitchStm node)
    {
        inASwitchStm(node);
        if(node.getStm() != null)
        {
            node.getStm().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        {
            List<PSwitchClause> copy = new ArrayList<PSwitchClause>(node.getSwitchClause());
            for(PSwitchClause e : copy)
            {
                e.apply(this);
            }
        }
        outASwitchStm(node);
    }

    @Override
    public void caseAForStm(AForStm node)
    {
        inAForStm(node);
        if(node.getInit() != null)
        {
            node.getInit().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        if(node.getPost() != null)
        {
            node.getPost().apply(this);
        }
        {
            List<PStm> copy = new ArrayList<PStm>(node.getStm());
            for(PStm e : copy)
            {
                e.apply(this);
            }
        }
        outAForStm(node);
    }

    @Override
    public void caseABreakStm(ABreakStm node)
    {
        inABreakStm(node);
        outABreakStm(node);
    }

    @Override
    public void caseAContinueStm(AContinueStm node)
    {
        inAContinueStm(node);
        outAContinueStm(node);
    }

    @Override
    public void caseAConditionalSwitchClause(AConditionalSwitchClause node)
    {
        inAConditionalSwitchClause(node);
        {
            List<PExp> copy = new ArrayList<PExp>(node.getExp());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PStm> copy = new ArrayList<PStm>(node.getStm());
            for(PStm e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getFallthroughStm() != null)
        {
            node.getFallthroughStm().apply(this);
        }
        outAConditionalSwitchClause(node);
    }

    @Override
    public void caseADefaultSwitchClause(ADefaultSwitchClause node)
    {
        inADefaultSwitchClause(node);
        {
            List<PStm> copy = new ArrayList<PStm>(node.getStm());
            for(PStm e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getFallthroughStm() != null)
        {
            node.getFallthroughStm().apply(this);
        }
        outADefaultSwitchClause(node);
    }

    @Override
    public void caseAFallthroughStm(AFallthroughStm node)
    {
        inAFallthroughStm(node);
        outAFallthroughStm(node);
    }

    @Override
    public void caseASimplePrintOp(ASimplePrintOp node)
    {
        inASimplePrintOp(node);
        outASimplePrintOp(node);
    }

    @Override
    public void caseALinePrintOp(ALinePrintOp node)
    {
        inALinePrintOp(node);
        outALinePrintOp(node);
    }
	//postfix_op:
	public void caseAIncPostfixOp(AIncPostfixOp node)
	{
		p("++");
	}
    
	public void caseADecPostfixOp(ADecPostfixOp node)
	{
		p("--");
	}
    
	//assign_op:
	public void caseAPlusAssignOp(APlusAssignOp node)
	{
		p("+");
	}
    
	public void caseAMinusAssignOp(AMinusAssignOp node)
	{
		p("-");
	}
    
	public void caseAStarAssignOp(AStarAssignOp node)
	{
		p("*");
	}
    
	public void caseASlashAssignOp(ASlashAssignOp node)
	{
		p("/");
	}
    
	public void caseAPercentAssignOp(APercentAssignOp node)
	{
		p("%");
	}   
    
	public void caseAAmpAssignOp(AAmpAssignOp node)
	{
		p("&");
	}
	
	public void caseAPipeAssignOp(APipeAssignOp node)
	{
		p("|");
	}
	
	public void caseACaretAssignOp(ACaretAssignOp node)
	{
		p("^");
	}    
    
	public void caseALshiftAssignOp(ALshiftAssignOp node)
	{
		p("<<");
	}    
    
	public void caseARshiftAssignOp(ARshiftAssignOp node)
	{
		p(">>");
	}     
    
	public void caseAAmpCaretAssignOp(AAmpCaretAssignOp node)
	{
		p("&^");
	}       
    
    	//binary_op
    	public void caseALogicalOrBinaryOp(ALogicalOrBinaryOp node)
    	{
    		p("||");
    	}
    	
    	public void caseALogicalAndBinaryOp(ALogicalAndBinaryOp node)
    	{
    		p("&&");
    	}
    	
    	public void caseAEqBinaryOp(AEqBinaryOp node)
    	{
    		p("==");
    	}    	
 
     	public void caseAGtBinaryOp(AGtBinaryOp node)
    	{
    		p(">");
    	}

    	public void caseALtBinaryOp(ALtBinaryOp node)
    	{
    		p("<");
    	}

    	public void caseAGtEqBinaryOp(AGtEqBinaryOp node)
    	{
    		p(">=");
    	}
    	
    	public void caseALtEqBinaryOp(ALtEqBinaryOp node)
    	{
    		p("<=");
    	}

	public void caseAPlusBinaryOp(APlusBinaryOp node)
	{
		p("+");
	}
	    
	public void caseAMinusAssignOp(AMinusBinaryOp node)
	{
		p("-=");
	}
	    
	public void caseAStarBinaryOp(AStarBinaryOp node)
	{
		p("*");
	}
	    
	public void caseASlashBinaryOp(ASlashBinaryOp node)
	{
		p("/");
	}
	    
	public void caseAPercentBinaryOp(APercentBinaryOp node)
	{
		p("%");
	}   
	    
	public void caseAAmpBinaryOp(AAmpBinaryOp node)
	{
		p("&");
	}
	
	public void caseAPipeBinaryOp (APipeBinaryOp node)
	{
		p("|");
	}
	
	public void caseACaretBinaryOp(ACaretBinaryOp node)
	{
		p("^");
	}    
	    
	public void caseALshiftBinaryOp(ALshiftBinaryOp node)
	{
		p("<<");
	}    
	    
	public void caseARshiftBinaryOp(ARshiftBinaryOp node)
	{
		p(">>");
	}     
	    
	public void caseAAmpCaretBinaryOp(AAmpCaretBinaryOp node)
	{
		p("&^");
	} 
	
	//unary_op
	public void caseAPlusUnaryOp(APlusUnaryOp node)
	{
		p("+");
	}
	
	public void caseAMinusUnaryOp(AMinusUnaryOp node)
	{
		p("-");	
	}
	   
	public void caseAExclamationUnaryOp(AExclamationUnaryOp node)
	{
		   	p("!");	
	}
	    
	public void caseACaretUnaryOp(ACaretUnaryOp node)
	{
		p("^");	
	}
	
	//exp:
	public void caseAVariableExp(AVariableExp node)
	{
		p(node.getId().getText());
	}


    @Override
    public void caseANotEqBinaryOp(ANotEqBinaryOp node)
    {
        inANotEqBinaryOp(node);
        p("!=");
        outANotEqBinaryOp(node);
    }


    @Override
    public void caseAMinusBinaryOp(AMinusBinaryOp node)
    {
        inAMinusBinaryOp(node);
        p("-");
        outAMinusBinaryOp(node);
    }

    @Override
    public void caseAArrayAccessExp(AArrayAccessExp node)
    {
        inAArrayAccessExp(node);
        if(node.getArray() != null)
        {
            node.getArray().apply(this);
        }
        if(node.getIndex() != null)
        {
            node.getIndex().apply(this);
        }
        outAArrayAccessExp(node);
    }

    @Override
    public void caseAFieldAccessExp(AFieldAccessExp node)
    {
        inAFieldAccessExp(node);
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAFieldAccessExp(node);
    }

    @Override
    public void caseALitIntExp(ALitIntExp node)
    {
        inALitIntExp(node);
        if(node.getLitInt() != null)
        {
            node.getLitInt().apply(this);
            node.getLitInt().apply(this);
        }
        outALitIntExp(node);
    }

    @Override
    public void caseALitFloatExp(ALitFloatExp node)
    {
        inALitFloatExp(node);
        if(node.getLitFloat() != null)
        {
        	p(node.getLitFloat().getText());
            node.getLitFloat().apply(this);
        }
        outALitFloatExp(node);
    }

    @Override
    public void caseALitHexExp(ALitHexExp node)
    {
        inALitHexExp(node);
        if(node.getLitHex() != null)
        {
        	p(node.getLitHex().getText());
            node.getLitHex().apply(this);
        }
        outALitHexExp(node);
    }

    @Override
    public void caseALitOctalExp(ALitOctalExp node)
    {
        inALitOctalExp(node);
        if(node.getLitOctal() != null)
        {
        	p(node.getLitOctal().getText());
            node.getLitOctal().apply(this);
        }
        outALitOctalExp(node);
    }

    @Override
    public void caseALitInterpretedExp(ALitInterpretedExp node)
    {
        inALitInterpretedExp(node);
        if(node.getLitInterpreted() != null)
        {
        	p(node.getLitInterpreted().getText());
            node.getLitInterpreted().apply(this);
        }
        outALitInterpretedExp(node);
    }

    @Override
    public void caseALitRawExp(ALitRawExp node)
    {
        inALitRawExp(node);
        if(node.getLitRaw() != null)
        {
        	p(node.getLitRaw().getText());
            node.getLitRaw().apply(this);
        }
        outALitRawExp(node);
    }

    @Override
    public void caseALitRuneExp(ALitRuneExp node)
    {
        inALitRuneExp(node);
        if(node.getLitRune() != null)
        {
        	p(node.getLitRune().getText());
            node.getLitRune().apply(this);
        }
        outALitRuneExp(node);
    }

    @Override
    public void caseAFunctionCallExp(AFunctionCallExp node)
    {
        inAFunctionCallExp(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        {
            List<PExp> copy = new ArrayList<PExp>(node.getExp());
            for(PExp e : copy)
            {
                e.apply(this);
            }
        }
        outAFunctionCallExp(node);
    }

    @Override
    public void caseABaseTypeCastExp(ABaseTypeCastExp node)
    {
        inABaseTypeCastExp(node);
        if(node.getTypeExp() != null)
        {
            node.getTypeExp().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outABaseTypeCastExp(node);
    }

    @Override
    public void caseAAppendExp(AAppendExp node)
    {
        inAAppendExp(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAAppendExp(node);
    }

    @Override
    public void caseABinaryExp(ABinaryExp node)
    {
        inABinaryExp(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getBinaryOp() != null)
        {
            node.getBinaryOp().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outABinaryExp(node);
    }

    @Override
    public void caseAUnaryExp(AUnaryExp node)
    {
        inAUnaryExp(node);
        if(node.getUnaryOp() != null)
        {
            node.getUnaryOp().apply(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().apply(this);
        }
        outAUnaryExp(node);
    }

}
