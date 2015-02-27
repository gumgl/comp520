package golite;

import golite.analysis.DepthFirstAdapter;
import golite.node.*;

public class GoLiteWeeder extends DepthFirstAdapter {
	protected int loopNestingLevel = 0;

	PositionHelper positionHelper;

	public static void weed(Node node, PositionHelper positionHelper) {
		GoLiteWeeder weeder = new GoLiteWeeder();
		weeder.positionHelper = positionHelper;
		node.apply(weeder);
	}

	/* Loop-specific statements */
	@Override
	public void inAForStm(AForStm node) {
		if (node.getPost() instanceof AShortVariableDecStm) {
			throw new GoLiteWeedingException(
					positionHelper.lineAndPos(node.getPost())
						+ " Loop post statements cannot be variable declarations"
				);
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
			throw new GoLiteWeedingException(positionHelper.lineAndPos(node) + " continue statement occurs outside loop");
		}
	}

	@Override
	public void inABreakStm(ABreakStm node) {
		if (loopNestingLevel == 0) {
			throw new GoLiteWeedingException(positionHelper.lineAndPos(node) + " break statement occurs outside loop");
		}
	}

	/* Switch statements */
	@Override
	public void inASwitchStm(ASwitchStm node) {
		boolean defaultEncountered = false;

		if (node.getSwitchClause().isEmpty()) {
			return;
		}

		Node last = node.getSwitchClause().getLast();
		PFallthroughStm lastFallthrough = null;

		if (last instanceof ADefaultSwitchClause) {
			lastFallthrough = ((ADefaultSwitchClause) last).getFallthroughStm();
		} else if (last instanceof AConditionalSwitchClause) {
			lastFallthrough = ((AConditionalSwitchClause) last).getFallthroughStm();
		}

		if (lastFallthrough != null) {
			throw new GoLiteWeedingException(positionHelper.lineAndPos(lastFallthrough)
					+ " cannot end switch statement with fallthrough"
				);
		}

		for (Node switchClause : node.getSwitchClause()) {
			if (switchClause instanceof ADefaultSwitchClause) {
				if (defaultEncountered) {
					throw new GoLiteWeedingException(
							positionHelper.lineAndPos(switchClause) +
							" second default clause in switch statement"
						);
				}
				defaultEncountered = true;
			}
		}
	}

	/* Short variable declarations */
	@Override
	public void inAShortVariableDecStm(AShortVariableDecStm node) {
		for (Node id : node.getIds()) {
			if (!(id instanceof AVariableExp)) {
				throw new GoLiteWeedingException(positionHelper.lineAndPos(id) + " expected id");
			}
		}

		if (node.getIds().size() != node.getExp().size()) {
			throw new GoLiteWeedingException(positionHelper.lineAndPos(node) + " should be as many identifiers as expressions");
		}
	}

	/* Regular variable declarations */
	@Override
	public void inATypedVariableSpec(ATypedVariableSpec node) {
		int expressionCount = node.getExp().size();
		if (expressionCount > 0 && expressionCount != node.getId().size()) {
			throw new GoLiteWeedingException(positionHelper.lineAndPos(node) + " should be as many identifiers as expressions");
		}
	}

	@Override
	public void inAUntypedVariableSpec(AUntypedVariableSpec node) {
		if (node.getExp().size() != node.getId().size()) {
			throw new GoLiteWeedingException(positionHelper.lineAndPos(node) + " should be as many identifiers as expressions");
		}
	}

	/* Assignment statements */
	@Override
	public void inAAssignStm(AAssignStm node) {
		if (node.getExp().size() != node.getLvalue().size()) {
			throw new GoLiteWeedingException(positionHelper.lineAndPos(node) + " should be as many values as targets");
		}
	}
}
