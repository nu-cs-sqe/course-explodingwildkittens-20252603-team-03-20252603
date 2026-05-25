package domain.model;

import domain.action.NoAction;
import domain.action.SkipAction;
import domain.enums.CardName;
import domain.enums.CardType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

	private static Card defuseCard() {
		return new Card(CardType.DEFUSE, CardName.DEFUSE, new NoAction());
	}

	private static Card skipCard() {
		return new Card(CardType.SKIP, CardName.SKIP, new SkipAction());
	}

	@Test
	void constructor_SetsIdAndName() {
		Player player = new Player("p1", "Alice");
		assertEquals("p1", player.getId());
		assertEquals("Alice", player.getName());
	}

	@Test
	void constructor_InitializesEmptyHand() {
		Player player = new Player("p1", "Alice");
		assertTrue(player.getHand().isEmpty());
	}

	@Test
	void constructor_InitializesEmptyPeekBuffer() {
		Player player = new Player("p1", "Alice");
		assertTrue(player.getPeekCards().isEmpty());
	}

	@Test
	void addCard_EmptyHand_AddsCard() {
		Player player = new Player("p1", "Alice");
		Card card = defuseCard();
		player.addCard(card);
		assertEquals(1, player.getHand().size());
		assertSame(card, player.getHand().get(0));
	}

	@Test
	void addCard_NonEmptyHand_AppendsCard() {
		Player player = new Player("p1", "Alice");
		Card first = defuseCard();
		Card second = skipCard();
		player.addCard(first);
		player.addCard(second);
		assertEquals(2, player.getHand().size());
		assertSame(first, player.getHand().get(0));
		assertSame(second, player.getHand().get(1));
	}

	@Test
	void removeCard_CardInHand_RemovesCard() {
		Player player = new Player("p1", "Alice");
		Card card = defuseCard();
		player.addCard(card);
		player.removeCard(card);
		assertTrue(player.getHand().isEmpty());
	}

	@Test
	void removeCard_CardNotInHand_Unchanged() {
		Player player = new Player("p1", "Alice");
		Card inHand = defuseCard();
		Card other = new Card(CardType.DEFUSE, CardName.DEFUSE, new NoAction());
		player.addCard(inHand);
		player.removeCard(other);
		assertEquals(1, player.getHand().size());
		assertSame(inHand, player.getHand().get(0));
	}

	@Test
	void removeCard_EmptyHand_DoesNothing() {
		Player player = new Player("p1", "Alice");
		assertDoesNotThrow(() -> player.removeCard(defuseCard()));
		assertTrue(player.getHand().isEmpty());
	}

	@Test
	void hasCard_EmptyHand_ReturnsFalse() {
		Player player = new Player("p1", "Alice");
		assertFalse(player.hasCard(CardType.DEFUSE));
	}

	@Test
	void hasCard_MatchingType_ReturnsTrue() {
		Player player = new Player("p1", "Alice");
		player.addCard(defuseCard());
		assertTrue(player.hasCard(CardType.DEFUSE));
	}

	@Test
	void hasCard_NoMatchingType_ReturnsFalse() {
		Player player = new Player("p1", "Alice");
		player.addCard(skipCard());
		assertFalse(player.hasCard(CardType.DEFUSE));
	}

	@Test
	void removeCardOfType_NoMatch_ReturnsEmpty() {
		Player player = new Player("p1", "Alice");
		player.addCard(skipCard());
		assertEquals(Optional.empty(), player.removeCardOfType(CardType.DEFUSE));
		assertEquals(1, player.getHand().size());
	}

	@Test
	void removeCardOfType_OneMatch_RemovesAndReturnsThatCard() {
		Player player = new Player("p1", "Alice");
		Card card = defuseCard();
		player.addCard(card);
		assertSame(card, player.removeCardOfType(CardType.DEFUSE).orElseThrow());
		assertTrue(player.getHand().isEmpty());
	}

	@Test
	void removeCardOfType_MultipleMatches_RemovesFirstInHandOrder() {
		Player player = new Player("p1", "Alice");
		Card skip = skipCard();
		Card first = defuseCard();
		Card second = new Card(CardType.DEFUSE, CardName.DEFUSE, new NoAction());
		player.addCard(skip);
		player.addCard(first);
		player.addCard(second);
		assertSame(first, player.removeCardOfType(CardType.DEFUSE).orElseThrow());
		assertEquals(2, player.getHand().size());
		assertSame(skip, player.getHand().get(0));
		assertSame(second, player.getHand().get(1));
	}

	@Test
	void storePeek_Null_ClearsPeek() {
		Player player = new Player("p1", "Alice");
		player.storePeek(List.of(defuseCard()));
		player.storePeek(null);
		assertTrue(player.getPeekCards().isEmpty());
	}

	@Test
	void storePeek_NonNull_ReplacesPeek() {
		Player player = new Player("p1", "Alice");
		Card a = defuseCard();
		Card b = skipCard();
		player.storePeek(Arrays.asList(a, b));
		assertEquals(2, player.getPeekCards().size());
		assertSame(a, player.getPeekCards().get(0));
		assertSame(b, player.getPeekCards().get(1));
	}

	@Test
	void storePeek_SecondCall_ReplacesFirst() {
		Player player = new Player("p1", "Alice");
		Card firstBatch = defuseCard();
		Card secondBatch = skipCard();
		player.storePeek(List.of(firstBatch));
		player.storePeek(List.of(secondBatch));
		assertEquals(1, player.getPeekCards().size());
		assertSame(secondBatch, player.getPeekCards().get(0));
	}

	@Test
	void clearPeek_NonEmpty_ClearsPeek() {
		Player player = new Player("p1", "Alice");
		player.storePeek(List.of(defuseCard()));
		player.clearPeek();
		assertTrue(player.getPeekCards().isEmpty());
	}

	@Test
	void clearPeek_AlreadyEmpty_NoThrow() {
		Player player = new Player("p1", "Alice");
		assertDoesNotThrow(player::clearPeek);
		assertTrue(player.getPeekCards().isEmpty());
	}

	@Test
	void getHand_IsUnmodifiable() {
		Player player = new Player("p1", "Alice");
		assertThrows(UnsupportedOperationException.class, () -> player.getHand().add(defuseCard()));
	}

	@Test
	void getPeekCards_IsUnmodifiable() {
		Player player = new Player("p1", "Alice");
		assertThrows(UnsupportedOperationException.class, () -> player.getPeekCards().add(defuseCard()));
	}

	@Test
	void wasAttacked_InitialState_ReturnsFalse() {
		Player player = new Player("p1", "Alice");
		assertFalse(player.wasAttacked());
	}

	@Test
	void setWasAttacked_WhenFalse_SetsToTrue() {
		Player player = new Player("p1", "Alice");
		player.setWasAttacked();
		assertTrue(player.wasAttacked());
	}

	@Test
	void setWasAttacked_WhenAlreadyTrue_RemainsTrue() {
		Player player = new Player("p1", "Alice");
		player.setWasAttacked();
		player.setWasAttacked();
		assertTrue(player.wasAttacked());
	}

	@Test
	void resetWasAttacked_WhenTrue_SetsToFalse() {
		Player player = new Player("p1", "Alice");
		player.setWasAttacked();
		player.resetWasAttacked();
		assertFalse(player.wasAttacked());
	}

	@Test
	void resetWasAttacked_WhenAlreadyFalse_RemainsAlreadyFalse() {
		Player player = new Player("p1", "Alice");
		player.resetWasAttacked();
		assertFalse(player.wasAttacked());
	}

	@Test
	void isActive_InitialState_ReturnsTrue() {
		Player player = new Player("p1", "Alice");
		assertTrue(player.isActive());
	}

	@Test
	void eliminatePlayer_WhenActive_SetsToFalse() {
		Player player = new Player("p1", "Alice");
		player.eliminatePlayer();
		assertFalse(player.isActive());
	}

	@Test
	void eliminatePlayer_WhenAlreadyEliminated_RemainsInactive() {
		Player player = new Player("p1", "Alice");
		player.eliminatePlayer();
		player.eliminatePlayer();
		assertFalse(player.isActive());
	}
}
