import java.util.Map;

import golite.analysis.DepthFirstAdapter;
import golite.node.*;

public class GoLiteWeeder extends DepthFirstAdapter {
	protected Start root;
	protected Map<Node, Token> tokenMap;

	protected int loopNestingLevel = 0;

	@Override
	public void inStart(Start node) {
		super.inStart(node);
		root = node;
		tokenMap = null;
	}

	@Override
	public void outStart(Start node) {
		super.outStart(node);
		root = null;
		tokenMap = null;
	}

	/* Loop-specific statements */
	@Override
	public void inAForStm(AForStm node) {
		loopNestingLevel++;
	}

	@Override
	public void outAForStm(AForStm node) {
		loopNestingLevel--;
	}

	@Override
	public void caseAContinueStm(AContinueStm node) {
		if (loopNestingLevel == 0) {
			throw new GoLiteWeedingException(getLineAndPos(node) + " continue statement occurs outside loop");
		}
	}

	@Override
	public void caseABreakStm(ABreakStm node) {
		if (loopNestingLevel == 0) {
			throw new GoLiteWeedingException(getLineAndPos(node) + " break statement occurs outside loop");
		}
	}

	/* Switch statements */
	@Override
	public void caseASwitchStm(ASwitchStm node) {
		boolean defaultEncountered = false;

		for (Node switchClause : node.getSwitchClause()) {
			if (switchClause instanceof ADefaultSwitchClause) {
				if (defaultEncountered) {
					throw new GoLiteWeedingException(
							getLineAndPos(switchClause) +
							" second default clause in switch statement"
						);
				}
				defaultEncountered = true;
			}
		}
	}

	/* Short variable declarations */
	@Override
	public void caseAShortVariableDecStm(AShortVariableDecStm node) {
		for (Node id : node.getIds()) {
			if (!(id instanceof AVariableExp)) {
				throw new GoLiteWeedingException(getLineAndPos(id) + " expected id");
			}
		}

		if (node.getIds().size() != node.getExp().size()) {
			throw new GoLiteWeedingException(getLineAndPos(node) + " should be as many identifiers as expressions");
		}
	}

	/* Regular variable declarations */
	@Override
	public void caseATypedVariableSpec(ATypedVariableSpec node) {
		int expressionCount = node.getExp().size();
		if (expressionCount > 0 && expressionCount != node.getId().size()) {
			throw new GoLiteWeedingException(getLineAndPos(node) + " should be as many identifiers as expressions");
		}
	}

	@Override
	public void caseAUntypedVariableSpec(AUntypedVariableSpec node) {
		if (node.getExp().size() != node.getId().size()) {
			throw new GoLiteWeedingException(getLineAndPos(node) + " should be as many identifiers as expressions");
		}
	}

	/* Assignment statements */
	@Override
	public void caseAAssignStm(AAssignStm node) {
		if (node.getExp().size() != node.getLvalue().size()) {
			throw new GoLiteWeedingException(getLineAndPos(node) + " should be as many values as targets");
		}
	}

	public String getLineAndPos(Node node) {
		Token tok;

		if (node instanceof Token) {
			tok = (Token) node;
		} else {
			TokenMapper tm = new TokenMapper();
			if (tokenMap == null) {
				root.apply(tm);
				tokenMap = tm.getMap();
			}

			tok = tokenMap.get(node);
		}

		return "["+tok.getLine()+","+tok.getPos()+"]";
	}
}
