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