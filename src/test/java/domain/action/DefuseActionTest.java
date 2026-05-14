package domain.action;

import domain.input.IPlayerInput;
import domain.model.GameState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefuseActionTest {

	private static final int DECK_SIZE = 3;

	private static GameState mockGameStateWithDeckSize(int deckSize) {
		GameState mock = EasyMock.createMock(GameState.class);
		EasyMock.expect(mock.getDeckSize()).andReturn(deckSize);
		return mock;
	}

	@Test
	void execute_NoPendingKitten_ThrowsIllegalStateException() {
		GameState mockGameState = mockGameStateWithDeckSize(DECK_SIZE);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(mockInput.promptInsertPosition(DECK_SIZE)).andReturn(0);
		mockGameState.insertPendingCardAt(0);
		EasyMock.expectLastCall().andThrow(new IllegalStateException());

		EasyMock.replay(mockGameState, mockInput);

		assertThrows(IllegalStateException.class,
				() -> new DefuseAction(mockInput).execute(mockGameState));

		EasyMock.verify(mockGameState, mockInput);
	}

	@Test
	void execute_KittenPending_PositionZero_InsertsAtTop() {
		GameState mockGameState = mockGameStateWithDeckSize(DECK_SIZE);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(mockInput.promptInsertPosition(DECK_SIZE)).andReturn(0);
		mockGameState.insertPendingCardAt(0);
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockGameState, mockInput);

		new DefuseAction(mockInput).execute(mockGameState);

		EasyMock.verify(mockGameState, mockInput);
	}

	@Test
	void execute_KittenPending_PositionDeckSize_InsertsAtBottom() {
		GameState mockGameState = mockGameStateWithDeckSize(DECK_SIZE);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(mockInput.promptInsertPosition(DECK_SIZE)).andReturn(DECK_SIZE);
		mockGameState.insertPendingCardAt(DECK_SIZE);
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockGameState, mockInput);

		new DefuseAction(mockInput).execute(mockGameState);

		EasyMock.verify(mockGameState, mockInput);
	}

	@Test
	void execute_KittenPending_PositionMiddle_InsertsAtMiddle() {
		GameState mockGameState = mockGameStateWithDeckSize(DECK_SIZE);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(mockInput.promptInsertPosition(DECK_SIZE)).andReturn(1);
		mockGameState.insertPendingCardAt(1);
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockGameState, mockInput);

		new DefuseAction(mockInput).execute(mockGameState);

		EasyMock.verify(mockGameState, mockInput);
	}
}
