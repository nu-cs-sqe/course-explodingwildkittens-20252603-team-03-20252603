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

	public GameState(List<Player> players, Deck deck) {
		this.status = GameStatus.ACTIVE;
		this.deck = new Deck(deck);
		this.turnState = new TurnState();
	}

	public boolean isActive() {
		return status == GameStatus.ACTIVE;
	}

	public void endGame() {
		status = GameStatus.ENDED;
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
		if (deck.isEmpty()) {
			throw new IllegalStateException("Cannot draw from an empty deck");
		}
		return deck.drawTop();
	}

	public boolean isDeckEmpty() {
		return deck.isEmpty();
	}

	public void insertIntoDeck(Card card, int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Error: index is less than 0");
		}
		deck.insertAt(card, index);
	}

	public void shuffleDeck() {
		deck.shuffle();
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
		return deck.size();
	}

	public void insertPendingCardAt(int position) {
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public TurnState turnState() {
		return turnState;
	}
}