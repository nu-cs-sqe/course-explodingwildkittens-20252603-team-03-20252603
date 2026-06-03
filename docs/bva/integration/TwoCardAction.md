# BVA Analysis for two card Action

## Scope

Integration tests for **two card** card action
These tests test the end to end action of two card action

## Assumptions and Notes
- Assumes system is already launched
- Assumes the current Player is playing a turn

## User Story:
- System shows a list of players in the game for the current_player to choose one
- Player chooses the player whom the want to rob
- System asks player to choose a card from their deck (should be a blind selection so we can ask them to write any number within the length of the deck)
- System adds that card at that index to this players hand
- System removes that card from the robbed player’s hand


# CardAction under test:
spaces: _game logic_, boolean

cases:
- valid card combo; not noped; card is robbed
- valid card combo; noped; card is not robbed
- invalid card combo; card is not robbed; graceful error


| test_Name                              | State of the System         | Expected output        | Implemented?       |
|----------------------------------------|-----------------------------|------------------------|--------------------|
| twoCard_ValidCombo_NotNoped_CardRobbed | valid card combo; not noped | card is robbed         | :white-check-mark: |
| twoCard_ValidCombo_Noped_CardNotRobbed | valid card combo; noped     | card is not robbed     | :white-check-mark: |
| twoCard_InvalidCombo_GracefulError     | invalid card combo          | graceful error; no rob | :white-check-mark: |