package ui;

import domain.factory.ComboValidator;
import domain.factory.DeckFactory;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.Deck;
import domain.model.GameState;
import domain.model.Player;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("UUF_UNUSED_FIELD")
public class GameController {
	private static final int MIN_PLAYERS = 2;
	private static final int MAX_PLAYERS = 5;

	private GameState gameState;
	private IGameDisplay display;
	private IPlayerInput input;
	private DeckFactory deckFactory;
	private ComboValidator comboValidator;

	public GameController(IGameDisplay display, IPlayerInput input) {
		this.display = display;
		this.input = input;
	}

	public void startGame() {
		int numPlayers = input.promptNumPlayers();
		while (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
			display.showMessage("Please enter a number of players between 2 and 5.");
			numPlayers = input.promptNumPlayers();
		}
		List<Player> players = buildPlayers(numPlayers);
		this.gameState = new GameState(players, new Deck(new ArrayList<>()));
	}

	public void endGame() {
		gameState.endGame();
		display.showWinner(gameState.getCurrentPlayer());
		input.promptRestart();
	}

	public void playATurn() {
	}

	public void playCard(List<Card> cards) {
	}

	public void drawCard() {
	}

	private List<Player> buildPlayers(int numPlayers) {
		List<Player> players = new ArrayList<>();
		for (int i = 0; i < numPlayers; i++) {
			players.add(new Player("p" + (i + 1), "Player " + (i + 1)));
		}
		return players;
	}
}