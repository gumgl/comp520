/* This code originated as a project for the COMP 520 class at McGill
 * University in Winter 2015. Any subsequent COMP 520 student who is
 * viewing this code must follow the course rules and report any viewing
 * and/or use of the code. */

package golite;

import java.util.LinkedList;
import java.util.List;

import golite.analysis.DepthFirstAdapter;
import golite.node.*;

public class GoLiteWeeder extends DepthFirstAdapter {
	protected int loopNestingLevel = 0;
	protected int switchNestingLevel = 0;
	protected LinkedList<Boolean> switchDefaultEncountered = new LinkedList<Boolean>();
	protected PositionHelper positionHelper;

	public static void weed(Node node, PositionHelper positionHelper) {
		GoLiteWeeder weeder = new GoLiteWeeder();
		weeder.positionHelper = positionHelper;
		node.apply(weeder);
	}

	private void throwError(Node node, String message) {
		throw new GoLiteWeedingException(positionHelper.lineAndPos(node)+" "+message);
	}

	private void throwVariableLengthError(Node node) {
		throwError(node, "There should be as many variables as expressions");
	}

	private void throwNoReturnError(Node node) {
		throwError(node, "Missing return at end of function");
	}

	private void ensureIsLvalue(PExp lvalue) {
		if (lvalue instanceof AArrayAccessExp) {
			ensureIsLvalue(((AArrayAccessExp)lvalue).getArray());
		} else if (lvalue instanceof AFieldAccessExp) {
			ensureIsLvalue(((AFieldAccessExp)lvalue).getExp());
		} else if (!(lvalue instanceof AVariableExp)) {
			throwError(lvalue, "Expected an addressable expression");
		}
	}
	
	private void ensureNotBlank(TId variable) {
		if (variable.getText().equals("_"))
			throwError(variable, "Cannot use _ as value");
	}

	/* Loop-specific statements */
	@Override
	public void inAForStm(AForStm node) {
		if (node.getPost() instanceof AShortVariableDecStm) {
			throwError(node.getPost(), "Cannot declare variables in for-increment statements");
		}

		loopNestingLevel++;
	}

	@Override
	public void outAForStm(AForStm node) {
		loopNestingLevel--;
	}

	@Override
	public void inAContinueStm(AContinueStm node) {
		if (loopNestingLevel == 0) {
			throwError(node, "Continue statement occurs outside loop");
		}
	}

	@Override
	public void inABreakStm(ABreakStm node) {
		if (loopNestingLevel == 0 && switchNestingLevel == 0) {
			throwError(node, "Break statement occurs outside loop or switch");
		}
	}

	/* Function declarations */
	@Override
	public void inAFunctionDeclaration(AFunctionDeclaration node) {
		ensureNotBlank(node.getId());
		
		if (node.getReturnType() != null) {
			ensureStatementBlockReturns(node, node.getStm());
		}
	}
	
	@Override
	public void inAFuncParam(AFuncParam node)
	{
		for (TId id : node.getId()) {
			ensureNotBlank(id);
		}
	}

	protected void ensureStatementBlockReturns(Node parent, List<PStm> statements) {
		if (statements == null || statements.size() == 0)
			throwNoReturnError(parent);
			
		PStm lastStm = statements.get(statements.size()-1);

		if (lastStm instanceof AReturnStm)
			return;

		if (lastStm instanceof AForStm) {
			ensureBranchesReturn((AForStm)lastStm);
		} else if (lastStm instanceof AIfStm) {
			ensureBranchesReturn((AIfStm)lastStm);
		} else if (lastStm instanceof ASwitchStm) {
			ensureBranchesReturn((ASwitchStm)lastStm);
		} else if (lastStm instanceof ABlockStm) {
			ensureStatementBlockReturns(lastStm, ((ABlockStm) lastStm).getStm());
		} else {
			throwNoReturnError(lastStm);
		}
	}

	protected void ensureBranchesReturn(AForStm node) {
		if (node.getExp() != null || BreakStatementLocator.containsBreak(node)) {
			throwNoReturnError(node);
		}
	}

	protected void ensureBranchesReturn(AIfStm node) {
		ensureStatementBlockReturns(node, node.getIfBlock());
		ensureStatementBlockReturns(node, node.getElseBlock());
	}

