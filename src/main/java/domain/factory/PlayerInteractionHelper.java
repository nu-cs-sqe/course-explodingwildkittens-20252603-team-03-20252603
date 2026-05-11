package domain.factory;

import domain.enums.CardType;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.Player;

import java.util.List;
import java.util.Random;

public class PlayerInteractionHelper {

    private final IPlayerInput input;
    private final Random random;

    public PlayerInteractionHelper(IPlayerInput input, Random random) {
        this.input = input;
        this.random = random;
    }

    public void stealRandomCard(Player from, Player to) {
        List<Card> hand = from.getHand();
        if (hand.isEmpty()) {
            return;
        }
        Card card = hand.get(random.nextInt(hand.size()));
        from.removeCard(card);
        to.addCard(card);
    }

    public void stealNamedCard(Player from, Player to, CardType type) {
        if (!from.hasCard(type)) {
            return;
        }
        Card card = from.getCardOfType(type);
        from.removeCard(card);
        to.addCard(card);
    }

    public void giveCard(Player from, Player to) {
        List<Card> selection = input.promptCardSelection(from);
        if (selection.isEmpty()) {
            return;
        }
        Card card = selection.get(0);
        from.removeCard(card);
        to.addCard(card);
    }
}
