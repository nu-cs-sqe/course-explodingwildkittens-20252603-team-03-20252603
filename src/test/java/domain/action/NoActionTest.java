package domain.action;

import domain.model.GameState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class NoActionTest {

	@Test
	void execute_AnyGameState_DoesNothing() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.replay(mockGameState);
		new NoAction().execute(mockGameState);
		EasyMock.verify(mockGameState);
	}
}
