package domain.model;

import domain.enums.CardName;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private Random random;
    private List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
        this.random = new Random();
    }

    public Deck(List<Card> cards, Random random) {
        this.cards = cards;
        this.random = random;
    }

    public void shuffle() {
        Collections.shuffle(this.cards, this.random);
    }

    public Card drawTop() throws IllegalStateException {
        if (this.cards.isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck");
        }
        return cards.remove(0);
    }

    public void insertAt(Card card, int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > cards.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        cards.add(index, card);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int countCardsByName(CardName cardName) {
        int count = 0;
        for (Card card : cards) {
            if (card.isName(cardName)) {
                count++;
            }
        }
        return count;
    }
}