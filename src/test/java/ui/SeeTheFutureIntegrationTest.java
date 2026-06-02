package ui;

import domain.action.NoAction;
import domain.action.SeeTheFutureAction;
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
public class SeeTheFutureIntegrationTest {
	private static final int TWO_PLAYERS = 2;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}

	@Test
	void seeTheFuture_IsNotNoped_PeekCardsShown(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		PlayerInteractionHelper  playerInteractionHelper = new PlayerInteractionHelper(input, new Random());
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card seeTheFutureCard = new Card(
				CardType.SEE_THE_FUTURE,
				CardName.SEE_THE_FUTURE,
				new SeeTheFutureAction(playerInteractionHelper));
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		drawPileCards.add(seeTheFutureCard);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(seeTheFutureCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice())
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(seeTheFutureCard)).once();
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(false);
		input.showPeekCards(EasyMock.isA(Player.class));
		EasyMock.expectLastCall().once();

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
	void seeTheFuture_IsNoped_NoPeekCardsShown(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		PlayerInteractionHelper  playerInteractionHelper = new PlayerInteractionHelper(input, new Random());
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction());
		Card seeTheFutureCard = new Card(
				CardType.SEE_THE_FUTURE,
				CardName.SEE_THE_FUTURE,
				new SeeTheFutureAction(playerInteractionHelper));
		Card cattermelonCard = new Card(CardType.CAT_CARD, CardName.CATTERMELON, new NoAction());
		drawPileCards.add(seeTheFutureCard);
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(seeTheFutureCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice())
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(seeTheFutureCard)).once();
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
}
