package ui;

import domain.input.IPlayerInput;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class GameControllerTest {

	@Test
	void startGame_ValidMinPlayers_InitializesWithoutError() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(input.promptNumPlayers()).andReturn(2);

		EasyMock.replay(display, input);

		GameController controller = new GameController(display, input);
		controller.startGame();

		EasyMock.verify(display, input);
	}
}