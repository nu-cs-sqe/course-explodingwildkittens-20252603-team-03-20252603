package domain.action;

import domain.model.GameState;
import domain.model.TurnState;

public class SkipAction implements CardAction {

    public void execute(GameState gameState) {
        TurnState turnState = gameState.turnState();
        turnState.enableSkipDraw();
    }
}
