package ui;

import domain.action.CardAction;
import domain.factory.ComboValidator;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;
import domain.model.TurnState;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class GameController {
	private final GameState gameState;
	private final IGameDisplay display;
	private final IPlayerInput input;
	private final ComboValidator comboValidator;

	// 4 params: design.puml requires gameState, display, input, and comboValidator as distinct dependencies
	@SuppressFBWarnings("EI_EXPOSE_REP2")
	public GameController(GameState gameState, IGameDisplay display,
			IPlayerInput input, ComboValidator comboValidator) {
		this.gameState = gameState;
		this.display = display;
		this.input = input;
		this.comboValidator = comboValidator;
	}

	public void startGame(int numPlayers) {
	}

	public void playATurn() {
	}

	public void playCard(List<Card> cards) {
		if (cards == null) {
			throw new IllegalArgumentException("cards must not be null");
		}
		if (!comboValidator.isValid(cards)) {
			display.showMessage("Invalid card selection.");
			return;
		}
		TurnState turnState = gameState.turnState();
		turnState.setPendingAction(cards.get(0));
		discardPlayedCards(cards);
		applyNopeWindow(turnState);
		if (turnState.nopeCount() % 2 == 0) {
			CardAction action = comboValidator.resolveAction(cards);
			action.execute(gameState);
		}
		turnState.clearPendingAction();
	}

	private void discardPlayedCards(List<Card> cards) {
		for (Card card : cards) {
			gameState.removeCardFromCurrentPlayer(card);
			gameState.discardCard(card);
		}
	}

	private void applyNopeWindow(TurnState turnState) {
		List<Player> others = gameState.getOtherActivePlayers();
		for (Player player : others) {
			if (input.promptNope(List.of(player))) {
				turnState.incrementNopeCount();
			}
		}
	}

	public void drawCard() {
	}

	public void endGame() {
	}
}
