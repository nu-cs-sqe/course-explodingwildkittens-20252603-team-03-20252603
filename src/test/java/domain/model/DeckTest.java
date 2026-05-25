package domain.model;

import domain.enums.CardName;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

	private static final int THREE_CARD_DECK_SIZE = 3;
	private static final int FOUR_CARD_DECK_SIZE = 4;

	// --- shuffle() ---

	@Test
	void shuffle_EmptyDeck_Shuffles() {
		Random mockRandom = EasyMock.createMock(Random.class);

		EasyMock.expect(mockRandom.nextInt(EasyMock.anyInt())).andReturn(0).anyTimes();

		List<Card> cards = new ArrayList<>();
		Deck deck = new Deck(cards, mockRandom);

		//should call random somewhere or shuffle class
		EasyMock.replay(mockRandom);

		assertDoesNotThrow(deck::shuffle);
		EasyMock.verify(mockRandom);
	}

	@Test
	void shuffle_DeckWithOneElement_Shuffles() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Random mockRandom = EasyMock.createMock(Random.class);

		EasyMock.expect(mockRandom.nextInt(EasyMock.anyInt())).andReturn(0).anyTimes();

		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		Deck deck = new Deck(cards, mockRandom);

		EasyMock.replay(mockCard1, mockRandom);

		assertDoesNotThrow(deck::shuffle);
		EasyMock.verify(mockCard1, mockRandom);
	}

	@Test
	void shuffle_DeckWithMoreThanOneElement_Shuffles() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		Random mockRandom = EasyMock.createMock(Random.class);

		EasyMock.expect(mockRandom.nextInt(EasyMock.anyInt())).andReturn(0).atLeastOnce();

		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards, mockRandom);

		EasyMock.replay(mockCard1, mockCard2, mockCard3, mockRandom);

		assertDoesNotThrow(deck::shuffle);
		EasyMock.verify(mockCard1, mockCard2, mockCard3, mockRandom);
	}

	@Test
	void drawTop_EmptyDeck_ThrowsIllegalStateException() {
		List<Card> cards = new ArrayList<>();
		Deck deck = new Deck(cards);

		assertThrows(IllegalStateException.class, deck::drawTop);
	}

	@Test
	void drawTop_DeckWithOneElement_RemovesOneCard() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		Deck deck = new Deck(cards);

		assertEquals(1, deck.size());
		Card drawnCard = deck.drawTop();
		EasyMock.replay(mockCard1);

		assertNotNull(drawnCard);
		assertEquals(mockCard1, drawnCard);
		assertEquals(0, deck.size());
	}

	@Test
	void drawTop_DeckWithMoreThanOneElement_RemovesOneCard() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		assertEquals(THREE_CARD_DECK_SIZE, deck.size());
		Card drawnCard = deck.drawTop();
		EasyMock.replay(mockCard1, mockCard2, mockCard3);

		assertNotNull(drawnCard);
		assertEquals(mockCard1, drawnCard);
		assertEquals(2, deck.size());
	}


	@Test
	void insertAt_EmptyDeckIndexZero_AddsOne() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1);

		deck.insertAt(mockCard1, 0);
		assertEquals(1, deck.size());
	}

	@Test
	void insertAt_EmptyDeckIndexOne_ThrowsIndexOutOfBoundsException() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1);

		assertEquals(0, deck.size());
		assertThrows(IndexOutOfBoundsException.class, () -> deck.insertAt(mockCard1, 1));

	}

	@Test
	void insertAt_DeckWithOneElementIndexWithinBoundsIndexZero_AddsOne() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2);

		assertEquals(1, deck.size());
		deck.insertAt(mockCard2, 0);
		assertEquals(2, deck.size());
		Card drawnCard = deck.drawTop();
		assertEquals(mockCard2, drawnCard);
	}

	@Test
	void insertAt_DeckWithOneElementIndexWithinBoundsIndexOne_AddsOne() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2);

		assertEquals(1, deck.size());
		deck.insertAt(mockCard2, 1);
		assertEquals(2, deck.size());
		deck.drawTop();
		Card drawnCard2 = deck.drawTop();
		assertEquals(drawnCard2, mockCard2);
	}

	@Test
	void insertAt_DeckWithOneElementIndexLargerThanBoundsIndexTwo_ThrowsIndexOutOfBoundsException() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2);

		assertEquals(1, deck.size());
		assertThrows(IndexOutOfBoundsException.class, () -> deck.insertAt(mockCard2, 2));
	}

	@Test
	void insertAt_DeckWithMoreThanOneElementWithinBounds_AddsOne() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		Card mockCard4 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2, mockCard3, mockCard4);

		assertEquals(THREE_CARD_DECK_SIZE, deck.size());
		deck.insertAt(mockCard4, 1);
		assertEquals(FOUR_CARD_DECK_SIZE, deck.size());

	}

	@Test
	void insertAt_DeckWithMoreThanOneElementLargerThanBounds_ThrowsIndexOutOfBoundsException() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		Card mockCard4 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2, mockCard3, mockCard4);

		assertEquals(THREE_CARD_DECK_SIZE, deck.size());
		assertThrows(IndexOutOfBoundsException.class, () -> deck.insertAt(mockCard4, FOUR_CARD_DECK_SIZE));
	}

	@Test
	void insertAt_DeckWithMoreThanOneElementIndexMinusOne_ThrowsIndexOutOfBoundsException() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		Card mockCard4 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2, mockCard3, mockCard4);

		assertEquals(THREE_CARD_DECK_SIZE, deck.size());
		assertThrows(IndexOutOfBoundsException.class, () -> deck.insertAt(mockCard4, -1));
	}

	@Test
	void insertAt_DeckWithMoreThanOneElementIndexZero_AddsOne() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		Card mockCard4 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2, mockCard3, mockCard4);

		assertEquals(THREE_CARD_DECK_SIZE, deck.size());
		deck.insertAt(mockCard4, 0);
		assertEquals(FOUR_CARD_DECK_SIZE, deck.size());
		Card drawnCard = deck.drawTop();
		assertEquals(mockCard4, drawnCard);
	}


	@Test
	void insertAt_DeckWithMoreThanOneElementLargestValidIndex_AddsOne() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		Card mockCard4 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2, mockCard3, mockCard4);

		int largestValidIndex = deck.size();
		deck.insertAt(mockCard4, largestValidIndex);
		assertEquals(FOUR_CARD_DECK_SIZE, deck.size());

	}

	@Test
	void insertAt_DeckWithMoreThanOneElementLargestValidIndexPlusOne_ThrowsIndexOutOfBoundsException() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		Card mockCard4 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2, mockCard3, mockCard4);

		int outOfBoundsIndex = deck.size() + 1;
		assertThrows(IndexOutOfBoundsException.class, () -> deck.insertAt(mockCard4, outOfBoundsIndex));
	}

	@Test
	void size_EmptyDeck_ReturnsZero() {
		List<Card> cards = new ArrayList<>();
		Deck deck = new Deck(cards);

		assertEquals(0, deck.size());
	}

	@Test
	void size_DeckWithOneElement_ReturnsOne() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1);

		assertEquals(1, deck.size());
	}

	@Test
	void size_DeckWithMoreThanOneElement_ReturnsSize() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2, mockCard3);

		assertEquals(THREE_CARD_DECK_SIZE, deck.size());
	}

	@Test
	void isEmpty_EmptyDeck_ReturnsTrue() {
		List<Card> cards = new ArrayList<>();
		Deck deck = new Deck(cards);

		assertTrue(deck.isEmpty());
	}

	@Test
	void isEmpty_DeckWithOneElement_ReturnsFalse() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1);

		assertFalse(deck.isEmpty());
	}

	@Test
	void isEmpty_DeckWithMoreThanOneElement_ReturnsFalse() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		EasyMock.replay(mockCard1, mockCard2, mockCard3);

		assertFalse(deck.isEmpty());
	}


	@Test
	void countCardsByName_EmptyDeck_ReturnsZero() {
		List<Card> cards = new ArrayList<>();
		Deck deck = new Deck(cards);

		assertEquals(0, deck.countCardsByName(CardName.DEFUSE));
	}

	@Test
	void countCardsByName_DeckWithOneElementOfThatName_ReturnsOne() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		Deck deck = new Deck(cards);

		EasyMock.expect(mockCard1.isName(CardName.DEFUSE)).andReturn(true);
		EasyMock.replay(mockCard1);

		assertEquals(1, deck.countCardsByName(CardName.DEFUSE));
		EasyMock.verify(mockCard1);
	}

	@Test
	void countCardsByName_DeckWithMoreThanOneElementOfThatName_ReturnsNumberOfInstancesOfThatName() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		EasyMock.expect(mockCard1.isName(CardName.DEFUSE)).andReturn(true);
		EasyMock.expect(mockCard2.isName(CardName.DEFUSE)).andReturn(true);
		EasyMock.expect(mockCard3.isName(CardName.DEFUSE)).andReturn(false);
		EasyMock.replay(mockCard1, mockCard2, mockCard3);

		assertEquals(2, deck.countCardsByName(CardName.DEFUSE));
		EasyMock.verify(mockCard1, mockCard2, mockCard3);
	}

	@Test
	void countCardsByName_DeckWithMoreThanOneElementButNoElementsOfThatName_ReturnsZero() {
		Card mockCard1 = EasyMock.createMock(Card.class);
		Card mockCard2 = EasyMock.createMock(Card.class);
		Card mockCard3 = EasyMock.createMock(Card.class);
		List<Card> cards = new ArrayList<>();
		cards.add(mockCard1);
		cards.add(mockCard2);
		cards.add(mockCard3);
		Deck deck = new Deck(cards);

		EasyMock.expect(mockCard1.isName(CardName.NOPE)).andReturn(false);
		EasyMock.expect(mockCard2.isName(CardName.NOPE)).andReturn(false);
		EasyMock.expect(mockCard3.isName(CardName.NOPE)).andReturn(false);
		EasyMock.replay(mockCard1, mockCard2, mockCard3);

		assertEquals(0, deck.countCardsByName(CardName.NOPE));
		EasyMock.verify(mockCard1, mockCard2, mockCard3);
	}
}