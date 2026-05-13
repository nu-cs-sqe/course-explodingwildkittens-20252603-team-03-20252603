# BVA Analysis for TurnState

## Public interface for this class

- `TurnState()` (constructor) → TurnState with default turn state
- `enableSkipDraw()` → void
- `startAttack(turns: int)` → void
- `setPendingAction(card: Card)` → void
- `clearPendingAction()` → void
- `incrementNopeCount()` → void
- `decrementTurns()` → void
- `shouldSkipDraw()` → boolean
- `isAttacking()` → boolean
- `nopeCount()` → int
- `turnsRemaining()` → int
- `reset()` → void

## Assumptions and notes

- On construction and after `reset()`, default state is: `turnsRemaining()=1`, `shouldSkipDraw()=false`, `isAttacking()=false`, `nopeCount()=0`, `pendingAction=null`.
- `decrementTurns()` floors at 0 and does not go negative.
- A `pendingAction()` getter (not currently in the design) must be added to make `setPendingAction`/`clearPendingAction` directly testable.
- `isAttacking` is set to `true` by `startAttack()` and only cleared by `reset()`.



### Method under test: `TurnState()` (constructor)

spaces: initial value of each field

| test_Name                                  | State of the System | Expected output          | Implemented? |
|--------------------------------------------|---------------------|--------------------------|--------------|
| constructor_InitializesDefaultTurnCount    | new TurnState       | turnsRemaining() = 1     | :x:          |
| constructor_InitializesSkipDrawFalse       | new TurnState       | shouldSkipDraw() = false | :x:          |
| constructor_InitializesNotAttacking        | new TurnState       | isAttacking() = false    | :x:          |
| constructor_InitializesNopeCountZero       | new TurnState       | nopeCount() = 0          | :x:          |
| constructor_InitializesNoPendingAction     | new TurnState       | pendingAction() = null   | :x:          |



### Method under test: `enableSkipDraw()`

spaces: skipDraw = {false, already true}

cases:
- skipDraw was false → becomes true
- skipDraw already true → stays true

| test_Name                                  | State of the System            | Expected output         | Implemented? |
|--------------------------------------------|--------------------------------|-------------------------|--------------|
| enableSkipDraw_WhenFalse_SetsSkipDrawTrue  | default state (skipDraw=false) | shouldSkipDraw() = true | :x:          |
| enableSkipDraw_AlreadyTrue_RemainsTrue     | enableSkipDraw called twice    | shouldSkipDraw() = true | :x:          |



### Method under test: `startAttack(turns: int)`

spaces: turns = {0, 1, 2}

cases:
- turns = 0: isAttacking set, turnsToTake = 0
- turns = 1: minimum meaningful attack
- turns = 2: standard attack

| test_Name                                        | State of the System    | Expected output                        | Implemented? |
|--------------------------------------------------|------------------------|----------------------------------------|--------------|
| startAttack_TwoTurns_SetsIsAttackingAndTurns     | default state, turns=2 | isAttacking()=true, turnsRemaining()=2 | :x:          |
| startAttack_OneTurn_SetsIsAttackingAndOneTurn    | default state, turns=1 | isAttacking()=true, turnsRemaining()=1 | :x:          |
| startAttack_ZeroTurns_SetsIsAttackingZeroTurns   | default state, turns=0 | isAttacking()=true, turnsRemaining()=0 | :x:          |



### Method under test: `setPendingAction(card: Card)`

spaces: card = {null, non-null}; existing pendingAction = {null, non-null}

cases:
- non-null card, no prior pending action
- non-null card, replaces existing pending action
- null card

| test_Name                                          | State of the System         | Expected output            | Implemented? |
|----------------------------------------------------|-----------------------------|----------------------------|--------------|
| setPendingAction_NonNullCard_StoresPendingAction   | no prior pending action     | pendingAction() = card     | :x:          |
| setPendingAction_ReplacesExisting_StoresNewCard    | prior pending action exists | pendingAction() = new card | :x:          |
| setPendingAction_NullCard_StoresNull               | any state                   | pendingAction() = null     | :x:          |



### Method under test: `clearPendingAction()`

spaces: pendingAction = {non-null, already null}

cases:
- pendingAction is set → clears to null
- pendingAction already null → stays null, no throw

| test_Name                                  | State of the System    | Expected output        | Implemented? |
|--------------------------------------------|------------------------|------------------------|--------------|
| clearPendingAction_WhenSet_ClearsToNull    | pendingAction was set  | pendingAction() = null | :x:          |
| clearPendingAction_AlreadyNull_NoThrow     | pendingAction was null | pendingAction() = null | :x:          |



### Method under test: `incrementNopeCount()`

spaces: nopeCount = {0, positive}

cases:
- nopeCount = 0 → becomes 1
- nopeCount = 1 → becomes 2

| test_Name                                  | State of the System | Expected output | Implemented? |
|--------------------------------------------|---------------------|-----------------|--------------|
| incrementNopeCount_FromZero_BecomesOne     | nopeCount = 0       | nopeCount() = 1 | :x:          |
| incrementNopeCount_FromPositive_Increments | nopeCount = 1       | nopeCount() = 2 | :x:          |



### Method under test: `decrementTurns()`

spaces: turnsToTake = {0, 1, 2}

cases:
- turnsToTake = 0: floors at 0, no underflow
- turnsToTake = 1: decrements to 0
- turnsToTake = 2: decrements to 1

| test_Name                           | State of the System               | Expected output      | Implemented? |
|-------------------------------------|-----------------------------------|----------------------|--------------|
| decrementTurns_FromOne_BecomesZero  | turnsToTake = 1 (default)         | turnsRemaining() = 0 | :x:          |
| decrementTurns_FromTwo_BecomesOne   | turnsToTake = 2 (via startAttack) | turnsRemaining() = 1 | :x:          |
| decrementTurns_FromZero_StaysZero   | turnsToTake = 0                   | turnsRemaining() = 0 | :x:          |



### Method under test: `reset()`

spaces: state = {default/clean, all fields dirty}

cases:
- dirty state → all fields reset to defaults
- already default → remains default

| test_Name                                 | State of the System                                                  | Expected output                                                                              | Implemented? |
|-------------------------------------------|----------------------------------------------------------------------|----------------------------------------------------------------------------------------------|--------------|
| reset_FromDirtyState_RestoresDefaults     | skipDraw=true, isAttacking=true, nopeCount=3, pendingAction=non-null | turnsRemaining()=1, shouldSkipDraw()=false, isAttacking()=false, nopeCount()=0, pendingAction()=null | :x: |
| reset_FromDefaultState_RemainsDefault     | fresh TurnState                                                      | all defaults unchanged                                                                       | :x:          |
