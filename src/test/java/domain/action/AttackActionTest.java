package domain.action;

import domain.model.GameState;
import domain.model.TurnState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class AttackActionTest {

	@Test
	void execute_ValidGameState_StartsAttackWithTwoTurns() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);

		EasyMock.expect(mockGameState.turnState()).andReturn(mockTurnState);
		mockTurnState.startAttack(2);
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockGameState, mockTurnState);

		new AttackAction().execute(mockGameState);

		EasyMock.verify(mockGameState, mockTurnState);
	}
}
