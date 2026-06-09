# BVA Analysis for FavorAction

## Public interface for this class
- execute(gameState) → void

## Assumptions and notes
- PlayerInteractionHelper handles target selection (pickTarget) and card transfer (giveCard).
- FavorAction filters out the current player from the active players list before calling pickTarget.
- giveCard prompts the target player to choose a card to give; if the target has an empty hand, no card is transferred.
- If no other active players exist, an IllegalStateException is thrown.



### Method under test: `execute()`
spaces: number of other active players, target hand size

cases:
- no other active players: IllegalStateException
- one other player, empty hand: no card transferred
- one other player, one card: that card transferred to current player
- one other player, many cards: target-chosen card transferred
- multiple other players: selected player's chosen card transferred
- max size (N/A)

| test_Name                                                | State of the System                           | Expected output                                | Implemented?       |
|----------------------------------------------------------|-----------------------------------------------|------------------------------------------------|--------------------|
| execute_NoOtherActivePlayers_ThrowsIllegalStateException | only one active player (current)              | IllegalStateException                          | :white_check_mark: |
| execute_OneOtherPlayerEmptyHand_NoTransfer               | one target with 0 cards                       | no card moved                                  | :white_check_mark: |
| execute_OneOtherPlayerOneCard_TransfersThatCard          | one target with 1 card, target gives it       | current player gains card, target loses it     | :white_check_mark: |
| execute_MultipleOtherPlayers_TransfersFromSelectedTarget | multiple targets; selected has 1 card         | current player gains card from selected target | :white_check_mark: |
