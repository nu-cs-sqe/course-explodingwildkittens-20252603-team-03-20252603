package ui;

import domain.model.GameState;
import domain.factory.DeckFactory;
import domain.factory.ComboValidator;
import domain.input.IPlayerInput;
import domain.model.Card;

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
	}

	public void drawCard() {
	}

	public void endGame() {
	}
}