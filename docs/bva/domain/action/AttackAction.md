# BVA Analysis for AttackAction

## Public interface for this class
- execute(gameState) → void

## Assumptions and notes
- GameController advances the player after this action; AttackAction only configures the attack in TurnState.
- An attack always forces exactly 2 turns on the next player.



### Method under test: `execute()`
spaces: TurnState attack state

cases:
- no active attack: startAttack(2) called
- already attacking (N/A — stacking attack behaviour is a GameController concern, not this action)

| test_Name                                              | State of the System              | Expected output                         | Implemented?       |
|--------------------------------------------------------|----------------------------------|-----------------------------------------|--------------------|
| execute_ValidGameState_StartsAttackWithTwoTurns        | GameState with valid TurnState   | turnState.startAttack(2) is called      | :white_check_mark: |
