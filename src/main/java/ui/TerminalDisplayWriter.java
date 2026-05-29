package ui;

import domain.enums.CardName;
import domain.enums.CardType;
import domain.model.Card;
import domain.model.GameState;
import domain.model.Player;

import java.io.PrintStream;
import java.util.List;

final class TerminalDisplayWriter {

	private final PrintStream output;

	TerminalDisplayWriter(PrintStream output) {
		this.output = output;
	}

	void showGameState(GameState gameState) {
		printHeader();
		printDeckSize(gameState.getDeckSize());
		printDiscardPileSize(gameState.getDiscardPileSize());
		printActivePlayerCount(gameState.activePlayerCount());
		printTurnsRemaining(gameState.turnState().turnsRemaining());
	}

	private void printHeader() {
		output.println(ViewMessages.format("view.game.state.header"));
	}

	private void printDeckSize(int deckSize) {
		output.println(ViewMessages.format("view.deck.size", deckSize));
	}

	private void printDiscardPileSize(int discardPileSize) {
		output.println(ViewMessages.format("view.discard.size", discardPileSize));
	}

	private void printActivePlayerCount(int activePlayerCount) {
		output.println(ViewMessages.format("view.active.players", activePlayerCount));
	}

	private void printTurnsRemaining(int turnsRemaining) {
		output.println(ViewMessages.format("view.turns.remaining", turnsRemaining));
	}

	void showPlayerHand(Player player) {
		output.println(ViewMessages.format("view.hand.header", player.getName()));
		printNumberedCards(player.getHand());
		printPeekCards(player);
	}

	private void printPeekCards(Player player) {
		List<Card> peekCards = player.getPeekCards();
		if (peekCards.isEmpty()) {
			return;
		}
		output.println(ViewMessages.format("view.peek.cards"));
		printNumberedCards(peekCards);
	}

	void printNumberedCards(List<Card> cards) {
		for (int index = 0; index < cards.size(); index++) {
			int displayNumber = index + 1;
			String label = formatCard(cards.get(index));
			output.println(ViewMessages.format("view.card.list.item", displayNumber, label));
		}
	}

	private String formatCard(Card card) {
		for (CardName cardName : CardName.values()) {
			if (card.isName(cardName)) {
				return cardName.name().replace('_', ' ');
			}
		}
		return ViewMessages.format("view.card.unknown");
	}

	void showMessage(String message) {
		output.println(message);
	}

	void showWinner(Player player) {
		output.println(ViewMessages.format("view.winner", player.getName()));
	}

	void showCurrentPlayer(Player player) {
		output.println(ViewMessages.format("view.current.player.turn", player.getName()));
	}

	void printNumberedPlayers(List<Player> candidates) {
		for (int index = 0; index < candidates.size(); index++) {
			int displayNumber = index + 1;
			output.println(ViewMessages.format("view.card.list.item", displayNumber,
				candidates.get(index).getName()));
		}
	}

	void printNumberedCardTypes(CardType[] types) {
		for (int index = 0; index < types.length; index++) {
			int displayNumber = index + 1;
			String label = types[index].name().replace('_', ' ');
			output.println(ViewMessages.format("view.card.list.item", displayNumber, label));
		}
	}

	void printPlayerChoiceMenu() {
		output.println(ViewMessages.format("view.menu.play.card", 1));
		output.println(ViewMessages.format("view.menu.done.playing"));
		output.print(ViewMessages.format("view.prompt.choose"));
	}

	void printInvalidSelection() {
		output.println(ViewMessages.format("view.invalid.selection"));
	}
}
