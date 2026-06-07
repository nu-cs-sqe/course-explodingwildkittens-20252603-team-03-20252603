package ui;

import domain.action.NoAction;
import domain.action.ShuffleAction;
import domain.action.SkipAction;
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
public class NopeIntegrationTest {
	private static final int TWO_PLAYERS = 2;
	private static final int TWO_TURNS = 2;
	private static final int ONE_CARD = 1;
	private static final int ONE_PLAYER = 1;
	private static final int THREE_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}

	@Test
	void nope_ZeroNopes_CardExecuted(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		drawPileCards.add(skipCard);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(skipCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(skipCard)).once();
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(false);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int sizeAfter = firstPlayer.getHand().size();
		int sizeOfSecondPlayerHand = secondPlayer.getHand().size();

		assertEquals(sizeBefore-1, sizeAfter);
		assertEquals(sizeBefore, sizeOfSecondPlayerHand);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void nope_OneNope_CardNotExecuted(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card nopeCard = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		drawPileCards.add(skipCard);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(skipCard);
		playerHandCards.add(nopeCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(skipCard)).once();
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(true).once();

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int sizeAfter = firstPlayer.getHand().size();
		int sizeOfSecondPlayerHand = secondPlayer.getHand().size();

		assertEquals(sizeBefore, sizeAfter);
		assertEquals(sizeBefore-ONE_CARD, sizeOfSecondPlayerHand);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void nope_TwoNopes_CardExecuted(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		drawPileCards.add(skipCard);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(skipCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(THREE_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(skipCard)).once();
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class)))
				.andReturn(true)
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
		int sizeOfSecondPlayerHand = secondPlayer.getHand().size();

		assertEquals(sizeBefore-1, sizeAfter);
		assertEquals(sizeBefore-ONE_CARD, sizeOfSecondPlayerHand);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);

	}


	@Test
	void nope_ThreeNopes_CardNotExecuted(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		drawPileCards.add(skipCard);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(skipCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(FOUR_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(skipCard)).once();
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class)))
				.andReturn(true)
				.andReturn(true)
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
		int sizeOfSecondPlayerHand = secondPlayer.getHand().size();

		assertEquals(sizeBefore, sizeAfter);
		assertEquals(sizeBefore-ONE_CARD, sizeOfSecondPlayerHand);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void nope_ThreePlayers_OneLeftWithNope_OnlyOnePlayerPrompted(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card skipCard1 = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card skipCard2 = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card skipCard3 = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(skipCard1);
		drawPileCards.add(skipCard2);
		drawPileCards.add(skipCard3);
		playerHandCards.add(cattermelonCard);
		playerHandCards.add(skipCard1);
		playerHandCards.add(skipCard2);
		playerHandCards.add(skipCard3);

		EasyMock.expect(input.promptNumPlayers()).andReturn(FOUR_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().times(TWO_TURNS);
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS)
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(skipCard1))
				.andReturn(List.of(skipCard2));
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class)))
				.andReturn(true).times(TWO_PLAYERS)
				.andReturn(false).times(ONE_PLAYER)
				.andReturn(true).times(ONE_PLAYER)
				.andReturn(false).times(ONE_PLAYER);

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

		assertEquals(firstPlayerSizeBefore-1, firstPlayerSizeAfter);
		assertEquals(secondPlayerSizeBefore, secondPlayerSizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);
		assertNotEquals(firstPlayer, thirdPlayer);
		assertNotEquals(secondPlayer, thirdPlayer);

		EasyMock.verify(display, input);


	}


}
