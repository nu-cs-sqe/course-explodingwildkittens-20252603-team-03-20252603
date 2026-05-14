package domain.action;

import domain.model.GameState;

public interface CardAction {
	void execute(GameState gameState);
}