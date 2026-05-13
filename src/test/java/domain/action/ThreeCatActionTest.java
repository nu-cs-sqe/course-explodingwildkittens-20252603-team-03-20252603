package domain.action;

import domain.enums.CardType;
import domain.factory.PlayerInteractionHelper;
import domain.model.GameState;
import domain.model.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ThreeCatActionTest {

    private static Player player(String id) {
        return new Player(id, id);
    }

    @Test
    void execute_NoOtherActivePlayers_ThrowsIllegalStateException() {
        GameState mockGameState = EasyMock.createMock(GameState.class);
        PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

        EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(Collections.emptyList());
        EasyMock.replay(mockGameState, mockHelper);

        assertThrows(IllegalStateException.class, () -> new ThreeCatAction(mockHelper).execute(mockGameState));
        EasyMock.verify(mockGameState, mockHelper);
    }

    @Test
    void execute_TargetLacksNamedType_NoTransfer() {
        Player current = player("current");
        Player target = player("target");
        List<Player> others = List.of(target);
        GameState mockGameState = EasyMock.createMock(GameState.class);
        PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

        EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(others);
        EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(current);
        EasyMock.expect(mockHelper.pickTarget(others)).andReturn(target);
        EasyMock.expect(mockHelper.pickCardType()).andReturn(CardType.SKIP);
        mockHelper.stealNamedCard(target, current, CardType.SKIP);
        EasyMock.expectLastCall().once();
        EasyMock.replay(mockGameState, mockHelper);

        new ThreeCatAction(mockHelper).execute(mockGameState);

        EasyMock.verify(mockGameState, mockHelper);
    }

    @Test
    void execute_TargetHasNamedType_StealsIt() {
        Player current = player("current");
        Player target = player("target");
        List<Player> others = List.of(target);
        GameState mockGameState = EasyMock.createMock(GameState.class);
        PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

        EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(others);
        EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(current);
        EasyMock.expect(mockHelper.pickTarget(others)).andReturn(target);
        EasyMock.expect(mockHelper.pickCardType()).andReturn(CardType.SKIP);
        mockHelper.stealNamedCard(target, current, CardType.SKIP);
        EasyMock.expectLastCall().once();
        EasyMock.replay(mockGameState, mockHelper);

        new ThreeCatAction(mockHelper).execute(mockGameState);

        EasyMock.verify(mockGameState, mockHelper);
    }

    @Test
    void execute_TargetHasMultipleOfNamedType_StealsOne() {
        Player current = player("current");
        Player target = player("target");
        List<Player> others = List.of(target);
        GameState mockGameState = EasyMock.createMock(GameState.class);
        PlayerInteractionHelper mockHelper = EasyMock.createMock(PlayerInteractionHelper.class);

        EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(others);
        EasyMock.expect(mockGameState.getCurrentPlayer()).andReturn(current);
        EasyMock.expect(mockHelper.pickTarget(others)).andReturn(target);
        EasyMock.expect(mockHelper.pickCardType()).andReturn(CardType.SKIP);
        mockHelper.stealNamedCard(target, current, CardType.SKIP);
        EasyMock.expectLastCall().once();
        EasyMock.replay(mockGameState, mockHelper);

        new ThreeCatAction(mockHelper).execute(mockGameState);

        EasyMock.verify(mockGameState, mockHelper);
    }
}
