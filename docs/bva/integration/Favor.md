# BVA Analysis for skip card Action

## Scope

Integration tests for **favor** card action
These tests test the end to end action of favor card

## Assumptions and Notes
- Assumes system is already launched
- Assumes the current Player is playing a turn

## UserStory Being Verified
- System shows a list of players in the game for the current_player to choose one
- Player chooses the player whom the want to rob
- System asks that player to choose a card to give you
- if they have any cards, that player chooses a card from their hand
   - System adds that card to this players hand
   - System removes that card from the robbed player’s hand

# CardAction under test:
spaces: _game logic_, boolean

cases:
- user plays a turn; card is not noped; favor asks for card
- user plays a turn; card is not noped; player has no cards; continues on --> no error
- user plays a turn; card is noped; does not ask for card

| test_Name                             | State of the System            | Expected output      | Implemented?       |
|---------------------------------------|--------------------------------|----------------------|--------------------|
| favor_NotNoped_HasCards_CardIsRobbed  | not noped; has cards           | card is robbed       | :white-check-mark: |
| favor_NotNoped_NoCards_NothingHappens | not noped; player has no cards | no error, no robbery | :white-check-mark: |
| favor_Noped_CardNotRobbed             | noped                          | card not robbed      | :white-check-mark: |