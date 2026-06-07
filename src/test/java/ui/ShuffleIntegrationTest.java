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
public class ShuffleIntegrationTest {

	private static final int TWO_PLAYERS = 2;
	private static final int RANDOM_SEED = 42;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}


	@Test
	void shuffle_IsNoped_DeckNotShuffled(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card beardCard = new Card(CardType.CAT_CARD, CardName.BEARD_CAT, new NoAction());
		Card hairyPotatoCard = new Card(CardType.CAT_CARD, CardName.HAIRY_POTATO_CAT, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		drawPileCards.add(skipCard);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(beardCard);
		drawPileCards.add(hairyPotatoCard);
		playerHandCards.add(shuffleCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(shuffleCard)).once();
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(true);
		display.showMessage(EasyMock.anyString());
		EasyMock.expectLastCall().once();

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards, new Random(RANDOM_SEED));
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		List<Card> deckBefore = new ArrayList<>(gc.gameState().peekTopOfDeck(gc.gameState().getDeckSize()));
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int sizeAfter = firstPlayer.getHand().size();
		List<Card> deckAfter = new ArrayList<>(gc.gameState().peekTopOfDeck(gc.gameState().getDeckSize()));

		assertEquals(sizeBefore, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);
		assertEquals(deckBefore.subList(1, deckBefore.size()), deckAfter);

		EasyMock.verify(display, input);
	}

	@Test
	void shuffle_IsNotNoped_DeckIsShuffled(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card beardCard = new Card(CardType.CAT_CARD, CardName.BEARD_CAT, new NoAction());
		Card hairyPotatoCard = new Card(CardType.CAT_CARD, CardName.HAIRY_POTATO_CAT, new NoAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		drawPileCards.add(skipCard);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(beardCard);
		drawPileCards.add(hairyPotatoCard);
		playerHandCards.add(shuffleCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(shuffleCard)).once();
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(false);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards, new Random(RANDOM_SEED));
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
