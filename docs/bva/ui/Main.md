# BVA Analysis for Main

## Public interface (methods under analysis)

- `runGame(controller: GameController)` → void

## Assumptions and notes

- `runGame` calls `controller.startGame()` exactly once before the loop.
- The loop calls `controller.playATurn()` on each iteration and exits when `controller.isGameActive()` returns false.
- `main` is the composition root and is not under test; only `runGame` is tested.

---

### Method under test: `runGame()`

spaces: number of turns = { 0, 1, many }

cases:
- game is already inactive (zero turns played)
- game active for exactly one turn
- game active for multiple turns

| test_Name                                              | State of the System                              | Expected output                              | Implemented?       |
|--------------------------------------------------------|--------------------------------------------------|----------------------------------------------|--------------------|
| runGame_gameInactiveImmediately_playsZeroTurns         | isGameActive() returns false immediately         | startGame called once; playATurn never called | :x: |
| runGame_gameActiveOneTurn_playsOneTurn                 | isGameActive() returns true once then false      | startGame called once; playATurn called once  | :x: |
| runGame_gameActiveMultipleTurns_playsMultipleTurns     | isGameActive() returns true three times then false | startGame called once; playATurn called three times | :x: |
