# BVA Analysis for GameController

## Public interface (methods under analysis)

- `GameController(display: IGameDisplay, input: IPlayerInput)` (constructor) → new GameController
- `startGame()` → void
- `endGame()` → void
- `isGameActive()` → boolean
- `playCard(cards: List<Card>)` → void

## Assumptions and notes

- `startGame()` calls `input.promptNumPlayers()` and re-prompts (via `display.showMessage`) if the value is outside [2, 5].
- `startGame()` creates a `DeckFactory` with the validated `numPlayers` and calls `deckFactory.buildDeck()`, which returns a shuffled deck containing all action and cat cards plus (numPlayers − 1) exploding kitten cards (no defuse cards).
- `startGame()` deals 7 cards to each player by calling `deck.drawTop()` in a loop, then gives each player exactly 1 defuse card obtained from `deckFactory.buildDefuseCards()` (defuse cards are not drawn from the deck).
- `startGame()` initializes `GameState` with the resulting deck and player list, then calls `playATurn()` to begin the first turn.
- `endGame()` calls `gameState.endGame()`, then `display.showWinner()` with the sole surviving player (the only player left in the active queue), then delegates the restart/close decision to `input.promptRestart()`. If `promptRestart()` returns `true`, `startGame()` is called again from the top.
- `endGame()` must only be called after `startGame()` has successfully initialized a `GameState`; calling it beforehand is undefined.
- `isGameActive()` delegates to `gameState.isActive()`; added to kill the PITest mutant that removes the `gameState.endGame()` call.
- `playCard` delegates validation to `ComboValidator.isValid()`. If invalid, it shows an error message via `display` and returns immediately — no state is changed.
- If valid, cards are removed from the current player's hand and added to the discard pile before the Nope window opens.
- The Nope window asks each other player individually in sequence. If a player nopes, `turnState.incrementNopeCount()` is called and the loop continues. The action executes only if `nopeCount` is even after all players have been asked.
- After the window (noped or not), `turnState.clearPendingAction()` is always called.
- `ComboValidator.resolveAction()` is used to obtain the correct `CardAction`; `execute(gameState)` is called on it when the action proceeds.



### Method under test: `startGame()`

spaces: numPlayers = {< 2, 2, 5, > 5}

cases:
- numPlayers below minimum (e.g., 1): `showMessage` called, `promptNumPlayers` called again until valid
- numPlayers above maximum (e.g., 6): `showMessage` called, `promptNumPlayers` called again until valid
- numPlayers = 2 (minimum valid): no error shown, game initializes
- numPlayers = 5 (maximum valid): no error shown, game initializes

| test_Name                                                            | State of the System                                                         | Expected output                                        | Implemented?       |
|----------------------------------------------------------------------|-----------------------------------------------------------------------------|--------------------------------------------------------|--------------------|
| startGame_InvalidNumPlayersBelowMin_ShowsErrorAndRepromptsNumPlayers | promptNumPlayers() returns 1 on first call (invalid), then 2 on second call | showMessage called once; promptNumPlayers called twice | :white_check_mark: |
| startGame_InvalidNumPlayersAboveMax_ShowsErrorAndRepromptsNumPlayers | promptNumPlayers() returns 6 on first call (invalid), then 2 on second call | showMessage called once; promptNumPlayers called twice | :white_check_mark: |
| startGame_ValidMinPlayers_InitializesWithoutError                    | promptNumPlayers() returns 2 (valid minimum)                                | showMessage never called; promptNumPlayers called once | :white_check_mark: |
| startGame_ValidMaxPlayers_InitializesWithoutError                    | promptNumPlayers() returns 5 (valid maximum)                                | showMessage never called; promptNumPlayers called once | :white_check_mark: |



### Method under test: `endGame()`

spaces: promptRestart = {true, false}

Precondition: exactly 1 active player remains in `GameState` (the survivor); this is guaranteed by the Remove Player flow before `endGame()` is ever called.

cases:
- 1 active player → `gameState.endGame()` called, game becomes inactive; `showWinner` called with survivor
- promptRestart = true: `startGame()` is called again (promptNumPlayers invoked once more)
- promptRestart = false: game ends, `startGame()` is not called again

| test_Name                                       | State of the System                                                     | Expected output                            | Implemented?       |
|-------------------------------------------------|-------------------------------------------------------------------------|--------------------------------------------|---------------------|
| endGame_OneActivePlayer_SetsGameInactive        | game started with 2 players; promptRestart returns false                | isGameActive() = false                     | :white_check_mark: |
| endGame_OneActivePlayer_DisplaysSurvivor        | game started with 2 players; 1 player eliminated; survivor is player 2 | showWinner called with player 2            | :white_check_mark: |
| endGame_PromptRestartTrue_CallsStartGame        | 1 player remains; promptRestart returns true                            | promptNumPlayers called a second time      | :white_check_mark: |
| endGame_PromptRestartFalse_DoesNotCallStartGame | 1 player remains; promptRestart returns false                           | promptNumPlayers called exactly once total | :white_check_mark: |



### Method under test: `playCard(cards: List<Card>)`

spaces: cards validity × Nope response

