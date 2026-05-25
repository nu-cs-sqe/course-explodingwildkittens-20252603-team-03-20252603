package ui;

import domain.input.IPlayerInput;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameControllerTest {

	private static final int FIVE_PLAYERS_IN_GAME = 5;
	private static final int SIX_PLAYERS_ATTEMPTED = 6;

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

	@Test
	void startGame_InvalidNumPlayersBelowMin_ShowsErrorAndRepromptsNumPlayers() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(input.promptNumPlayers()).andReturn(1).andReturn(2);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();

		EasyMock.replay(display, input);

		GameController controller = new GameController(display, input);
		controller.startGame();

		EasyMock.verify(display, input);
	}

	@Test
	void startGame_InvalidNumPlayersAboveMax_ShowsErrorAndRepromptsNumPlayers() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(input.promptNumPlayers()).andReturn(SIX_PLAYERS_ATTEMPTED).andReturn(2);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();

		EasyMock.replay(display, input);

		GameController controller = new GameController(display, input);
		controller.startGame();

		EasyMock.verify(display, input);
	}

	@Test
	void endGame_OneActivePlayer_SetsGameInactive() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		display.showWinner(EasyMock.anyObject());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptRestart()).andReturn(false);

		EasyMock.replay(display, input);

		GameController controller = new GameController(display, input);
		controller.startGame();
		controller.endGame();

		assertFalse(controller.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void endGame_OneActivePlayer_DisplaysSurvivor() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		display.showWinner(EasyMock.anyObject());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptRestart()).andReturn(false);

		EasyMock.replay(display, input);

		GameController controller = new GameController(display, input);
		controller.startGame();
		controller.endGame();

		EasyMock.verify(display, input);
	}

	@Test
	void endGame_PromptRestartFalse_DoesNotCallStartGame() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		display.showWinner(EasyMock.anyObject());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptRestart()).andReturn(false);

		EasyMock.replay(display, input);

		GameController controller = new GameController(display, input);
		controller.startGame();
		controller.endGame();

		EasyMock.verify(display, input);
	}

	@Test
	void endGame_PromptRestartTrue_CallsStartGame() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);

		EasyMock.expect(input.promptNumPlayers()).andReturn(2).andReturn(2);
		display.showWinner(EasyMock.anyObject());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptRestart()).andReturn(true);

		EasyMock.replay(display, input);

		GameController controller = new GameController(display, input);
		controller.startGame();
		controller.endGame();

		EasyMock.verify(display, input);
	}
}