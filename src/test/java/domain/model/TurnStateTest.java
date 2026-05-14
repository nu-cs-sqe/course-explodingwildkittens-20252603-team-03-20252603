package domain.model;

import domain.action.NoAction;
import domain.enums.CardName;
import domain.enums.CardType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TurnStateTest {

	private static Card anyCard() {
		return new Card(CardType.SKIP, CardName.SKIP, new NoAction());
	}
	private static final int NUM_TURNS_PER_PLAYER = 3;

	@Test
	void constructor_InitializesDefaultTurnCount() {
		TurnState turnState = new TurnState();
		assertEquals(1, turnState.turnsRemaining());
	}

	@Test
	void constructor_InitializesSkipDrawFalse() {
		TurnState turnState = new TurnState();
		assertFalse(turnState.shouldSkipDraw());
	}

	@Test
	void constructor_InitializesNotAttacking() {
		TurnState turnState = new TurnState();
		assertFalse(turnState.isAttacking());
	}

	@Test
	void constructor_InitializesNopeCountZero() {
		TurnState turnState = new TurnState();
		assertEquals(0, turnState.nopeCount());
	}

	@Test
	void constructor_InitializesNoPendingAction() {
		TurnState turnState = new TurnState();
		assertEquals(Optional.empty(), turnState.pendingAction());
	}

	@Test
	void enableSkipDraw_WhenFalse_SetsSkipDrawTrue() {
		TurnState turnState = new TurnState();
		turnState.enableSkipDraw();
		assertTrue(turnState.shouldSkipDraw());
	}

	@Test
	void enableSkipDraw_AlreadyTrue_RemainsTrue() {
		TurnState turnState = new TurnState();
		turnState.enableSkipDraw();
		turnState.enableSkipDraw();
		assertTrue(turnState.shouldSkipDraw());
	}

	@Test
	void startAttack_TwoTurns_SetsIsAttackingAndTurns() {
		TurnState turnState = new TurnState();
		turnState.startAttack(2);
		assertTrue(turnState.isAttacking());
		assertEquals(2, turnState.turnsRemaining());
	}

	@Test
	void startAttack_OneTurn_SetsIsAttackingAndOneTurn() {
		TurnState turnState = new TurnState();
		turnState.startAttack(1);
		assertTrue(turnState.isAttacking());
		assertEquals(1, turnState.turnsRemaining());
	}

	@Test
	void startAttack_ZeroTurns_SetsIsAttackingZeroTurns() {
		TurnState turnState = new TurnState();
		turnState.startAttack(0);
		assertTrue(turnState.isAttacking());
		assertEquals(0, turnState.turnsRemaining());
	}

	@Test
	void setPendingAction_NonNullCard_StoresPendingAction() {
		TurnState turnState = new TurnState();
		Card card = anyCard();
		turnState.setPendingAction(card);
		assertSame(card, turnState.pendingAction().orElseThrow());
	}

	@Test
	void setPendingAction_ReplacesExisting_StoresNewCard() {
		TurnState turnState = new TurnState();
		Card second = anyCard();
		turnState.setPendingAction(anyCard());
		turnState.setPendingAction(second);
		assertSame(second, turnState.pendingAction().orElseThrow());
	}

	@Test
	void clearPendingAction_WhenSet_ClearsToEmpty() {
		TurnState turnState = new TurnState();
		turnState.setPendingAction(anyCard());
		turnState.clearPendingAction();
		assertEquals(Optional.empty(), turnState.pendingAction());
	}

	@Test
	void clearPendingAction_AlreadyEmpty_NoThrow() {
		TurnState turnState = new TurnState();
		assertDoesNotThrow(turnState::clearPendingAction);
		assertEquals(Optional.empty(), turnState.pendingAction());
	}

	@Test
	void incrementNopeCount_FromZero_BecomesOne() {
		TurnState turnState = new TurnState();
		turnState.incrementNopeCount();
		assertEquals(1, turnState.nopeCount());
	}

	@Test
	void incrementNopeCount_FromPositive_Increments() {
		TurnState turnState = new TurnState();
		turnState.incrementNopeCount();
		turnState.incrementNopeCount();
		assertEquals(2, turnState.nopeCount());
	}

	@Test
	void decrementTurns_FromOne_BecomesZero() {
		TurnState turnState = new TurnState();
		turnState.decrementTurns();
		assertEquals(0, turnState.turnsRemaining());
	}

	@Test
	void decrementTurns_FromTwo_BecomesOne() {
		TurnState turnState = new TurnState();
		turnState.startAttack(2);
		turnState.decrementTurns();
		assertEquals(1, turnState.turnsRemaining());
	}

	@Test
	void decrementTurns_FromZero_StaysZero() {
		TurnState turnState = new TurnState();
		turnState.startAttack(0);
		turnState.decrementTurns();
		assertEquals(0, turnState.turnsRemaining());
	}

	@Test
	void reset_FromDirtyState_RestoresDefaults() {
		TurnState turnState = new TurnState();
		turnState.enableSkipDraw();
		turnState.startAttack(NUM_TURNS_PER_PLAYER);
		turnState.incrementNopeCount();
		turnState.setPendingAction(anyCard());
		turnState.reset();
		assertEquals(1, turnState.turnsRemaining());
		assertFalse(turnState.shouldSkipDraw());
		assertFalse(turnState.isAttacking());
		assertEquals(0, turnState.nopeCount());
		assertEquals(Optional.empty(), turnState.pendingAction());
	}

	@Test
	void reset_FromDefaultState_RemainsDefault() {
		TurnState turnState = new TurnState();
		turnState.reset();
		assertEquals(1, turnState.turnsRemaining());
		assertFalse(turnState.shouldSkipDraw());
		assertFalse(turnState.isAttacking());
		assertEquals(0, turnState.nopeCount());
		assertEquals(Optional.empty(), turnState.pendingAction());
	}
}
