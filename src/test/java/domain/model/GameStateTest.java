package domain.model;

import domain.enums.GameStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStateTest {

	@Test
	public void isActive_StatusEnded_ReturnsFalse() {
		GameState gameState = new GameState(GameStatus.ENDED);
		assertFalse(gameState.isActive());
	}

	@Test
	public void isActive_StatusActive_ReturnsTrue() {
		GameState gameState = new GameState(GameStatus.ACTIVE);
		assertTrue(gameState.isActive());
	}

	@Test
	public void endGame_ActiveGame_SetsStatusEnded() {
		GameState gs = new GameState(GameStatus.ACTIVE);
		gs.endGame();
		assertFalse(gs.isActive());
	}
}
