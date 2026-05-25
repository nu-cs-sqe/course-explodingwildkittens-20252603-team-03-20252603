package domain.action;

import domain.factory.PlayerInteractionHelper;
import domain.model.GameState;
import domain.model.Player;

import java.util.List;

public class TwoCatAction implements CardAction {

	private final PlayerInteractionHelper helper;

	public TwoCatAction(PlayerInteractionHelper helper) {
		this.helper = helper;
	}

	public void execute(GameState gameState) {
		List<Player> others = gameState.getOtherActivePlayers();
		if (others.isEmpty()) {
			throw new IllegalStateException("No other active players to target");
		}
		Player target = helper.pickTarget(others);
		Player current = gameState.getCurrentPlayer();
		helper.stealRandomCard(target, current);
	}
}
