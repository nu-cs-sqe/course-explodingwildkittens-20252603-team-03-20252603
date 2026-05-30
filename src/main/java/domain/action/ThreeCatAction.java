package domain.action;

import domain.enums.CardType;
import domain.factory.PlayerInteractionHelper;
import domain.model.GameState;
import domain.model.Player;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ThreeCatAction implements CardAction {

	private final PlayerInteractionHelper helper;

	public ThreeCatAction(PlayerInteractionHelper helper) {
		this.helper = helper;
	}

	public void execute(GameState gameState) {
		List<Player> others = gameState.getOtherActivePlayers();
		if (others.isEmpty()) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IllegalStateException(bundle.getString("error.no.other.players"));
		}
		Player target = helper.pickTarget(others);
		Player current = gameState.getCurrentPlayer();
		CardType type = helper.pickCardType();
		helper.stealNamedCard(target, current, type);
	}
}
