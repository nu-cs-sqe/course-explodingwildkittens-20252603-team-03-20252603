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

| test_Name                                        | State of the System | Expected output    |    Implemented?    |
|--------------------------------------------------|---------------------|--------------------|:------------------:|
| handleDrawingCards_skipDrawIsTrue_SkipsDraw      | skipDraw=true       | drawCard not called | :white_check_mark: |
| handleDrawingCards_skipDrawIsFalse_DrawsCard     | skipDraw=false      | drawCard called     |        :x:         |



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

| test_Name                                                                            | State of the System                                                    | Expected output                                           | Implemented? |
|--------------------------------------------------------------------------------------|------------------------------------------------------------------------|-----------------------------------------------------------|:------------:|
| handleTurnTaking_notAttacking_DecrementsAndReturnsOne                                | isAttacking=false, turnsRemaining (current player)=1                   | returns 1 for nextplayer, turnsRemaining decremented to 0 |     :x:      |
| handleTurnTaking_attackingNotWasAttacked_DecrementsAndReturnsTwo                     | isAttacking=true, wasAttacked=false, turnsRemaining (current player)=1 | returns 2 for nextplayer, turnsRemaining decremented to 0 |     :x:      |
| handleTurnTaking_attackingWasAttacked_turnsRemainingIsOne_DecrementsAndReturnsThree  | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=1  | returns 3 for nextplayer, turnsRemaining decremented to 0 |     :x:      |
| handleTurnTaking_attackingWasAttacked_turnsRemainingIsTwo_DecrementsAndReturnsFour   | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=2  | returns 4 for nextplayer, turnsRemaining decremented to 1 |     :x:      |
| handleTurnTaking_attackingWasAttacked_turnsRemainingIsThree_DecrementsAndReturnsFive | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=3  | returns 5 for nextplayer, turnsRemaining decremented to 2 |     :x:      |
| handleTurnTaking_attackingWasAttacked_turnsRemainingIsFour_DecrementsAndReturnsSix   | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=4  | returns 6 for nextplayer, turnsRemaining decremented to 3 |     :x:      |
| handleTurnTaking_notAttacking_turnsRemainingisThree_DecrementsAndReturnsOne          | isAttacking=false, turnsRemaining (current player)=3                   | returns 1 for nextplayer, turnsRemaining decremented to 2 |     :x:      |
