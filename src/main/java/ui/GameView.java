package ui;

import domain.enums.CardType;
import domain.enums.PlayerChoice;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class GameView implements IGameDisplay, IPlayerInput {

	private final Scanner scanner;
	private final PrintStream output;

	public GameView() {
		this(new Scanner(System.in, StandardCharsets.UTF_8), System.out);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP2")
	public GameView(Scanner scanner, PrintStream output) {
		this.scanner = scanner;
		this.output = output;
	}

	public void showMessage(String message) {
		output.println(message);
	}

	public void showGameState(GameState gameState) {
		throw new UnsupportedOperationException();
	}

	public void showPlayerHand(Player player) {
		throw new UnsupportedOperationException();
	}

	public void showWinner(Player player) {
		throw new UnsupportedOperationException();
	}

	public void showCurrentPlayer(Player player) {
		throw new UnsupportedOperationException();
	}

	public List<Card> promptCardSelection(Player player) {
		throw new UnsupportedOperationException();
	}

	public int promptNumPlayers() {
		throw new UnsupportedOperationException();
	}

	public boolean promptNope(List<Player> players) {
		throw new UnsupportedOperationException();
	}

	public int promptInsertPosition(int deckSize) {
		throw new UnsupportedOperationException();
	}

	public Player promptTargetSelection(List<Player> candidates) {
		throw new UnsupportedOperationException();
	}

	public CardType promptCardType() {
		throw new UnsupportedOperationException();
	}

	public boolean promptRestart() {
		throw new UnsupportedOperationException();
	}

	public PlayerChoice promptPlayerChoice() {
		throw new UnsupportedOperationException();
	}
}
