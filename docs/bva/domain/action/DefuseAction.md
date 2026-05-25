# BVA Analysis for DefuseAction

## Public interface for this class
- execute(gameState) → void

## Assumptions and notes
- The exploding kitten card is stored in TurnState.pendingAction before execute() is called by GameController.
- IPlayerInput.promptInsertPosition(deckSize) returns the player's chosen position.
- After insertion, pendingAction is cleared.
- Invalid positions (negative or beyond deckSize) are rejected by Deck.insertAt() and are not re-validated here.



### Method under test: `execute()`
spaces: pendingAction presence, insert position relative to deck size

cases:
- pendingAction is null: IllegalStateException — no kitten to defuse
- pendingAction set, position 0 (top of deck): kitten inserted at index 0
- pendingAction set, position = deckSize (bottom of deck): kitten inserted at last position
- pendingAction set, position in middle: kitten inserted at given index
- position -1 (N/A — rejected by Deck.insertAt, not this action's responsibility)
- position > deckSize (N/A — rejected by Deck.insertAt)

| test_Name                                              | State of the System                                          | Expected output                              | Implemented? |
|--------------------------------------------------------|--------------------------------------------------------------|----------------------------------------------|--------------|
| execute_NoPendingKitten_ThrowsIllegalStateException    | pendingAction is null                                        | IllegalStateException                        | :x:          |
| execute_KittenPending_PositionZero_InsertsAtTop        | pendingAction = kitten, promptInsertPosition returns 0       | kitten at index 0, pendingAction cleared     | :x:          |
| execute_KittenPending_PositionDeckSize_InsertsAtBottom | pendingAction = kitten, promptInsertPosition returns deckSize | kitten at bottom, pendingAction cleared     | :x:          |
| execute_KittenPending_PositionMiddle_InsertsAtMiddle   | pendingAction = kitten, promptInsertPosition returns middle  | kitten at given index, pendingAction cleared | :x:          |
