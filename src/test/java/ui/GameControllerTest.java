package ui;

import domain.action.CardAction;
import domain.action.NoAction;
import domain.enums.CardName;
import domain.enums.CardType;
import domain.factory.ComboValidator;
import domain.input.IPlayerInput;
import domain.model.*;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("checkstyle:MethodLength")
public class GameControllerTest {

	private static final int FIVE_PLAYERS_IN_GAME = 5;
	private static final int SIX_PLAYERS_ATTEMPTED = 6;

	private GameState mockGameState;
	private IGameDisplay mockDisplay;
	private IPlayerInput mockInput;
	private ComboValidator mockValidator;
	private GameController controller;
	private static final int THREE_TURNS = 3;
	private static final int FOUR_TURNS = 4;
	private static final int FIVE_TURNS = 5;
	private static final int SIX_TURNS = 6;

	@BeforeEach
	void setUp() {
		mockGameState = EasyMock.createMock(GameState.class);
		mockDisplay = EasyMock.createMock(IGameDisplay.class);
		mockInput = EasyMock.createMock(IPlayerInput.class);
		mockValidator = EasyMock.createMock(ComboValidator.class);
		controller = new GameController(mockGameState, mockDisplay, mockInput, mockValidator);
	}

	private GameController createGameController(GameState gameState) {
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		ComboValidator mockComboValidator = EasyMock.createMock(ComboValidator.class);
		return new GameController(gameState, mockDisplay, mockInput, mockComboValidator);
	}

