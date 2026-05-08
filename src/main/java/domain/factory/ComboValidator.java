package domain.factory;

import domain.action.CardAction;
import domain.model.Card;
import java.util.List;

public class ComboValidator {
    public boolean isValid(List<Card> cards) { return false; }
    public CardAction resolveAction(List<Card> cards) { return null; }
}