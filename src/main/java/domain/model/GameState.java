package domain.model;

import domain.enums.CardType;
import domain.enums.GameStatus;

import java.util.*;

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
		this.discardPile = new ArrayList<>();
		this.eliminatedPlayers = new ArrayList<>();
		this.activePlayers = new LinkedList<>(players);
		this.turnState = new TurnState();
	}

	public boolean isActive() {
		return status == GameStatus.ACTIVE;
	}

	public void endGame() {
		status = GameStatus.ENDED;
	}

	public Player getCurrentPlayer() {
		if (activePlayers.isEmpty()) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IllegalStateException(bundle.getString("error.no.players.left"));
		}
		return activePlayers.peek();
	}

	public void advancePlayer() {
		activePlayers.offer(activePlayers.poll());
	}

	public void eliminateCurrentPlayer() {
		if (activePlayers.size() == 1) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IllegalStateException(bundle.getString("error.eliminate.last.player"));
		}
		Player currentPlayer = getCurrentPlayer();
		currentPlayer.eliminatePlayer();
		eliminatedPlayers.add(activePlayers.poll());
	}

	public int activePlayerCount() {
		return activePlayers.size();
	}

	public List<Player> getEliminatedPlayers() {
		return Collections.unmodifiableList(eliminatedPlayers);
	}

	public Card drawFromDeck() {
		if (deck.isEmpty()) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IllegalStateException(bundle.getString("error.draw.empty.deck"));
		}
		return deck.drawTop();
	}

	public boolean isDeckEmpty() {
		return deck.isEmpty();
	}

	public void insertIntoDeck(Card card, int index) {
		if (index < 0) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IndexOutOfBoundsException(bundle.getString("error.index.less.than.zero"));
		}
		deck.insertAt(card, index);
	}

	public void shuffleDeck() {
		deck.shuffle();
	}

	public void discardCard(Card card) {
		if (card == null) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IllegalArgumentException(bundle.getString("error.card.null"));
		}
		discardPile.add(card);
	}

	public int getDiscardPileSize() {
		return discardPile.size();
	}

	public List<Card> getDiscardPile() {
		return Collections.unmodifiableList(discardPile);
	}

	public void addCardToCurrentPlayer(Card card) {
		getCurrentPlayer().addCard(card);
	}

	public void removeCardFromCurrentPlayer(Card card) {
		getCurrentPlayer().removeCard(card);
	}

	public boolean currentPlayerHasCard(CardType type) {
		return getCurrentPlayer().hasCard(type);
	}

	public Player getNextPlayer() {
		if (activePlayers.size() < 2) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IllegalStateException(bundle.getString("error.no.next.player"));
		}
		Iterator<Player> it = activePlayers.iterator();
		it.next();
		return it.next();
	}

	public List<Player> getOtherActivePlayers() {
		List<Player> others = new ArrayList<>(activePlayers);
		others.remove(getCurrentPlayer());
		return Collections.unmodifiableList(others);
	}

	public List<Card> peekTopOfDeck(int n) {
		return deck.peekTop(n);
	}

	public int getDeckSize() {
		return deck.size();
	}

	public void insertPendingCardAt(int position) {
		if (turnState.pendingAction().isEmpty()) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IllegalStateException(bundle.getString("error.no.pending.action"));
		}
		insertIntoDeck(turnState.pendingAction().get(), position);
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public TurnState turnState() {
		return turnState;
	}
}