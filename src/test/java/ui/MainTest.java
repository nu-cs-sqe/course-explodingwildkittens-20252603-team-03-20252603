package ui;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class MainTest {

	private static final int THREE_TURNS = 3;

	@Test
	void runGame_gameInactiveImmediately_playsZeroTurns() {
		GameController controller = EasyMock.createMock(GameController.class);
		controller.startGame();
		EasyMock.expect(controller.isGameActive()).andReturn(false);
		EasyMock.replay(controller);

		Main.runGame(controller);

		EasyMock.verify(controller);
	}

	@Test
	void runGame_gameActiveOneTurn_playsOneTurn() {
		GameController controller = EasyMock.createMock(GameController.class);
		controller.startGame();
		EasyMock.expect(controller.isGameActive()).andReturn(true).andReturn(false);
		controller.playATurn();
		EasyMock.replay(controller);

		Main.runGame(controller);

		EasyMock.verify(controller);
	}

	@Test
	void runGame_gameActiveMultipleTurns_playsMultipleTurns() {
		GameController controller = EasyMock.createMock(GameController.class);
		controller.startGame();
		EasyMock.expect(controller.isGameActive()).andReturn(true).times(THREE_TURNS).andReturn(false);
		controller.playATurn();
		EasyMock.expectLastCall().times(THREE_TURNS);
		EasyMock.replay(controller);

		Main.runGame(controller);

		EasyMock.verify(controller);
	}
}