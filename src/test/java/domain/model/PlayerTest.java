package domain.model;

import domain.action.DefuseAction;
import domain.enums.CardName;
import domain.enums.CardType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private static Card defuseCard() {
        return new Card(CardType.DEFUSE, CardName.DEFUSE, new DefuseAction());
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
    void getHand_IsUnmodifiable() {
        Player player = new Player("p1", "Alice");
        assertThrows(UnsupportedOperationException.class, () -> player.getHand().add(defuseCard()));
    }

    @Test
    void getPeekCards_IsUnmodifiable() {
        Player player = new Player("p1", "Alice");
        assertThrows(UnsupportedOperationException.class, () -> player.getPeekCards().add(defuseCard()));
    }
}
