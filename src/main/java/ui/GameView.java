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
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Scanner;

public class GameView implements IGameDisplay, IPlayerInput {

	private static final String YES_ANSWER = "y";
	private static final String YES_ANSWER_FULL = "yes";
	private static final int PLAY_CARD_OPTION = 1;

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

	public void showGameState(GameState gameState) {
		printGameStateHeader();
		printDeckSize(gameState.getDeckSize());
		printDiscardPileSize(gameState.getDiscardPileSize());
		printActivePlayerCount(gameState.activePlayerCount());
		printTurnsRemaining(gameState.turnState().turnsRemaining());
	}

	private void printGameStateHeader() {
		output.println("--- Game State ---");
	}

	private void printDeckSize(int deckSize) {
		output.println("Deck size: " + deckSize);
	}

	private void printDiscardPileSize(int discardPileSize) {
		output.println("Discard pile: " + discardPileSize);
	}

	private void printActivePlayerCount(int activePlayerCount) {
		output.println("Active players: " + activePlayerCount);
	}

	private void printTurnsRemaining(int turnsRemaining) {
		output.println("Turns remaining: " + turnsRemaining);
	}

	public void showPlayerHand(Player player) {
		printHandHeader(player.getName());
		printNumberedCards(player.getHand());
		printPeekCards(player);
	}

	private void printHandHeader(String playerName) {
		output.println(playerName + "'s hand:");
	}

	private void printPeekCards(Player player) {
		List<Card> peekCards = player.getPeekCards();
		if (peekCards.isEmpty()) {
			return;
		}
		output.println("Peek cards:");
		printNumberedCards(peekCards);
	}

	private void printNumberedCards(List<Card> cards) {
		for (int index = 0; index < cards.size(); index++) {
			int displayNumber = index + 1;
			output.println(displayNumber + ". " + formatCard(cards.get(index)));
		}
	}

	private String formatCard(Card card) {
		for (CardName cardName : CardName.values()) {
			if (card.isName(cardName)) {
				return formatCardName(cardName);
			}
		}
		return "Unknown card";
	}

	private String formatCardName(CardName cardName) {
		return cardName.name().replace('_', ' ');
	}

	public void showMessage(String message) {
		output.println(message);
	}

	public void showWinner(Player player) {
		output.println(player.getName() + " wins!");
	}

	public void showCurrentPlayer(Player player) {
		output.println("--- " + player.getName() + "'s turn ---");
	}

	public List<Card> promptCardSelection(Player player) {
		showPlayerHand(player);
		String line = readCardSelectionLine();
		return cardsAtIndices(player.getHand(), parseIndices(line));
	}

	private String readCardSelectionLine() {
		output.print("Enter card numbers to play (e.g. 1 or 1,2,3): ");
		return scanner.nextLine().trim();
	}

	private List<Card> cardsAtIndices(List<Card> hand, List<Integer> indices) {
		List<Card> selected = new ArrayList<>();
		for (int index : indices) {
			if (isValidHandIndex(hand, index)) {
				selected.add(hand.get(index));
			}
		}
		return selected;
	}

	private boolean isValidHandIndex(List<Card> hand, int index) {
		return index >= 0 && index < hand.size();
	}

	private List<Integer> parseIndices(String input) {
		List<Integer> indices = new ArrayList<>();
		if (input.isEmpty()) {
			return indices;
		}
		for (String token : input.split("[,\\s]+")) {
			addParsedIndex(indices, token);
		}
		return indices;
	}

	private void addParsedIndex(List<Integer> indices, String token) {
		if (token.isEmpty()) {
			return;
		}
		parseDisplayNumber(token).ifPresent(displayNumber -> indices.add(displayNumber - 1));
	}

	private OptionalInt parseDisplayNumber(String token) {
		try {
			return OptionalInt.of(Integer.parseInt(token));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}

	public int promptNumPlayers() {
		output.print("Enter number of players (2-5): ");
		return readInt();
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

	private OptionalInt parseIntegerLine(String line) {
		try {
			return OptionalInt.of(Integer.parseInt(line));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}

	public boolean promptNope(List<Player> players) {
		if (players.isEmpty()) {
			return false;
		}
		Player player = players.get(0);
		output.print(player.getName() + ", play a Nope? (y/n): ");
		return readYesNo();
	}

	private boolean readYesNo() {
		String answer = scanner.nextLine().trim().toLowerCase();
		return YES_ANSWER.equals(answer) || YES_ANSWER_FULL.equals(answer);
	}

	public int promptInsertPosition(int deckSize) {
		output.print("Enter deck position (0-" + deckSize + "): ");
		return readIntInRange(0, deckSize);
	}

	private int readIntInRange(int min, int max) {
		while (true) {
			int value = readInt();
			if (isWithinRange(value, min, max)) {
				return value;
			}
			printRangePrompt(min, max);
		}
	}

	private boolean isWithinRange(int value, int min, int max) {
		return value >= min && value <= max;
	}

	private void printRangePrompt(int min, int max) {
		output.print("Enter a number between " + min + " and " + max + ": ");
	}

	public Player promptTargetSelection(List<Player> candidates) {
		while (true) {
			printNumberedPlayers(candidates);
			int index = readPlayerIndex();
			if (isValidIndex(index, candidates.size())) {
				return candidates.get(index);
			}
			output.println("Invalid selection.");
		}
	}

	private int readPlayerIndex() {
		output.print("Select player number: ");
		return readInt() - 1;
	}

	private boolean isValidIndex(int index, int size) {
		return index >= 0 && index < size;
	}

	private void printNumberedPlayers(List<Player> candidates) {
		for (int index = 0; index < candidates.size(); index++) {
			int displayNumber = index + 1;
			output.println(displayNumber + ". " + candidates.get(index).getName());
		}
	}

	public CardType promptCardType() {
		CardType[] types = CardType.values();
		printNumberedCardTypes(types);
		while (true) {
			int index = readCardTypeIndex();
			if (isValidIndex(index, types.length)) {
				return types[index];
			}
			output.println("Invalid selection.");
		}
	}

	private int readCardTypeIndex() {
		output.print("Select card type number: ");
		return readInt() - 1;
	}

	private void printNumberedCardTypes(CardType[] types) {
		for (int index = 0; index < types.length; index++) {
			int displayNumber = index + 1;
			output.println(displayNumber + ". " + formatCardType(types[index]));
		}
	}

	private String formatCardType(CardType type) {
		return type.name().replace('_', ' ');
	}

	public boolean promptRestart() {
		output.print("Play again? (y/n): ");
		return readYesNo();
	}

	public PlayerChoice promptPlayerChoice() {
		printPlayerChoiceMenu();
		int choice = readInt();
		return resolvePlayerChoice(choice);
	}

	private void printPlayerChoiceMenu() {
		output.println(PLAY_CARD_OPTION + ". Play a card");
		output.println("2. Done playing cards");
		output.print("Choose: ");
	}

	private PlayerChoice resolvePlayerChoice(int choice) {
		if (choice == PLAY_CARD_OPTION) {
			return PlayerChoice.PLAY_CARD;
		}
		return PlayerChoice.DONE_PLAYING_CARDS;
	}
}
