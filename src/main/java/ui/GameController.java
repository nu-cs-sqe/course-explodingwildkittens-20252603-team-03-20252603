package ui;

import domain.factory.ComboValidator;
import domain.factory.DeckFactory;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.GameState;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("UUF_UNUSED_FIELD")
public class GameController {
	private GameState gameState;
	private IPlayerInput input;
	private DeckFactory deckFactory;
	private ComboValidator comboValidator;

	public GameController(IGameDisplay display, IPlayerInput input) {
		this.input = input;
	}

	public void startGame() {
		input.promptNumPlayers();
	}

	public void playATurn() {
	}

	public void playCard(List<Card> cards) {
	}

	public void drawCard() {
	}

	public void endGame() {
	}
}