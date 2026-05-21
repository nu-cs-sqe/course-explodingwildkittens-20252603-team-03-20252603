package ui;

import domain.action.CardAction;
import domain.factory.ComboValidator;
import domain.factory.DeckFactory;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;
import domain.model.TurnState;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("UUF_UNUSED_FIELD")
public class GameController {
	private GameState gameState;
	private IGameDisplay display;
	private IPlayerInput input;
	private DeckFactory deckFactory;
	private ComboValidator comboValidator;

	public GameController(GameState gameState, IGameDisplay display, IPlayerInput input,
			DeckFactory deckFactory, ComboValidator comboValidator) {
		this.gameState = gameState;
		this.display = display;
		this.input = input;
		this.deckFactory = deckFactory;
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
		if (input.promptNope(others)) {
			turnState.incrementNopeCount();
		}
	}

	public void drawCard() {
	}

	public void endGame() {
	}
}