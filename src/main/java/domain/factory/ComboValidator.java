package domain.factory;

import domain.action.*;
import domain.enums.CardType;
import domain.model.Card;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ComboValidator {

	private static final ResourceBundle BUNDLE =
		ResourceBundle.getBundle("labels", Locale.getDefault());
	private final PlayerInteractionHelper helper;
	private static final int MAX_COMBO_SIZE = 3;

	public ComboValidator(PlayerInteractionHelper helper) {
		this.helper = helper;
	}

	public boolean isValid(List<Card> cards) {
		if (cards == null || cards.isEmpty()) {
			return false;
		}
		if (cards.size() == 1) return isValidSingle(cards.get(0));
		if (cards.size() == 2 || cards.size() == MAX_COMBO_SIZE) return isValidCatCombo(cards);
		return false;
	}

	private boolean isValidSingle(Card card) {
		return !card.isType(CardType.CAT_CARD)
				&& !card.isType(CardType.EXPLODING_KITTEN)
				&& !card.isType(CardType.DEFUSE);
	}

	private boolean isValidCatCombo(List<Card> cards) {
		Card first = cards.get(0);
		if (!first.isType(CardType.CAT_CARD)) {
			return false;
		}
		for (Card card : cards) {
			if (!card.isType(CardType.CAT_CARD) || !first.isSameName(card)) {
				return false;
			}
		}
		return true;
	}

	public CardAction resolveAction(List<Card> cards) {
		if (!isValid(cards)) {
			throw new IllegalArgumentException(BUNDLE.getString("error.invalid.combo"));
		}
		if (cards.size() == 1) {
			Card card = cards.get(0);
			if (card.isType(CardType.SKIP)) { return new SkipAction(); }
			if (card.isType(CardType.ATTACK)) { return new AttackAction(); }
			if (card.isType(CardType.SHUFFLE)) { return new ShuffleAction(); }
			if (card.isType(CardType.SEE_THE_FUTURE)) { return new SeeTheFutureAction(); }
			if (card.isType(CardType.FAVOR)) { return new FavorAction(helper); }
			if (card.isType(CardType.NOPE)) { return new NopeAction(); }
		}
		if (cards.size() == 2) { return new TwoCatAction(helper); }

		return new ThreeCatAction(helper);

	}
}
