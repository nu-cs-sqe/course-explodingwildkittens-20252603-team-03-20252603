package domain.model;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class TurnState {

	private static final ResourceBundle BUNDLE =
		ResourceBundle.getBundle("labels", Locale.getDefault());
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

	public void startAttack() {
		isAttacking = true;
	}

	public void setPendingAction(Card card) {
		if (card == null) {
			throw new IllegalArgumentException(BUNDLE.getString("error.card.arg.null"));
		}
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

	public void reset(int turns) {
		turnsToTake = turns;
		skipDraw = false;
		isAttacking = false;
		pendingAction = null;
		nopeCount = 0;
	}

	// overloaded method to set default value in case turns is not passed in
	public void reset() {
		reset(DEFAULT_TURNS);
	}

}
