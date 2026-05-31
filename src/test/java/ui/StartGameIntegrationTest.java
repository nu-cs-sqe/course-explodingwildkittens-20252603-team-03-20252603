package ui;

import domain.factory.ComboValidator;
import domain.factory.PlayerInteractionHelper;
import domain.input.IPlayerInput;
import domain.enums.CardType;
import domain.model.GameState;
import domain.model.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartGameIntegrationTest {

	private static final int NEGATIVE_NUM_PLAYERS = -1;
	private static final int INITIAL_HAND_SIZE = 8;
	private static final int DEFUSE_CARDS_PER_PLAYER = 1;
	private static final int TWO_PLAYERS = 2;
	private static final int ONE_EXPLODING_KITTEN = 1;
	private static final int ONE_PLAYER = 1;
	private static final int THREE_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;
	private static final int FIVE_PLAYERS = 5;
	private static final int SIX_PLAYERS = 6;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}

	@Test
	void startGame_TwoPlayers_GameStateIsActive() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
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

		GameController gc = new GameController(display, input, realComboValidator(input));
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

		GameController gc = new GameController(display, input, realComboValidator(input));
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

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_NumPlayersNegative_ShowsErrorAndRepromptsUntilValid() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(NEGATIVE_NUM_PLAYERS).andReturn(TWO_PLAYERS);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_NumPlayersBelowMin_ShowsErrorAndRepromptsUntilValid() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(ONE_PLAYER).andReturn(TWO_PLAYERS);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_NumPlayersAboveMax_ShowsErrorAndRepromptsUntilValid() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(SIX_PLAYERS).andReturn(TWO_PLAYERS);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		assertTrue(gc.isGameActive());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_TwoPlayers_AllPlayersAreActive() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
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

		GameController gc = new GameController(display, input, realComboValidator(input));
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
		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		assertEquals("p1", gc.gameState().getCurrentPlayer().getId());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_FivePlayers_FirstPlayerIsCurrentPlayer() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(FIVE_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		assertEquals("p1", gc.gameState().getCurrentPlayer().getId());
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_TwoPlayers_EachPlayerHasEightCards() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		GameState gameState = gc.gameState();
		List<Player> allPlayers = new ArrayList<>();
		allPlayers.add(gameState.getCurrentPlayer());
		allPlayers.addAll(gameState.getOtherActivePlayers());
		for (Player player : allPlayers) {
			assertEquals(INITIAL_HAND_SIZE, player.getHand().size());
		}
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_TwoPlayers_EachPlayerHasExactlyOneDefuse() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		GameState gameState = gc.gameState();
		List<Player> allPlayers = new ArrayList<>();
		allPlayers.add(gameState.getCurrentPlayer());
		allPlayers.addAll(gameState.getOtherActivePlayers());
		for (Player player : allPlayers) {
			long defuseCount = player.getHand().stream()
					.filter(card -> card.isType(CardType.DEFUSE))
					.count();
			assertEquals(DEFUSE_CARDS_PER_PLAYER, defuseCount);
		}
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_FivePlayers_EachPlayerHasExactlyOneDefuse() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(FIVE_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		GameState gameState = gc.gameState();
		List<Player> allPlayers = new ArrayList<>();
		allPlayers.add(gameState.getCurrentPlayer());
		allPlayers.addAll(gameState.getOtherActivePlayers());
		for (Player player : allPlayers) {
			long defuseCount = player.getHand().stream()
					.filter(card -> card.isType(CardType.DEFUSE))
					.count();
			assertEquals(DEFUSE_CARDS_PER_PLAYER, defuseCount);
		}
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_FivePlayers_EachPlayerHasEightCards() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(FIVE_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		GameState gameState = gc.gameState();
		List<Player> allPlayers = new ArrayList<>();
		allPlayers.add(gameState.getCurrentPlayer());
		allPlayers.addAll(gameState.getOtherActivePlayers());
		for (Player player : allPlayers) {
			assertEquals(INITIAL_HAND_SIZE, player.getHand().size());
		}
		EasyMock.verify(display, input);
	}

	@Test
	void startGame_TwoPlayers_DeckContainsOneExplodingKitten() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();

		GameState gameState = gc.gameState();
		long explodingKittenCount = gameState.peekTopOfDeck(gameState.getDeckSize()).stream()
				.filter(card -> card.isType(CardType.EXPLODING_KITTEN))
				.count();
		assertEquals(ONE_EXPLODING_KITTEN, explodingKittenCount);
		EasyMock.verify(display, input);
	}
}