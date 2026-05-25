package ui;

import domain.model.GameState;
import domain.model.Player;

public interface IGameDisplay {
	void showGameState(GameState gameState);

	void showPlayerHand(Player player);

	void showMessage(String message);

	void showWinner(Player player);

	void showCurrentPlayer(Player player);
}