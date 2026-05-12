package domain.model;

import domain.action.CardAction;
import domain.enums.CardName;
import domain.enums.CardType;

public class Card {
    private CardType type;
    private CardName name;
    private CardAction action;

    public Card(CardType cardType, CardName cardName, CardAction cardAction) {
        this.type = cardType;
        this.name = cardName;
        this.action = cardAction;
    }

    public boolean isType(CardType cardType) {
        return (this.type == cardType);
    }

    public boolean isName(CardName cardName) {
        return (this.name == cardName);
    }

    public void execute(GameState gameState) {
        this.action.execute(gameState);
    }

    public boolean isSameName(Card other) {
        return this.name == other.name;
    }

    public void setAction(CardAction cardAction) {
        this.action = cardAction;
    }
}