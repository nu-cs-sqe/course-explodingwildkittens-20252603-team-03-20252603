# BVA Analysis for GameController

## Public interface for this class

- `GameController(display: IGameDisplay, input: IPlayerInput)` (constructor) → new GameController
- `startGame()` → void
- `endGame()` → void

## Assumptions and notes

- `startGame()` calls `input.promptNumPlayers()` and re-prompts (via `display.showMessage`) if the value is outside [2, 5].
- `startGame()` creates a `DeckFactory` with the validated `numPlayers` and calls `deckFactory.buildDeck()`, which returns a shuffled deck containing all action and cat cards plus (numPlayers − 1) exploding kitten cards (no defuse cards).
- `startGame()` deals 7 cards to each player by calling `deck.drawTop()` in a loop, then gives each player exactly 1 defuse card obtained from `deckFactory.buildDefuseCards()` (defuse cards are not drawn from the deck).
- `startGame()` initializes `GameState` with the resulting deck and player list, then calls `playATurn()` to begin the first turn.
- `endGame()` calls `gameState.endGame()`, then `display.showWinner()` with the sole surviving player (the only player left in the active queue), then delegates the restart/close decision to `input.promptRestart()`. If `promptRestart()` returns `true`, `startGame()` is called again from the top.
- `endGame()` must only be called after `startGame()` has successfully initialized a `GameState`; calling it beforehand is undefined.
- Tests for this class verify observable mock interactions on `IGameDisplay` and `IPlayerInput`. Internal postconditions (player hand size, deck composition, discard pile state) are delegated to `DeckFactory` and `GameState` and are tested in their own BVA docs.



### Method under test: `startGame()`

spaces: numPlayers = {< 2, 2, 5, > 5}

cases:
- numPlayers below minimum (e.g., 1): `showMessage` called, `promptNumPlayers` called again until valid
- numPlayers above maximum (e.g., 6): `showMessage` called, `promptNumPlayers` called again until valid
- numPlayers = 2 (minimum valid): no error shown, game initializes
- numPlayers = 5 (maximum valid): no error shown, game initializes

| test_Name                                                             | State of the System                                                          | Expected output                                        | Implemented?   |
|-----------------------------------------------------------------------|------------------------------------------------------------------------------|--------------------------------------------------------|----------------|
| startGame_InvalidNumPlayersBelowMin_ShowsErrorAndRepromptsNumPlayers | promptNumPlayers() returns 1 on first call (invalid), then 2 on second call | showMessage called once; promptNumPlayers called twice | :cross_mark:   |
| startGame_InvalidNumPlayersAboveMax_ShowsErrorAndRepromptsNumPlayers | promptNumPlayers() returns 6 on first call (invalid), then 2 on second call | showMessage called once; promptNumPlayers called twice | :cross_mark:   |
| startGame_ValidMinPlayers_InitializesWithoutError                    | promptNumPlayers() returns 2 (valid minimum)                                 | showMessage never called; promptNumPlayers called once | :cross_mark:   |
| startGame_ValidMaxPlayers_InitializesWithoutError                    | promptNumPlayers() returns 5 (valid maximum)                                 | showMessage never called; promptNumPlayers called once | :cross_mark:   |



### Method under test: `endGame()`

spaces: promptRestart = {true, false}

Precondition: exactly 1 active player remains in `GameState` (the survivor); this is guaranteed by the Remove Player flow before `endGame()` is ever called.

cases:
- 1 active player → `showWinner` called with the sole surviving player
- promptRestart = true: `startGame()` is called again (promptNumPlayers invoked once more)
- promptRestart = false: game ends, `startGame()` is not called again

| test_Name                                       | State of the System                                                     | Expected output                            | Implemented?   |
|-------------------------------------------------|-------------------------------------------------------------------------|--------------------------------------------|----------------|
| endGame_OneActivePlayer_DisplaysSurvivor        | game started with 2 players; 1 player eliminated; survivor is player 2 | showWinner called with player 2            | :cross_mark:   |
| endGame_PromptRestartTrue_CallsStartGame        | 1 player remains; promptRestart returns true                            | promptNumPlayers called a second time      | :cross_mark:   |
| endGame_PromptRestartFalse_DoesNotCallStartGame | 1 player remains; promptRestart returns false                           | promptNumPlayers called exactly once total | :cross_mark:   |