package domain.action;

import domain.model.GameState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class ShuffleActionTest {

	@Test
	void execute_ValidGameState_ShufflesDeck() {
		GameState mockGameState = EasyMock.createMock(GameState.class);

		mockGameState.shuffleDeck();
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockGameState);

		new ShuffleAction().execute(mockGameState);

		EasyMock.verify(mockGameState);
	}
}
