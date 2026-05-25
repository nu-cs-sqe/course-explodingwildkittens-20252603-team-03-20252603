# BVA Analysis for NopeAction

## Public interface for this class
- execute(gameState) → void

## Assumptions and notes
- NopeAction only increments the nope counter.
- Evaluating whether an action is currently noped (odd count = noped, even = un-noped) is the responsibility of GameController.



### Method under test: `execute()`
spaces: TurnState nopeCount value

cases:
- nopeCount is 0 (first nope): increment called
- nopeCount is 1 (counter-nope): increment called — same delegation regardless of current count

| test_Name                                         | State of the System              | Expected output                             | Implemented? |
|---------------------------------------------------|----------------------------------|---------------------------------------------|--------------|
| execute_ValidGameState_IncrementsNopeCount        | GameState with valid TurnState   | turnState.incrementNopeCount() is called    | :x:          |
