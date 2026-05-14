package domain.factory;

import domain.enums.CardName;
import domain.enums.CardType;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.Deck;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckFactoryTest {

	private static final int FIVE_PLAYERS = 5;
	private static final int EXPECTED_DEFUSE_CARD_COUNT = 6;
	private static final int EXPECTED_EXPLODING_KITTEN_FIVE_PLAYERS = 4;
	private static final int EXPECTED_SKIP_CARD_COUNT = 4;
	private static final int EXPECTED_ATTACK_CARD_COUNT = 4;
	private static final int EXPECTED_SHUFFLE_CARD_COUNT = 4;
	private static final int EXPECTED_SEE_THE_FUTURE_CARD_COUNT = 5;
	private static final int EXPECTED_FAVOR_CARD_COUNT = 4;
	private static final int EXPECTED_NOPE_CARD_COUNT = 5;
	private static final int EXPECTED_TOTAL_CAT_CARDS = 20;
	private static final int EXPECTED_CAT_CARD_TYPE_COUNT = 4;

	private static DeckFactory factory(int numPlayers) {
		IPlayerInput mock = EasyMock.createMock(IPlayerInput.class);
		EasyMock.replay(mock);
		return new DeckFactory(numPlayers, mock);
	}

	// --- buildDeck ---

	@Test
	void buildDeck_TwoPlayers_ReturnsDeck() {
		DeckFactory factory = factory(2);
		Deck deck = factory.buildDeck();
		assertNotNull(deck);
		// other card type asserts will be added once deck class is implemented as integration tests
	}

	@Test
	void buildDeck_FivePlayers_ReturnsDeck() {
		DeckFactory factory = factory(FIVE_PLAYERS);
		Deck deck = factory.buildDeck();
		assertNotNull(deck);
		// other card type asserts will be added once deck class is implemented as integration tests
	}

	// --- buildExplodingKittenCards() ---

	@Test
	void buildExplodingKittenCards_TwoPlayers_ReturnsOneCard() {
		DeckFactory factory = factory(2);
		List<Card> cards = factory.buildExplodingKittenCards();
		assertEquals(1, cards.size());
		assertTrue(cards.get(0).isType(CardType.EXPLODING_KITTEN));
		assertTrue(cards.get(0).isName(CardName.EXPLODING_KITTEN));
	}

	@Test
	void buildExplodingKittenCards_FivePlayers_ReturnsFourCards() {
		DeckFactory factory = factory(FIVE_PLAYERS);
		List<Card> cards = factory.buildExplodingKittenCards();
		assertEquals(EXPECTED_EXPLODING_KITTEN_FIVE_PLAYERS, cards.size());
		for (Card card : cards) {
			assertTrue(card.isType(CardType.EXPLODING_KITTEN));
			assertTrue(card.isName(CardName.EXPLODING_KITTEN));
		}
	}

	// --- buildDefuseCards() ---

	@Test
	void buildDefuseCards_ReturnsSixDefuseCards() {
		DeckFactory factory = factory(2);
		List<Card> cards = factory.buildDefuseCards();
		assertEquals(EXPECTED_DEFUSE_CARD_COUNT, cards.size());
		for (Card card : cards) {
			assertTrue(card.isType(CardType.DEFUSE));
		}
	}

	// --- getNumExplodingKittenCards() ---

	@Test
	void getNumExplodingKittenCards_TwoPlayers_ReturnsOne() {
		DeckFactory deckFactory = factory(2);
		assertEquals(1, deckFactory.getNumExplodingKittenCards());
	}

	@Test
	void getNumExplodingKittenCards_FivePlayers_ReturnsFour() {
		DeckFactory deckFactory = factory(FIVE_PLAYERS);
		assertEquals(EXPECTED_EXPLODING_KITTEN_FIVE_PLAYERS, deckFactory.getNumExplodingKittenCards());
	}

	// --- generate methods ---

	@Test
	void generateDefuseCards_ReturnsSixCards() {
		DeckFactory deckFactory = factory(2);
		List<Card> cards = deckFactory.generateDefuseCards();
		assertEquals(EXPECTED_DEFUSE_CARD_COUNT, cards.size());
		for (Card card : cards) {
			assertTrue(card.isType(CardType.DEFUSE));
			assertTrue(card.isName(CardName.DEFUSE));
		}
	}

	@Test
	void generateExplodingKittenCards_TwoPlayers_ReturnsOneCard() {
		DeckFactory deckFactory = factory(2);
		List<Card> cards = deckFactory.generateExplodingKittenCards();
		assertEquals(1, cards.size());
		assertTrue(cards.get(0).isType(CardType.EXPLODING_KITTEN));
		assertTrue(cards.get(0).isName(CardName.EXPLODING_KITTEN));
	}

	@Test
	void generateExplodingKittenCards_FivePlayers_ReturnsFourCards() {
		DeckFactory deckFactory = factory(FIVE_PLAYERS);
		List<Card> cards = deckFactory.generateExplodingKittenCards();
		assertEquals(EXPECTED_EXPLODING_KITTEN_FIVE_PLAYERS, cards.size());
		for (Card card : cards) {
			assertTrue(card.isType(CardType.EXPLODING_KITTEN));
			assertTrue(card.isName(CardName.EXPLODING_KITTEN));
		}
	}

	@Test
	void generateSkipCards_ReturnsFourCards() {
		DeckFactory deckFactory = factory(2);
		List<Card> cards = deckFactory.generateSkipCards();
		assertEquals(EXPECTED_SKIP_CARD_COUNT, cards.size());
		for (Card card : cards) {
			assertTrue(card.isType(CardType.SKIP));
			assertTrue(card.isName(CardName.SKIP));
		}
	}

	@Test
	void generateAttackCards_ReturnsFourCards() {
		DeckFactory deckFactory = factory(2);
		List<Card> cards = deckFactory.generateAttackCards();
		assertEquals(EXPECTED_ATTACK_CARD_COUNT, cards.size());
		for (Card card : cards) {
			assertTrue(card.isType(CardType.ATTACK));
			assertTrue(card.isName(CardName.ATTACK));
		}
	}

	@Test
	void generateShuffleCards_ReturnsFourCards() {
		DeckFactory deckFactory = factory(2);
		List<Card> cards = deckFactory.generateShuffleCards();
		assertEquals(EXPECTED_SHUFFLE_CARD_COUNT, cards.size());
		for (Card card : cards) {
			assertTrue(card.isType(CardType.SHUFFLE));
			assertTrue(card.isName(CardName.SHUFFLE));
		}
	}

	@Test
	void generateSeeTheFutureCards_ReturnsFiveCards() {
		DeckFactory deckFactory = factory(2);
		List<Card> cards = deckFactory.generateSeeTheFutureCards();
		assertEquals(EXPECTED_SEE_THE_FUTURE_CARD_COUNT, cards.size());
		for (Card card : cards) {
			assertTrue(card.isType(CardType.SEE_THE_FUTURE));
			assertTrue(card.isName(CardName.SEE_THE_FUTURE));
		}
	}

	@Test
	void generateFavorCards_ReturnsFourCards() {
		DeckFactory deckFactory = factory(2);
		List<Card> cards = deckFactory.generateFavorCards();
		assertEquals(EXPECTED_FAVOR_CARD_COUNT, cards.size());
		for (Card card : cards) {
			assertTrue(card.isType(CardType.FAVOR));
			assertTrue(card.isName(CardName.FAVOR));
		}
	}

	@Test
	void generateNopeCards_ReturnsFiveCards() {
		DeckFactory deckFactory = factory(2);
		List<Card> cards = deckFactory.generateNopeCards();
		assertEquals(EXPECTED_NOPE_CARD_COUNT, cards.size());
		for (Card card : cards) {
			assertTrue(card.isType(CardType.NOPE));
			assertTrue(card.isName(CardName.NOPE));
		}
	}

	@Test
	void generateCatCards_ReturnsTwentyCards() {
		DeckFactory deckFactory = factory(2);
		List<Card> cards = deckFactory.generateCatCards();
		assertEquals(EXPECTED_TOTAL_CAT_CARDS, cards.size());

		for (Card card : cards) {
			assertTrue(card.isType(CardType.CAT_CARD));
		};

		assertEquals(EXPECTED_CAT_CARD_TYPE_COUNT, countByName(cards, CardName.BEARD_CAT));
		assertEquals(EXPECTED_CAT_CARD_TYPE_COUNT, countByName(cards, CardName.TACO_CAT));
		assertEquals(EXPECTED_CAT_CARD_TYPE_COUNT, countByName(cards, CardName.RAINBOW_RALPHING_CAT));
		assertEquals(EXPECTED_CAT_CARD_TYPE_COUNT, countByName(cards, CardName.HAIRY_POTATO_CAT));
		assertEquals(EXPECTED_CAT_CARD_TYPE_COUNT, countByName(cards, CardName.CATTERMELON));
	}

	private int countByName(List<Card> cards, CardName name) {
		int count = 0;
		for (Card card : cards) {
			if (card.isName(name)) count++;
		}
		return count;
	}
}