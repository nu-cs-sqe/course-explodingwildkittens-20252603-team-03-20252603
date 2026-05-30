package ui;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class MainTest {

	@Test
	void runGame_gameInactiveImmediately_playsZeroTurns() {
		GameController controller = EasyMock.createMock(GameController.class);
		controller.startGame();
		EasyMock.expect(controller.isGameActive()).andReturn(false);
		EasyMock.replay(controller);

		Main.runGame(controller);

		EasyMock.verify(controller);
	}
}