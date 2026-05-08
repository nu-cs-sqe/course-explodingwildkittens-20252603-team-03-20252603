package domain.model;

import domain.action.CardAction;
import domain.enums.CardType;

public class Card {
    private CardType type;
    private String name;
    private CardAction action;

    public boolean isType(CardType type) { return false; }
    public void execute(GameState gameState) {}
}