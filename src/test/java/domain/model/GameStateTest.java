package domain.model;

import domain.action.SkipAction;
import domain.enums.CardName;
import domain.enums.CardType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

	private List<Player> twoPlayers() {
		return List.of(new Player("p1", "Caroline"), new Player("p2", "Mercy"));
	}

	private Deck emptyDeck() {
		return new Deck(new ArrayList<>());
	}

	private Deck nonEmptyDeck() {
		return new Deck(List.of(new Card(CardType.SKIP, CardName.SKIP, new SkipAction())));
	}

	@Test
	public void isActive_StatusActive_ReturnsTrue() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		assertTrue(gs.isActive());
	}

	@Test
	public void isActive_StatusEnded_ReturnsFalse() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		gs.endGame();
		assertFalse(gs.isActive());
	}

	@Test
	public void endGame_ActiveGame_SetsStatusEnded() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		gs.endGame();
		assertFalse(gs.isActive());
	}

	@Test
	public void endGame_AlreadyEnded_RemainsEnded() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		gs.endGame();
		gs.endGame();
		assertFalse(gs.isActive());
	}

	@Test
	public void isDeckEmpty_EmptyDeck_ReturnsTrue() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		assertTrue(gs.isDeckEmpty());
	}

	@Test
	public void isDeckEmpty_NonEmptyDeck_ReturnsFalse() {
		GameState gs = new GameState(twoPlayers(), nonEmptyDeck());
		assertFalse(gs.isDeckEmpty());
	}

	@Test
	public void getDeckSize_EmptyDeck_ReturnsZero() {
		GameState gs = new GameState(twoPlayers(), emptyDeck());
		assertEquals(0, gs.getDeckSize());
	}

	@Test
	public void getDeckSize_OneCard_ReturnsOne() {
		GameState gs = new GameState(twoPlayers(), nonEmptyDeck());
		assertEquals(1, gs.getDeckSize());
	}
}