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

public class ShuffleIntegrationTest {

	private static final int TWO_PLAYERS = 2;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}

	private static PlayerInteractionHelper realPlayerInteractionHelper(IPlayerInput input) {
		return new PlayerInteractionHelper(input, new Random());
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
		playerHandCards.add(skipCard);
		playerHandCards.add(cattermelonCard);
		playerHandCards.add(beardCard);
		playerHandCards.add(hairyPotatoCard);
		List<Card> originalCardOrder = playerHandCards;
		playerHandCards.add(shuffleCard);
		drawPileCards.add(shuffleCard);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showMessage(ViewMessages.format("num.players"));
		EasyMock.expectLastCall().once();
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expect(input.promptPlayerChoice())
				.andReturn(PlayerChoice.PLAY_CARD)
				.andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class)))
				.andReturn(List.of(shuffleCard)).once();
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
		List<Card> currentOrderCards = firstPlayer.getHand();

		assertEquals(sizeBefore, sizeAfter);
		assertNotEquals(firstPlayer,  secondPlayer);
		assertEquals(originalCardOrder, currentOrderCards);
	}
}
