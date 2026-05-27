package ui;

import domain.enums.CardName;
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
import java.util.ArrayList;
import java.util.OptionalInt;

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

	public void showPlayerHand(Player player) {
		printHandHeader(player.getName());
		printNumberedCards(player.getHand());
		printPeekCards(player);
	}

	public void showWinner(Player player) {
		output.println(player.getName() + " wins!");
	}

	public void showCurrentPlayer(Player player) {
		output.println("--- " + player.getName() + "'s turn ---");
	}

	public void showGameState(GameState gameState) {
		printGameStateHeader();
		printDeckSize(gameState.getDeckSize());
		printDiscardPileSize(gameState.getDiscardPileSize());
		printActivePlayerCount(gameState.activePlayerCount());
		printTurnsRemaining(gameState.turnState().turnsRemaining());
	}

	public void showMessage(String message) {
		output.println(message);
	}

	public int promptNumPlayers() {
		output.print("Enter number of players (2-5): ");
		return readInt();
	}

	private void printActivePlayerCount(int activePlayerCount) {
		output.println("Active players: " + activePlayerCount);
	}

	private int readInt() {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			OptionalInt value = parseIntegerLine(line);
			if (value.isPresent()) {
				return value.getAsInt();
			}
			output.print("Enter a valid number: ");
		}
		return 0;
	}

	private void printHandHeader(String playerName) {
		output.println(playerName + "'s hand:");
	}

	private String formatCardName(CardName cardName) {
		return cardName.name().replace('_', ' ');
	}

	private OptionalInt parseIntegerLine(String line) {
		try {
			return OptionalInt.of(Integer.parseInt(line));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}

	private void printTurnsRemaining(int turnsRemaining) {
		output.println("Turns remaining: " + turnsRemaining);
	}

	private String formatCard(Card card) {
		for (CardName cardName : CardName.values()) {
			if (card.isName(cardName)) {
				return formatCardName(cardName);
			}
		}
		return "Unknown card";
	}

	private void printDiscardPileSize(int discardPileSize) {
		output.println("Discard pile: " + discardPileSize);
	}

	private void printNumberedCards(List<Card> cards) {
		for (int index = 0; index < cards.size(); index++) {
			int displayNumber = index + 1;
			output.println(displayNumber + ". " + formatCard(cards.get(index)));
		}
	}

	private void printDeckSize(int deckSize) {
		output.println("Deck size: " + deckSize);
	}

	private void printPeekCards(Player player) {
		List<Card> peekCards = player.getPeekCards();
		if (peekCards.isEmpty()) {
			return;
		}
		output.println("Peek cards:");
		printNumberedCards(peekCards);
	}

	private void printGameStateHeader() {
		output.println("--- Game State ---");
	}

	public List<Card> promptCardSelection(Player player) {
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
