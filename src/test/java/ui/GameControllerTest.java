package ui;

import domain.action.CardAction;
import domain.action.NoAction;
import domain.enums.CardName;
import domain.enums.CardType;
import domain.factory.ComboValidator;
import domain.factory.DeckFactory;
import domain.input.IPlayerInput;
import domain.model.Card;
import domain.model.GameState;
import domain.model.TurnState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private GameState mockGameState;
    private IGameDisplay mockDisplay;
    private IPlayerInput mockInput;
    private DeckFactory mockDeckFactory;
    private ComboValidator mockValidator;
    private GameController controller;

    @BeforeEach
    void setUp() {
        mockGameState = EasyMock.createMock(GameState.class);
        mockDisplay = EasyMock.createMock(IGameDisplay.class);
        mockInput = EasyMock.createMock(IPlayerInput.class);
        mockDeckFactory = EasyMock.createMock(DeckFactory.class);
        mockValidator = EasyMock.createMock(ComboValidator.class);
        controller = new GameController(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator);
    }

    private Card defuseCard() {
        return new Card(CardType.DEFUSE, CardName.DEFUSE, new NoAction());
    }

    private Card catCard() {
        return new Card(CardType.CAT_CARD, CardName.TACO_CAT, new NoAction());
    }

    private Card skipCard() {
        return new Card(CardType.SKIP, CardName.SKIP, new NoAction());
    }

    private void expectValidPlaySetup(List<Card> cards, TurnState turnState, CardAction mockAction) {
        EasyMock.expect(mockValidator.isValid(cards)).andReturn(true);
        EasyMock.expect(mockGameState.turnState()).andReturn(turnState).anyTimes();
        for (Card card : cards) {
            mockGameState.removeCardFromCurrentPlayer(card);
            EasyMock.expectLastCall().once();
            mockGameState.discardCard(card);
            EasyMock.expectLastCall().once();
        }
        EasyMock.expect(mockGameState.getOtherActivePlayers()).andReturn(Collections.emptyList());
        EasyMock.expect(mockInput.promptNope(Collections.emptyList())).andReturn(false);
        EasyMock.expect(mockValidator.resolveAction(cards)).andReturn(mockAction);
        mockAction.execute(mockGameState);
        EasyMock.expectLastCall().once();
    }

    @Test
    void playCard_NullList_ThrowsIllegalArgumentException() {
        EasyMock.replay(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator);

        assertThrows(IllegalArgumentException.class, () -> controller.playCard(null));

        EasyMock.verify(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator);
    }

    @Test
    void playCard_EmptyList_ShowsErrorMessage() {
        List<Card> cards = Collections.emptyList();
        EasyMock.expect(mockValidator.isValid(cards)).andReturn(false);
        mockDisplay.showMessage(EasyMock.anyString());
        EasyMock.expectLastCall().once();
        EasyMock.replay(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator);

        controller.playCard(cards);

        EasyMock.verify(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator);
    }

    @Test
    void playCard_SingleDefuse_ShowsErrorMessage() {
        List<Card> cards = List.of(defuseCard());
        EasyMock.expect(mockValidator.isValid(cards)).andReturn(false);
        mockDisplay.showMessage(EasyMock.anyString());
        EasyMock.expectLastCall().once();
        EasyMock.replay(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator);

        controller.playCard(cards);

        EasyMock.verify(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator);
    }

    @Test
    void playCard_SingleCatCard_ShowsErrorMessage() {
        List<Card> cards = List.of(catCard());
        EasyMock.expect(mockValidator.isValid(cards)).andReturn(false);
        mockDisplay.showMessage(EasyMock.anyString());
        EasyMock.expectLastCall().once();
        EasyMock.replay(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator);

        controller.playCard(cards);

        EasyMock.verify(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator);
    }

    @Test
    void playCard_ValidSingleCard_NotNoped_ExecutesAction() {
        List<Card> cards = List.of(skipCard());
        TurnState turnState = new TurnState();
        CardAction mockAction = EasyMock.createMock(CardAction.class);
        expectValidPlaySetup(cards, turnState, mockAction);
        EasyMock.replay(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator, mockAction);

        controller.playCard(cards);

        EasyMock.verify(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator, mockAction);
    }

    @Test
    void playCard_ValidSingleCard_NotNoped_CardsAddedToDiscard() {
        List<Card> cards = List.of(skipCard());
        TurnState turnState = new TurnState();
        CardAction mockAction = EasyMock.createMock(CardAction.class);
        expectValidPlaySetup(cards, turnState, mockAction);
        EasyMock.replay(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator, mockAction);

        controller.playCard(cards);

        EasyMock.verify(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator, mockAction);
    }

    @Test
    void playCard_ValidSingleCard_NotNoped_ClearsPendingAction() {
        List<Card> cards = List.of(skipCard());
        TurnState turnState = new TurnState();
        CardAction mockAction = EasyMock.createMock(CardAction.class);
        expectValidPlaySetup(cards, turnState, mockAction);
        EasyMock.replay(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator, mockAction);

        controller.playCard(cards);

        EasyMock.verify(mockGameState, mockDisplay, mockInput, mockDeckFactory, mockValidator, mockAction);
        assertTrue(turnState.pendingAction().isEmpty());
    }
}
