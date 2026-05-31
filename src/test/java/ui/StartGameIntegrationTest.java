package ui;

import domain.input.IPlayerInput;
import domain.model.GameState;
import domain.model.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartGameIntegrationTest {

	private static final int NEGATIVE_NUM_PLAYERS = -1;
	private static final int ONE_PLAYER = 1;
	private static final int THREE_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;
	private static final int FIVE_PLAYERS = 5;
	private static final int SIX_PLAYERS = 6;

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

	@Test
	void startGame_FivePlayers_GameStateIsActive() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(FIVE_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_NumPlayersNegative_ShowsErrorAndRepromptsUntilValid() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(NEGATIVE_NUM_PLAYERS).andReturn(2);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_NumPlayersBelowMin_ShowsErrorAndRepromptsUntilValid() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(ONE_PLAYER).andReturn(2);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_NumPlayersAboveMax_ShowsErrorAndRepromptsUntilValid() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(SIX_PLAYERS).andReturn(2);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_TwoPlayers_AllPlayersAreActive() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		GameState gameState = gc.gameState();
		List<Player> allPlayers = new ArrayList<>();
		allPlayers.add(gameState.getCurrentPlayer());
		allPlayers.addAll(gameState.getOtherActivePlayers());
		for (Player player : allPlayers) {
			assertTrue(player.isActive());
		}
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_FivePlayers_AllPlayersAreActive() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(FIVE_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		GameState gameState = gc.gameState();
		List<Player> allPlayers = new ArrayList<>();
		allPlayers.add(gameState.getCurrentPlayer());
		allPlayers.addAll(gameState.getOtherActivePlayers());
		for (Player player : allPlayers) {
			assertTrue(player.isActive());
		}
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_TwoPlayers_FirstPlayerIsCurrentPlayer() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(2);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input);
		gc.startGame();

		assertEquals("p1", gc.gameState().getCurrentPlayer().getId());
		EasyMock.verify(display, input);
	}
}