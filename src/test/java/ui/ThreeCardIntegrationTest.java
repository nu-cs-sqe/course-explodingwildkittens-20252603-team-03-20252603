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
public class ThreeCardIntegrationTest {
	private static final int TWO_PLAYERS = 2;
	private static final int ONE_CARD = 1;
	private static final int THREE_CARDS = 3;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}

	@Test
	void threeCard_ValidCombo_NotNoped_CardInHand_CardRobbed(){
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
		Card cattermelon3 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
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
		playerHandCards.add(cattermelon3);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);

		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(cattermelon1, cattermelon2, cattermelon3)).once();

		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(false);

		EasyMock.expect(input.promptTargetSelection(EasyMock.isA(List.class))).andReturn(player2);
		EasyMock.expect(input.promptCardType()).andReturn(CardType.SHUFFLE).once();
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards,  players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int sizeAfter = firstPlayer.getHand().size();
		int expectedSize = sizeBefore - THREE_CARDS + ONE_CARD + ONE_CARD;
		Player secondPlayer = gc.gameState().getCurrentPlayer();

		assertEquals(expectedSize, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);

	}


	@Test
	void threeCard_ValidCombo_NotNoped_CardInNotHand_CardNotRobbed(){
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
		Card cattermelon3 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
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
		playerHandCards.add(cattermelon3);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);

		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(cattermelon1, cattermelon2, cattermelon3)).once();

		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(false);

		EasyMock.expect(input.promptTargetSelection(EasyMock.isA(List.class))).andReturn(player2);
		EasyMock.expect(input.promptCardType()).andReturn(CardType.SKIP).once();
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards,  players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int sizeAfter = firstPlayer.getHand().size();
		int expectedSize = sizeBefore - THREE_CARDS + ONE_CARD;
		Player secondPlayer = gc.gameState().getCurrentPlayer();

		assertEquals(expectedSize, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);

	}


	@Test
	void threeCard_ValidCombo_Noped_CardNotRobbed(){
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
		Card cattermelon3 = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
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
		playerHandCards.add(cattermelon3);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);

		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(cattermelon1, cattermelon2, cattermelon3)).once();

		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(true);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards,  players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int sizeAfter = firstPlayer.getHand().size();
		int expectedSize = sizeBefore - THREE_CARDS + ONE_CARD;
		Player secondPlayer = gc.gameState().getCurrentPlayer();

		assertEquals(expectedSize, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);

	}


	@Test
	void threeCard_InvalidCombo_GracefulError(){
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
		playerHandCards.add(cattermelon2);
		playerHandCards.add(beardCat);
		playerHandCards.add(shuffleCard1);
		playerHandCards.add(shuffleCard2);
		playerHandCards.add(shuffleCard3);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		drawPileCards.add(shuffleCard3);


		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(cattermelon1, beardCat, cattermelon2)).once();
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
