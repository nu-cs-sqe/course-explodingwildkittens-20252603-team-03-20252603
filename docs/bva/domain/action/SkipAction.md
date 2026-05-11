# BVA Analysis for SkipAction

## Public interface for this class
- execute(gameState) → void

## Assumptions and notes
- GameState and TurnState are valid and non-null.
- GameController is responsible for advancing the player after a skip; this action only signals the intent to skip.



### Method under test: `execute()`
spaces: TurnState skipDraw flag

cases:
- skipDraw already false (normal turn): enableSkipDraw() called
- skipDraw already true (N/A — a duplicate skip has no additional effect at the action level)

| test_Name                                     | State of the System             | Expected output                        | Implemented? |
|-----------------------------------------------|---------------------------------|----------------------------------------|--------------|
| execute_ValidGameState_EnablesSkipDraw        | GameState with a valid TurnState | turnState.enableSkipDraw() is called  | :x:          |
