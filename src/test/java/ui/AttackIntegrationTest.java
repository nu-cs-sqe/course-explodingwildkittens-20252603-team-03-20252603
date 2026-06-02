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

public class AttackIntegrationTest {
	private final int TWO_PLAYERS = 2;
	private final int ONE_TURN = 1;
	private final int TWO_TURNS = 2;

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
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(attackCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expect(input.promptPlayerChoice())
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
		drawPileCards.add(cattermelonCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(attackCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expect(input.promptPlayerChoice())
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

}
