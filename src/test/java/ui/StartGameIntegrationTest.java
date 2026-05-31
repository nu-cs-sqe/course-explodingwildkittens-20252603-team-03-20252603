package ui;

import domain.input.IPlayerInput;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartGameIntegrationTest {

	private static final int THREE_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;

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

	@Test
	void startGame_ThreePlayers_GameStateIsActive() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(THREE_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_FourPlayers_GameStateIsActive() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(FOUR_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}
}