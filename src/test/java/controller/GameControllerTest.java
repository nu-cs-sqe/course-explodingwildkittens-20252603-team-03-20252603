package controller;

import domain.model.GameState;
import domain.model.Player;
import domain.model.TurnState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

	private static final int THREE_TURNS = 3;
	private static final int FOUR_TURNS = 4;
	private static final int FIVE_TURNS = 5;
	private static final int SIX_TURNS = 6;

	private GameController createGameController(GameState gameState) {
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		return new GameController(mockDisplay, mockInput, gameState);
	}

	private GameState createMockGameState(TurnState turnState) {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.turnState()).andReturn(turnState).anyTimes();
		return mockGameState;
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
}