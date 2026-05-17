package controller;

import domain.action.DefuseAction;
import domain.enums.CardType;
import domain.enums.PlayerChoice;
import domain.factory.PlayerInteractionHelper;
import domain.model.GameState;
import domain.factory.DeckFactory;
import domain.factory.ComboValidator;
import domain.model.Card;
import domain.model.Player;

import java.util.List;
import java.util.Random;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings({"UUF_UNUSED_FIELD", "URF_UNREAD_FIELD", "UWF_UNWRITTEN_FIELD", "NP_UNWRITTEN_FIELD"})
public class GameController {
	private static final int DEFAULT_NORMAL_TURNS = 1;
	private static final int DEFAULT_ATTACKING_TURNS = 2;

	private GameState gameState;
	private final IGameDisplay display;
	private final IPlayerInput input;
	private DeckFactory deckFactory;
	private ComboValidator comboValidator;

	public GameController(IGameDisplay display, IPlayerInput input) {
		this.display = display;
		this.input = input;

		PlayerInteractionHelper playerInteractionHelper = new PlayerInteractionHelper(input, new Random());
		ComboValidator comboValidator = new ComboValidator(playerInteractionHelper);

		this.comboValidator = comboValidator;
	}

	// for testing only
	GameController(IGameDisplay display, IPlayerInput input, GameState gameState) {
		this.display = display;
		this.input = input;
		this.gameState = gameState;
	}



	public void startGame(int numPlayers) {
	}

	public void playATurn() {
		if (!readyToPlayATurn()) {
			throw new IllegalStateException("Game state is not ready to play a turn");}
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
				turnsForNextPlayer = handleTurnTaking();}
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

	public void endGame() {
	}
}