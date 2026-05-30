package domain.model;

import domain.enums.CardName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class Deck {
	private Random random;
	private List<Card> cards;

	public Deck(List<Card> cards) {
		this.cards = new ArrayList<>(cards);
		this.random = new Random();
	}

	@SuppressFBWarnings("EI_EXPOSE_REP2")
	Deck(List<Card> cards, Random random) {
		this.cards = new ArrayList<>(cards);
		this.random = random;
	}

	// Defensive copy constructor: prevents external mutation of internal deck state
	Deck(Deck other) {
		this.cards = new ArrayList<>(other.cards);
		this.random = new Random();
	}

	public void shuffle() {
		Collections.shuffle(this.cards, this.random);
	}

	public Card drawTop() throws IllegalStateException {
		if (this.cards.isEmpty()) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IllegalStateException(bundle.getString("error.draw.empty.deck"));
		}
		return cards.remove(0);
	}

	public void insertAt(Card card, int index) throws IndexOutOfBoundsException {
		if (index < 0 || index > cards.size()) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IndexOutOfBoundsException(
				MessageFormat.format(bundle.getString("error.index.out.of.bounds"), index));
		}
		cards.add(index, card);
	}

	public int size() {
		return cards.size();
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public List<Card> peekTop(int n) {
		if (n > cards.size()) {
			ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
			throw new IllegalArgumentException(bundle.getString("error.peek.exceeds.deck"));
		}
		return Collections.unmodifiableList(cards.subList(0, n));
	}

	public int countCardsByName(CardName cardName) {
		int count = 0;
		for (Card card : cards) {
			if (card.isName(cardName)) {
				count++;
			}
		}
		return count;
	}

	public List<Card> dealCards(int count) {
		ResourceBundle bundle = ResourceBundle.getBundle("labels", Locale.getDefault());
		if (this.cards.isEmpty()) {
			throw new IllegalStateException(bundle.getString("error.draw.empty.deck"));
		}
		if (this.cards.size() < count) {
			throw new IllegalStateException(bundle.getString("error.deal.exceeds.deck"));
		}

		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			Card card = this.cards.remove(0);
			cards.add(card);
		}
		return cards;
	}

	public void addToDeck(List<Card> cards) {
		this.cards.addAll(cards);
	}
}