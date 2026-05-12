package domain.factory;

import domain.action.*;
import domain.enums.CardName;
import domain.enums.CardType;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeckFactory {

    private final int numPlayers;
    private final IPlayerInput input;
    private final PlayerInteractionHelper helper;

    public DeckFactory(int numPlayers, IPlayerInput input) {
        this.numPlayers = numPlayers;
        this.input = input;
        this.helper = new PlayerInteractionHelper(input, new Random());
    }

    private static final int NUM_DEFUSE_CARDS = 6;
    private static final int NUM_SKIP_CARDS = 4;
    private static final int NUM_ATTACK_CARDS = 4;
    private static final int NUM_SHUFFLE_CARDS = 4;
    private static final int NUM_SEE_THE_FUTURE_CARDS = 5;
    private static final int NUM_FAVOR_CARDS = 4;
    private static final int NUM_NOPE_CARDS = 5;
    private static final int NUM_CAT_CARDS = 20;

    int getNumExplodingKittenCards() {
        return this.numPlayers - 1;
    }

    List<Card> generateDefuseCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < NUM_DEFUSE_CARDS; i++) {
            cards.add(new Card(CardType.DEFUSE, CardName.DEFUSE, new DefuseAction(input)));
        }
        return cards;
    }

    List<Card> generateExplodingKittenCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < getNumExplodingKittenCards(); i++) {
            cards.add(new Card(CardType.EXPLODING_KITTEN, CardName.EXPLODING_KITTEN, new NoAction()));
        }
        return cards;
    }

    List<Card> generateSkipCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < NUM_SKIP_CARDS; i++) {
            cards.add(new Card(CardType.SKIP, CardName.SKIP, new SkipAction()));
        }
        return cards;
    }

    List<Card> generateAttackCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < NUM_ATTACK_CARDS; i++) {
            cards.add(new Card(CardType.ATTACK, CardName.ATTACK, new AttackAction()));
        }
        return cards;
    }

    List<Card> generateShuffleCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < NUM_SHUFFLE_CARDS; i++) {
            cards.add(new Card(CardType.SHUFFLE, CardName.SHUFFLE, new ShuffleAction()));
        }
        return cards;
    }

    List<Card> generateSeeTheFutureCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < NUM_SEE_THE_FUTURE_CARDS; i++) {
            cards.add(new Card(CardType.SEE_THE_FUTURE, CardName.SEE_THE_FUTURE, new SeeTheFutureAction()));
        }
        return cards;
    }

    List<Card> generateFavorCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < NUM_FAVOR_CARDS; i++) {
            cards.add(new Card(CardType.FAVOR, CardName.FAVOR, new FavorAction(helper)));
        }
        return cards;
    }

    List<Card> generateNopeCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < NUM_NOPE_CARDS; i++) {
            cards.add(new Card(CardType.NOPE, CardName.NOPE, new NopeAction()));
        }
        return cards;
    }

    List<Card> generateCatCards() {
        List<Card> cards = new ArrayList<>();
        CardName[] catNames = {
            CardName.TACO_CAT,
            CardName.CATTERMELON,
            CardName.HAIRY_POTATO_CAT,
            CardName.BEARD_CAT,
            CardName.RAINBOW_RALPHING_CAT
        };
        for (CardName catName : catNames) {
            for (int i = 0; i < NUM_CAT_CARDS / catNames.length; i++) {
                cards.add(new Card(CardType.CAT_CARD, catName, new NoAction()));
            }
        }
        return cards;
    }

    public List<Card> buildExplodingKittenCards() {
        return generateExplodingKittenCards();
    }

    public List<Card> buildDefuseCards() {
        return generateDefuseCards();
    }

    public Deck buildDeck() {
        List<Card> cards = new ArrayList<>();
        cards.addAll(generateAttackCards());
        cards.addAll(generateCatCards());
        cards.addAll(generateNopeCards());
        cards.addAll(generateSeeTheFutureCards());
        cards.addAll(generateFavorCards());
        cards.addAll(generateShuffleCards());
        cards.addAll(generateSkipCards());

        return new Deck(cards);
    }
}