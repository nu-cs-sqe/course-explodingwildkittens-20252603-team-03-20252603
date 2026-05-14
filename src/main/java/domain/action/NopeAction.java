package domain.action;

import domain.model.GameState;
import domain.model.TurnState;

public class NopeAction implements CardAction {

	public void execute(GameState gameState) {
		TurnState turnState = gameState.turnState();
		turnState.incrementNopeCount();
	}
}
