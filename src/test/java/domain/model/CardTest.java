package domain.model;

import domain.action.CardAction;
import domain.action.NoAction;
import domain.enums.CardName;
import domain.enums.CardType;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

	@Test
	void isType_SameType_ReturnsTrue() {
		NoAction defuseAction = new NoAction();
		Card card = new Card(CardType.DEFUSE, CardName.DEFUSE, defuseAction);
		assertTrue(card.isType(CardType.DEFUSE));
	}

	@Test
	void isType_DifferentType_ReturnsFalse() {
		NoAction defuseAction = new NoAction();
		Card card = new Card(CardType.DEFUSE, CardName.DEFUSE, defuseAction);
		assertFalse(card.isType(CardType.ATTACK));
	}

	@Test
	void execute_CallsCardAction() {

		GameState mockGameState = EasyMock.createMock(GameState.class);
		CardAction mockCardAction = EasyMock.createMock(CardAction.class);

		mockCardAction.execute(mockGameState);
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockCardAction, mockGameState);

		Card card = new Card(CardType.SKIP, CardName.SKIP, mockCardAction);
		card.execute(mockGameState);

		EasyMock.verify(mockCardAction, mockGameState);
	}
}
