package domain.model;

import domain.enums.CardType;
import domain.enums.GameStatus;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("UUF_UNUSED_FIELD")
public class GameState {
	private GameStatus status;
	private Queue<Player> activePlayers;
	private List<Player> eliminatedPlayers;
	private Deck deck;
	private List<Card> discardPile;
	private TurnState turnState;

	public boolean isActive() {
		return false;
	}

	public void endGame() {
	}

	public Player getCurrentPlayer() {
		return null;
	}

	public void advancePlayer() {
	}

	public void eliminateCurrentPlayer() {
	}

	public int activePlayerCount() {
		return 0;
	}

	public Card drawFromDeck() {
		return null;
	}

	public boolean isDeckEmpty() {
		return false;
	}

	public void insertIntoDeck(Card card, int index) {
	}

	public void shuffleDeck() {
	}

	public void discardCard(Card card) {
	}

	public void addCardToCurrentPlayer(Card card) {
	}

	public void removeCardFromCurrentPlayer(Card card) {
	}

	public boolean currentPlayerHasCard(CardType type) {
		return false;
	}

	public List<Player> getOtherActivePlayers() {
		return Collections.emptyList();
	}

	public List<Card> peekTopOfDeck(int n) {
		return null;
	}

	public int getDeckSize() {
		return 0;
	}

	public void insertPendingCardAt(int position) {
	}

	public TurnState turnState() {
		return null;
	}
}