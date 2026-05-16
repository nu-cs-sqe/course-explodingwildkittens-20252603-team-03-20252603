package controller;

import domain.model.GameState;
import domain.model.TurnState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

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
}