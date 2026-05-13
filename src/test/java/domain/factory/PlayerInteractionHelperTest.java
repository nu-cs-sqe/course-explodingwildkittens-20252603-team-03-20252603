package domain.factory;

import domain.action.AttackAction;
import domain.action.SkipAction;
import domain.enums.CardName;
import domain.enums.CardType;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerInteractionHelperTest {
	private final long RANDOM_SEED_GENERATOR_NUMBER = 42L;

	private static Card skipCard() {
		return new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
	}

	private static Card attackCard() {
		return new Card(CardType.ATTACK, CardName.ATTACK, new AttackAction());
	}

	private static Player player(String id) {
		return new Player(id, id);
	}

	private static IPlayerInput unusedInput() {
		IPlayerInput mock = EasyMock.createMock(IPlayerInput.class);
		EasyMock.replay(mock);
		return mock;
	}

	private static PlayerInteractionHelper helper(IPlayerInput input) {
		return new PlayerInteractionHelper(input, new Random());
	}

	@Test
	void stealRandomCard_FromEmptyHand_NoTransfer() {
		Player from = player("from");
		Player to = player("to");

		helper(unusedInput()).stealRandomCard(from, to);

		assertTrue(from.getHand().isEmpty());
		assertTrue(to.getHand().isEmpty());
	}

	@Test
	void stealRandomCard_FromOneCard_TransfersThatCard() {
		Player from = player("from");
		Player to = player("to");
		Card card = skipCard();
		from.addCard(card);

		helper(unusedInput()).stealRandomCard(from, to);

		assertTrue(from.getHand().isEmpty());
		assertSame(card, to.getHand().get(0));
	}

	@Test
	void stealRandomCard_FromManyCards_TransfersOneCard() {
		Player from = player("from");
		Player to = player("to");
		Card card1 = skipCard();
		Card card2 = attackCard();
		Card card3 = skipCard();
		from.addCard(card1);
		from.addCard(card2);
		from.addCard(card3);

		new PlayerInteractionHelper(unusedInput(), new Random(0)).stealRandomCard(from, to);

		assertEquals(2, from.getHand().size());
		assertEquals(1, to.getHand().size());
		assertTrue(card1 == to.getHand().get(0)
				|| card2 == to.getHand().get(0)
				|| card3 == to.getHand().get(0));
	}

	@Test
	void stealNamedCard_TypeNotPresent_NoTransfer() {
		Player from = player("from");
		Player to = player("to");
		from.addCard(attackCard());

		helper(unusedInput()).stealNamedCard(from, to, CardType.SKIP);

		assertEquals(1, from.getHand().size());
		assertTrue(to.getHand().isEmpty());
	}

	@Test
	void stealNamedCard_OneOfType_TransfersThatCard() {
		Player from = player("from");
		Player to = player("to");
		Card skip = skipCard();
		from.addCard(skip);

		helper(unusedInput()).stealNamedCard(from, to, CardType.SKIP);

		assertTrue(from.getHand().isEmpty());
		assertSame(skip, to.getHand().get(0));
	}

	@Test
	void stealNamedCard_MultipleOfType_TransfersOne() {
		Player from = player("from");
		Player to = player("to");
		from.addCard(skipCard());
		from.addCard(skipCard());

		helper(unusedInput()).stealNamedCard(from, to, CardType.SKIP);

		assertEquals(1, from.getHand().size());
		assertEquals(1, to.getHand().size());
	}

	@Test
	void giveCard_SelectionEmpty_NoTransfer() {
		Player from = player("from");
		Player to = player("to");
		from.addCard(skipCard());

		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(mockInput.promptCardSelection(from)).andReturn(Collections.emptyList());
		EasyMock.replay(mockInput);

		new PlayerInteractionHelper(mockInput, new Random()).giveCard(from, to);

		assertEquals(1, from.getHand().size());
		assertTrue(to.getHand().isEmpty());
		EasyMock.verify(mockInput);
	}

	@Test
	void giveCard_SelectionOneCard_TransfersThatCard() {
		Player from = player("from");
		Player to = player("to");
		Card card = skipCard();
		from.addCard(card);

		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(mockInput.promptCardSelection(from)).andReturn(List.of(card));
		EasyMock.replay(mockInput);

		new PlayerInteractionHelper(mockInput, new Random()).giveCard(from, to);

		assertTrue(from.getHand().isEmpty());
		assertSame(card, to.getHand().get(0));
		EasyMock.verify(mockInput);
	}

	@Test
	void giveCard_SelectedCardNotInHand_NoTransfer() {
		Player from = player("from");
		Player to = player("to");
		Card cardInHand = skipCard();
		Card cardNotInHand = attackCard();
		from.addCard(cardInHand);

		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		EasyMock.expect(mockInput.promptCardSelection(from)).andReturn(List.of(cardNotInHand));
		EasyMock.replay(mockInput);

		new PlayerInteractionHelper(mockInput, new Random()).giveCard(from, to);

		assertEquals(1, from.getHand().size());
		assertTrue(to.getHand().isEmpty());
		EasyMock.verify(mockInput);
	}

	@Test
	void pickTarget_ReturnsCandidateFromInput() {
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		Player mockPlayer = EasyMock.createMock(Player.class);
		Random mockRandom = EasyMock.createMock(Random.class);
		List<Player> candidates = List.of(mockPlayer);

		EasyMock.expect(mockRandom.nextLong()).andReturn(RANDOM_SEED_GENERATOR_NUMBER);
		EasyMock.expect(mockInput.promptTargetSelection(candidates)).andReturn(mockPlayer);
		EasyMock.replay(mockInput, mockPlayer, mockRandom);

		PlayerInteractionHelper helper = new PlayerInteractionHelper(mockInput, mockRandom);
		assertEquals(mockPlayer, helper.pickTarget(candidates));

		EasyMock.verify(mockInput, mockPlayer);
	}

	@Test
	void pickCardType_ReturnsCardFromInput() {
		IPlayerInput mockInput = EasyMock.createMock(IPlayerInput.class);
		CardType mockCardType = CardType.SKIP;
		Random mockRandom = EasyMock.createMock(Random.class);

		EasyMock.expect(mockRandom.nextLong()).andReturn(RANDOM_SEED_GENERATOR_NUMBER);
		EasyMock.expect(mockInput.promptCardType()).andReturn(mockCardType);
		EasyMock.replay(mockInput, mockRandom);

		PlayerInteractionHelper helper = new PlayerInteractionHelper(mockInput, mockRandom);
		assertEquals(mockCardType, helper.pickCardType());

		EasyMock.verify(mockInput, mockRandom);
	}


}
