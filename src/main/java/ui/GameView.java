package ui;

import domain.enums.CardType;
import domain.input.IPlayerInput;
import domain.enums.PlayerChoice;
import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;

import java.util.List;

public class GameView implements IGameDisplay, IPlayerInput {
	public void showGameState(GameState gameState) {
	}

	public void showPlayerHand(Player player) {
	}

	public void showMessage(String message) {
	}

	public void showWinner(Player player) {
	}

	public void showCurrentPlayer(Player player) {
	}

	public List<Card> promptCardSelection(Player player) {
		return null;
	}

	public int promptNumPlayers() {
		return 0;
	}

	public boolean promptNope(List<Player> players) {
		return false;
	}

	public int promptInsertPosition(int deckSize) {
		return 0;
	}

	public Player promptTargetSelection(List<Player> candidates) {
		throw new UnsupportedOperationException();
	}

	public CardType promptCardType() {
		throw new UnsupportedOperationException();
	}

	public boolean promptRestart() {
		return false;
	}

	public PlayerChoice promptPlayerChoice() {return null;}


}