package domain.action;

import domain.model.GameState;
import domain.model.Player;
import domain.model.TurnState;

public class AttackAction implements CardAction {

	public void execute(GameState gameState) {
		TurnState turnState = gameState.turnState();
		turnState.startAttack();
		Player nextPlayer = gameState.getNextPlayer();
		nextPlayer.setWasAttacked();
	}
}
