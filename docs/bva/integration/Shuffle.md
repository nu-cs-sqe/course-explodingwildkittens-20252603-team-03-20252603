# BVA Analysis for shuffle card Action

## Scope

Integration tests for **shuffle** card action
These tests test the end to end action of shuffle card

## Assumptions and Notes
- Assumes system is already launched
- Assumes the current Player is playing a turn

# CardAction under test:
spaces: _game logic_

cases:
- user plays a turn; card is not noped; deck is shuffled
- user plays a turn; card is noped; deck is not shuffled


| test_Name                         | State of the System                    | Expected output      | Implemented?       |
|-----------------------------------|----------------------------------------|----------------------|--------------------|
| shuffle_IsNoped_DeckNotShuffled   | user plays a card, action is noped     | deck is not shuffled | :white_check_mark: |
| shuffle_IsNotNoped_DeckIsShuffled | user plays a card, action is not noped | deck is shuffled     | :white_check_mark: |
