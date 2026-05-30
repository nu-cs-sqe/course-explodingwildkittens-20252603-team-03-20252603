package ui;

import domain.action.DefuseAction;
import domain.enums.CardType;
import domain.enums.PlayerChoice;
import domain.model.*;
import domain.action.CardAction;
import domain.factory.ComboValidator;
import domain.factory.DeckFactory;
import domain.input.IPlayerInput;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings({"UUF_UNUSED_FIELD", "URF_UNREAD_FIELD", "UWF_UNWRITTEN_FIELD", "NP_UNWRITTEN_FIELD"})
public class GameController {
	private static final int MIN_PLAYERS = 2;
	private static final int MAX_PLAYERS = 5;
	private static final int DEFAULT_NORMAL_TURNS = 1;
	private static final int DEFAULT_ATTACKING_TURNS = 2;
	private static final int NUM_CARDS_PER_PLAYER = 7;

	private GameState gameState;
	private final IGameDisplay display;
	private final IPlayerInput input;
	private DeckFactory deckFactory;
	private ComboValidator comboValidator;

	@SuppressFBWarnings("EI_EXPOSE_REP2")
	public GameController(IGameDisplay display, IPlayerInput input, ComboValidator comboValidator) {
		this.display = display;
		this.input = input;
		this.comboValidator = comboValidator;
	}

	// 4 params: design.puml requires gameState, display, input, and comboValidator as distinct dependencies
	@SuppressWarnings("checkstyle:ParameterNumber")
	@SuppressFBWarnings("EI_EXPOSE_REP2")
	GameController(GameState gameState, IGameDisplay display,
		IPlayerInput input, ComboValidator comboValidator){
		this.gameState = gameState;
		this.display = display;
		this.input = input;
		this.comboValidator = comboValidator;
	}

	public void startGame() {
		int numPlayers = input.promptNumPlayers();
		while (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
			display.showMessage(ViewMessages.format("num.players"));
			numPlayers = input.promptNumPlayers();
		}
		this.deckFactory = new DeckFactory(numPlayers, input);
		List<Player> players = buildPlayers(numPlayers);
		Deck finalDeck = dealCardsAndReturnDeck(players);
		this.gameState = new GameState(players, finalDeck);
	}

	Deck dealCardsAndReturnDeck(List<Player> players) {
		Deck deck = deckFactory.buildDeck();
		List<Card> defuseCards = deckFactory.buildDefuseCards();
		List<Card> explodingKittenCards = deckFactory.buildExplodingKittenCards();

		deck.shuffle();
		for (Player player : players) {
			List<Card> cards = deck.dealCards(NUM_CARDS_PER_PLAYER);
			player.addCards(cards);

			if (!defuseCards.isEmpty()) {
				Card defuseCard = defuseCards.remove(0);
				player.addCard(defuseCard);
			}
		}

		deck.addToDeck(explodingKittenCards);
		deck.addToDeck(defuseCards);
		deck.shuffle();
		return deck;
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
		if (!readyToPlayATurn()) {
			throw new IllegalStateException(ViewMessages.format("error.not.ready.to.play"));}
		Player currentPlayer = gameState.getCurrentPlayer();
		display.showCurrentPlayer(currentPlayer);
		int turnsForNextPlayer = DEFAULT_NORMAL_TURNS;
		while (hasToPlayATurn()) {
			PlayerChoice playerChoice = input.promptPlayerChoice();
			if (playerChoice == PlayerChoice.PLAY_CARD) {
				List<Card> chosenCards = input.promptCardSelection(currentPlayer);
				playCard(chosenCards);
			} else {
				handleDrawingCards();
				}
			turnsForNextPlayer = handleTurnTaking();
		}
		resetCurrentPlayerWasAttacked();
		resetGameState(turnsForNextPlayer);
		advanceGameToNextPlayer();
	}


	boolean readyToPlayATurn() {
		if (!gameState.isActive()) {
			return false;
		}

		Player currentPlayer = gameState.getCurrentPlayer();
		if (!currentPlayer.isActive()) {
			return false;
		}

		return true;
	}

	boolean hasToPlayATurn() {
		if (gameState.turnState().isAttacking()){
			return false;
		}
		else {
			int turnsLeft = gameState.turnState().turnsRemaining();
			return turnsLeft != 0;
		}
	}

	void handleDrawingCards() {
		boolean shouldSkipDraw = gameState.turnState().shouldSkipDraw();
		if (!shouldSkipDraw) {
			drawCard();
		}
	}

	int handleTurnTaking() {
		boolean currentPlayerIsAttacking = gameState.turnState().isAttacking();

		if (!currentPlayerIsAttacking) {
			gameState.turnState().decrementTurns();
			return DEFAULT_NORMAL_TURNS;
		} else {
			Player currentPlayer = gameState.getCurrentPlayer();
			if (currentPlayer.wasAttacked()){
				int nextPlayerTurns = gameState.turnState().turnsRemaining() + DEFAULT_ATTACKING_TURNS;
				gameState.turnState().decrementTurns();
				return nextPlayerTurns;
			} else {
				int nextPlayerTurns = DEFAULT_ATTACKING_TURNS;
				gameState.turnState().decrementTurns();
				return nextPlayerTurns;
			}
		}

	}

	void resetCurrentPlayerWasAttacked() {
		Player currentPlayer = gameState.getCurrentPlayer();
		currentPlayer.resetWasAttacked();
	};

	void resetGameState(int turnsForNextPlayer) {
		gameState.turnState().reset(turnsForNextPlayer);
	}

	void advanceGameToNextPlayer() {
		gameState.advancePlayer();
	}

	public void playCard(List<Card> cards) {
		if (cards == null) {
			throw new IllegalArgumentException(ViewMessages.format("error.cards.arg.null"));
		}
		if (!comboValidator.isValid(cards)) {
			display.showMessage(ViewMessages.format("error.invalid.card"));
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
			if (input.promptNope(player)) {
				turnState.incrementNopeCount();
			}
		}
	}

	public void drawCard() {
		Card card = gameState.drawFromDeck();
		if (card.isType(CardType.EXPLODING_KITTEN)) {
			gameState.turnState().setPendingAction(card);
			if (gameState.currentPlayerHasCard(CardType.DEFUSE)) {
				new DefuseAction(input).execute(gameState);
			} else {
				gameState.eliminateCurrentPlayer();
			}
		} else {
			gameState.addCardToCurrentPlayer(card);
		}
	}

	private List<Player> buildPlayers(int numPlayers) {
		List<Player> players = new ArrayList<>();
		for (int i = 0; i < numPlayers; i++) {
			players.add(new Player(
				"p" + (i + 1),
				ViewMessages.format("player.name", i + 1)));
		}
		return players;
	}
}
