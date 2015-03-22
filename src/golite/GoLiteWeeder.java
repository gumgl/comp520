package golite;

import java.util.List;

import golite.analysis.DepthFirstAdapter;
import golite.node.*;

public class GoLiteWeeder extends DepthFirstAdapter {
	protected int loopNestingLevel = 0;
	protected int switchNestingLevel = 0;
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
			List<PStm> body = node.getStm();
			if (body.size() == 0 || !(body.get(body.size()-1) instanceof AReturnStm))
				throwError(node, "Function with return type does not end with return statement");
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
			throwError(lastFallthrough, "Cannot end switch statement with fallthrough");
		}

		for (Node switchClause : node.getSwitchClause()) {
			if (switchClause instanceof ADefaultSwitchClause) {
				if (defaultEncountered) {
					throwError(switchClause, "Second default clause in switch statement");
				}
				defaultEncountered = true;
			}
		}

		switchNestingLevel++;
	}

	@Override
	public void outASwitchStm(ASwitchStm node) {
		switchNestingLevel--;
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
	}
}
