package ui;

import domain.factory.ComboValidator;
import domain.factory.PlayerInteractionHelper;

import java.util.Random;

public class Main {
	static void runGame(GameController controller) {
		controller.startGame();
		while (controller.isGameActive()) {
			controller.playATurn();
		}
	}

	public static void main(String[] args) {
		GameView view = new GameView();
		ViewMessages.setLocale(view.promptLocale());
		PlayerInteractionHelper helper = new PlayerInteractionHelper(view, new Random());
		ComboValidator comboValidator = new ComboValidator(helper);
		GameController controller = new GameController(view, view, comboValidator);
		runGame(controller);
	}
}