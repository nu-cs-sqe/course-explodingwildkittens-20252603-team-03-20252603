package domain.action;

import domain.model.GameState;
import domain.model.Player;
import domain.model.TurnState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class AttackActionTest {

	@Test
	void execute_ValidGameState_StartsAttack() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		TurnState mockTurnState = EasyMock.createMock(TurnState.class);
		Player mockPlayer = EasyMock.createMock(Player.class);

		EasyMock.expect(mockGameState.turnState()).andReturn(mockTurnState);
		EasyMock.expect(mockGameState.getNextPlayer()).andReturn(mockPlayer);

		mockPlayer.setWasAttacked();
		EasyMock.expectLastCall().once();
		mockTurnState.startAttack();
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockGameState, mockTurnState, mockPlayer);

		new AttackAction().execute(mockGameState);

		EasyMock.verify(mockGameState, mockTurnState,  mockPlayer);
	}
}
