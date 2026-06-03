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
public class PlayATurnIntegrationTest {
	private static final int TWO_PLAYERS = 2;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}


	@Test
	void playATurn_ActionIsNoped_UserNotAllowedToPlayCard() {

		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expect(input.promptPlayerChoice())
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		List<Card> cards = new ArrayList<>();
		cards.add(skipCard);
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		cards.add(nopeCard1);
		cards.add(nopeCard2);
		cards.add(nopeCard3);
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(true);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class))).andReturn(List.of(skipCard)).once();

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(cards);
		gc.startGame(deck, cards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int before = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int after = firstPlayer.getHand().size();

		assertEquals(before, after);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);

	}

	@Test
	void playATurn_InvalidCardCombo_CardIsNotPlayed() {

		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expect(input.promptPlayerChoice())
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SKIP, new ShuffleAction());
		List<Card> cards = new ArrayList<>();
		cards.add(skipCard);
		cards.add(shuffleCard);
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		cards.add(nopeCard1);
		cards.add(nopeCard2);
		cards.add(nopeCard3);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class))).andReturn(List.of(skipCard, shuffleCard)).once();
		display.showMessage(ViewMessages.format("error.invalid.card"));
		EasyMock.expectLastCall().once();

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(cards);
		gc.startGame(deck, cards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int before = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int after = firstPlayer.getHand().size();

		assertEquals(before+1, after);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);

	}

	@Test
	void playATurn_PlayZeroCards_GameIsAdvanced(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expect(input.promptPlayerChoice()).andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.replay(display, input);

		List<Card> cards = new ArrayList<>();
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SKIP, new ShuffleAction());
		cards.add(skipCard);
		cards.add(shuffleCard);
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		cards.add(nopeCard1);
		cards.add(nopeCard2);
		cards.add(nopeCard3);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(cards);
		gc.startGame(deck, cards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int before = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int after = firstPlayer.getHand().size();

		assertEquals(before+1, after);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void playATurn_PlayOneCardNotNopedValidCard_CardIsPlayed(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		playerHandCards.add(skipCard);
		drawPileCards.add(skipCard);
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expect(input.promptPlayerChoice())
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
		int before = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int after = firstPlayer.getHand().size();

		assertEquals(before-1, after);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);
	}

	@Test
	void playATurn_PlayManyCardsNotNopedValidCards_CardsArePlayed(){
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		Card shuffleCard = new Card(CardType.SHUFFLE, CardName.SKIP, new ShuffleAction());
		playerHandCards.add(skipCard);
		playerHandCards.add(shuffleCard);
		drawPileCards.add(skipCard);
		Card nopeCard1 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard2 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		Card nopeCard3 = new Card(CardType.NOPE, CardName.NOPE, new NoAction());
		playerHandCards.add(nopeCard1);
		playerHandCards.add(nopeCard2);
		playerHandCards.add(nopeCard3);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expect(input.promptPlayerChoice())
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(skipCard)).once()
				.andReturn(List.of(shuffleCard)).once();
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(false).andReturn(false);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int before = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int after = firstPlayer.getHand().size();

		assertEquals(before-2, after);
		assertNotEquals(firstPlayer,  secondPlayer);

		EasyMock.verify(display, input);

	}
}
