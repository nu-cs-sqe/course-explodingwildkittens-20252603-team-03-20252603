package domain.action;

import domain.input.IPlayerInput;
import domain.model.GameState;

public class DefuseAction implements CardAction {

    private final IPlayerInput input;

    public DefuseAction(IPlayerInput input) {
        this.input = input;
    }

    public void execute(GameState gameState) {
        int position = input.promptInsertPosition(gameState.getDeckSize());
        gameState.insertPendingCardAt(position);
    }
}
