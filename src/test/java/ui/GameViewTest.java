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

	private static Player mockNamedPlayer(String name) {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.getName()).andReturn(name).anyTimes();
		EasyMock.replay(mockPlayer);
		return mockPlayer;
	}

	private static GameState mockActiveGameState() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockGameState.getDeckSize()).andReturn(DECK_SIZE);
		EasyMock.expect(mockGameState.getDiscardPileSize()).andReturn(DISCARD_SIZE);
		EasyMock.expect(mockGameState.activePlayerCount()).andReturn(ACTIVE_PLAYERS);
		EasyMock.expect(mockGameState.turnState()).andReturn(mockTurnState);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(TURNS_REMAINING);
		EasyMock.replay(mockGameState, mockTurnState);
		return mockGameState;
	}

	private static Player mockPlayerWithHand(String name, List<Card> hand) {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.getName()).andReturn(name).anyTimes();
		EasyMock.expect(mockPlayer.getHand()).andReturn(hand).anyTimes();
		EasyMock.expect(mockPlayer.getPeekCards()).andReturn(List.of()).anyTimes();
		EasyMock.replay(mockPlayer);
		return mockPlayer;
	}

	private static Player mockPlayerWithPeek(String name, List<Card> peekCards) {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.getName()).andReturn(name).anyTimes();
		EasyMock.expect(mockPlayer.getHand()).andReturn(List.of()).anyTimes();
		EasyMock.expect(mockPlayer.getPeekCards()).andReturn(peekCards).anyTimes();
		EasyMock.replay(mockPlayer);
		return mockPlayer;
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

	@Test
	void showWinner_NamedPlayer_PrintsWinnerLine() {
		Player mockPlayer = mockNamedPlayer("Alice");
		createView("").showWinner(mockPlayer);
		assertTrue(capturedOutput().contains("Alice wins!"));
		EasyMock.verify(mockPlayer);
	}

	@Test
	void showCurrentPlayer_NamedPlayer_PrintsTurnHeader() {
		Player mockPlayer = mockNamedPlayer("Bob");
		createView("").showCurrentPlayer(mockPlayer);
		assertTrue(capturedOutput().contains("Bob's turn"));
		EasyMock.verify(mockPlayer);
	}

	@Test
	void showGameState_ActiveGame_PrintsSummary() {
		GameState mockGameState = mockActiveGameState();
		createView("").showGameState(mockGameState);
		String output = capturedOutput();
		assertTrue(output.contains("Deck size: " + DECK_SIZE));
		assertTrue(output.contains("Discard pile: " + DISCARD_SIZE));
		assertTrue(output.contains("Active players: " + ACTIVE_PLAYERS));
		assertTrue(output.contains("Turns remaining: " + TURNS_REMAINING));
		EasyMock.verify(mockGameState);
	}

	@Test
	void showPlayerHand_EmptyHand_PrintsHeaderOnly() {
		Player mockPlayer = mockPlayerWithHand("Alice", List.of());
		createView("").showPlayerHand(mockPlayer);
		String output = capturedOutput();
		assertTrue(output.contains("Alice's hand:"));
		assertFalse(output.contains("1. "));
		EasyMock.verify(mockPlayer);
	}

	@Test
	void showPlayerHand_WithCards_PrintsNumberedCards() {
		Player mockPlayer = mockPlayerWithHand("Alice", List.of(skipCard()));
		createView("").showPlayerHand(mockPlayer);
		assertTrue(capturedOutput().contains("1. SKIP"));
		EasyMock.verify(mockPlayer);
	}

	@Test
	void showPlayerHand_WithPeekCards_PrintsPeekSection() {
		Player mockPlayer = mockPlayerWithPeek("Alice", List.of(skipCard()));
		createView("").showPlayerHand(mockPlayer);
		assertTrue(capturedOutput().contains("Peek cards:"));
		EasyMock.verify(mockPlayer);
	}

	@Test
	void promptNumPlayers_ValidInput_ReturnsValue() {
		int count = createView("3\n").promptNumPlayers();
		assertEquals(THREE_PLAYERS, count);
	}

	@Test
	void promptNumPlayers_InvalidThenValid_ReturnsValue() {
		int count = createView("x\n4\n").promptNumPlayers();
		assertEquals(FOUR_PLAYERS, count);
	}

	@Test
	void promptCardSelection_EmptyInput_ReturnsEmptyList() {
		Player mockPlayer = mockPlayerWithHand("Alice", List.of(skipCard()));
		List<Card> selected = createView("\n").promptCardSelection(mockPlayer);
		assertTrue(selected.isEmpty());
		EasyMock.verify(mockPlayer);
	}

	@Test
	void promptCardSelection_SingleIndex_ReturnsCard() {
		Card card = skipCard();
		Player mockPlayer = mockPlayerWithHand("Alice", List.of(card));
		List<Card> selected = createView("1\n").promptCardSelection(mockPlayer);
		assertEquals(SINGLE_CARD, selected.size());
		assertEquals(card, selected.get(0));
		EasyMock.verify(mockPlayer);
	}

	@Test
	void promptCardSelection_MultipleIndices_ReturnsCards() {
		Player mockPlayer = mockPlayerWithHand("Alice", List.of(skipCard(), skipCard()));
		List<Card> selected = createView("1,2\n").promptCardSelection(mockPlayer);
		assertEquals(TWO_CARDS, selected.size());
		EasyMock.verify(mockPlayer);
	}

	@Test
	void promptCardSelection_OutOfRangeIndex_ReturnsEmptyList() {
		Player mockPlayer = mockPlayerWithHand("Alice", List.of(skipCard()));
		List<Card> selected = createView("9\n").promptCardSelection(mockPlayer);
		assertTrue(selected.isEmpty());
		EasyMock.verify(mockPlayer);
	}

	@Test
	void promptNope_EmptyList_ReturnsFalse() {
		assertFalse(createView("y\n").promptNope(List.of()));
	}

	@Test
	void promptNope_YesAnswer_ReturnsTrue() {
		Player mockPlayer = mockNamedPlayer("Bob");
		assertTrue(createView("y\n").promptNope(List.of(mockPlayer)));
		EasyMock.verify(mockPlayer);
	}

	@Test
	void promptNope_NoAnswer_ReturnsFalse() {
		Player mockPlayer = mockNamedPlayer("Bob");
		assertFalse(createView("n\n").promptNope(List.of(mockPlayer)));
		EasyMock.verify(mockPlayer);
	}

	@Test
	void promptInsertPosition_OutOfRangeThenValid_ReturnsValue() {
		int position = createView("5\n1\n").promptInsertPosition(DECK_SIZE_TWO);
		assertEquals(INSERT_POSITION_ONE, position);
	}

	@Test
	void promptInsertPosition_MinBoundary_ReturnsZero() {
		int position = createView("0\n").promptInsertPosition(DECK_SIZE_THREE);
		assertEquals(0, position);
	}

}
