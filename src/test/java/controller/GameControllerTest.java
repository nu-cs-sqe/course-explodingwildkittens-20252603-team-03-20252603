package controller;

import domain.enums.CardType;
import domain.enums.PlayerChoice;
import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;
import domain.model.TurnState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

	private static final int THREE_TURNS = 3;
	private static final int FOUR_TURNS = 4;
	private static final int FIVE_TURNS = 5;
	private static final int SIX_TURNS = 6;

	private GameController createGameController(GameState gameState) {
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		return new GameController(mockDisplay, mockInput, gameState);
	}

	private GameState createMockGameState(TurnState turnState) {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.turnState()).andReturn(turnState).anyTimes();
		return mockGameState;
	}

	@Test
	void hasToPlayATurn_isAttackingIsTrue_ReturnsFalse() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(true);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		assertFalse(controller.hasToPlayATurn());

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsZero_ReturnsFalse() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(false);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(0);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		assertFalse(controller.hasToPlayATurn());

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsOne_ReturnsTrue() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(false);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(1);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		assertTrue(controller.hasToPlayATurn());

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsGreaterThanOne_ReturnsTrue() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(false);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(2);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		assertTrue(controller.hasToPlayATurn());

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsIntMax_ReturnsTrue() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(false);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(Integer.MAX_VALUE);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		assertTrue(controller.hasToPlayATurn());

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void handleDrawingCards_skipDrawIsTrue_SkipsDraw() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		EasyMock.expect(mockTurnState.shouldSkipDraw()).andReturn(true);

		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		controller.handleDrawingCards();

		EasyMock.verify(mockGameState, mockTurnState);
	}

	@Test
	void handleTurnTaking_notAttacking_DecrementsAndReturnsOne() {
		TurnState turnState = new TurnState();
		// i am not attacking
		// i was also not attacked (i just have one turn to play)
		int currentTurns = 1;
		turnState.reset(currentTurns);

		GameState mockGameState = createMockGameState(turnState);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(1, nextPlayerTurns);
		assertEquals(0, turnState.turnsRemaining());
		EasyMock.verify(mockGameState);
	}

	@Test
	void handleTurnTaking_notAttacking_turnsRemainingIsThree_DecrementsAndReturnsOne() {
		TurnState turnState = new TurnState();
		// have been attacked by someone who was attacked (i have to play 4 turns);
		// and I just played one turn (3 turns left)
		// i am not attacking the next guy
		int currentTurns = THREE_TURNS;
		turnState.reset(currentTurns);

		GameState mockGameState = createMockGameState(turnState);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(1, nextPlayerTurns);
		assertEquals(2, turnState.turnsRemaining());
		EasyMock.verify(mockGameState);
	}

	@Test
	void handleTurnTaking_attackingNotWasAttacked_DecrementsAndReturnsTwo() {
		TurnState turnState = new TurnState();
		// i am attacking the next guy;
		// i was not attacked, (i have one turn to play)
		int currentTurns = 1;
		turnState.reset(currentTurns);
		turnState.startAttack();

		GameState mockGameState = createMockGameState(turnState);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.wasAttacked()).andReturn(false);

		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(2, nextPlayerTurns);
		assertEquals(0, turnState.turnsRemaining());
		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void handleTurnTaking_attackingWasAttacked_turnsRemainingIsOne_DecrementsAndReturnsThree() {
		TurnState turnState = new TurnState();
		// i am attacking the next guy;
		// i was attacked, and i already played one turn (one turn left)
		int currentTurns = 1;
		turnState.reset(currentTurns);
		turnState.startAttack();

		GameState mockGameState = createMockGameState(turnState);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.wasAttacked()).andReturn(true);

		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(THREE_TURNS, nextPlayerTurns);
		assertEquals(0, turnState.turnsRemaining());
		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void handleTurnTaking_attackingWasAttacked_turnsRemainingIsTwo_DecrementsAndReturnsFour() {
		TurnState turnState = new TurnState();
		//i am attacking the next guy
		// i was attacked (so I have to play 2 turns and this is my first turn)
		int currentTurns = 2;
		turnState.reset(currentTurns);
		turnState.startAttack();

		GameState mockGameState = createMockGameState(turnState);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.wasAttacked()).andReturn(true);

		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(FOUR_TURNS, nextPlayerTurns);
		assertEquals(1, turnState.turnsRemaining());
		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void handleTurnTaking_attackingWasAttacked_turnsRemainingIsThree_DecrementsAndReturnsFive() {
		TurnState turnState = new TurnState();
		// i am attacking the next guy
		// I was attacked by someone who was also attacked (i have to play 4 turns)
		// I just played one turn; 3 turns left
		int currentTurns = THREE_TURNS;
		turnState.reset(currentTurns);
		turnState.startAttack();

		GameState mockGameState = createMockGameState(turnState);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.wasAttacked()).andReturn(true);

		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(FIVE_TURNS, nextPlayerTurns);
		assertEquals(2, turnState.turnsRemaining());
		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void handleTurnTaking_attackingWasAttacked_turnsRemainingIsFour_DecrementsAndReturnsSix() {
		TurnState turnState = new TurnState();
		// I am attacking the next guy
		// I was attacked by someone who was also attacked (I have to play 4 turns)
		// I have not played any yet (I have 4 turns to go)
		int currentTurns = FOUR_TURNS;
		turnState.reset(currentTurns);
		turnState.startAttack();

		GameState mockGameState = createMockGameState(turnState);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.wasAttacked()).andReturn(true);

		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		int nextPlayerTurns = controller.handleTurnTaking();

		assertEquals(SIX_TURNS, nextPlayerTurns);
		assertEquals(THREE_TURNS, turnState.turnsRemaining());
		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void resetGameState_turnsForNextPlayerIsOne_ResetsTurnState() {
		TurnState turnState = new TurnState();
		turnState.startAttack();
		turnState.enableSkipDraw();

		GameState mockGameState = createMockGameState(turnState);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		controller.resetGameState(1);

		assertEquals(1, turnState.turnsRemaining());
		assertFalse(turnState.isAttacking());
		assertFalse(turnState.shouldSkipDraw());
		EasyMock.verify(mockGameState);
	}

	@Test
	void resetGameState_turnsForNextPlayerIsTwo_ResetsTurnState() {
		TurnState turnState = new TurnState();
		turnState.startAttack();
		turnState.enableSkipDraw();

		GameState mockGameState = createMockGameState(turnState);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		controller.resetGameState(2);

		assertEquals(2, turnState.turnsRemaining());
		assertFalse(turnState.isAttacking());
		assertFalse(turnState.shouldSkipDraw());
		EasyMock.verify(mockGameState);
	}

	@Test
	void resetGameState_turnsForNextPlayerIsThree_ResetsTurnState() {
		TurnState turnState = new TurnState();
		turnState.startAttack();
		turnState.enableSkipDraw();

		GameState mockGameState = createMockGameState(turnState);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		controller.resetGameState(THREE_TURNS);

		assertEquals(THREE_TURNS, turnState.turnsRemaining());
		assertFalse(turnState.isAttacking());
		assertFalse(turnState.shouldSkipDraw());
		EasyMock.verify(mockGameState);
	}

	@Test
	void resetCurrentPlayerWasAttacked_wasAttackedIsTrue_ResetToFalse() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		GameState mockGameState = createMockGameState(mockTurnState);

		Player player = new Player("fakeId", "fakeName");
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(player);

		EasyMock.replay(mockGameState, mockTurnState);

		player.setWasAttacked();

		GameController controller = createGameController(mockGameState);
		controller.resetCurrentPlayerWasAttacked();

		assertFalse(player.wasAttacked());
		EasyMock.verify(mockGameState);
	}

	@Test
	void resetCurrentPlayerWasAttacked_wasAttackedIsFalse_ResetToFalse() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		GameState mockGameState = createMockGameState(mockTurnState);

		Player player = new Player("fakeId", "fakeName");
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(player);

		EasyMock.replay(mockGameState, mockTurnState);

		GameController controller = createGameController(mockGameState);
		controller.resetCurrentPlayerWasAttacked();

		assertFalse(player.wasAttacked());
		EasyMock.verify(mockGameState);
	}

	@Test
	void advanceGameToNextPlayer_CallsAdvancePlayerOnGameState() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		mockGameState.advancePlayer();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		controller.advanceGameToNextPlayer();

		EasyMock.verify(mockGameState);
	}

	@Test
	void readyToPlayATurn_gameStateNotActive_ReturnsFalse() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.isActive()).andReturn(false);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);
		assertFalse(controller.readyToPlayATurn());

		EasyMock.verify(mockGameState);
	}

	@Test
	void readyToPlayATurn_gameStateActiveCurrentPlayerNotActive_ReturnsFalse() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.isActive()).andReturn(false);
		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		assertFalse(controller.readyToPlayATurn());

		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void readyToPlayATurn_gameStateActiveCurrentPlayerActive_ReturnsTrue() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.expect(mockPlayer.isActive()).andReturn(true);
		EasyMock.replay(mockGameState, mockPlayer);

		GameController controller = createGameController(mockGameState);
		assertTrue(controller.readyToPlayATurn());

		EasyMock.verify(mockGameState, mockPlayer);
	}

	@Test
	void playATurn_notReadyToPlayATurn_throwsException() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.isActive()).andReturn(false);
		EasyMock.replay(mockGameState);

		GameController controller = createGameController(mockGameState);

		assertThrows(IllegalStateException.class, controller::playATurn);

		EasyMock.verify(mockGameState);
	}

	@Test
	void playATurn_ReadyToPlayATurn_DoesNotHaveToPlayATurn_NoLoopRun() {
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		Player mockPlayer = EasyMock.createMock(Player.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);

		EasyMock.expect(mockGameState.isActive()).andReturn(true);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(mockPlayer).anyTimes();
		EasyMock.expect(mockGameState.turnState()).andReturn(mockTurnState).anyTimes();
		EasyMock.expect(mockPlayer.isActive()).andReturn(true);

		EasyMock.expect(mockTurnState.isAttacking()).andReturn(true);

		mockDisplay.showCurrentPlayer(mockPlayer);

		mockPlayer.resetWasAttacked();

		mockTurnState.reset(1);

		mockGameState.advancePlayer();

		EasyMock.replay(mockGameState, mockTurnState, mockPlayer, mockDisplay, mockInput);

		GameController controller = new GameController(mockDisplay, mockInput, mockGameState);
		controller.playATurn();

		EasyMock.verify(mockGameState, mockTurnState, mockPlayer, mockDisplay, mockInput);
	}

	private void setupPlayATurnBaseExpectations(TurnState turnState, Player player, GameState gameState) {
		EasyMock.expect(gameState.isActive()).andReturn(true);
		EasyMock.expect(gameState.getCurrentPlayer()).andReturn(player).anyTimes();
		EasyMock.expect(gameState.turnState()).andReturn(turnState).anyTimes();
		EasyMock.expect(player.isActive()).andReturn(true);
	}

	@Test
	void playATurn_ReadyToPlayATurn_HasToPlayATurn_PlayACard_OneLoopRun_PlaysCards(){
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		Player mockPlayer = EasyMock.createMock(Player.class);
		List<Card> mockCards = EasyMock.createMock(List.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		setupPlayATurnBaseExpectations(mockTurnState, mockPlayer, mockGameState);
		mockDisplay.showCurrentPlayer(mockPlayer);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(false).andReturn(false);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(1).andReturn(0);
		EasyMock.expect(mockInput.promptPlayerChoice()).andReturn(PlayerChoice.PLAY_CARD);
		EasyMock.expect(mockInput.promptCardSelection(mockPlayer)).andReturn(mockCards);
		mockPlayer.resetWasAttacked();
		mockTurnState.reset(1);
		mockGameState.advancePlayer();
		EasyMock.replay(mockGameState, mockTurnState, mockPlayer, mockDisplay, mockInput);
		new GameController(mockDisplay, mockInput, mockGameState).playATurn();
		EasyMock.verify(mockGameState, mockTurnState, mockPlayer, mockDisplay, mockInput);
	}

	@Test
	void handleDrawingCards_skipDrawIsFalse_DrawsCard() {
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		Card mockCard = EasyMock.createMock(Card.class);
		GameState mockGameState = createMockGameState(mockTurnState);
		EasyMock.expect(mockTurnState.shouldSkipDraw()).andReturn(false);
		EasyMock.expect(mockGameState.drawFromDeck()).andReturn(mockCard);
		EasyMock.expect(mockCard.isType(CardType.EXPLODING_KITTEN)).andReturn(false);
		mockGameState.addCardToCurrentPlayer(mockCard);
		EasyMock.replay(mockGameState, mockTurnState, mockCard);
		createGameController(mockGameState).handleDrawingCards();
		EasyMock.verify(mockGameState, mockTurnState, mockCard);
	}

	@Test
	void drawCard_drawnCardIsNotExplodingKitten_AddsCardToPlayerHand() {
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		Card mockCard = EasyMock.createMock(Card.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.drawFromDeck()).andReturn(mockCard);
		EasyMock.expect(mockCard.isType(CardType.EXPLODING_KITTEN)).andReturn(false);
		mockGameState.addCardToCurrentPlayer(mockCard);
		EasyMock.replay(mockGameState, mockCard, mockInput, mockDisplay);
		new GameController(mockDisplay, mockInput, mockGameState).drawCard();
		EasyMock.verify(mockGameState, mockCard, mockInput, mockDisplay);
	}

	@Test
	void drawCard_drawnCardIsExplodingKitten_playerHasDefuse_ExecutesDefuseAction() {
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		Card mockCard = EasyMock.createMock(Card.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.drawFromDeck()).andReturn(mockCard);
		EasyMock.expect(mockCard.isType(CardType.EXPLODING_KITTEN)).andReturn(true);
		EasyMock.expect(mockGameState.turnState()).andReturn(mockTurnState);
		mockTurnState.setPendingAction(mockCard);
		EasyMock.expect(mockGameState.currentPlayerHasCard(CardType.DEFUSE)).andReturn(true);
		EasyMock.expect(mockGameState.getDeckSize()).andReturn(1);
		EasyMock.expect(mockInput.promptInsertPosition(1)).andReturn(0);
		mockGameState.insertPendingCardAt(0);
		EasyMock.replay(mockGameState, mockTurnState, mockCard, mockInput, mockDisplay);
		new GameController(mockDisplay, mockInput, mockGameState).drawCard();
		EasyMock.verify(mockGameState, mockTurnState, mockCard, mockInput, mockDisplay);
	}

	@Test
	void drawCard_drawnCardIsExplodingKitten_playerHasNoDefuse_EliminatesPlayer() {
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		Card mockCard = EasyMock.createMock(Card.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		EasyMock.expect(mockGameState.drawFromDeck()).andReturn(mockCard);
		EasyMock.expect(mockCard.isType(CardType.EXPLODING_KITTEN)).andReturn(true);
		EasyMock.expect(mockGameState.turnState()).andReturn(mockTurnState);
		mockTurnState.setPendingAction(mockCard);
		EasyMock.expect(mockGameState.currentPlayerHasCard(CardType.DEFUSE)).andReturn(false);
		mockGameState.eliminateCurrentPlayer();
		EasyMock.replay(mockGameState, mockTurnState, mockCard, mockInput, mockDisplay);
		new GameController(mockDisplay, mockInput, mockGameState).drawCard();
		EasyMock.verify(mockGameState, mockTurnState, mockCard, mockInput, mockDisplay);
	}

	@Test
	void playATurn_ReadyToPlayATurn_HasToPlayATurn_DonePlaying_OneLoopRun_DrawsCardsAndTurnTaking(){
		IGameDisplay mockDisplay = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		Player mockPlayer = EasyMock.createMock(Player.class);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		setupPlayATurnBaseExpectations(mockTurnState, mockPlayer, mockGameState);
		mockDisplay.showCurrentPlayer(mockPlayer);
		EasyMock.expect(mockTurnState.isAttacking()).andReturn(false).times(THREE_TURNS);
		EasyMock.expect(mockTurnState.turnsRemaining()).andReturn(1).andReturn(0);
		EasyMock.expect(mockInput.promptPlayerChoice()).andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(mockTurnState.shouldSkipDraw()).andReturn(true);
		mockTurnState.decrementTurns();
		mockPlayer.resetWasAttacked();
		mockTurnState.reset(1);
		mockGameState.advancePlayer();
		EasyMock.replay(mockGameState, mockTurnState, mockPlayer, mockDisplay, mockInput);
		new GameController(mockDisplay, mockInput, mockGameState).playATurn();
		EasyMock.verify(mockGameState, mockTurnState, mockPlayer, mockDisplay, mockInput);
	}
}