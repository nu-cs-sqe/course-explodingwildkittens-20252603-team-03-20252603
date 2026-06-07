package ui;

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
public class TwoCardIntegrationTest {

	private static final int TWO_PLAYERS = 2;
	private static final int ONE_CARD = 1;
	private static final int TWO_CARDS = 2;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}

	@Test
	void twoCard_ValidCombo_NotNoped_CardRobbed(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		Player player1 = new Player("P1", "Player1");
		Player player2 = new Player("P2", "Player2");
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		List<Player>  players = new ArrayList<>();
		players.add(player1);
		players.add(player2);

		Card cattermelon1 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card cattermelon2 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card shuffleCard1 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard2 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard3 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);

		playerHandCards.add(cattermelon1);
		playerHandCards.add(cattermelon2);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);

		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(cattermelon1, cattermelon2)).once();

		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(false);

		EasyMock.expect(input.promptTargetSelection(EasyMock.isA(List.class))).andReturn(player2);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards,  players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeOfPlayerTwoHandBefore = player2.getHand().size();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int sizeAfter = firstPlayer.getHand().size();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int sizeOfPlayerTwoHandAfter = secondPlayer.getHand().size();

		assertEquals(sizeBefore, sizeAfter);
		assertEquals(sizeOfPlayerTwoHandBefore-1 , sizeOfPlayerTwoHandAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}


	@Test
	void twoCard_ValidCombo_Noped_CardNotRobbed(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		Player player1 = new Player("P1", "Player1");
		Player player2 = new Player("P2", "Player2");
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		List<Player>  players = new ArrayList<>();
		players.add(player1);
		players.add(player2);

		Card cattermelon1 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card cattermelon2 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card shuffleCard1 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard2 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard3 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);

		playerHandCards.add(cattermelon1);
		playerHandCards.add(cattermelon2);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);


		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(cattermelon1, cattermelon2)).once();


		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(true);


		EasyMock.replay(display, input);


		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards,  players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int sizeAfter = firstPlayer.getHand().size();
		int expectedSize = sizeBefore - TWO_CARDS + ONE_CARD;
		Player secondPlayer = gc.gameState().getCurrentPlayer();


		assertEquals(expectedSize, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);


		EasyMock.verify(display, input);
	}

	@Test
	void twoCard_InvalidCombo_GracefulError(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		Player player1 = new Player("P1", "Player1");
		Player player2 = new Player("P2", "Player2");
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		List<Player>  players = new ArrayList<>();
		players.add(player1);
		players.add(player2);


		Card cattermelon1 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		Card beardCat = new Card(CardType.CAT_CARD, CardName.BEARD_CAT, new NoAction());
		Card shuffleCard1 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard2 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard3 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);

		playerHandCards.add(cattermelon1);
		playerHandCards.add(beardCat);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);

		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(cattermelon1, beardCat)).once();
		display.showMessage(ViewMessages.format("error.invalid.card"));
		EasyMock.expectLastCall().once();

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards,  players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int sizeAfter = firstPlayer.getHand().size();
		int expectedSize = sizeBefore  + ONE_CARD;
		Player secondPlayer = gc.gameState().getCurrentPlayer();

		assertEquals(expectedSize, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}
}
