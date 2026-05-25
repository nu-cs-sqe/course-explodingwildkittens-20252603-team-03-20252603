package domain.input;

import domain.enums.CardType;
import domain.enums.PlayerChoice;
import domain.model.Card;
import domain.model.Player;

import java.util.List;

public interface IPlayerInput {
	List<Card> promptCardSelection(Player player);

	int promptNumPlayers();

	boolean promptNope(List<Player> players);

	int promptInsertPosition(int deckSize);

	Player promptTargetSelection(List<Player> candidates);

	CardType promptCardType();

	boolean promptRestart();

	PlayerChoice  promptPlayerChoice();
}