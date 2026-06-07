package ui;

import domain.action.NoAction;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("checkstyle:MethodLength")
public class ExplodingKittenIntegrationTest {

	private static final int TWO_TURNS = 2;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}

	@Test
	void explodingKitten_HasDefuse_PlayerSurvivesAndEKReinserted() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		Player player1 = new Player("P1", "Player1");
		Player player2 = new Player("P2", "Player2");
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		List<Player> players = new ArrayList<>();
		Card ekCard = new Card(CardType.EXPLODING_KITTEN, CardName.EXPLODING_KITTEN, new NoAction());
		Card defuseCard = new Card(CardType.DEFUSE, CardName.DEFUSE, new NoAction());
		Card fillerCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new NoAction());
		drawPileCards.add(ekCard);
		playerHandCards.add(defuseCard);
		playerHandCards.add(fillerCard);
		players.add(player1);
		players.add(player2);

		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptInsertPosition(0)).andReturn(0);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards, players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		gc.playATurn();

		assertTrue(firstPlayer.isActive());
		assertNotEquals(firstPlayer, gc.gameState().getCurrentPlayer());
		assertEquals(1, gc.gameState().getDeckSize());
		assertEquals(1, gc.gameState().getDiscardPileSize());
		assertTrue(gc.gameState().getDiscardPile().stream().anyMatch(c -> c.isType(CardType.DEFUSE)));
		assertTrue(gc.gameState().peekTopOfDeck(1).get(0).isType(CardType.EXPLODING_KITTEN));

		EasyMock.verify(display, input);
	}

	@Test
	void explodingKitten_NoDefuse_PlayerEliminatedAndGameEnds() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		Player player1 = new Player("P1", "Player1");
		Player player2 = new Player("P2", "Player2");
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		List<Player> players = new ArrayList<>();
		Card ekCard = new Card(CardType.EXPLODING_KITTEN, CardName.EXPLODING_KITTEN, new NoAction());
		Card fillerCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new NoAction());
		drawPileCards.add(ekCard);
		playerHandCards.add(fillerCard);
		players.add(player1);
		players.add(player2);

		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		display.showEliminated(EasyMock.isA(Player.class));
		display.showWinner(player2);
		EasyMock.expect(input.promptRestart()).andReturn(false);

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards, players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		gc.playATurn();

		assertFalse(firstPlayer.isActive());
		assertTrue(firstPlayer.getHand().isEmpty());
		assertFalse(gc.isGameActive());
		assertEquals(0, gc.gameState().getDeckSize());
		assertTrue(gc.gameState().getDiscardPile().stream().anyMatch(c -> c.isType(CardType.EXPLODING_KITTEN)));
		assertEquals(player2, gc.gameState().getCurrentPlayer());

		EasyMock.verify(display, input);
	}

	@Test
	void explodingKitten_NoDefuse_EliminatedPlayerRemovedFromQueue() {
		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		Player player1 = new Player("P1", "Player1");
		Player player2 = new Player("P2", "Player2");
		Player player3 = new Player("P3", "Player3");
		List<Card> playerHandCards = new ArrayList<>();
		List<Card> drawPileCards = new ArrayList<>();
		List<Player> players = new ArrayList<>();
		Card ekCard = new Card(CardType.EXPLODING_KITTEN, CardName.EXPLODING_KITTEN, new NoAction());
		Card fillerCard = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new NoAction());
		Card deckFiller = new Card(CardType.SHUFFLE, CardName.SHUFFLE, new NoAction());
		drawPileCards.add(ekCard);
		drawPileCards.add(deckFiller);
		playerHandCards.add(fillerCard);
		players.add(player1);
		players.add(player2);
		players.add(player3);

		display.showCurrentPlayer(EasyMock.isA(Player.class), EasyMock.anyInt());
		EasyMock.expectLastCall().times(TWO_TURNS);
		EasyMock.expect(input.promptPlayerChoice(EasyMock.anyObject()))
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		display.showEliminated(EasyMock.isA(Player.class));

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		Deck deck = new Deck(drawPileCards);
		gc.startGame(deck, playerHandCards, players);
		Player firstPlayer = gc.gameState().getCurrentPlayer();

		gc.playATurn();
		assertFalse(firstPlayer.isActive());
		assertNotEquals(firstPlayer, gc.gameState().getCurrentPlayer());

		gc.playATurn();
		assertNotEquals(firstPlayer, gc.gameState().getCurrentPlayer());

		EasyMock.verify(display, input);
	}
}
