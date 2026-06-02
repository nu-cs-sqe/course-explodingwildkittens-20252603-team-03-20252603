# BVA Analysis for playATurn Integration Tests

## Scope

Integration tests for **playATurn**.
These tests test the following classes (not mocked)
- GameState
- Player
- TurnState
- Deck
- 


## Assumptions and Notes
- Assumes system is ready to play a turn
  - assumes game is active
  - assumes current player is also active
- Assumes game is already launched 
  - each player has a hand of cards
  - deck has cards already

## Method under test: `playATurn()`
spaces: _count, game logic_

cases:
- play a turn should not allow users to play card(s) if action is noped
- play a turn should not allow users to play card(s) if card combo is invalid
- play a turn should allow users to play 0 cards
- play a turn should allow users to play 1 card(s) if action is not noped, and is valid
- play a turn should allow users to play more than one card(s) if action is not noped, and is valid

| test_Name                                                | State of the System                                      | Expected output                                     | Implemented?       |
|----------------------------------------------------------|----------------------------------------------------------|-----------------------------------------------------|--------------------|
| playATurn_ActionIsNoped_CardIsNotPlayed                  | user plays a card, action is noped                       | system should not allow user to play card           | :white_check_mark: |
| playATurn_InvalidCardCombo_CardIsNotPlayed               | user plays a card, invalid card combo                    | system should not allow user to play card           | :white_check_mark: |
| playATurn_PlayZeroCards_GameIsAdvanced                   | user plays no cards                                      | system should give the user a card and advance game | :white_check_mark: |
| playATurn_PlayOneCardNotNopedValidCard_CardIsPlayed      | user plays a card, action is not noped, valid card combo | system should allow user to play card               | :white_check_mark: |
| playATurn_PlayManyCardsNotNopedValidCards_CardsArePlayed | user plays cards, action is not noped, valid card combo  | system should allow user to play cards              | :white_check_mark: |


