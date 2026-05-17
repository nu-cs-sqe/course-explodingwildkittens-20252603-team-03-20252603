# BVA Analysis for GameController

## Public interface for this class
- playATurn --> void

## Assumptions and notes
- This class handles the game logic. 
- It does not handle any data but calls relevant model(domain) classes based on the game logic and 
then calls the ui to display stuff to the user based on the game logic still.

  

### Method under test: `hasToPlayATurn()`
spaces: count, boolean 

cases:
- return true
- return false
- isAttacking is true 
- isAttacking is false
- turnsToTake = -1 (N/A)
- turnsToTake = 0
- turnsToTake = 1
- turnsToTake > 1
- turnsToTake is INT_MAX

| test_Name                                                                    | State of the System                       | Expected output | Implemented?       |
|------------------------------------------------------------------------------|-------------------------------------------|-----------------|--------------------|
| hasToPlayATurn_isAttackingIsTrue_ReturnsFalse                                | isAttacking=true                          | false           | :white_check_mark: |
| hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsZero_ReturnsFalse          | isAttacking=false, turnsRemaining=0       | false           | :white_check_mark: |
| hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsOne_ReturnsTrue            | isAttacking=false, turnsRemaining=1       | true            | :white_check_mark: |
| hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsGreaterThanOne_ReturnsTrue | isAttacking=false, turnsRemaining=2       | true            | :white_check_mark: |
| hasToPlayATurn_isAttackingIsFalse_turnsRemainingIsIntMax_ReturnsTrue         | isAttacking=false, turnsRemaining=INT_MAX | true            | :white_check_mark: |


### Method under test: `handleDrawingCards()`
spaces: boolean

cases:
- skipDraw is true
- skipDraw is false

| test_Name                                        | State of the System | Expected output     |    Implemented?    |
|--------------------------------------------------|---------------------|---------------------|:------------------:|
| handleDrawingCards_skipDrawIsTrue_SkipsDraw      | skipDraw=true       | drawCard not called | :white_check_mark: |
| handleDrawingCards_skipDrawIsFalse_DrawsCard     | skipDraw=false      | drawCard called     | :white_check_mark: |



### Method under test: `handleTurnTaking()`
spaces: boolean (isAttacking), boolean (wasAttacked), count (turnsRemaining)

cases:
- isAttacking = false
- isAttacking = true, wasAttacked = false
- isAttacking = true, wasAttacked = true
- wasAttacked = true when isAttacking = false (N/A — wasAttacked is only read when isAttacking is true)
- turnsRemaining = 1 (lower boundary, only relevant when isAttacking=true, wasAttacked=true)
- turnsRemaining = 2 (> 1)
- turnsRemaining = 3 (> 1, user-cited example)
- turnsRemaining = INT_MAX (N/A — would overflow when added to DEFAULT_ATTACKING_TURNS)

| test_Name                                                                            | State of the System                                                    | Expected output                                           |    Implemented?    |
|--------------------------------------------------------------------------------------|------------------------------------------------------------------------|-----------------------------------------------------------|:------------------:|
| handleTurnTaking_notAttacking_DecrementsAndReturnsOne                                | isAttacking=false, turnsRemaining (current player)=1                   | returns 1 for nextplayer, turnsRemaining decremented to 0 | :white_check_mark: |
| handleTurnTaking_attackingNotWasAttacked_DecrementsAndReturnsTwo                     | isAttacking=true, wasAttacked=false, turnsRemaining (current player)=1 | returns 2 for nextplayer, turnsRemaining decremented to 0 | :white_check_mark: |
| handleTurnTaking_attackingWasAttacked_turnsRemainingIsOne_DecrementsAndReturnsThree  | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=1  | returns 3 for nextplayer, turnsRemaining decremented to 0 | :white_check_mark: |
| handleTurnTaking_attackingWasAttacked_turnsRemainingIsTwo_DecrementsAndReturnsFour   | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=2  | returns 4 for nextplayer, turnsRemaining decremented to 1 | :white_check_mark: |
| handleTurnTaking_attackingWasAttacked_turnsRemainingIsThree_DecrementsAndReturnsFive | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=3  | returns 5 for nextplayer, turnsRemaining decremented to 2 | :white_check_mark: |
| handleTurnTaking_attackingWasAttacked_turnsRemainingIsFour_DecrementsAndReturnsSix   | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=4  | returns 6 for nextplayer, turnsRemaining decremented to 3 | :white_check_mark: |
| handleTurnTaking_notAttacking_turnsRemainingisThree_DecrementsAndReturnsOne          | isAttacking=false, turnsRemaining (current player)=3                   | returns 1 for nextplayer, turnsRemaining decremented to 2 | :white_check_mark: |



### Method under test: `resetCurrentPlayerWasAttacked()`
spaces: boolean 

cases:
- wasAttacked = false
- wasAttacked = true
- 
| test_Name                                                     | State of the System | Expected output                                           |    Implemented?    |
|---------------------------------------------------------------|---------------------|-----------------------------------------------------------|:------------------:|
| resetCurrentPlayerWasAttacked_wasAttackedIsTrue_ResetToFalse  | wasAttacked = true  | reset wasAttacked to false for future rounds              | :white_check_mark: |
| resetCurrentPlayerWasAttacked_wasAttackedIsFalse_ResetToFalse | wasAttacked = false | reset wasAttacked to false for future rounds              | :white_check_mark: |



### Method under test: `resetGameState(int turnsForNextPlayer)`
spaces: count (turnsForNextPlayer)

cases:
- turnsForNextPlayer = 1 (DEFAULT_NORMAL_TURNS — normal play, no attack)
- turnsForNextPlayer = 2 (DEFAULT_ATTACKING_TURNS — next player was attacked)
- turnsForNextPlayer > 2 (e.g., 3 — cascading attack scenario)
- turnsForNextPlayer = 0 (N/A — handleTurnTaking never returns 0)
- turnsForNextPlayer = INT_MAX (N/A — handleTurnTaking never returns INT_MAX)

note: dirty state (isAttacking=true, skipDraw=true) set before each call to verify all flags are cleared by reset

| test_Name                                                               | State of the System                                        | Expected output                                                        | Implemented?       |
|-------------------------------------------------------------------------|------------------------------------------------------------|------------------------------------------------------------------------|--------------------|
| resetGameState_turnsForNextPlayerIsOne_ResetsTurnState                  | isAttacking=true, skipDraw=true, turnsForNextPlayer=1      | turnsRemaining=1, isAttacking=false, skipDraw=false                    | :white_check_mark: |
| resetGameState_turnsForNextPlayerIsTwo_ResetsTurnState                  | isAttacking=true, skipDraw=true, turnsForNextPlayer=2      | turnsRemaining=2, isAttacking=false, skipDraw=false                    | :white_check_mark: |
| resetGameState_turnsForNextPlayerIsThree_ResetsTurnState                | isAttacking=true, skipDraw=true, turnsForNextPlayer=3      | turnsRemaining=3, isAttacking=false, skipDraw=false                    | :white_check_mark: |