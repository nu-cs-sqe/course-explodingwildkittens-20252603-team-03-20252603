package domain.input;

import domain.model.Card;
import domain.model.Player;

import java.util.List;

public interface IPlayerInput {
    List<Card> promptCardSelection(Player player);
    int promptNumPlayers();
    boolean promptNope(List<Player> players);
    int promptInsertPosition(int deckSize);
}