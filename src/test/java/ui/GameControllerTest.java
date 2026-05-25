package ui;

import domain.action.CardAction;
import domain.action.NoAction;
import domain.enums.CardName;
import domain.enums.CardType;
import domain.factory.ComboValidator;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;
import domain.model.TurnState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

	private GameState mockGameState;
	private IGameDisplay mockDisplay;
	private IPlayerInput mockInput;
	private ComboValidator mockValidator;
	private GameController controller;

	@BeforeEach
	void setUp() {
		mockGameState = EasyMock.createMock(GameState.class);
		mockDisplay = EasyMock.createMock(IGameDisplay.class);
		mockInput = EasyMock.createMock(IPlayerInput.class);
		mockValidator = EasyMock.createMock(ComboValidator.class);
		controller = new GameController(mockGameState, mockDisplay, mockInput, mockValidator);
	}

	private Card defuseCard() {
		return new Card(CardType.DEFUSE, CardName.DEFUSE, new NoAction());
	}

	private Card catCard() {
		return new Card(CardType.CAT_CARD, CardName.TACO_CAT, new NoAction());
	}

	private Card skipCard() {
		return new Card(CardType.SKIP, CardName.SKIP, new NoAction());
	}

	private Player otherPlayer() {
		return new Player("p2", "Other Player");
	}

	private void expectBasePlaySetup(List<Card> cards, TurnState turnState) {
		EasyMock.expect(mockValidator.isValid(cards)).andReturn(true);
		EasyMock.expect(mockGameState.turnState()).andReturn(turnState).anyTimes();
		for (Card card : cards) {
			mockGameState.removeCardFromCurrentPlayer(card);
			EasyMock.expectLastCall().once();
			mockGameState.discardCard(card);
			EasyMock.expectLastCall().once();
		}
	}

	private void expectValidPlaySetup(List<Card> cards, TurnState turnState, CardAction mockAction) {
		expectBasePlaySetup(cards, turnState);
		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(Collections.emptyList());
		EasyMock.expect(mockValidator.resolveAction(cards)).andReturn(mockAction);
		mockAction.execute(mockGameState);
		EasyMock.expectLastCall().once();
	}

	private void expectNopedPlaySetup(List<Card> cards, TurnState turnState) {
		Player other = otherPlayer();
		expectBasePlaySetup(cards, turnState);
		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(List.of(other));
		EasyMock.expect(mockInput.promptNope(List.of(other))).andReturn(true);
	}

	@Test
	void playCard_NullList_ThrowsIllegalArgumentException() {
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		assertThrows(IllegalArgumentException.class, () -> controller.playCard(null));

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
	}

	@Test
	void playCard_EmptyList_ShowsErrorMessage() {
		List<Card> cards = Collections.emptyList();
		EasyMock.expect(mockValidator.isValid(cards)).andReturn(false);
		mockDisplay.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
	}

	@Test
	void playCard_SingleDefuse_ShowsErrorMessage() {
		List<Card> cards = List.of(defuseCard());
		EasyMock.expect(mockValidator.isValid(cards)).andReturn(false);
		mockDisplay.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
	}

	@Test
	void playCard_SingleCatCard_ShowsErrorMessage() {
		List<Card> cards = List.of(catCard());
		EasyMock.expect(mockValidator.isValid(cards)).andReturn(false);
		mockDisplay.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
	}

	@Test
	void playCard_ValidSingleCard_NotNoped_ExecutesAction() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		CardAction mockAction = EasyMock.createMock(CardAction.class);
		expectValidPlaySetup(cards, turnState, mockAction);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);
	}

	@Test
	void playCard_ValidSingleCard_NotNoped_CardsAddedToDiscard() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		CardAction mockAction = EasyMock.createMock(CardAction.class);
		expectValidPlaySetup(cards, turnState, mockAction);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);
	}

	@Test
	void playCard_ValidSingleCard_NotNoped_ClearsPendingAction() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		CardAction mockAction = EasyMock.createMock(CardAction.class);
		expectValidPlaySetup(cards, turnState, mockAction);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);
		assertTrue(turnState.pendingAction().isEmpty());
	}

	@Test
	void playCard_ValidSingleCard_Noped_ActionNotExecuted() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		expectNopedPlaySetup(cards, turnState);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
	}

	@Test
	void playCard_ValidSingleCard_Noped_IncrementsNopeCount() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		expectNopedPlaySetup(cards, turnState);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
		assertEquals(1, turnState.nopeCount());
	}

	@Test
	void playCard_ValidSingleCard_Noped_CardsStillDiscarded() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		expectNopedPlaySetup(cards, turnState);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
	}

	@Test
	void playCard_ValidSingleCard_Noped_ClearsPendingAction() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		expectNopedPlaySetup(cards, turnState);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
		assertTrue(turnState.pendingAction().isEmpty());
	}

	@Test
	void playCard_TwoCatCombo_NotNoped_ExecutesActionAndDiscardsCards() {
		List<Card> cards = List.of(catCard(), catCard());
		TurnState turnState = new TurnState();
		CardAction mockAction = EasyMock.createMock(CardAction.class);
		expectValidPlaySetup(cards, turnState, mockAction);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);
	}

	@Test
	void playCard_ThreeCatCombo_NotNoped_ExecutesActionAndDiscardsCards() {
		List<Card> cards = List.of(catCard(), catCard(), catCard());
		TurnState turnState = new TurnState();
		CardAction mockAction = EasyMock.createMock(CardAction.class);
		expectValidPlaySetup(cards, turnState, mockAction);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);
	}

	@Test
	void applyNopeWindow_OnePlayer_DoesNotNope_NopeCountUnchanged() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		Player other = otherPlayer();
		CardAction mockAction = EasyMock.createMock(CardAction.class);
		expectBasePlaySetup(cards, turnState);
		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(List.of(other));
		EasyMock.expect(mockInput.promptNope(List.of(other))).andReturn(false);
		EasyMock.expect(mockValidator.resolveAction(cards)).andReturn(mockAction);
		mockAction.execute(mockGameState);
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);
		assertEquals(0, turnState.nopeCount());
	}

	@Test
	void applyNopeWindow_MultiplePlayers_NobodyNopes_NopeCountUnchanged() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		Player p1 = new Player("p1", "Player 1");
		Player p2 = new Player("p2", "Player 2");
		Player p3 = new Player("p3", "Player 3");
		CardAction mockAction = EasyMock.createMock(CardAction.class);
		expectBasePlaySetup(cards, turnState);
		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(List.of(p1, p2, p3));
		EasyMock.expect(mockInput.promptNope(List.of(p1))).andReturn(false);
		EasyMock.expect(mockInput.promptNope(List.of(p2))).andReturn(false);
		EasyMock.expect(mockInput.promptNope(List.of(p3))).andReturn(false);
		EasyMock.expect(mockValidator.resolveAction(cards)).andReturn(mockAction);
		mockAction.execute(mockGameState);
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator, mockAction);
		assertEquals(0, turnState.nopeCount());
	}

	@Test
	void applyNopeWindow_MultiplePlayers_OneNopes_NopeCountIsOne() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		Player p1 = new Player("p1", "Player 1");
		Player p2 = new Player("p2", "Player 2");
		Player p3 = new Player("p3", "Player 3");
		expectBasePlaySetup(cards, turnState);
		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(List.of(p1, p2, p3));
		EasyMock.expect(mockInput.promptNope(List.of(p1))).andReturn(false);
		EasyMock.expect(mockInput.promptNope(List.of(p2))).andReturn(true);
		EasyMock.expect(mockInput.promptNope(List.of(p3))).andReturn(false);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
		assertEquals(1, turnState.nopeCount());
	}

	@Test
	void applyNopeWindow_MultiplePlayers_AllNope_NopeCountIsThree() {
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		Player p1 = new Player("p1", "Player 1");
		Player p2 = new Player("p2", "Player 2");
		Player p3 = new Player("p3", "Player 3");
		expectBasePlaySetup(cards, turnState);
		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(List.of(p1, p2, p3));
		EasyMock.expect(mockInput.promptNope(List.of(p1))).andReturn(true);
		EasyMock.expect(mockInput.promptNope(List.of(p2))).andReturn(true);
		EasyMock.expect(mockInput.promptNope(List.of(p3))).andReturn(true);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
		assertEquals(3, turnState.nopeCount());
	}
}
