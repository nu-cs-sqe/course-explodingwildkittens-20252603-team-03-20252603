package domain.model;

import domain.enums.CardType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Player {
	private final String id;
	private final String name;
	private final List<Card> hand;
	private final List<Card> peekCards;
	private boolean wasAttacked;
	private boolean isActive;

	public Player(String id, String name) {
		this.id = id;
		this.name = name;
		this.hand = new ArrayList<>();
		this.peekCards = new ArrayList<>();
		this.wasAttacked = false;
		this.isActive = true;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Card> getHand() {
		return Collections.unmodifiableList(hand);
	}

	public List<Card> getPeekCards() {
		return Collections.unmodifiableList(peekCards);
	}

	public void addCard(Card card) {
		hand.add(card);
	}

	public void removeCard(Card card) {
		hand.remove(card);
	}

	public boolean hasCard(CardType type) {
		for (Card card : hand) {
			if (card.isType(type)) {
				return true;
			}
		}
		return false;
	}

	public Optional<Card> removeCardOfType(CardType type) {
		for (Card card : hand) {
			if (card.isType(type)) {
				hand.remove(card);
				return Optional.of(card);
			}
		}
		return Optional.empty();
	}

	public void storePeek(List<Card> cards) {
		peekCards.clear();
		if (cards != null) {
			peekCards.addAll(cards);
		}
	}

	public void clearPeek() {
		peekCards.clear();
	}

	public boolean wasAttacked() {
		return wasAttacked;
	}

	public void setWasAttacked() {
		this.wasAttacked = true;
	}

	public void resetWasAttacked() {
		this.wasAttacked = false;
	}

	public boolean isActive() {
		return isActive;
	}

	public void eliminatePlayer() {
		this.isActive = false;
	}

	public void addCards(List<Card> cards) {
		hand.addAll(cards);
	}

}
