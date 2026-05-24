package ui;

import domain.input.IPlayerInput;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class GameControllerTest {

	private static final int FIVE_PLAYERS_IN_GAME = 5;

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

	@Test
	void startGame_ValidMaxPlayers_InitializesWithoutError() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(input.promptNumPlayers()).andReturn(FIVE_PLAYERS_IN_GAME);

		EasyMock.replay(display, input);

		GameController controller = new GameController(display, input);
		controller.startGame();

		EasyMock.verify(display, input);
	}
}