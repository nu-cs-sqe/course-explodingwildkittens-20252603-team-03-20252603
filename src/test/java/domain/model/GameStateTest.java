package domain.model;

import domain.action.SkipAction;
import domain.enums.CardName;
import domain.enums.CardType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {
	private static final int THREE_CARD_DECK_SIZE = 3;
	private static final int CARD_INDEX_IN_DECK = 3;

	private List<Player> twoPlayers() {
		return List.of(new Player("p1", "Caroline"), new Player("p2", "Mercy"));
	}

	private Deck emptyDeck() {
		return new Deck(new ArrayList<>());
	}

	private Deck nonEmptyDeck() {
		return new Deck(List.of(new Card(CardType.SKIP, CardName.SKIP, new SkipAction())));
	}

	private Deck multiCardDeck() {
		return new Deck(List.of(
				new Card(CardType.SKIP, CardName.SKIP, new SkipAction()),
				new Card(CardType.SKIP, CardName.SKIP, new SkipAction())
		));
	}

	@Test
	public void isActive_StatusActive_ReturnsTrue() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		assertTrue(gs.isActive());
	}

	@Test
	public void isActive_StatusEnded_ReturnsFalse() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		gs.endGame();
		assertFalse(gs.isActive());
	}

	@Test
	public void endGame_ActiveGame_SetsStatusEnded() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		gs.endGame();
		assertFalse(gs.isActive());
	}

	@Test
	public void endGame_AlreadyEnded_RemainsEnded() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		gs.endGame();
		gs.endGame();
		assertFalse(gs.isActive());
	}

	@Test
	public void isDeckEmpty_EmptyDeck_ReturnsTrue() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		assertTrue(gs.isDeckEmpty());
	}

	@Test
	public void isDeckEmpty_NonEmptyDeck_ReturnsFalse() {
		GameState gs = new GameState(twoPlayers(), nonEmptyDeck());
		assertFalse(gs.isDeckEmpty());
	}

	@Test
	public void getDeckSize_EmptyDeck_ReturnsZero() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		assertEquals(0, gs.getDeckSize());
	}

	@Test
	public void getDeckSize_OneCard_ReturnsOne() {
		GameState gs = new GameState(twoPlayers(), nonEmptyDeck());
		assertEquals(1, gs.getDeckSize());
	}

	@Test
	public void getDeckSize_MultipleCards_ReturnsCount() {
		GameState gs = new GameState(twoPlayers(), multiCardDeck());
		assertEquals(2, gs.getDeckSize());
	}

	@Test
	public void turnState_ReturnsNonNullTurnState() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		assertNotNull(gs.turnState());
	}

	@Test
	public void shuffleDeck_DelegatesShuffleToDeck() {
		GameState gs = new GameState(twoPlayers(), multiCardDeck());
		assertDoesNotThrow(gs::shuffleDeck);
	}

	@Test
	public void drawFromDeck_EmptyDeck_ThrowsIllegalStateException() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		assertThrows(IllegalStateException.class, gs::drawFromDeck);
	}

	@Test
	public void drawFromDeck_OneCard_ReturnsCard() {
		GameState gs = new GameState(twoPlayers(), nonEmptyDeck());
		assertNotNull(gs.drawFromDeck());
	}

	@Test
	public void drawFromDeck_OneCard_EmptiesDeck() {
		GameState gs = new GameState(twoPlayers(), nonEmptyDeck());
		gs.drawFromDeck();
		assertTrue(gs.isDeckEmpty());
	}

	@Test
	public void drawFromDeck_MultipleCards_ReturnsTopCard() {
		GameState gs = new GameState(twoPlayers(), multiCardDeck());
		assertNotNull(gs.drawFromDeck());
	}

	@Test
	public void drawFromDeck_MultipleCards_DeckSizeDecrementsBy1() {
		GameState gs = new GameState(twoPlayers(), multiCardDeck());
		gs.drawFromDeck();
		assertEquals(1, gs.getDeckSize());
	}

	@Test
	public void insertIntoDeck_InvalidIndex_ThrowsIndexOutOfBoundsException() {
		GameState gs = new GameState(twoPlayers(), multiCardDeck());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		assertThrows(IndexOutOfBoundsException.class, () -> gs.insertIntoDeck(skipCard, -1));
	}

	@Test
	public void insertIntoDeck_EmptyDeckIndexZero_InsertsCard() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		gs.insertIntoDeck(skipCard, 0);
		assertEquals(1, gs.getDeckSize());
	}

	@Test
	public void insertIntoDeck_EmptyDeckIndexOne_ThrowsIndexOutOfBoundsException() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		assertThrows(IndexOutOfBoundsException.class, () -> gs.insertIntoDeck(skipCard, 1));
	}

	@Test
	public void insertIntoDeck_NonEmptyDeckValidIndex_InsertsCard() {
		GameState gs = new GameState(twoPlayers(), multiCardDeck());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		gs.insertIntoDeck(skipCard, 1);
		assertEquals(THREE_CARD_DECK_SIZE, gs.getDeckSize());
	}

	@Test
	public void insertIntoDeck_NonEmptyDeckIndexPastBounds_ThrowsIndexOutOfBoundsException() {
		GameState gs = new GameState(twoPlayers(), multiCardDeck());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		assertThrows(IndexOutOfBoundsException.class, () -> gs.insertIntoDeck(skipCard, CARD_INDEX_IN_DECK));
	}

	@Test
	public void discardCard_NullCard_ThrowsIllegalArgumentException() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		assertThrows(IllegalArgumentException.class, () -> gs.discardCard(null));
	}

	@Test
	public void discardCard_EmptyPile_AddsCard() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		gs.discardCard(skipCard);
		assertEquals(1, gs.getDiscardPileSize());
	}

	@Test
	public void discardCard_NonEmptyPile_AppendsCard() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		gs.discardCard(new Card(CardType.SEE_THE_FUTURE, CardName.SEE_THE_FUTURE, new SkipAction()));
		gs.discardCard(new Card(CardType.SEE_THE_FUTURE, CardName.SEE_THE_FUTURE, new SkipAction()));
		assertEquals(2, gs.getDiscardPileSize());
	}

	@Test
	public void getCurrentPlayer_EmptyQueue_ThrowsIllegalStateException() {
		GameState gs = new GameState(List.of(), emptyDeck());
		assertThrows(IllegalStateException.class, gs::getCurrentPlayer);
	}

	@Test
	public void getCurrentPlayer_OnePlayer_ReturnsThatPlayer() {
		Player player = new Player("p1", "Caroline");
		GameState gs = new GameState(List.of(player), emptyDeck());
		assertEquals(player, gs.getCurrentPlayer());
	}

	@Test
	public void getCurrentPlayer_MultiplePlayer_ReturnsFrontPlayer() {
		Player front = new Player("p1", "Chibu");
		Player second = new Player("p2", "Austin");
		GameState gs = new GameState(List.of(front, second), emptyDeck());
		assertEquals(front, gs.getCurrentPlayer());
	}

	@Test
	public void advancePlayer_OnePlayer_SamePlayerRemainsCurrentPlayer() {
		Player player = new Player("p1", "Mercy");
		GameState gs = new GameState(List.of(player), emptyDeck());
		gs.advancePlayer();
		assertEquals(player, gs.getCurrentPlayer());
	}

	@Test
	public void advancePlayer_MultiplePlayers_NextPlayerBecomesCurrentPlayer() {
		Player first = new Player("p1", "Caroline");
		Player second = new Player("p2", "Chibu");
		GameState gs = new GameState(List.of(first, second), emptyDeck());
		gs.advancePlayer();
		assertEquals(second, gs.getCurrentPlayer());
	}

	@Test
	public void advancePlayer_MultiplePlayers_PreviousCurrentMovesToBack() {
		Player first = new Player("p1", "Caroline");
		Player second = new Player("p2", "Mercy");
		GameState gs = new GameState(List.of(first, second), emptyDeck());
		gs.advancePlayer();
		gs.advancePlayer();
		assertEquals(first, gs.getCurrentPlayer());
	}

	@Test
	public void addCardToCurrentPlayer_EmptyHand_AddsCard() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		Card skipCard = new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
		gs.addCardToCurrentPlayer(skipCard);
		int numCardsPlayerHas = gs.getCurrentPlayer().getHand().size();
		assertEquals(1, numCardsPlayerHas);
	}
}