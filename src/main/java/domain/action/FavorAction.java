package domain.action;

import domain.factory.PlayerInteractionHelper;
import domain.model.GameState;
import domain.model.Player;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class FavorAction implements CardAction {

	private final PlayerInteractionHelper helper;

	public FavorAction(PlayerInteractionHelper helper) {
		this.helper = helper;
	}

	public void execute(GameState gameState) {
		List<Player> others = gameState.getOtherActivePlayers();
		if (others.isEmpty()) {
			throw new IllegalStateException(ResourceBundle.getBundle("labels", Locale.getDefault()).getString("error.no.other.players"));
		}
		Player target = helper.pickTarget(others);
		Player current = gameState.getCurrentPlayer();
		helper.giveCard(target, current);
	}
}
