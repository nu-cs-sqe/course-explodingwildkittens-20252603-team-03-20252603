package domain.factory;

import domain.action.*;
import domain.enums.CardName;
import domain.enums.CardType;
import domain.model.Card;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

public class ComboValidatorTest {

	private static ComboValidator validator() {
		PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);
		EasyMock.replay(mockHelper);
		return new ComboValidator(mockHelper);
	}

	private static Card card(CardType type, CardName name) {
		return new Card(type, name, new NoAction());
	}


	@Test
	void isValid_NullList_ReturnsFalse() {
		assertFalse(validator().isValid(null));
	}

	@Test
	void isValid_EmptyList_ReturnsFalse() {
		assertFalse(validator().isValid(List.of()));
	}

	@Test
	void isValid_OneActionCard_ReturnsTrue() {
		assertTrue(validator().isValid(List.of(card(CardType.SKIP, CardName.SKIP))));
	}

	@Test
	void isValid_OneCatCard_ReturnsFalse() {
		assertFalse(validator().isValid(List.of(card(CardType.CAT_CARD, CardName.TACO_CAT))));
	}

	@Test
	void isValid_OneExplodingKitten_ReturnsFalse() {
		assertFalse(validator().isValid(List.of(card(CardType.EXPLODING_KITTEN, CardName.EXPLODING_KITTEN))));
	}

	@Test
	void isValid_OneDefuse_ReturnsFalse() {
		assertFalse(validator().isValid(List.of(card(CardType.DEFUSE, CardName.DEFUSE))));
	}

	@Test
	void isValid_TwoMatchingCatCards_ReturnsTrue() {
		List<Card> cards = List.of(
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.TACO_CAT)
		);
		assertTrue(validator().isValid(cards));
	}

	@Test
	void isValid_TwoDifferentCatCards_ReturnsFalse() {
		List<Card> cards = List.of(
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.CATTERMELON)
		);
		assertFalse(validator().isValid(cards));
	}

	@Test
	void isValid_TwoCardsOneNotCat_ReturnsFalse() {
		List<Card> cards = List.of(
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.SKIP, CardName.SKIP)
		);
		assertFalse(validator().isValid(cards));
	}

	@Test
	void isValid_TwoCardsBothNotCat_ReturnsFalse() {
		List<Card> cards = List.of(
				card(CardType.SEE_THE_FUTURE, CardName.NOPE),
				card(CardType.SKIP, CardName.SKIP)
		);
		assertFalse(validator().isValid(cards));
	}

	@Test
	void isValid_ThreeMatchingCatCards_ReturnsTrue() {
		List<Card> cards = List.of(
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.TACO_CAT)
		);
		assertTrue(validator().isValid(cards));
	}

	@Test
	void isValid_ThreeDifferentCatCards_ReturnsFalse() {
		List<Card> cards = List.of(
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.CATTERMELON),
				card(CardType.CAT_CARD, CardName.BEARD_CAT)
		);
		assertFalse(validator().isValid(cards));
	}

	@Test
	void isValid_FourOrMoreCards_ReturnsFalse() {
		List<Card> cards = List.of(
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.TACO_CAT)
		);
		assertFalse(validator().isValid(cards));
	}


	@Test
	void resolveAction_OneSkip_ReturnsSkipAction() {
		CardAction action = validator().resolveAction(List.of(card(CardType.SKIP, CardName.SKIP)));
		assertInstanceOf(SkipAction.class, action);
	}

	@Test
	void resolveAction_OneAttack_ReturnsAttackAction() {
		CardAction action = validator().resolveAction(List.of(card(CardType.ATTACK, CardName.ATTACK)));
		assertInstanceOf(AttackAction.class, action);
	}

	@Test
	void resolveAction_OneShuffle_ReturnsShuffleAction() {
		CardAction action = validator().resolveAction(List.of(card(CardType.SHUFFLE, CardName.SHUFFLE)));
		assertInstanceOf(ShuffleAction.class, action);
	}

	@Test
	void resolveAction_OneSeeFuture_ReturnsSeeTheFutureAction() {
		CardAction action = validator().resolveAction(
				List.of(card(CardType.SEE_THE_FUTURE, CardName.SEE_THE_FUTURE))
		);
		assertInstanceOf(SeeTheFutureAction.class, action);
	}

	@Test
	void resolveAction_OneFavor_ReturnsFavorAction() {
		CardAction action = validator().resolveAction(List.of(card(CardType.FAVOR, CardName.FAVOR)));
		assertInstanceOf(FavorAction.class, action);
	}

	@Test
	void resolveAction_OneNope_ThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,
				() -> validator().resolveAction(List.of(card(CardType.NOPE, CardName.NOPE))));
	}

	@Test
	void resolveAction_TwoMatchingCats_ReturnsTwoCatAction() {
		List<Card> cards = List.of(
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.TACO_CAT)
		);
		CardAction action = validator().resolveAction(cards);
		assertInstanceOf(TwoCatAction.class, action);
	}

	@Test
	void resolveAction_ThreeMatchingCats_ReturnsThreeCatAction() {
		List<Card> cards = List.of(
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.TACO_CAT),
				card(CardType.CAT_CARD, CardName.TACO_CAT)
		);
		CardAction action = validator().resolveAction(cards);
		assertInstanceOf(ThreeCatAction.class, action);
	}

	@Test
	void resolveAction_InvalidCombo_ThrowsIllegalArgumentException() {
		List<Card> cards = List.of(card(CardType.CAT_CARD, CardName.TACO_CAT));
		assertThrows(IllegalArgumentException.class, () -> validator().resolveAction(cards));
	}

	@Test
	void resolveAction_ValidButUnhandledCardType_ThrowsIllegalArgumentException() {
		Card unknownCard = createNiceMock(Card.class);
		replay(unknownCard);
		assertThrows(IllegalArgumentException.class,
				() -> validator().resolveAction(List.of(unknownCard)));
	}

	@Test
	void isValid_OneNope_ReturnsFalse() {
		assertFalse(validator().isValid(List.of(card(CardType.NOPE, CardName.NOPE))));
	}
}
