package ui;

import domain.model.Card;
import domain.model.Player;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.Scanner;

final class TerminalInputReader {

	private static final int MIN_PLAYERS = 2;
	private static final int MAX_PLAYERS = 5;
	private static final int DISPLAY_NUMBER_OFFSET = 1;
	private static final int LOCALE_FIRST_OPTION = 1;
	private static final int LOCALE_LAST_OPTION = 4;
	private static final int LOCALE_FRENCH_OPTION = 2;
	private static final int LOCALE_GERMAN_OPTION = 3;
	private static final int LOCALE_SPANISH_OPTION = 4;

	private final Scanner scanner;
	private final PrintStream output;
	private final TerminalDisplayWriter display;

	TerminalInputReader(Scanner scanner, PrintStream output, TerminalDisplayWriter display) {
		this.scanner = scanner;
		this.output = output;
		this.display = display;
	}

	Locale promptLocale() {
		output.println("Select language / Sprache wählen / Choisir la langue / Seleccionar idioma:");
		output.println("1. English");
		output.println("2. Français (French)");
		output.println("3. Deutsch (German)");
		output.println("4. Español (Spanish)");
		output.print("Choice (1-4): ");
		int choice = readLocaleChoice();

		if (choice == LOCALE_GERMAN_OPTION) {
			return Locale.GERMAN;
		} else if (choice == LOCALE_SPANISH_OPTION) {
			return new Locale("es");
		} else if (choice == LOCALE_FRENCH_OPTION) {
			return Locale.FRENCH;
		} else return Locale.ENGLISH;
	}

	private int readLocaleChoice() {
		return readIntInRange(LOCALE_FIRST_OPTION, LOCALE_LAST_OPTION);
	}

	private static final String CANCEL_KEYWORD = "DONE_PLAYING";

	List<Card> promptCardSelection(Player player) {
		display.showPlayerHand(player);
		output.print(ViewMessages.format("view.prompt.card.selection"));
		String line = scanner.nextLine().trim();
		if (line.equalsIgnoreCase(CANCEL_KEYWORD)) {
			return Collections.emptyList();
		}
		return cardsAtIndices(player.getHand(), parseIndices(line));
	}

	int promptNumPlayers() {
		output.print(ViewMessages.format("view.prompt.num.players"));
		return readIntInRange(MIN_PLAYERS, MAX_PLAYERS);
	}

	boolean promptNope(Player player) {
		if (player == null) {
			throw new IllegalArgumentException(ViewMessages.format("error.card.arg.null"));
		}
		output.print(ViewMessages.format("view.prompt.nope", player.getName()));
		return readYesNo();
	}

	void showPeekCards(Player player) {
		display.printPeekCards(player);
	}

	int promptInsertPosition(int deckSize) {
		output.print(ViewMessages.format("view.prompt.insert.position", deckSize));
		return readIntInRange(0, deckSize);
	}

	Player promptTargetSelection(List<Player> candidates) {
		while (true) {
			display.printNumberedPlayers(candidates);
			int index = readPlayerIndex();
			if (isValidIndex(index, candidates.size())) {
				return candidates.get(index);
			}
			display.printInvalidSelection();
		}
	}

	int readCardTypeIndex() {
		output.print(ViewMessages.format("view.prompt.card.type"));
		return readInt() - DISPLAY_NUMBER_OFFSET;
	}

	boolean promptRestart() {
		output.print(ViewMessages.format("view.prompt.restart"));
		return readYesNo();
	}

	int readPlayerChoice(Player player) {
		display.printPlayerChoiceMenu(player);
		return readInt();
	}

	private int readPlayerIndex() {
		output.print(ViewMessages.format("view.prompt.select.player"));
		return readInt() - DISPLAY_NUMBER_OFFSET;
	}

	int readIntInRange(int min, int max) {
		while (true) {
			int value = readInt();
			if (isWithinRange(value, min, max)) {
				return value;
			}
			output.print(ViewMessages.format("view.prompt.range", min, max));
		}
	}

	private int readInt() {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			OptionalInt value = parseIntegerLine(line);
			if (value.isPresent()) {
				return value.getAsInt();
			}
			output.print(ViewMessages.format("view.prompt.valid.number"));
		}
		throw new IllegalStateException(ViewMessages.format("error.input.exhausted"));
	}

	private boolean readYesNo() {
		String yes = ViewMessages.format("input.yes");
		String no = ViewMessages.format("input.no");
		while (scanner.hasNextLine()) {
			String answer = scanner.nextLine().trim().toLowerCase();
			if (yes.equals(answer)) {
				return true;
			}
			if (no.equals(answer)) {
				return false;
			}
			output.print(ViewMessages.format("view.prompt.yes.no"));
		}
		throw new IllegalStateException(ViewMessages.format("error.input.exhausted"));
	}

	private OptionalInt parseIntegerLine(String line) {
		try {
			return OptionalInt.of(Integer.parseInt(line));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}

	private boolean isWithinRange(int value, int min, int max) {
		return value >= min && value <= max;
	}

	private boolean isValidIndex(int index, int size) {
		return index >= 0 && index < size;
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
		parseDisplayNumber(token).ifPresent(
			displayNumber -> indices.add(displayNumber - DISPLAY_NUMBER_OFFSET));
	}

	private OptionalInt parseDisplayNumber(String token) {
		try {
			return OptionalInt.of(Integer.parseInt(token));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}
}
