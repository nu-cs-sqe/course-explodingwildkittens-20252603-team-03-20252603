# BVA Analysis for TwoCatAction

## Public interface for this class
- execute(gameState) → void

## Assumptions and notes
- PlayerInteractionHelper.stealRandomCard handles the random card theft.
- Current player picks a target via PlayerInteractionHelper.pickTarget.
- If no other active players exist, an IllegalStateException is thrown.
- If the target has an empty hand, no card is stolen (handled by stealRandomCard).



### Method under test: `execute()`
spaces: number of other active players, target hand size

cases:
- no other active players: IllegalStateException
- target has 0 cards: no card stolen
- target has 1 card: that card stolen
- target has many cards: one random card stolen
- max size (N/A)

| test_Name                                                | State of the System              | Expected output                           | Implemented?       |
|----------------------------------------------------------|----------------------------------|-------------------------------------------|--------------------|
| execute_NoOtherActivePlayers_ThrowsIllegalStateException | only one active player (current) | IllegalStateException                     | :white_check_mark: |
| execute_TargetHasNoCards_NoTransfer                      | target has empty hand            | no card moved                             | :white_check_mark: |
| execute_TargetHasOneCard_StealsThatCard                  | target has 1 card                | current player gains it, target loses it  | :white_check_mark: |
| execute_TargetHasMultipleCards_StealsOneCard             | target has 3 cards               | current gains 1 card, target loses 1      | :white_check_mark: |
