package domain.action;

import domain.factory.PlayerInteractionHelper;
import domain.model.GameState;
import domain.model.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FavorActionTest {

	private static Player player(String id) {
		return new Player(id, id);
	}

	@Test
	void execute_NoOtherActivePlayers_ThrowsIllegalStateException() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(Collections.emptyList());
		EasyMock.replay(mockGameState, mockHelper);

		assertThrows(IllegalStateException.class, () -> new FavorAction(mockHelper).execute(mockGameState));
		EasyMock.verify(mockGameState, mockHelper);
	}

	@Test
	void execute_OneOtherPlayerEmptyHand_NoTransfer() {
		Player current = player("current");
		Player target = player("target");
		List<Player> others = List.of(target);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(others);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(current);
		EasyMock.expect(mockHelper.pickTarget(others)).andReturn(target);
		mockHelper.giveCard(target, current);
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockHelper);

		new FavorAction(mockHelper).execute(mockGameState);

		EasyMock.verify(mockGameState, mockHelper);
	}

	@Test
	void execute_OneOtherPlayerOneCard_TransfersThatCard() {
		Player current = player("current");
		Player target = player("target");
		List<Player> others = List.of(target);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(others);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(current);
		EasyMock.expect(mockHelper.pickTarget(others)).andReturn(target);
		mockHelper.giveCard(target, current);
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockHelper);

		new FavorAction(mockHelper).execute(mockGameState);

		EasyMock.verify(mockGameState, mockHelper);
	}

	@Test
	void execute_MultipleOtherPlayers_TransfersFromSelectedTarget() {
		Player current = player("current");
		Player target1 = player("target1");
		Player target2 = player("target2");
		List<Player> others = List.of(target1, target2);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(others);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(current);
		EasyMock.expect(mockHelper.pickTarget(others)).andReturn(target1);
		mockHelper.giveCard(target1, current);
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockHelper);

		new FavorAction(mockHelper).execute(mockGameState);

		EasyMock.verify(mockGameState, mockHelper);
	}
}
