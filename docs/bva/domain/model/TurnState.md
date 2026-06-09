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
- `pendingAction()` → Optional<Card>
- `reset()` → void

## Assumptions and notes

- On construction and after `reset()`, default state is: `turnsRemaining()=1`, `shouldSkipDraw()=false`, `isAttacking()=false`, `nopeCount()=0`, `pendingAction()=Optional.empty()`.
- `decrementTurns()` floors at 0 and does not go negative.
- `pendingAction()` returns `Optional<Card>` — passing null to `setPendingAction` is not permitted per Clean Code guidelines.
- `isAttacking` is set to `true` by `startAttack()` and only cleared by `reset()`.



### Method under test: `TurnState()` (constructor)

spaces: initial value of each field

| test_Name                                  | State of the System | Expected output                    | Implemented?       |
|--------------------------------------------|---------------------|------------------------------------|--------------------|
| constructor_InitializesDefaultTurnCount    | new TurnState       | turnsRemaining() = 1               | :white_check_mark: |
| constructor_InitializesSkipDrawFalse       | new TurnState       | shouldSkipDraw() = false           | :white_check_mark: |
| constructor_InitializesNotAttacking        | new TurnState       | isAttacking() = false              | :white_check_mark: |
| constructor_InitializesNopeCountZero       | new TurnState       | nopeCount() = 0                    | :white_check_mark: |
| constructor_InitializesNoPendingAction     | new TurnState       | pendingAction() = Optional.empty() | :white_check_mark: |



### Method under test: `enableSkipDraw()`

spaces: skipDraw = {false, already true}

cases:
- skipDraw was false → becomes true
- skipDraw already true → stays true

| test_Name                                  | State of the System            | Expected output         | Implemented?       |
|--------------------------------------------|--------------------------------|-------------------------|--------------------|
| enableSkipDraw_WhenFalse_SetsSkipDrawTrue  | default state (skipDraw=false) | shouldSkipDraw() = true | :white_check_mark: |
| enableSkipDraw_AlreadyTrue_RemainsTrue     | enableSkipDraw called twice    | shouldSkipDraw() = true | :white_check_mark: |



### Method under test: `startAttack(turns: int)`

spaces: turns = {0, 1, 2}

cases:
- turns = 0: isAttacking set, turnsToTake = 0
- turns = 1: minimum meaningful attack
- turns = 2: standard attack

| test_Name                                        | State of the System    | Expected output                        | Implemented?       |
|--------------------------------------------------|------------------------|----------------------------------------|--------------------|
| startAttack_TwoTurns_SetsIsAttackingAndTurns     | default state, turns=2 | isAttacking()=true, turnsRemaining()=2 | :white_check_mark: |
| startAttack_OneTurn_SetsIsAttackingAndOneTurn    | default state, turns=1 | isAttacking()=true, turnsRemaining()=1 | :white_check_mark: |
| startAttack_ZeroTurns_SetsIsAttackingZeroTurns   | default state, turns=0 | isAttacking()=true, turnsRemaining()=0 | :white_check_mark: |



### Method under test: `setPendingAction(card: Card)`

spaces: existing pendingAction = {empty, present}

cases:
- card provided, no prior pending action
- card provided, replaces existing pending action

| test_Name                                          | State of the System         | Expected output                          | Implemented?       |
|----------------------------------------------------|-----------------------------|------------------------------------------|--------------------|
| setPendingAction_NonNullCard_StoresPendingAction   | no prior pending action     | pendingAction() = Optional.of(card)      | :white_check_mark: |
| setPendingAction_ReplacesExisting_StoresNewCard    | prior pending action exists | pendingAction() = Optional.of(new card)  | :white_check_mark: |



### Method under test: `clearPendingAction()`

spaces: pendingAction = {present, already empty}

cases:
- pendingAction is set → clears to empty
- pendingAction already empty → stays empty, no throw

| test_Name                                  | State of the System       | Expected output                    | Implemented?       |
|--------------------------------------------|---------------------------|------------------------------------|--------------------|
| clearPendingAction_WhenSet_ClearsToEmpty   | pendingAction was set     | pendingAction() = Optional.empty() | :white_check_mark: |
| clearPendingAction_AlreadyEmpty_NoThrow    | pendingAction was empty   | pendingAction() = Optional.empty() | :white_check_mark: |



### Method under test: `incrementNopeCount()`

spaces: nopeCount = {0, positive}

cases:
- nopeCount = 0 → becomes 1
- nopeCount = 1 → becomes 2

| test_Name                                  | State of the System | Expected output | Implemented?       |
|--------------------------------------------|---------------------|-----------------|--------------------|
| incrementNopeCount_FromZero_BecomesOne     | nopeCount = 0       | nopeCount() = 1 | :white_check_mark: |
| incrementNopeCount_FromPositive_Increments | nopeCount = 1       | nopeCount() = 2 | :white_check_mark: |



### Method under test: `decrementTurns()`

spaces: turnsToTake = {0, 1, 2}

cases:
- turnsToTake = 0: floors at 0, no underflow
- turnsToTake = 1: decrements to 0
- turnsToTake = 2: decrements to 1

| test_Name                           | State of the System               | Expected output      | Implemented?       |
|-------------------------------------|-----------------------------------|----------------------|--------------------|
| decrementTurns_FromOne_BecomesZero  | turnsToTake = 1 (default)         | turnsRemaining() = 0 | :white_check_mark: |
| decrementTurns_FromTwo_BecomesOne   | turnsToTake = 2 (via startAttack) | turnsRemaining() = 1 | :white_check_mark: |
| decrementTurns_FromZero_StaysZero   | turnsToTake = 0                   | turnsRemaining() = 0 | :white_check_mark: |



### Method under test: `reset()`

spaces: state = {default/clean, all fields dirty}

cases:
- dirty state → all fields reset to defaults
- already default → remains default

| test_Name                             | State of the System                                                 | Expected output                                                                                                  | Implemented?       |
|---------------------------------------|---------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|--------------------|
| reset_FromDirtyState_RestoresDefaults | skipDraw=true, isAttacking=true, nopeCount=3, pendingAction=present | turnsRemaining()=1, shouldSkipDraw()=false, isAttacking()=false, nopeCount()=0, pendingAction()=Optional.empty() | :white_check_mark: |
| reset_FromDefaultState_RemainsDefault | fresh TurnState                                                     | all defaults unchanged                                                                                           | :white_check_mark: |
