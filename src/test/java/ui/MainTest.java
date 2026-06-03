package ui;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MainTest {

	private static final int THREE_TURNS = 3;

	@Test
	void main_DefaultConstructor_CreatesInstance() {
		assertNotNull(new Main());
	}

	@Test
	void main_InputExhaustedAfterStart_ThrowsIllegalStateException() {
		InputStream originalIn = System.in;
		PrintStream originalOut = System.out;
		try {
			System.setIn(new ByteArrayInputStream("2\n".getBytes(StandardCharsets.UTF_8)));
			System.setOut(new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8));
			assertThrows(IllegalStateException.class, () -> Main.main(new String[]{}));
		} finally {
			System.setIn(originalIn);
			System.setOut(originalOut);
		}
	}

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