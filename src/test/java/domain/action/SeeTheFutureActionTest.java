package domain.action;

import domain.enums.CardName;
import domain.enums.CardType;
import domain.factory.PlayerInteractionHelper;
import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SeeTheFutureActionTest {

	private static final int NUM_CARDS_TO_PEEK = 3;
	private static Card card() {
		return new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
	}

	private static Player player() {
		return new Player("p1", "Alice");
	}

	private static GameState mockReturning(Player player, List<Card> peeked) {
		GameState mock = EasyMock.createMock(GameState.class);
		EasyMock.expect(mock.peekTopOfDeck(NUM_CARDS_TO_PEEK)).andReturn(peeked);
		EasyMock.expect(mock.getCurrentPlayer()).andReturn(player);
		EasyMock.replay(mock);
		return mock;
	}

	@Test
	void execute_EmptyDeck_StoresPeekWithEmptyList() {
		PlayerInteractionHelper helper = EasyMock.createMock(PlayerInteractionHelper.class);
		Player player = player();
		GameState mockGameState = mockReturning(player, Collections.emptyList());

		new SeeTheFutureAction(helper).execute(mockGameState);

		assertTrue(player.getPeekCards().isEmpty());
		EasyMock.verify(mockGameState);
	}

	@Test
	void execute_DeckWithOneCard_StoresPeekWithOneCard() {
		PlayerInteractionHelper helper = EasyMock.createMock(PlayerInteractionHelper.class);
		Player player = player();
		Card card1 = card();
		GameState mockGameState = mockReturning(player, List.of(card1));

		new SeeTheFutureAction(helper).execute(mockGameState);

		assertEquals(List.of(card1), player.getPeekCards());
		EasyMock.verify(mockGameState);
	}

	@Test
	void execute_DeckWithTwoCards_StoresPeekWithTwoCards() {
		PlayerInteractionHelper helper = EasyMock.createMock(PlayerInteractionHelper.class);
		Player player = player();
		Card card1 = card();
		Card card2 = card();
		GameState mockGameState = mockReturning(player, List.of(card1, card2));

		new SeeTheFutureAction(helper).execute(mockGameState);

		assertEquals(List.of(card1, card2), player.getPeekCards());
		EasyMock.verify(mockGameState);
	}

	@Test
	void execute_DeckWithThreeOrMoreCards_StoresPeekWithThreeCards() {
		PlayerInteractionHelper helper = EasyMock.createMock(PlayerInteractionHelper.class);
		Player player = player();
		Card card1 = card();
		Card card2 = card();
		Card card3 = card();
		GameState mockGameState = mockReturning(player, List.of(card1, card2, card3));

		new SeeTheFutureAction(helper).execute(mockGameState);

		assertEquals(List.of(card1, card2, card3), player.getPeekCards());
		EasyMock.verify(mockGameState);
	}
}
