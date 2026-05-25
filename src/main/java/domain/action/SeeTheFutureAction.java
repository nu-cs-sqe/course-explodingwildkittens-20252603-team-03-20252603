package domain.action;

import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;

import java.util.List;

public class SeeTheFutureAction implements CardAction {

	private static final int NUM_CARDS_TO_PEEK = 3;

	public void execute(GameState gameState) {
		List<Card> topCards = gameState.peekTopOfDeck(NUM_CARDS_TO_PEEK);
		Player currentPlayer = gameState.getCurrentPlayer();
		currentPlayer.storePeek(topCards);
	}
}
