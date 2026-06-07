package ui;

import domain.action.AttackAction;
import domain.action.NoAction;
import domain.action.ShuffleAction;
import domain.enums.CardName;
import domain.enums.CardType;
import domain.enums.PlayerChoice;
import domain.factory.ComboValidator;
import domain.factory.PlayerInteractionHelper;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.Deck;
import domain.model.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings("checkstyle:MethodLength")
public class AttackIntegrationTest {
	private static final int TWO_PLAYERS = 2;
	private static final int THREE_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;
	private static final int ONE_TURN = 1;
	private static final int ONE_PLAYER = 1;
	private static final int TWO_TURNS = 2;
	private static final int THREE_TURNS = 3;
	private static final int FOUR_TURNS = 4;
	private static final int SIX_TURNS = 6;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}

	@Test
	void attack_NotInitiallyAttacked_NotNoped_NextPlayerAttacked(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card attackCard = new Card(CardType.ATTACK, CardName.ATTACK, new AttackAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard4 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		playerHandCards.add(nopeCard4);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(attackCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(attackCard)).once();
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class)))
				.andReturn(false);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int sizeAfter = firstPlayer.getHand().size();
		int turnsForNextPlayer = gc.getTurnsRemaining();

		assertEquals(sizeBefore-1, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);
		assertEquals(TWO_TURNS, turnsForNextPlayer);

		EasyMock.verify(display, input);
	}


	@Test
	void attack_NotInitiallyAttacked_Noped_NextPlayerNotAttacked(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card attackCard = new Card(CardType.ATTACK, CardName.ATTACK, new AttackAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard4 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		playerHandCards.add(nopeCard4);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(attackCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(attackCard)).once();
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class)))
				.andReturn(true);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int sizeAfter = firstPlayer.getHand().size();
		int turnsForNextPlayer = gc.getTurnsRemaining();

		assertEquals(sizeBefore, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);
		assertEquals(ONE_TURN, turnsForNextPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void attack_InitiallyAttacked_FirstTurn_NotNoped_NextPlayerAttacked(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card attackCard = new Card(CardType.ATTACK, CardName.ATTACK, new AttackAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard4 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		playerHandCards.add(nopeCard4);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(attackCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(THREE_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().times(TWO_TURNS);
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.PLAY_CARD);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(attackCard)).times(TWO_TURNS);
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class)))
				.andReturn(false).anyTimes();
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int firstPlayerSizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int firstPlayerSizeAfter = firstPlayer.getHand().size();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int secondPlayerSizeBefore = secondPlayer.getHand().size();
		gc.playATurn();
		int secondPlayerSizeAfter = secondPlayer.getHand().size();
		Player thirdPlayer = gc.gameState().getCurrentPlayer();

		int turnsForLastPlayer = gc.getTurnsRemaining();

		assertEquals(firstPlayerSizeBefore-1, firstPlayerSizeAfter);
		assertEquals(secondPlayerSizeBefore-1, secondPlayerSizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);
		assertNotEquals(firstPlayer, thirdPlayer);
		assertNotEquals(secondPlayer, thirdPlayer);
		assertEquals(FOUR_TURNS, turnsForLastPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void attack_InitiallyAttacked_FirstTurn_Noped_NextPlayerNotAttacked(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard1 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard2 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard3 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card attackCard = new Card(CardType.ATTACK, CardName.ATTACK, new AttackAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard4 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		playerHandCards.add(nopeCard4);

		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);
		playerHandCards.add(attackCard);
		playerHandCards.add(cattermelonCard);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);

		EasyMock.expect(input.promptNumPlayers()).andReturn(THREE_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().times(TWO_TURNS);
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS)
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(attackCard))
				.andReturn(List.of(attackCard))
				.andReturn(List.of(shuffleCard1));
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class)))
				.andReturn(false).times(TWO_PLAYERS)
				.andReturn(false).times(ONE_PLAYER)
				.andReturn(true).times(ONE_PLAYER)
				.andReturn(false).times(TWO_PLAYERS);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int firstPlayerSizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int firstPlayerSizeAfter = firstPlayer.getHand().size();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int secondPlayerSizeBefore = secondPlayer.getHand().size();
		gc.playATurn();
		int secondPlayerSizeAfter = secondPlayer.getHand().size();
		Player thirdPlayer = gc.gameState().getCurrentPlayer();

		int turnsForLastPlayer = gc.getTurnsRemaining();

		assertEquals(firstPlayerSizeBefore-1, firstPlayerSizeAfter);
		assertEquals(secondPlayerSizeBefore, secondPlayerSizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);
		assertNotEquals(firstPlayer, thirdPlayer);
		assertNotEquals(secondPlayer, thirdPlayer);
		assertEquals(ONE_TURN, turnsForLastPlayer);

		EasyMock.verify(display, input);
	}


	@Test
	void attack_InitiallyAttacked_SecondTurn_NotNoped_NextPlayerAttacked(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard1 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard2 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard3 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card attackCard = new Card(CardType.ATTACK, CardName.ATTACK, new AttackAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard4 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		playerHandCards.add(nopeCard4);

		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);
		playerHandCards.add(attackCard);
		playerHandCards.add(cattermelonCard);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);

		EasyMock.expect(input.promptNumPlayers()).andReturn(THREE_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().times(TWO_TURNS);
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS)
				.andReturn(PlayerChoice.PLAY_CARD);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(attackCard))
				.andReturn(List.of(shuffleCard1))
				.andReturn(List.of(attackCard));
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class)))
				.andReturn(false).times(TWO_PLAYERS)
				.andReturn(false).times(TWO_PLAYERS)
				.andReturn(false).times(TWO_PLAYERS);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int firstPlayerSizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int firstPlayerSizeAfter = firstPlayer.getHand().size();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int secondPlayerSizeBefore = secondPlayer.getHand().size();
		gc.playATurn();
		int secondPlayerSizeAfter = secondPlayer.getHand().size();
		Player thirdPlayer = gc.gameState().getCurrentPlayer();

		int turnsForLastPlayer = gc.getTurnsRemaining();

		assertEquals(firstPlayerSizeBefore-1, firstPlayerSizeAfter);
		assertEquals(secondPlayerSizeBefore-1, secondPlayerSizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);
		assertNotEquals(firstPlayer, thirdPlayer);
		assertNotEquals(secondPlayer, thirdPlayer);
		assertEquals(THREE_TURNS, turnsForLastPlayer);

		EasyMock.verify(display, input);
	}


	@Test
	void attack_InitiallyAttacked_SecondTurn_Noped_NextPlayerNotAttacked() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard1 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard2 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard3 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card attackCard = new Card(CardType.ATTACK, CardName.ATTACK, new AttackAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard4 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		playerHandCards.add(nopeCard4);

		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);
		playerHandCards.add(attackCard);
		playerHandCards.add(cattermelonCard);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);

		EasyMock.expect(input.promptNumPlayers()).andReturn(THREE_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().times(TWO_TURNS);
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS)
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(attackCard))
				.andReturn(List.of(shuffleCard1))
				.andReturn(List.of(attackCard));
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class)))
				.andReturn(false).times(TWO_PLAYERS)
				.andReturn(false).times(TWO_PLAYERS)
				.andReturn(false).times(ONE_PLAYER)
				.andReturn(true).times(ONE_PLAYER);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int firstPlayerSizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int firstPlayerSizeAfter = firstPlayer.getHand().size();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int secondPlayerSizeBefore = secondPlayer.getHand().size();
		gc.playATurn();
		int secondPlayerSizeAfter = secondPlayer.getHand().size();
		Player thirdPlayer = gc.gameState().getCurrentPlayer();

		int turnsForLastPlayer = gc.getTurnsRemaining();

		assertEquals(firstPlayerSizeBefore - 1, firstPlayerSizeAfter);
		assertEquals(secondPlayerSizeBefore , secondPlayerSizeAfter);
		assertNotEquals(firstPlayer, secondPlayer);
		assertNotEquals(firstPlayer, thirdPlayer);
		assertNotEquals(secondPlayer, thirdPlayer);
		assertEquals(ONE_TURN, turnsForLastPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void attack_ThreeStackedAttacks_FirstTurn_NotNoped_NextPlayerAttacked() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard1 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard2 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard3 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard4 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard5 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard6 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card attackCard = new Card(CardType.ATTACK, CardName.ATTACK, new AttackAction());
		Card cattermelonCard1 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card cattermelonCard2 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card cattermelonCard3 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard4 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		playerHandCards.add(nopeCard4);

		drawPileCards.add(cattermelonCard1);
		drawPileCards.add(cattermelonCard2);
		drawPileCards.add(cattermelonCard3);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);
		drawPileCards.add(shuffleCard4);
		drawPileCards.add(shuffleCard5);
		drawPileCards.add(shuffleCard6);
		playerHandCards.add(attackCard);
		playerHandCards.add(cattermelonCard1);
		playerHandCards.add(cattermelonCard2);
		playerHandCards.add(cattermelonCard3);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);
		playerHandCards.add(shuffleCard4);
		playerHandCards.add(shuffleCard5);
		playerHandCards.add(shuffleCard6);

		EasyMock.expect(input.promptNumPlayers()).andReturn(FOUR_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().times(THREE_TURNS);
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.PLAY_CARD);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(attackCard))
				.andReturn(List.of(attackCard))
				.andReturn(List.of(attackCard));
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class)))
				.andReturn(false).times(THREE_PLAYERS)
				.andReturn(false).times(THREE_PLAYERS)
				.andReturn(false).times(THREE_PLAYERS);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().times(TWO_TURNS);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int firstPlayerSizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int firstPlayerSizeAfter = firstPlayer.getHand().size();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int secondPlayerSizeBefore = secondPlayer.getHand().size();
		gc.playATurn();
		int secondPlayerSizeAfter = secondPlayer.getHand().size();
		Player thirdPlayer = gc.gameState().getCurrentPlayer();
		int thirdPlayerSizeBefore = thirdPlayer.getHand().size();
		gc.playATurn();
		int thirdPlayerSizeAfter = thirdPlayer.getHand().size();
		Player fourthPlayer = gc.gameState().getCurrentPlayer();

		int turnsForLastPlayer = gc.getTurnsRemaining();

		assertEquals(firstPlayerSizeBefore - 1, firstPlayerSizeAfter);
		assertEquals(secondPlayerSizeBefore -1 , secondPlayerSizeAfter);
		assertEquals(thirdPlayerSizeBefore -1 , thirdPlayerSizeAfter);
		assertNotEquals(firstPlayer, secondPlayer);
		assertNotEquals(firstPlayer, thirdPlayer);
		assertNotEquals(secondPlayer, thirdPlayer);
		assertNotEquals(firstPlayer, fourthPlayer);
		assertNotEquals(secondPlayer, fourthPlayer);
		assertNotEquals(thirdPlayer, fourthPlayer);
		assertEquals(SIX_TURNS, turnsForLastPlayer);

		EasyMock.verify(display, input);
	}

}