---

#### Partition 1 — Invalid card selection (ComboValidator rejects)

cases:
- null list → invalid
- empty list → invalid
- single DEFUSE card → invalid (can't be played directly)
- single CAT_CARD alone → invalid (needs a pair)

| test_Name                                              | State of the System                        | Expected output                                          | Implemented? |
|--------------------------------------------------------|--------------------------------------------|----------------------------------------------------------|--------------|
| playCard_NullList_ShowsErrorAndNoStateChange           | valid game, null passed                    | display.showMessage called; no cards discarded           | :white_check_mark: |
| playCard_EmptyList_ShowsErrorAndNoStateChange          | valid game, empty list passed              | display.showMessage called; no cards discarded           | :white_check_mark: |
| playCard_SingleDefuse_ShowsErrorAndNoStateChange       | player holds Defuse card                   | display.showMessage called; card stays in hand           | :white_check_mark: |
| playCard_SingleCatCard_ShowsErrorAndNoStateChange      | player holds a cat card                    | display.showMessage called; card stays in hand           | :white_check_mark: |

---

#### Partition 2 — Valid single card, not Noped

cases:
- promptNope returns false → action executes, cards discarded, pending cleared

| test_Name                                              | State of the System                        | Expected output                                          | Implemented? |
|--------------------------------------------------------|--------------------------------------------|----------------------------------------------------------|--------------|
| playCard_ValidSingleCard_NotNoped_ExecutesAction       | player holds Skip; promptNope returns false | cards removed from hand, discarded; SkipAction executes | :white_check_mark: |
| playCard_ValidSingleCard_NotNoped_ClearsPendingAction  | player holds Skip; promptNope returns false | pendingAction() = Optional.empty() after call            | :white_check_mark: |
| playCard_ValidSingleCard_NotNoped_CardsAddedToDiscard  | player holds Skip; promptNope returns false | discard pile size increases by 1                         | :white_check_mark: |

---

#### Partition 3 — Valid single card, Noped

cases:
- promptNope returns true → action does NOT execute; nopeCount incremented; cards still discarded

| test_Name                                              | State of the System                        | Expected output                                          | Implemented? |
|--------------------------------------------------------|--------------------------------------------|----------------------------------------------------------|--------------|
| playCard_ValidSingleCard_Noped_ActionNotExecuted       | player holds Skip; one player nopes        | SkipAction not executed                                  | :white_check_mark: |
| playCard_ValidSingleCard_Noped_IncrementsNopeCount     | player holds Skip; one player nopes        | turnState.nopeCount() = 1                                | :white_check_mark: |
| playCard_ValidSingleCard_Noped_CardsStillDiscarded     | player holds Skip; one player nopes        | cards removed from hand and added to discard pile        | :white_check_mark: |
| playCard_ValidSingleCard_Noped_ClearsPendingAction     | player holds Skip; one player nopes        | pendingAction() = Optional.empty() after call            | :white_check_mark: |

---

#### Partition 4 — Valid 2-cat combo, not Noped

cases:
- 2 matching cat cards → TwoCatAction executes

| test_Name                                              | State of the System                          | Expected output                                          | Implemented? |
|--------------------------------------------------------|----------------------------------------------|----------------------------------------------------------|--------------|
| playCard_TwoCatCombo_NotNoped_ExecutesAction           | player holds 2 matching cats; no one nopes   | TwoCatAction executes; 2 cards discarded                 | :white_check_mark: |

---

#### Partition 5 — Valid 3-cat combo, not Noped

cases:
- 3 matching cat cards → ThreeCatAction executes

| test_Name                                              | State of the System                          | Expected output                                          | Implemented? |
|--------------------------------------------------------|----------------------------------------------|----------------------------------------------------------|--------------|
| playCard_ThreeCatCombo_NotNoped_ExecutesAction         | player holds 3 matching cats; no one nopes   | ThreeCatAction executes; 3 cards discarded               | :white_check_mark: |

---

#### Partition 6 — Nope window: multiple players

cases:
- no other players → promptNope never called, action executes
- one player, does not nope → nopeCount unchanged
- one player, nopes → nopeCount = 1
- multiple players, nobody nopes → nopeCount unchanged
- multiple players, one nopes → nopeCount = 1
- multiple players, all nope → nopeCount = 3

| test_Name                                                    | State of the System                          | Expected output      | Implemented? |
|--------------------------------------------------------------|----------------------------------------------|----------------------|--------------|
| applyNopeWindow_OnePlayer_DoesNotNope_NopeCountUnchanged     | 1 other player, returns false                | nopeCount = 0        | :white_check_mark: |
| applyNopeWindow_MultiplePlayers_NobodyNopes_NopeCountUnchanged | 3 other players, all return false          | nopeCount = 0        | :white_check_mark: |
| applyNopeWindow_MultiplePlayers_OneNopes_NopeCountIsOne      | 3 other players, one returns true            | nopeCount = 1        | :white_check_mark: |
| applyNopeWindow_MultiplePlayers_AllNope_NopeCountIsThree     | 3 other players, all return true             | nopeCount = 3        | :white_check_mark: |
