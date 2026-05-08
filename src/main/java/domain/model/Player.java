package domain.model;

import domain.enums.CardType;
import java.util.List;

public class Player {
    private String id;
    private String name;
    private List<Card> hand;
    private List<Card> peekCards;

    public void addCard(Card card) {}
    public void removeCard(Card card) {}
    public boolean hasCard(CardType type) { return false; }
    public Card getCardOfType(CardType type) { return null; }
    public void storePeek(List<Card> cards) {}
    public void clearPeek() {}
}