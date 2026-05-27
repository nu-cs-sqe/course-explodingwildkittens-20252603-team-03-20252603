package ui;

import domain.action.SkipAction;
import domain.enums.CardName;
import domain.enums.CardType;
import domain.enums.PlayerChoice;
import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;
import domain.model.TurnState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameViewTest {

	private static final int SINGLE_CARD = 1;
	private static final int TWO_CARDS = 2;
	private static final int THREE_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;
	private static final int DECK_SIZE_TWO = 2;
	private static final int DECK_SIZE_THREE = 3;
	private static final int INSERT_POSITION_ONE = 1;
	private static final int DECK_SIZE = 2;
	private static final int DISCARD_SIZE = 1;
	private static final int ACTIVE_PLAYERS = 2;
	private static final int TURNS_REMAINING = 1;

	private ByteArrayOutputStream outputBuffer;

	private GameView createView(String input) {
		outputBuffer = new ByteArrayOutputStream();
		PrintStream output = new PrintStream(outputBuffer, true, StandardCharsets.UTF_8);
		Scanner scanner = new Scanner(new StringReader(input));
		return new GameView(scanner, output);
	}

	private String capturedOutput() {
		return outputBuffer.toString(StandardCharsets.UTF_8);
	}

	private static Card skipCard() {
		return new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
	}

	@Test
	void showMessage_NonEmpty_PrintsMessage() {
		createView("").showMessage("Hello");
		assertTrue(capturedOutput().contains("Hello"));
	}

	@Test
	void showMessage_Empty_PrintsBlankLine() {
		createView("").showMessage("");
		assertTrue(capturedOutput().contains(System.lineSeparator()));
	}

}
