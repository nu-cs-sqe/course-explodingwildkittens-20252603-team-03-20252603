package ui;

import domain.input.IPlayerInput;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartGameIntegrationTest {

	@Test
	void startGame_TwoPlayers_GameStateIsActive() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}
}