package ui;

import domain.action.FavorAction;
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
public class FavorIntegrationTest {
	private static final int TWO_PLAYERS = 2;
	private static final int ONE_CARD = 1;

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
		drawPileCards.add(favorCard);
		drawPileCards.add(shuffleCard);
		playerHandCards.add(favorCard);
		playerHandCards.add(shuffleCard);
		players.add(player1);
		players.add(player2);

		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expectLastCall().once();
		EasyMock.expect(input.promptPlayerChoice())
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
}
