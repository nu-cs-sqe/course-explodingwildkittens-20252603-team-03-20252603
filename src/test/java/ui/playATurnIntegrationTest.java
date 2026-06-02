package ui;

import domain.action.FavorAction;
import domain.action.ShuffleAction;
import domain.action.SkipAction;
import domain.enums.CardName;
import domain.enums.CardType;
import domain.enums.PlayerChoice;
import domain.factory.ComboValidator;
import domain.factory.PlayerInteractionHelper;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class playATurnIntegrationTest {
	private static final int ONE_PLAYER = 1;
	private static final int TWO_PLAYERS = 2;
	private static final int THREE_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;
	private static final int FIVE_PLAYERS = 5;
	private static final int SIX_PLAYERS = 6;

	private static ComboValidator realComboValidator(IPlayerInput input) {
		return new ComboValidator(new PlayerInteractionHelper(input, new Random()));
	}

	private static PlayerInteractionHelper realPlayerInteractionHelper(IPlayerInput input) {
		return new PlayerInteractionHelper(input, new Random());
	}

	@Test
	void playATurn_ActionIsNoped_UserNotAllowedToPlayCard() {
		// asks user for choice
		// calls play card
		// card is discarded
		// apply nope window - action is noped
		// card not executed

		IGameDisplay display = EasyMock.createMock(IGameDisplay.class);
		IPlayerInput input = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(input.promptNumPlayers()).andReturn(TWO_PLAYERS);
		display.showMessage(ViewMessages.format("num.players"));
		EasyMock.expectLastCall().once();
		display.showCurrentPlayer(EasyMock.isA(Player.class));
		EasyMock.expect(input.promptPlayerChoice()).andReturn(PlayerChoice.PLAY_CARD).andReturn(PlayerChoice.DONE_PLAYING_CARDS);
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		List<Card> cards = new ArrayList<Card>();
		cards.add(skipCard);
		EasyMock.expect(input.promptNope(EasyMock.isA(Player.class))).andReturn(true);
		EasyMock.expect(input.promptCardSelection(EasyMock.isA(Player.class))).andReturn(cards).once();

		EasyMock.replay(display, input);

		GameController gc = new GameController(display, input, realComboValidator(input));
		gc.startGame();
		Player firstPlayer = gc.gameState().getCurrentPlayer();
		int before = firstPlayer.getHand().size();
		gc.playATurn();
		Player secondPlayer = gc.gameState().getCurrentPlayer();
		int after = firstPlayer.getHand().size();

		assertEquals(before, after);
		assertNotEquals(firstPlayer,  secondPlayer);

	}
}
