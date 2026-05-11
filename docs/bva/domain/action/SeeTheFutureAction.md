# BVA Analysis for SeeTheFutureAction

## Public interface for this class
- execute(gameState) → void

## Assumptions and notes
- GameState.peekTopOfDeck(n) returns up to n cards; fewer cards are returned if the deck has fewer than n.
- The result is stored in the current player's peek buffer via storePeek().
- Deck order is not changed by this action.



### Method under test: `execute()`
spaces: collection (deck size relative to 3)

cases:
- empty: peekTopOfDeck(3) returns [], storePeek([]) called
- one element: returns [card1], storePeek([card1]) called
- two elements: returns [card1, card2], storePeek([card1, card2]) called
- three elements: returns exactly [card1, card2, card3]
- more than three: peek returns top 3 only
- max size (N/A — identical behaviour to more than three)

| test_Name                                                     | State of the System   | Expected output                                     | Implemented? |
|---------------------------------------------------------------|-----------------------|-----------------------------------------------------|--------------|
| execute_EmptyDeck_StoresPeekWithEmptyList                     | deck has 0 cards      | currentPlayer.storePeek([]) is called               | :x:          |
| execute_DeckWithOneCard_StoresPeekWithOneCard                 | deck has 1 card       | currentPlayer.storePeek([card1]) is called          | :x:          |
| execute_DeckWithTwoCards_StoresPeekWithTwoCards               | deck has 2 cards      | currentPlayer.storePeek([card1, card2]) is called   | :x:          |
| execute_DeckWithThreeOrMoreCards_StoresPeekWithThreeCards     | deck has 3+ cards     | currentPlayer.storePeek([card1, card2, card3])      | :x:          |