	protected void ensureBranchesReturn(ASwitchStm node) {
		List<PSwitchClause> clauses = node.getSwitchClause();
		boolean defaultFound = false;
		
		for (PSwitchClause clauseProduction : clauses) {
			ASwitchClause clause = (ASwitchClause)clauseProduction;
			if (clause.getFallthroughStm() == null)
				ensureStatementBlockReturns(clause, clause.getStm());

			if (clause.getSwitchCase() instanceof ADefaultSwitchCase) {
				defaultFound = true;
			}
		}
		
		if (!defaultFound)
			throwNoReturnError(node);
	}

	protected static class BreakStatementLocator extends DepthFirstAdapter {
		private static BreakStatementLocator breakLocator = new BreakStatementLocator();

		@SuppressWarnings("serial")
		private class FoundBreak extends RuntimeException {
		}

		public static boolean containsBreak(Node node) {
			try {
				node.apply(breakLocator);
			} catch (FoundBreak a) {
				return true;
			}

			return false;
		}

		private BreakStatementLocator() {
			super();
		}

		@Override
		public void inABreakStm(ABreakStm node) {
			throw new FoundBreak();
		}
	}

	/* Switch statements */
	@Override
	public void inASwitchStm(ASwitchStm node) {
		switchDefaultEncountered.push(false);
		switchNestingLevel++;

		if (!node.getSwitchClause().isEmpty()) {
			ASwitchClause last = (ASwitchClause)node.getSwitchClause().getLast();
			if (last.getFallthroughStm() != null) {
				throwError(last.getFallthroughStm(), "Cannot end switch statement with fallthrough");
			}
		}
	}

	@Override
	public void outASwitchStm(ASwitchStm node) {
		switchDefaultEncountered.pop();
		switchNestingLevel--;
	}

	@Override
	public void outADefaultSwitchCase(ADefaultSwitchCase node) {
		if (switchDefaultEncountered.peek()) {
			throwError(node, "Second default clause in switch statement");
		}
		switchDefaultEncountered.set(0, true);
		super.outADefaultSwitchCase(node);
	}

	/* Short variable declarations: Override the case method to stop it
	 * from recursing on the Id expressions, which have special weeding
	 * rules here */
	@Override
	public void caseAShortVariableDecStm(AShortVariableDecStm node) {
		for (Node id : node.getIds()) {
			if (!(id instanceof AVariableExp)) {
				throwError(id, "Expected id");
			}
		}

		if (node.getIds().size() != node.getExp().size()) {
			throwVariableLengthError(node);
		}

		for (PExp e : node.getExp()) {
			e.apply(this);
		}
	}

	/* Regular variable declarations */
	@Override
	public void inATypedVariableSpec(ATypedVariableSpec node) {
		int expressionCount = node.getExp().size();
		if (expressionCount > 0 && expressionCount != node.getId().size()) {
			throwVariableLengthError(node);
		}
	}

	@Override
	public void inAUntypedVariableSpec(AUntypedVariableSpec node) {
		if (node.getExp().size() != node.getId().size()) {
			throwVariableLengthError(node);
		}
	}

	/* Assignment statements */
	@Override
	public void inAAssignStm(AAssignStm node) {
		if (node.getExp().size() != node.getLvalue().size()) {
			throwError(node, "There should be as many values as targets");
		}

		for (PExp lvalue : node.getLvalue()) {
			ensureIsLvalue(lvalue);
		}
	}

	@Override
	public void inAOpAssignStm(AOpAssignStm node) {
		ensureIsLvalue(node.getLvalue());
	}

	/* Increment/decrement statements */
	@Override
	public void inAIncDecStm(AIncDecStm node) {
		ensureIsLvalue(node.getExp());
	}
	
	@Override
	public void inAProgram(AProgram node)
	{
		ensureNotBlank(node.getPackageName());
	}
	
	@Override
	public void inAVariableExp(AVariableExp node) {
		ensureNotBlank(node.getId());
	}
	
	@Override
	public void inAAppendExp(AAppendExp node)
	{
		ensureNotBlank(node.getId());
	}
}
