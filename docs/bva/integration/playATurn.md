# BVA Analysis for playATurn Integration Tests

## Scope

Integration tests for **playATurn**.
These tests test the following classes (not mocked)
- GameState
- Player
- TurnState


## Assumptions and Notes
- Assumes system is ready to play a turn
  - assumes game is active
  - assumes current player is also active
- Assumes game is already launched 
  - each player has a hand of cards
  - deck has cards already

## Method under test: `playATurn()`
spaces: _game logic_

cases:
- play a turn should allow users to play many cards
- end of a turn should draw card if needed and advance to next player
- play a turn (invalid card combo) should gracefully display error and ask user to choose again

### Partition 1 - Play a card; user plays one card is prompted to choose again

### Partition 2 - Play a card; user plays one card, selects done playing and system draws card and goes to the next player

### Partition 3 - Play a card; user plays invalid card combo, gracefully displays the error to user and moves to next player


