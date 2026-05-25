package ui;

import domain.action.CardAction;
import domain.factory.ComboValidator;
import domain.factory.DeckFactory;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;
import domain.model.TurnState;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class GameController {
	private static final int MIN_PLAYERS = 2;
	private static final int MAX_PLAYERS = 5;

	private GameState gameState;
	private final IGameDisplay display;
	private final IPlayerInput input;
	private DeckFactory deckFactory;
	private ComboValidator comboValidator;

	@SuppressFBWarnings("EI_EXPOSE_REP2")
	public GameController(IGameDisplay display, IPlayerInput input) {
		this.display = display;
		this.input = input;
	}

	// 4 params: design.puml requires gameState, display, input, and comboValidator as distinct dependencies
	@SuppressFBWarnings("EI_EXPOSE_REP2")
	public GameController(GameState gameState, IGameDisplay display,
			IPlayerInput input, ComboValidator comboValidator) {
		this.gameState = gameState;
		this.display = display;
		this.input = input;
		this.comboValidator = comboValidator;
	}

	public void startGame() {
		int numPlayers = input.promptNumPlayers();
		while (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
			display.showMessage("Please enter a number of players between 2 and 5.");
			numPlayers = input.promptNumPlayers();
		}
		this.deckFactory = new DeckFactory(numPlayers, input);
		List<Player> players = buildPlayers(numPlayers);
		this.gameState = new GameState(players, deckFactory.buildDeck());
	}

	public void endGame() {
		gameState.endGame();
		display.showWinner(gameState.getCurrentPlayer());
		if (input.promptRestart()) {
			startGame();
		}
	}

	public boolean isGameActive() {
		return gameState.isActive();
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

	private List<Player> buildPlayers(int numPlayers) {
		List<Player> players = new ArrayList<>();
		for (int i = 0; i < numPlayers; i++) {
			players.add(new Player("p" + (i + 1), "Player " + (i + 1)));
		}
		return players;
	}
}
