package domain.action;

import domain.factory.PlayerInteractionHelper;
import domain.model.GameState;
import domain.model.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TwoCatActionTest {

	private static Player player(String id) {
		return new Player(id, id);
	}

	@Test
	void execute_NoOtherActivePlayers_ThrowsIllegalStateException() {
		GameState mockGameState = EasyMock.createMock(GameState.class);
		PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(Collections.emptyList());
		EasyMock.replay(mockGameState, mockHelper);

		assertThrows(IllegalStateException.class, () -> new TwoCatAction(mockHelper).execute(mockGameState));
		EasyMock.verify(mockGameState, mockHelper);
	}

	@Test
	void execute_TargetHasNoCards_NoTransfer() {
		Player current = player("current");
		Player target = player("target");
		List<Player> others = List.of(target);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(others);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(current);
		EasyMock.expect(mockHelper.pickTarget(others)).andReturn(target);
		mockHelper.stealRandomCard(target, current);
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockHelper);

		new TwoCatAction(mockHelper).execute(mockGameState);

		EasyMock.verify(mockGameState, mockHelper);
	}

	@Test
	void execute_TargetHasOneCard_StealsThatCard() {
		Player current = player("current");
		Player target = player("target");
		List<Player> others = List.of(target);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(others);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(current);
		EasyMock.expect(mockHelper.pickTarget(others)).andReturn(target);
		mockHelper.stealRandomCard(target, current);
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockHelper);

		new TwoCatAction(mockHelper).execute(mockGameState);

		EasyMock.verify(mockGameState, mockHelper);
	}

	@Test
	void execute_TargetHasMultipleCards_StealsOneCard() {
		Player current = player("current");
		Player target = player("target");
		List<Player> others = List.of(target);
		GameState mockGameState = EasyMock.createMock(GameState.class);
		PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

		EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(others);
		EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(current);
		EasyMock.expect(mockHelper.pickTarget(others)).andReturn(target);
		mockHelper.stealRandomCard(target, current);
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameState, mockHelper);

		new TwoCatAction(mockHelper).execute(mockGameState);

		EasyMock.verify(mockGameState, mockHelper);
	}
}
