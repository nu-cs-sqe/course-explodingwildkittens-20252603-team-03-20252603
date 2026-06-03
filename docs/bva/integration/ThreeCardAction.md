# BVA Analysis for three card Action

## Scope

Integration tests for **three card** card action
These tests test the end to end action of three card action

## Assumptions and Notes
- Assumes system is already launched
- Assumes the current Player is playing a turn

## User Story:
- System displays a list of all other active players for the current player to choose from
- Player chooses the player whom they want to rob
- System prompts the player to name a specific card type they want
- Player says what card they want.
- System checks if that card exists in that player’s hand.
- If yes,
  - System adds that card to this players hand
  - System removes that card from the robbed player’s hand
- If no,
  - System just displays “client doesn't have that card”


# CardAction under test:
spaces: _game logic_, boolean

cases:
- valid card combo; not noped; card exists in hand; card is robbed
- valid card combo; not noped; card doesn't exist in hand; card not robbed
- valid card combo; noped; card is not robbed
- invalid card combo; card is not robbed; graceful error


| test_Name                                                 | State of the System                           | Expected output        | Implemented?       |
|-----------------------------------------------------------|-----------------------------------------------|------------------------|--------------------|
| threeCard_ValidCombo_NotNoped_CardInHand_CardRobbed       | valid card combo; not noped; card in hand     | card is robbed         | :white-check-mark: |
| threeCard_ValidCombo_NotNoped_CardInNotHand_CardNotRobbed | valid card combo; not noped; card not in hand | card is not robbed     | :white-check-mark: |
| threeCard_ValidCombo_Noped_CardNotRobbed                  | valid card combo; noped                       | card is not robbed     | :white-check-mark: |
| threeCard_InvalidCombo_GracefulError                      | invalid card combo                            | graceful error; no rob | :white-check-mark: |