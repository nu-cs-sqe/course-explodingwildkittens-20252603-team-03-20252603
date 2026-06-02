# BVA Analysis for see the future card Action

## Scope

Integration tests for **see the future** card action
These tests test the end to end action of see the future card

## Assumptions and Notes
- Assumes system is already launched
- Assumes the current Player is playing a turn

## UserStory being verified
- System reads the top 3 cards of the deck
- System adds them to the player's instance
- System shows the player the peek cards (see the future cards)

## CardAction under test:
spaces: _game logic_, boolean

cases:
- user plays a turn; card is noped; no peek cards are shown
- user plays a turn; card is not noped; peek cards are shown


| test_Name                              | State of the System                    | Expected output         | Implemented?       |
|----------------------------------------|----------------------------------------|-------------------------|--------------------|
| seeTheFuture_IsNoped_NoPeekCardsShown  | user plays a card, action is noped     | no peek cards are shown | :white_check_mark: |
| seeTheFuture_IsNotNoped_PeekCardsShown | user plays a card, action is not noped | peek cards are shown    | :white_check_mark: |
