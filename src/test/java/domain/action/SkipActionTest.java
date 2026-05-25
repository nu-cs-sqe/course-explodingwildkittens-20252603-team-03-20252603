package domain.action;

import domain.model.GameState;
import domain.model.TurnState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class SkipActionTest {

	@Test
	void execute_ValidGameState_EnablesSkipDraw() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);

		EasyMock.expect(mockGameState.turnState()).andReturn(mockTurnState);
		mockTurnState.enableSkipDraw();
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockGameState, mockTurnState);

		new SkipAction().execute(mockGameState);

		EasyMock.verify(mockGameState, mockTurnState);
	}
}
