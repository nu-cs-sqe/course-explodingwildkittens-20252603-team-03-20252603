# BVA Analysis for ThreeCatAction

## Public interface for this class
- execute(gameState) → void

## Assumptions and notes
- PlayerInteractionHelper.stealNamedCard handles the named card theft.
- Current player names a card type via PlayerInteractionHelper.pickCardType (delegates to IPlayerInput).
- If the target does not have the named type, no card is transferred (no exception).
- If no other active players exist, an IllegalStateException is thrown.



### Method under test: `execute()`
spaces: number of other active players, named card type presence in target hand

cases:
- no other active players: IllegalStateException
- target does not have named type: no card stolen
- target has exactly one of named type: that card stolen
- target has multiple of named type: one card of that type stolen
- max size (N/A)

| test_Name                                                | State of the System                       | Expected output                      | Implemented? |
|----------------------------------------------------------|-------------------------------------------|--------------------------------------|--------------|
| execute_NoOtherActivePlayers_ThrowsIllegalStateException | only one active player (current)          | IllegalStateException                | :x:          |
| execute_TargetLacksNamedType_NoTransfer                  | target has no SKIP, current requests SKIP | no card moved                        | :x:          |
| execute_TargetHasNamedType_StealsIt                      | target has 1 SKIP, current requests SKIP  | current gains SKIP, target loses it  | :x:          |
| execute_TargetHasMultipleOfNamedType_StealsOne           | target has 2 SKIP, current requests SKIP  | current gains 1 SKIP, target loses 1 | :x:          |
