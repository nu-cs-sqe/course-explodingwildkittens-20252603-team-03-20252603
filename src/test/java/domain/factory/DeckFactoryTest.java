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
        DeckFactory factory = factory(5);
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
        DeckFactory factory = factory(5);
        List<Card> cards = factory.buildExplodingKittenCards();
        assertEquals(4, cards.size());
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
        assertEquals(6, cards.size());
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
        DeckFactory deckFactory = factory(5);
        assertEquals(4, deckFactory.getNumExplodingKittenCards());
    }

    // --- generate methods ---

    @Test
    void generateDefuseCards_ReturnsSixCards() {
        DeckFactory deckFactory = factory(2);
        List<Card> cards = deckFactory.generateDefuseCards();
        assertEquals(6, cards.size());
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
        DeckFactory deckFactory = factory(5);
        List<Card> cards = deckFactory.generateExplodingKittenCards();
        assertEquals(4, cards.size());
        for (Card card : cards) {
            assertTrue(card.isType(CardType.EXPLODING_KITTEN));
            assertTrue(card.isName(CardName.EXPLODING_KITTEN));
        }
    }

    @Test
    void generateSkipCards_ReturnsFourCards() {
        DeckFactory deckFactory = factory(2);
        List<Card> cards = deckFactory.generateSkipCards();
        assertEquals(4, cards.size());
        for (Card card : cards) {
            assertTrue(card.isType(CardType.SKIP));
            assertTrue(card.isName(CardName.SKIP));
        }
    }

    @Test
    void generateAttackCards_ReturnsFourCards() {
        DeckFactory deckFactory = factory(2);
        List<Card> cards = deckFactory.generateAttackCards();
        assertEquals(4, cards.size());
        for (Card card : cards) {
            assertTrue(card.isType(CardType.ATTACK));
            assertTrue(card.isName(CardName.ATTACK));
        }
    }

    @Test
    void generateShuffleCards_ReturnsFourCards() {
        DeckFactory deckFactory = factory(2);
        List<Card> cards = deckFactory.generateShuffleCards();
        assertEquals(4, cards.size());
        for (Card card : cards) {
            assertTrue(card.isType(CardType.SHUFFLE));
            assertTrue(card.isName(CardName.SHUFFLE));
        }
    }

    @Test
    void generateSeeTheFutureCards_ReturnsFiveCards() {
        DeckFactory deckFactory = factory(2);
        List<Card> cards = deckFactory.generateSeeTheFutureCards();
        assertEquals(5, cards.size());
        for (Card card : cards) {
            assertTrue(card.isType(CardType.SEE_THE_FUTURE));
            assertTrue(card.isName(CardName.SEE_THE_FUTURE));
        }
    }

    @Test
    void generateFavorCards_ReturnsFourCards() {
        DeckFactory deckFactory = factory(2);
        List<Card> cards = deckFactory.generateFavorCards();
        assertEquals(4, cards.size());
        for (Card card : cards) {
            assertTrue(card.isType(CardType.FAVOR));
            assertTrue(card.isName(CardName.FAVOR));
        }
    }

    @Test
    void generateNopeCards_ReturnsFiveCards() {
        DeckFactory deckFactory = factory(2);
        List<Card> cards = deckFactory.generateNopeCards();
        assertEquals(5, cards.size());
        for (Card card : cards) {
            assertTrue(card.isType(CardType.NOPE));
            assertTrue(card.isName(CardName.NOPE));
        }
    }

    @Test
    void generateCatCards_ReturnsTwentyCards() {
        DeckFactory deckFactory = factory(2);
        List<Card> cards = deckFactory.generateCatCards();
        assertEquals(20, cards.size());

        int numBeard_Cat_Cards = 0;
        int numTaco_Cat_Cards = 0;
        int numHairy_Potato_Cat_Cards = 0;
        int numCattermelon_Cat_Cards = 0;
        int numRainbow_Ralphing_Cat_Cards = 0;

        for (Card card : cards) {
            assertTrue(card.isType(CardType.CAT_CARD));

            if (card.isName(CardName.BEARD_CAT)){
                numBeard_Cat_Cards ++;
            } else if (card.isName(CardName.TACO_CAT)){
                numTaco_Cat_Cards ++;
            } else if (card.isName(CardName.RAINBOW_RALPHING_CAT)){
                numRainbow_Ralphing_Cat_Cards ++;
            } else if (card.isName(CardName.HAIRY_POTATO_CAT)){
                numHairy_Potato_Cat_Cards ++;
            } else if (card.isName(CardName.CATTERMELON)){
                numCattermelon_Cat_Cards ++;
            }
        }

        assertEquals(4, numBeard_Cat_Cards);
        assertEquals(4, numRainbow_Ralphing_Cat_Cards);
        assertEquals(4, numCattermelon_Cat_Cards);
        assertEquals(4, numHairy_Potato_Cat_Cards);
        assertEquals(4, numTaco_Cat_Cards);
    }
}