	private GameState createMockGameState(TurnState turnState) {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.turnState()).andReturn(turnState).anyTimes();
		return mockGameState;
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
		EasyMock.expect(mockInput.promptNope(other)).andReturn(true);
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
		EasyMock.expect(mockInput.promptNope(other)).andReturn(false);
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
		EasyMock.expect(mockInput.promptNope(p1)).andReturn(false);
		EasyMock.expect(mockInput.promptNope(p2)).andReturn(false);
		EasyMock.expect(mockInput.promptNope(p3)).andReturn(false);
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
		EasyMock.expect(mockInput.promptNope(p1)).andReturn(false);
		EasyMock.expect(mockInput.promptNope(p2)).andReturn(true);
		EasyMock.expect(mockInput.promptNope(p3)).andReturn(false);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
		assertEquals(1, turnState.nopeCount());
	}

	@Test
	void applyNopeWindow_MultiplePlayers_AllNope_NopeCountIsThree() {
		final int expectedNopeCount = 3;
		List<Card> cards = List.of(skipCard());
		TurnState turnState = new TurnState();
		Player p1 = new Player("p1", "Player 1");
		Player p2 = new Player("p2", "Player 2");
		Player p3 = new Player("p3", "Player 3");
		expectBasePlaySetup(cards, turnState);
		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(List.of(p1, p2, p3));
		EasyMock.expect(mockInput.promptNope(p1)).andReturn(true);
		EasyMock.expect(mockInput.promptNope(p2)).andReturn(true);
		EasyMock.expect(mockInput.promptNope(p3)).andReturn(true);
		EasyMock.replay(mockGameState, mockDisplay, mockInput, mockValidator);

		controller.playCard(cards);

		EasyMock.verify(mockGameState, mockDisplay, mockInput, mockValidator);
		assertEquals(expectedNopeCount, turnState.nopeCount());
	}

	@Test
	void startGame_ValidMinPlayers_InitializesWithoutError() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		EasyMock.verify(display, input);
	}

	@Test
	void startGame_ValidMaxPlayers_InitializesWithoutError() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(FIVE_PLAYERS_IN_GAME);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		EasyMock.verify(display, input);
	}

	@Test
	void startGame_InvalidNumPlayersBelowMin_ShowsErrorAndRepromptsNumPlayers() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(1).andReturn(2);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		EasyMock.verify(display, input);
	}

	@Test
	void startGame_InvalidNumPlayersAboveMax_ShowsErrorAndRepromptsNumPlayers() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(SIX_PLAYERS_ATTEMPTED).andReturn(2);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		EasyMock.verify(display, input);
	}

	@Test
	void dealCardsAndReturnDeck_twoPlayers_updatesPlayersHandsAndReturnsDeck() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		ComboValidator comboValidator = EasyMock.createMock(ComboValidator.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		List<Player> players = List.of(new Player("p1", "Player 1"), new Player("p2", "Player 2"));
		Deck deck = gc.dealCardsAndReturnDeck(players);

		assertEquals(1, deck.countCardsByName(CardName.EXPLODING_KITTEN));
		for (Player player : players) {
			assertFalse(player.getHand().isEmpty());
		}
		EasyMock.verify(display, input);
	}

	@Test
	void dealCardsAndReturnDeck_fivePlayers_updatesPlayersHandsAndReturnsDeck() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		ComboValidator comboValidator = EasyMock.createMock(ComboValidator.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(FIVE_PLAYERS_IN_GAME);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		List<Player> players = List.of(
				new Player("p1", "Player 1"), new Player("p2", "Player 2"),
				new Player("p3", "Player 3"), new Player("p4", "Player 4"),
				new Player("p5", "Player 5"));
		Deck deck = gc.dealCardsAndReturnDeck(players);

		assertEquals(FIVE_PLAYERS_IN_GAME - 1, deck.countCardsByName(CardName.EXPLODING_KITTEN));
		for (Player player : players) {
			assertFalse(player.getHand().isEmpty());
		}
		EasyMock.verify(display, input);
	}

	@Test
	void endGame_OneActivePlayer_SetsGameInactive() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		display.showWinner(EasyMock.anyObject());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptRestart()).andReturn(false);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();
		gc.endGame();

		assertFalse(gc.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void endGame_OneActivePlayer_DisplaysSurvivor() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		display.showWinner(EasyMock.anyObject());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptRestart()).andReturn(false);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();
		gc.endGame();

		EasyMock.verify(display, input);
	}

	@Test
	void endGame_PromptRestartFalse_DoesNotCallStartGame() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		display.showWinner(EasyMock.anyObject());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptRestart()).andReturn(false);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();
		gc.endGame();

		EasyMock.verify(display, input);
	}

	@Test
	void endGame_PromptRestartTrue_CallsStartGame() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(2).andReturn(2);
		display.showWinner(EasyMock.anyObject());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptRestart()).andReturn(true);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();
		gc.endGame();

		EasyMock.verify(display, input);
	}

	@Test
	void hasToPlayATurn_isAttackingIsTrue_ReturnsFalse() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(true);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		assertFalse(controller.hasToPlayATurn());

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsZero_ReturnsFalse() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(false);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(0);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		assertFalse(controller.hasToPlayATurn());

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsOne_ReturnsTrue() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(false);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(1);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		assertTrue(controller.hasToPlayATurn());

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsGreaterThanOne_ReturnsTrue() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(false);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(2);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		assertTrue(controller.hasToPlayATurn());

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsIntMax_ReturnsTrue() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(false);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(Integer.MAX_VALUE);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		assertTrue(controller.hasToPlayATurn());

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void handleDrawingCards_skipDrawIsTrue_SkipsDraw() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.shouldSkipDraw()).andReturn(true);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		controller.handleDrawingCards();

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void handleTurnTaking_notAttacking_DecrementsAndReturnsOne() {
		TurnState turnState = new TurnState();
		// i am not attacking
		// i was also not attacked (i just have one turn to play)
		int currentTurns = 1;
		turnState.reset(currentTurns);

		GameState mockGameState = createMockGameState(turnState);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(1, nextPlayerTurns);
		assertEquals(0, turnState.turnsRemaining());
		EasyMock.verify(mockGameState);
	}

	@Test
	void handleTurnTaking_notAttacking_turnsRemainingIsThree_DecrementsAndReturnsOne() {
		TurnState turnState = new TurnState();
		// have been attacked by someone who was attacked (i have to play 4 turns);
		// and I just played one turn (3 turns left)
		// i am not attacking the next guy
		int currentTurns = THREE_TURNS;
		turnState.reset(currentTurns);

		GameState mockGameState = createMockGameState(turnState);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(1, nextPlayerTurns);
		assertEquals(2, turnState.turnsRemaining());
		EasyMock.verify(mockGameState);
	}

	@Test
	void handleTurnTaking_attackingNotWasAttacked_DecrementsAndReturnsTwo() {
		TurnState turnState = new TurnState();
		// i am attacking the next guy;
		// i was not attacked, (i have one turn to play)
		int currentTurns = 1;
		turnState.reset(currentTurns);
		turnState.startAttack();

		GameState mockGameState = createMockGameState(turnState);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.wasAttacked()).andReturn(false);

		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(2, nextPlayerTurns);
		assertEquals(0, turnState.turnsRemaining());
		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void handleTurnTaking_attackingWasAttacked_turnsRemainingIsOne_DecrementsAndReturnsThree() {
		TurnState turnState = new TurnState();
		// i am attacking the next guy;
		// i was attacked, and i already played one turn (one turn left)
		int currentTurns = 1;
		turnState.reset(currentTurns);
		turnState.startAttack();

		GameState mockGameState = createMockGameState(turnState);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.wasAttacked()).andReturn(true);

		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(THREE_TURNS, nextPlayerTurns);
		assertEquals(0, turnState.turnsRemaining());
		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void handleTurnTaking_attackingWasAttacked_turnsRemainingIsTwo_DecrementsAndReturnsFour() {
		TurnState turnState = new TurnState();
		//i am attacking the next guy
		// i was attacked (so I have to play 2 turns and this is my first turn)
		int currentTurns = 2;
		turnState.reset(currentTurns);
		turnState.startAttack();

		GameState mockGameState = createMockGameState(turnState);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.wasAttacked()).andReturn(true);

		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(FOUR_TURNS, nextPlayerTurns);
		assertEquals(1, turnState.turnsRemaining());
		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void handleTurnTaking_attackingWasAttacked_turnsRemainingIsThree_DecrementsAndReturnsFive() {
		TurnState turnState = new TurnState();
		// i am attacking the next guy
		// I was attacked by someone who was also attacked (i have to play 4 turns)
		// I just played one turn; 3 turns left
		int currentTurns = THREE_TURNS;
		turnState.reset(currentTurns);
		turnState.startAttack();

		GameState mockGameState = createMockGameState(turnState);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.wasAttacked()).andReturn(true);

		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(FIVE_TURNS, nextPlayerTurns);
		assertEquals(2, turnState.turnsRemaining());
		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void handleTurnTaking_attackingWasAttacked_turnsRemainingIsFour_DecrementsAndReturnsSix() {
		TurnState turnState = new TurnState();
		// I am attacking the next guy
		// I was attacked by someone who was also attacked (I have to play 4 turns)
		// I have not played any yet (I have 4 turns to go)
		int currentTurns = FOUR_TURNS;
		turnState.reset(currentTurns);
		turnState.startAttack();

		GameState mockGameState = createMockGameState(turnState);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.wasAttacked()).andReturn(true);

		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(SIX_TURNS, nextPlayerTurns);
		assertEquals(THREE_TURNS, turnState.turnsRemaining());
		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void resetGameState_turnsForNextPlayerIsOne_ResetsTurnState() {
		TurnState turnState = new TurnState();
		turnState.startAttack();
		turnState.enableSkipDraw();

		GameState mockGameState = createMockGameState(turnState);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		controller.resetGameState(1);

		assertEquals(1, turnState.turnsRemaining());
		assertFalse(turnState.isAttacking());
		assertFalse(turnState.shouldSkipDraw());
		EasyMock.verify(mockGameState);
	}

	@Test
	void resetGameState_turnsForNextPlayerIsTwo_ResetsTurnState() {
		TurnState turnState = new TurnState();
		turnState.startAttack();
		turnState.enableSkipDraw();

		GameState mockGameState = createMockGameState(turnState);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		controller.resetGameState(2);

		assertEquals(2, turnState.turnsRemaining());
		assertFalse(turnState.isAttacking());
		assertFalse(turnState.shouldSkipDraw());
		EasyMock.verify(mockGameState);
	}

	@Test
	void resetGameState_turnsForNextPlayerIsThree_ResetsTurnState() {
		TurnState turnState = new TurnState();
		turnState.startAttack();
		turnState.enableSkipDraw();

		GameState mockGameState = createMockGameState(turnState);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		controller.resetGameState(THREE_TURNS);

		assertEquals(THREE_TURNS, turnState.turnsRemaining());
		assertFalse(turnState.isAttacking());
		assertFalse(turnState.shouldSkipDraw());
		EasyMock.verify(mockGameState);
	}

	@Test
	void resetCurrentPlayerWasAttacked_wasAttackedIsTrue_ResetToFalse() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		GameState mockGameState = createMockGameState(mockTurnState);

		Player player = new Player("fakeId", "fakeName");
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(player);

		EasyMock.replay(mockGameState, mockTurnState);

		player.setWasAttacked();

		GameController controller = createGameController(mockGameState);
		controller.resetCurrentPlayerWasAttacked();

		assertFalse(player.wasAttacked());
		EasyMock.verify(mockGameState);
	}

	@Test
	void resetCurrentPlayerWasAttacked_wasAttackedIsFalse_ResetToFalse() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		GameState mockGameState = createMockGameState(mockTurnState);

		Player player = new Player("fakeId", "fakeName");
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(player);

		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		controller.resetCurrentPlayerWasAttacked();

		assertFalse(player.wasAttacked());
		EasyMock.verify(mockGameState);
	}

	@Test
	void advanceGameToNextPlayer_CallsAdvancePlayerOnGameState() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		mockGameState.advancePlayer();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		controller.advanceGameToNextPlayer();

		EasyMock.verify(mockGameState);
	}

	@Test
	void readyToPlayATurn_gameStateNotActive_ReturnsFalse() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.isActive()).andReturn(false);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		assertFalse(controller.readyToPlayATurn());

		EasyMock.verify(mockGameState);
	}

	@Test
	void readyToPlayATurn_gameStateActiveCurrentPlayerNotActive_ReturnsFalse() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.isActive()).andReturn(false);
		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		assertFalse(controller.readyToPlayATurn());

		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void readyToPlayATurn_gameStateActiveCurrentPlayerActive_ReturnsTrue() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.isActive()).andReturn(true);
		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		assertTrue(controller.readyToPlayATurn());

		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void playATurn_notReadyToPlayATurn_throwsException() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.isActive()).andReturn(false);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);

		assertThrows(IllegalStateException.class, controller::playATurn);

		EasyMock.verify(mockGameState);
	}

	@Test
	void playATurn_ReadyToPlayATurn_DoesNotHaveToPlayATurn_NoLoopRun() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		ComboValidator comboValidator = EasyMock.createMock(ComboValidator.class);
		Player mockPlayer = EasyMock.createMock(Player.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		TurnState turnState = new TurnState();
		turnState.reset(0);
		EasyMock.expect(mockGameState.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer).anyTimes();
		EasyMock.expect(mockPlayer.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.turnState()).andReturn(turnState).anyTimes();
		display.showCurrentPlayer(mockPlayer);
		EasyMock.expectLastCall().once();
		mockPlayer.resetWasAttacked();
		EasyMock.expectLastCall().once();
		mockGameState.advancePlayer();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockPlayer, display, input, comboValidator);
		new GameController(mockGameState, display, input, comboValidator).playATurn();
		EasyMock.verify(mockGameState, mockPlayer, display, input, comboValidator);
	}

	@Test
	void playATurn_ReadyToPlayATurn_HasToPlayATurn_PlayACard_OneLoopRun_PlaysCards() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		ComboValidator comboValidator = EasyMock.createMock(ComboValidator.class);
		CardAction mockAction = EasyMock.createMock(CardAction.class);
		Player mockPlayer = EasyMock.createMock(Player.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		TurnState turnState = new TurnState();
		Card card = skipCard();
		List<Card> cards = List.of(card);
		EasyMock.expect(mockGameState.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer).anyTimes();
		EasyMock.expect(mockPlayer.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.turnState()).andReturn(turnState).anyTimes();
		display.showCurrentPlayer(mockPlayer);
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice()).andReturn(domain.enums.PlayerChoice.PLAY_CARD);
		EasyMock.expect(input.promptCardSelection(mockPlayer)).andReturn(cards);
		EasyMock.expect(comboValidator.isValid(cards)).andReturn(true);
		mockGameState.removeCardFromCurrentPlayer(card);
		EasyMock.expectLastCall().once();
		mockGameState.discardCard(card);
		EasyMock.expectLastCall().once();
		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(Collections.emptyList());
		EasyMock.expect(comboValidator.resolveAction(cards)).andReturn(mockAction);
		mockAction.execute(mockGameState);
		EasyMock.expectLastCall().once();
		mockPlayer.resetWasAttacked();
		EasyMock.expectLastCall().once();
		mockGameState.advancePlayer();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockPlayer, display, input, comboValidator, mockAction);
		new GameController(mockGameState, display, input, comboValidator).playATurn();
		EasyMock.verify(mockGameState, mockPlayer, display, input, comboValidator, mockAction);
	}

	@Test
	void playATurn_ReadyToPlayATurn_HasToPlayATurn_DonePlaying_OneLoopRun_DrawsCardsAndTurnTaking() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		ComboValidator comboValidator = EasyMock.createMock(ComboValidator.class);
		Card mockCard = EasyMock.createMock(Card.class);
		Player mockPlayer = EasyMock.createMock(Player.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		TurnState turnState = new TurnState();
		EasyMock.expect(mockGameState.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer).anyTimes();
		EasyMock.expect(mockPlayer.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.turnState()).andReturn(turnState).anyTimes();
		display.showCurrentPlayer(mockPlayer);
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice()).andReturn(domain.enums.PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(mockGameState.drawFromDeck()).andReturn(mockCard);
		EasyMock.expect(mockCard.isType(CardType.EXPLODING_KITTEN)).andReturn(false);
		mockGameState.addCardToCurrentPlayer(mockCard);
		EasyMock.expectLastCall().once();
		mockPlayer.resetWasAttacked();
		EasyMock.expectLastCall().once();
		mockGameState.advancePlayer();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockPlayer, display, input, comboValidator, mockCard);
		new GameController(mockGameState, display, input, comboValidator).playATurn();
		EasyMock.verify(mockGameState, mockPlayer, display, input, comboValidator, mockCard);
	}

	private void setupPlayATurnBaseExpectations(TurnState turnState, Player player, GameState gameState) {
		EasyMock.expect(gameState.isActive()).andReturn(true);
		EasyMock.expect(gameState.getCurrentPlayer()).andReturn(player).anyTimes();
		EasyMock.expect(gameState.turnState()).andReturn(turnState).anyTimes();
		EasyMock.expect(player.isActive()).andReturn(true);
	}

	@Test
	void handleDrawingCards_skipDrawIsFalse_DrawsCard() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		Card mockCard = EasyMock.createMock(Card.class);
		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.expect(mockTurnState.shouldSkipDraw()).andReturn(false);
		EasyMock.expect(mockGameState.drawFromDeck()).andReturn(mockCard);
		EasyMock.expect(mockCard.isType(CardType.EXPLODING_KITTEN)).andReturn(false);
		mockGameState.addCardToCurrentPlayer(mockCard);
		EasyMock.replay(mockGameState, mockTurnState, mockCard);
		createGameController(mockGameState).handleDrawingCards();
		EasyMock.verify(mockGameState, mockTurnState, mockCard);
	}

	@Test
	void drawCard_drawnCardIsNotExplodingKitten_AddsCardToPlayerHand() {
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		ComboValidator mockComboValidator = EasyMock.createMock(ComboValidator.class);
		Card mockCard = EasyMock.createMock(Card.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.drawFromDeck()).andReturn(mockCard);
		EasyMock.expect(mockCard.isType(CardType.EXPLODING_KITTEN)).andReturn(false);
		mockGameState.addCardToCurrentPlayer(mockCard);
		EasyMock.replay(mockGameState, mockCard, mockInput, mockDisplay);
		new GameController(mockGameState,mockDisplay, mockInput,mockComboValidator).drawCard();
		EasyMock.verify(mockGameState, mockCard, mockInput, mockDisplay);
	}

	@Test
	void drawCard_drawnCardIsExplodingKitten_playerHasDefuse_ExecutesDefuseAction() {
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		ComboValidator mockComboValidator = EasyMock.createMock(ComboValidator.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		Card mockCard = EasyMock.createMock(Card.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.drawFromDeck()).andReturn(mockCard);
		EasyMock.expect(mockCard.isType(CardType.EXPLODING_KITTEN)).andReturn(true);
		EasyMock.expect(mockGameState.turnState()).andReturn(mockTurnState);
		mockTurnState.setPendingAction(mockCard);
		EasyMock.expect(mockGameState.currentPlayerHasCard(CardType.DEFUSE)).andReturn(true);
		EasyMock.expect(mockGameState.getDeckSize()).andReturn(1);
		EasyMock.expect(mockInput.promptInsertPosition(1)).andReturn(0);
		mockGameState.insertPendingCardAt(0);
		EasyMock.replay(mockGameState, mockTurnState, mockCard, mockInput, mockDisplay);
		new GameController(mockGameState, mockDisplay, mockInput, mockComboValidator).drawCard();
		EasyMock.verify(mockGameState, mockTurnState, mockCard, mockInput, mockDisplay);
	}

	@Test
	void drawCard_drawnCardIsExplodingKitten_playerHasNoDefuse_EliminatesPlayer() {
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		ComboValidator mockComboValidator = EasyMock.createMock(ComboValidator.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		Card mockCard = EasyMock.createMock(Card.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.drawFromDeck()).andReturn(mockCard);
		EasyMock.expect(mockCard.isType(CardType.EXPLODING_KITTEN)).andReturn(true);
		EasyMock.expect(mockGameState.turnState()).andReturn(mockTurnState);
		mockTurnState.setPendingAction(mockCard);
		EasyMock.expect(mockGameState.currentPlayerHasCard(CardType.DEFUSE)).andReturn(false);
		mockGameState.eliminateCurrentPlayer();
		EasyMock.replay(mockGameState, mockTurnState, mockCard, mockInput, mockDisplay);
		new GameController(mockGameState, mockDisplay, mockInput, mockComboValidator).drawCard();
		EasyMock.verify(mockGameState, mockTurnState, mockCard, mockInput, mockDisplay);
	}

}
