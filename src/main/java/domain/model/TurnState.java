package domain.model;

import java.util.Optional;

public class TurnState {

	private static final int DEFAULT_TURNS = 1;

	private int turnsToTake;
	private boolean skipDraw;
	private boolean isAttacking;
	private Card pendingAction;
	private int nopeCount;

	public TurnState() {
		reset();
	}

	public void enableSkipDraw() {
		skipDraw = true;
	}

	public void startAttack(int turns) {
		isAttacking = true;
		turnsToTake = turns;
	}

	public void setPendingAction(Card card) {
		pendingAction = card;
	}

	public void clearPendingAction() {
		pendingAction = null;
	}

	public void incrementNopeCount() {
		nopeCount++;
	}

	public void decrementTurns() {
		turnsToTake = Math.max(0, turnsToTake - 1);
	}

	public boolean shouldSkipDraw() {
		return skipDraw;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

	public int nopeCount() {
		return nopeCount;
	}

	public int turnsRemaining() {
		return turnsToTake;
	}

	public Optional<Card> pendingAction() {
		return Optional.ofNullable(pendingAction);
	}

	public void reset() {
		turnsToTake = DEFAULT_TURNS;
		skipDraw = false;
		isAttacking = false;
		pendingAction = null;
		nopeCount = 0;
	}
}
