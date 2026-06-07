package ui;

import domain.action.FavorAction;
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
public class FavorIntegrationTest {
	private static final int TWO_PLAYERS = 2;
	private static final int THREE_PLAYERS = 3;
	private static final int ONE_CARD = 1;
	private static final int TWO_TURNS = 2;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}


	@Test
	void favor_NotNoped_HasCards_CardIsRobbed(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		PlayerInteractionHelper  playerInteractionHelper = new PlayerInteractionHelper(input, new Random());
		Player player1 = new Player("P1", "Player1");
		Player player2 = new Player("P2", "Player2");
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		List<Player>  players = new ArrayList<>();
		Card favorCard = new Card(CardType.FAVOR, CardName.FAVOR, new FavorAction(playerInteractionHelper));
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		drawPileCards.add(favorCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(favorCard);
		playerHandCards.add(shuffleCard);
		players.add(player1);
		players.add(player2);

		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(favorCard)).once()
				.andReturn(List.of(shuffleCard)).once();

		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(false);

		EasyMock.expect(input.promptTargetSelection(EasyMock.isA(List.class))).andReturn(player2);
		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards,  players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int sizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int sizeAfter = firstPlayer.getHand().size();

		assertEquals(sizeBefore+ONE_CARD, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void favor_NotNoped_NoCards_NothingHappens(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		PlayerInteractionHelper  playerInteractionHelper = new PlayerInteractionHelper(input, new Random());
		Player player1 = new Player("P1", "Player1");
		Player player2 = new Player("P2", "Player2");
		Player player3 = new Player("P3", "Player3");
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		List<Player>  players = new ArrayList<>();
		Card favorCard = new Card(CardType.FAVOR, CardName.FAVOR, new FavorAction(playerInteractionHelper));
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card shuffleCard1 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card shuffleCard2 = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		drawPileCards.add(favorCard);
		drawPileCards.add(skipCard);
		drawPileCards.add(shuffleCard1);
		drawPileCards.add(shuffleCard2);
		playerHandCards.add(favorCard);
		players.add(player1);
		players.add(player2);
		players.add(player3);

		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().times(TWO_TURNS);
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS)
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(favorCard)).once()
				.andReturn(List.of(favorCard)).once()
				.andReturn(List.of(favorCard)).once()
				.andReturn(List.of(favorCard)).once();

		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(false).times(TWO_PLAYERS);
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(false).times(TWO_PLAYERS);

		EasyMock.expect(input.promptTargetSelection(EasyMock.isA(List.class)))
				.andReturn(player3)
				.andReturn(player3);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards,  players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int firstPlayerSizeBefore = firstPlayer.getHand().size();
		gc.playATurn();
		int firstPlayerSizeAfter = firstPlayer.getHand().size();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int secondPlayerSizeBefore = secondPlayer.getHand().size();
		gc.playATurn();
		int secondPlayerSizeAfter = secondPlayer.getHand().size();


		assertNotEquals(firstPlayerSizeBefore, firstPlayerSizeAfter);
		assertEquals(secondPlayerSizeBefore, secondPlayerSizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void favor_Noped_CardNotRobbed(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		PlayerInteractionHelper  playerInteractionHelper = new PlayerInteractionHelper(input, new Random());
		Player player1 = new Player("P1", "Player1");
		Player player2 = new Player("P2", "Player2");
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		List<Player>  players = new ArrayList<>();
		Card favorCard = new Card(CardType.FAVOR, CardName.FAVOR, new FavorAction(playerInteractionHelper));
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);
		drawPileCards.add(favorCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(favorCard);
		playerHandCards.add(shuffleCard);
		players.add(player1);
		players.add(player2);

		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(favorCard)).once();

		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(true);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards,  players);
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
