package domain.action;

import domain.model.GameState;
import domain.model.TurnState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class NopeActionTest {

	@Test
	void execute_ValidGameState_IncrementsNopeCount() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);

		EasyMock.expect(mockGameState.turnState()).andReturn(mockTurnState);
		mockTurnState.incrementNopeCount();
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockGameState, mockTurnState);

		new NopeAction().execute(mockGameState);

		EasyMock.verify(mockGameState, mockTurnState);
	}
}
