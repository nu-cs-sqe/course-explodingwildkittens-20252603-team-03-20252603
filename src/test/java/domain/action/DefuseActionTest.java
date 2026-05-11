package domain.action;

import domain.input.IPlayerInput;
import domain.model.GameState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefuseActionTest {

    private static GameState mockGameStateWithDeckSize(int deckSize) {
        GameState mock = EasyMock.createMock(GameState.class);
        EasyMock.expect(mock.getDeckSize()).andReturn(deckSize);
        return mock;
    }

    @Test
    void execute_NoPendingKitten_ThrowsIllegalStateException() {
        GameState mockGameState = mockGameStateWithDeckSize(3);
        IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);

        EasyMock.expect(mockInput.promptInsertPosition(3)).andReturn(0);
        mockGameState.insertPendingCardAt(0);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(mockGameState, mockInput);

        assertThrows(IllegalStateException.class,
                () -> new DefuseAction(mockInput).execute(mockGameState));

        EasyMock.verify(mockGameState, mockInput);
    }

    @Test
    void execute_KittenPending_PositionZero_InsertsAtTop() {
        GameState mockGameState = mockGameStateWithDeckSize(3);
        IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);

        EasyMock.expect(mockInput.promptInsertPosition(3)).andReturn(0);
        mockGameState.insertPendingCardAt(0);
        EasyMock.expectLastCall().once();

        EasyMock.replay(mockGameState, mockInput);

        new DefuseAction(mockInput).execute(mockGameState);

        EasyMock.verify(mockGameState, mockInput);
    }

    @Test
    void execute_KittenPending_PositionDeckSize_InsertsAtBottom() {
        GameState mockGameState = mockGameStateWithDeckSize(3);
        IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);

        EasyMock.expect(mockInput.promptInsertPosition(3)).andReturn(3);
        mockGameState.insertPendingCardAt(3);
        EasyMock.expectLastCall().once();

        EasyMock.replay(mockGameState, mockInput);

        new DefuseAction(mockInput).execute(mockGameState);

        EasyMock.verify(mockGameState, mockInput);
    }

    @Test
    void execute_KittenPending_PositionMiddle_InsertsAtMiddle() {
        GameState mockGameState = mockGameStateWithDeckSize(3);
        IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);

        EasyMock.expect(mockInput.promptInsertPosition(3)).andReturn(1);
        mockGameState.insertPendingCardAt(1);
        EasyMock.expectLastCall().once();

        EasyMock.replay(mockGameState, mockInput);

        new DefuseAction(mockInput).execute(mockGameState);

        EasyMock.verify(mockGameState, mockInput);
    }
}
