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
public class SkipIntegrationTest {

	private static final int TWO_PLAYERS = 2;
	private static final int THREE_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}

	@Test
	void skip_IsNotNoped_SkipsDraw(){
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

		assertEquals(sizeBefore-1, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}


	@Test
	void skip_IsNopedOnce_DoesNotSkip(){
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
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(true);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int sizeAfter = firstPlayer.getHand().size();

		assertEquals(sizeBefore, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void skip_IsNopedTwice_SkipsDraw(){
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

		assertEquals(sizeBefore-1, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void skip_IsNopedThrice_DoesNotSkip(){
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

		assertEquals(sizeBefore, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}
}
