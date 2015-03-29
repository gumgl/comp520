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

	private void ensureIsLvalue(PExp lvalue) {
		if (lvalue instanceof AArrayAccessExp) {
			ensureIsLvalue(((AArrayAccessExp)lvalue).getArray());
		} else if (lvalue instanceof AFieldAccessExp) {
			ensureIsLvalue(((AFieldAccessExp)lvalue).getExp());
		} else if (!(lvalue instanceof AVariableExp)) {
			throwError(lvalue, "Expected an addressable expression");
		}
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
		if (node.getReturnType() != null) {
			ensureStatementBlockReturns(node, node.getStm());
		}
	}

	protected void ensureStatementBlockReturns(Node parent, List<PStm> statements) {
		PStm lastStm = null;
		
		if (statements != null && statements.size() > 0)
			lastStm = statements.get(statements.size()-1);
		
		if (lastStm instanceof AReturnStm)
			return;
		
		if (lastStm instanceof AIfStm) {
			ensureBranchesReturn((AIfStm)lastStm);
		} else if (lastStm instanceof ASwitchStm) {
			ensureBranchesReturn((ASwitchStm)lastStm);
		} else {
			throwError(lastStm == null ? parent : lastStm,
					"Function with return type does not end with return statement");
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
			throwError(node, "Switch statement has no default, but it needs to always return");
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

	/* Short variable declarations */
	@Override
	public void inAShortVariableDecStm(AShortVariableDecStm node) {
		for (Node id : node.getIds()) {
			if (!(id instanceof AVariableExp)) {
				throwError(id, "Expected id");
			}
		}

		if (node.getIds().size() != node.getExp().size()) {
			throwVariableLengthError(node);
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
}
