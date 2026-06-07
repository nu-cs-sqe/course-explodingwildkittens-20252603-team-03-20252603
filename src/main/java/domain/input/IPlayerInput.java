package domain.input;

import domain.enums.CardType;
import domain.enums.PlayerChoice;
import domain.model.Card;
import domain.model.Player;

import java.util.List;
import java.util.Locale;

public interface IPlayerInput {
	List<Card> promptCardSelection(Player player);

	int promptNumPlayers();

	boolean promptNope(Player player);

	int promptInsertPosition(int deckSize);

	Player promptTargetSelection(List<Player> candidates);

	CardType promptCardType();

	boolean promptRestart();

	PlayerChoice promptPlayerChoice(Player player);

	void showPeekCards(Player player);

	Locale promptLocale();
}
