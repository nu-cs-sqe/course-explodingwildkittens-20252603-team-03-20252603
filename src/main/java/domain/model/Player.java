package domain.model;

import domain.enums.CardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private final String id;
    private final String name;
    private final List<Card> hand;
    private final List<Card> peekCards;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.hand = new ArrayList<>();
        this.peekCards = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    public List<Card> getPeekCards() {
        return Collections.unmodifiableList(peekCards);
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public boolean hasCard(CardType type) {
        for (Card card : hand) {
            if (card.isType(type)) {
                return true;
            }
        }
        return false;
    }

    public Card getCardOfType(CardType type) {
        for (Card card : hand) {
            if (card.isType(type)) {
                return card;
            }
        }
        return null;
    }

    public void storePeek(List<Card> cards) {}

    public void clearPeek() {}
}
