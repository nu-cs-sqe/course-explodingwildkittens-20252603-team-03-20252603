package domain.factory;

import domain.enums.CardType;
import domain.input.IPlayerInput;
import domain.model.Player;

public class PlayerInteractionHelper {
    private IPlayerInput input;
    public void stealRandomCard(Player from, Player to) {}
    public void stealNamedCard(Player from, Player to, CardType type) {}
}