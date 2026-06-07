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
import java.util.Locale;
import java.util.Scanner;

public class GameView implements IGameDisplay, IPlayerInput {

	private static final int PLAY_CARD_OPTION = 1;

	private final TerminalDisplayWriter display;
	private final TerminalInputReader input;

	public GameView() {
		this(new Scanner(System.in, StandardCharsets.UTF_8), System.out);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP2")
	public GameView(Scanner scanner, PrintStream output) {
		this.display = new TerminalDisplayWriter(output);
		this.input = new TerminalInputReader(scanner, output, display);
	}

	public Locale promptLocale() {
		return input.promptLocale();
	}

	public void showGameState(GameState gameState) {
		display.showGameState(gameState);
	}

	public void showPlayerHand(Player player) {
		display.showPlayerHand(player);
	}

	public void showMessage(String message) {
		display.showMessage(message);
	}

	public void showWinner(Player player) {
		display.showWinner(player);
	}

	public void showCurrentPlayer(Player player) {
		display.showCurrentPlayer(player);
	}

	public List<Card> promptCardSelection(Player player) {
		return input.promptCardSelection(player);
	}

	public int promptNumPlayers() {
		return input.promptNumPlayers();
	}

	public boolean promptNope(Player player) {
		return input.promptNope(player);
	}

	public int promptInsertPosition(int deckSize) {
		return input.promptInsertPosition(deckSize);
	}

	public Player promptTargetSelection(List<Player> candidates) {
		return input.promptTargetSelection(candidates);
	}

	public CardType promptCardType() {
		CardType[] types = CardType.values();
		display.printNumberedCardTypes(types);
		while (true) {
			int index = input.readCardTypeIndex();
			if (isValidIndex(index, types.length)) {
				return types[index];
			}
			display.printInvalidSelection();
		}
	}

	public boolean promptRestart() {
		return input.promptRestart();
	}

	public PlayerChoice promptPlayerChoice() {
		int choice = input.readPlayerChoice();
		return resolvePlayerChoice(choice);
	}

	private boolean isValidIndex(int index, int size) {
		return index >= 0 && index < size;
	}

	private PlayerChoice resolvePlayerChoice(int choice) {
		if (choice == PLAY_CARD_OPTION) {
			return PlayerChoice.PLAY_CARD;
		}
		return PlayerChoice.DONE_PLAYING_CARDS;
	}

	public void showPeekCards(Player player) {
		input.showPeekCards(player);
	}
}
