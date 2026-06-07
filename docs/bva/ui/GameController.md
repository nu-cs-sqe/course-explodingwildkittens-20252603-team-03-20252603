# BVA Analysis for GameController

## Public interface (methods under analysis)

- `GameController(display: IGameDisplay, input: domain.input.IPlayerInput, comboValidator: ComboValidator)` (constructor) → new GameController
- `startGame()` → void
- `endGame()` → void
- `isGameActive()` → boolean
- `playCard(cards: List<Card>)` → void
- `playATurn` --> void

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
- `playATurn` This method handles the game logic. It does not handle any data but calls relevant model(domain) classes based on the game logic and
  then calls the ui to display stuff to the user based on the game logic still.




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

### Method under test: `startGame() (created for integration tests)`

spaces: numPlayers = {< 2, 2, 5, > 5}

cases:
- numPlayers below minimum (e.g., 1): `showMessage` called, `promptNumPlayers` called again until valid
- numPlayers above maximum (e.g., 6): `showMessage` called, `promptNumPlayers` called again until valid
- numPlayers = 2 (minimum valid): no error shown, game initializes
- numPlayers = 5 (maximum valid): no error shown, game initializes

| test_Name                                                                                       | State of the System                                                         | Expected output                                                                  | Implemented?       |
|-------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------|----------------------------------------------------------------------------------|--------------------|
| startGame_ConstructorIntegrationTest_InvalidNumPlayersBelowMin_ShowsErrorAndRepromptsNumPlayers | promptNumPlayers() returns 1 on first call (invalid), then 2 on second call | showMessage called once; promptNumPlayers called twice                           | :white_check_mark: |
| startGame_ConstructorIntegrationTest_ValidMaxPlayers_InitializesWithoutError                    | promptNumPlayers() returns 6 on first call (invalid), then 2 on second call | showMessage called once; promptNumPlayers called twice                           | :white_check_mark: |
| startGame_ValidMinPlayers_InitializesWithoutError_PlayerNamesAreCorrect                         | promptNumPlayers() returns 2 (valid minimum)                                | showMessage never called; promptNumPlayers called once; player names are correct | :white_check_mark: |
| startGame_ConstructorIntegrationTest_ValidMinPlayers_InitializesWithoutError                    | promptNumPlayers() returns 2 (valid minimum)                                | showMessage never called; promptNumPlayers called once                           | :white_check_mark: |
| startGame_ConstructorIntegrationTest_InvalidNumPlayersAboveMax_ShowsErrorAndRepromptsNumPlayers | promptNumPlayers() returns 6 on first call (invalid), then 2 on second call | showMessage called once; promptNumPlayers called twice                           | :white_check_mark: |
| startGame_ConstructorIntegrationTest_ValidMinPlayers_PlayerNamesAreCorrect                      | promptNumPlayers() returns 2 (valid minimum)                                | showMessage never called; promptNumPlayers called once; player names are correct | :white_check_mark: |


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

#### Partition 6 — playCard sets the pending Action

cases:
- valid card is noped; action is not executed; assert pending action

| test_Name                                                            | State of the System                           | Expected output       | Implemented? |
|----------------------------------------------------------------------|-----------------------------------------------|-----------------------|--------------|
| playCard_ValidSingleCard_Noped_ActionNotExecuted_AssertPendingAction | valid card is noped; action is not executed   | pending action is set | :white_check_mark: |

---

#### Partition 7 — Nope window: multiple players

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

| test_Name                                        | State of the System | Expected output     |    Implemented?    |
|--------------------------------------------------|---------------------|---------------------|:------------------:|
| handleDrawingCards_skipDrawIsTrue_SkipsDraw      | skipDraw=true       | drawCard not called | :white_check_mark: |
| handleDrawingCards_skipDrawIsFalse_DrawsCard     | skipDraw=false      | drawCard called     | :white_check_mark: |



### Method under test: `setTurnsForNextPlayer()`
spaces: boolean (isAttacking), boolean (wasAttacked), turnsRemaining


cases:
- isAttacking = false
- isAttacking = true, wasAttacked = false
- isAttacking = true, wasAttacked = true
- wasAttacked = true when isAttacking = false (N/A — wasAttacked is only read when isAttacking is true)
- turnsRemaining = 1 (lower boundary, only relevant when isAttacking=true, wasAttacked=true)
- turnsRemaining = 2 (> 1)
- turnsRemaining = 3 (> 1, user-cited example)
- turnsRemaining = INT_MAX (N/A — would overflow when added to DEFAULT_ATTACKING_TURNS)


| test_Name                                                                    | State of the System                                                   | Expected output           |    Implemented?    |
|------------------------------------------------------------------------------|-----------------------------------------------------------------------|---------------------------|:------------------:|
| setTurnsForNextPlayer_notAttacking_ReturnsOne                                | isAttacking=false,                                                    | returns 1 for nextplayer  | :white_check_mark: |
| setTurnsForNextPlayer_attackingNotWasAttacked_ReturnsTwo                     | isAttacking=true, wasAttacked=false,                                  | returns 2 for nextplayer  | :white_check_mark: |
| setTurnsForNextPlayer_attackingWasAttacked_turnsRemainingIsOne_ReturnsThree  | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=1 | returns 3 for nextplayer  | :white_check_mark: |
| setTurnsForNextPlayer_attackingWasAttacked_turnsRemainingIsTwo_ReturnsFour   | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=2 | returns 4 for nextplayer, | :white_check_mark: |
| setTurnsForNextPlayer_attackingWasAttacked_turnsRemainingIsThree_ReturnsFive | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=3 | returns 5 for nextplayer, | :white_check_mark: |
| setTurnsForNextPlayer_attackingWasAttacked_turnsRemainingIsFour_ReturnsSix   | isAttacking=true, wasAttacked=true, turnsRemaining (current player)=4 | returns 6 for nextplayer  | :white_check_mark: |



### Method under test: `decrementTurns()`
spaces: count (turnsRemaining)

cases:
- start with one turn
- start with two turns
- turnsRemaining = 0 (N/A- can never start with 0 remaning turns)
- turnsRemaining = INT_MAX (N/A)

| test_Name                                    | State of the System  | Expected output                 |    Implemented?    |
|----------------------------------------------|----------------------|---------------------------------|:------------------:|
| decrementTurns_startWithOneTurn_decrements  | start with one turn  | turnsRemaining decremented to 0 | :white_check_mark: |
| decrementTurns_startWithTwoTurns_decrements | start with two turns | turnsRemaining decremented to 1 | :white_check_mark: |


### Method under test: `resetCurrentPlayerWasAttacked()`
spaces: boolean

cases:
- wasAttacked = false
- wasAttacked = true
-
| test_Name                                                     | State of the System | Expected output                                           |    Implemented?    |
|---------------------------------------------------------------|---------------------|-----------------------------------------------------------|:------------------:|
| resetCurrentPlayerWasAttacked_wasAttackedIsTrue_ResetToFalse  | wasAttacked = true  | reset wasAttacked to false for future rounds              | :white_check_mark: |
| resetCurrentPlayerWasAttacked_wasAttackedIsFalse_ResetToFalse | wasAttacked = false | reset wasAttacked to false for future rounds              | :white_check_mark: |



### Method under test: `resetGameState(int turnsForNextPlayer)`
spaces: count (turnsForNextPlayer)

cases:
- turnsForNextPlayer = 1 (DEFAULT_NORMAL_TURNS — normal play, no attack)
- turnsForNextPlayer = 2 (DEFAULT_ATTACKING_TURNS — next player was attacked)
- turnsForNextPlayer > 2 (e.g., 3 — cascading attack scenario)
- turnsForNextPlayer = 0 (N/A — handleTurnTaking never returns 0)
- turnsForNextPlayer = INT_MAX (N/A — handleTurnTaking never returns INT_MAX)

note: dirty state (isAttacking=true, skipDraw=true) set before each call to verify all flags are cleared by reset

| test_Name                                                               | State of the System                                        | Expected output                                                        | Implemented?       |
|-------------------------------------------------------------------------|------------------------------------------------------------|------------------------------------------------------------------------|--------------------|
| resetGameState_turnsForNextPlayerIsOne_ResetsTurnState                  | isAttacking=true, skipDraw=true, turnsForNextPlayer=1      | turnsRemaining=1, isAttacking=false, skipDraw=false                    | :white_check_mark: |
| resetGameState_turnsForNextPlayerIsTwo_ResetsTurnState                  | isAttacking=true, skipDraw=true, turnsForNextPlayer=2      | turnsRemaining=2, isAttacking=false, skipDraw=false                    | :white_check_mark: |
| resetGameState_turnsForNextPlayerIsThree_ResetsTurnState                | isAttacking=true, skipDraw=true, turnsForNextPlayer=3      | turnsRemaining=3, isAttacking=false, skipDraw=false                    | :white_check_mark: |



### Method under test: `readyToPlayATurn()`
spaces: boolean (gameState.isActive()), boolean (currentPlayer.isActive())

cases:
- gameState.isActive() = false → return false (short-circuits; currentPlayer never checked)
- gameState.isActive() = true, currentPlayer.isActive() = false → return false
- gameState.isActive() = true, currentPlayer.isActive() = true → return true
- currentPlayer.isActive() = false when gameState.isActive() = false (N/A — short-circuited)

| test_Name                                                              | State of the System                                        | Expected output | Implemented?       |
|------------------------------------------------------------------------|------------------------------------------------------------|-----------------|--------------------|
| readyToPlayATurn_gameStateNotActive_ReturnsFalse                       | gameState.isActive()=false                                 | false           | :white_check_mark: |
| readyToPlayATurn_gameStateActiveCurrentPlayerNotActive_ReturnsFalse    | gameState.isActive()=true, currentPlayer.isActive()=false  | false           | :white_check_mark: |
| readyToPlayATurn_gameStateActiveCurrentPlayerActive_ReturnsTrue        | gameState.isActive()=true, currentPlayer.isActive()=true   | true            | :white_check_mark: |



### Method under test: `advanceGameToNextPlayer()`
spaces: none (no parameters — method purely delegates to gameState.advancePlayer())

cases:
- advancePlayer() is called on gameState

| test_Name                                             | State of the System | Expected output                         | Implemented?       |
|-------------------------------------------------------|---------------------|-----------------------------------------|--------------------|
| advanceGameToNextPlayer_CallsAdvancePlayerOnGameState | any valid gameState | gameState.advancePlayer() called once   | :white_check_mark: |



### Method under test: `playATurn()`
spaces: none (no parameters only game logic

cases:
- readyToPlayATurn -> true
- readyToPlayAturn -> false
- hasToPlayATurn = true -> one loop run
- hasToPlayATurn = false ->no loop run
- play a card -> plays cards

| test_Name                                                                                | State of the System                                                | Expected output  | Implemented?       |
|------------------------------------------------------------------------------------------|--------------------------------------------------------------------|------------------|--------------------|
| playATurn_notReadyToPlayATurn_throwsException                                            | not ready to play a turn                                           | exception thrown | :white_check_mark: |
| playATurn_ReadyToPlayATurn_DoesNotHaveToPlayATurn_NoLoopRun                              | ready to play a turn; does not have to play a turn                 | no loop run      | :white_check_mark: |
| playATurn_ReadyToPlayATurn_HasToPlayATurn_PlayACard_OneLoopRun_PlaysCards                | ready to play a turn; has to play a turn; plays cards              | one loop run     | :white_check_mark: |
| playATurn_ReadyToPlayATurn_HasToPlayATurn_DonePlaying_OneLoopRun_DrawsCardsAndTurnTaking | ready to play a turn; has to play a turn; draws cards, turn taking | one loop run     | :white_check_mark: |



### Method under test: `drawCard()`
spaces: CardType (EXPLODING_KITTEN vs not), currentPlayerHasCard(DEFUSE) (only relevant when EXPLODING_KITTEN)

cases:
- drawn card is NOT EXPLODING_KITTEN → added to player's hand
- drawn card IS EXPLODING_KITTEN, player HAS defuse → DefuseAction executed, player not eliminated
- drawn card IS EXPLODING_KITTEN, player has NO defuse → player eliminated
- hasDefuse when not EXPLODING_KITTEN (N/A — defuse check is inside the exploding-kitten branch)

| test_Name                                                                   | State of the System                                        | Expected output                                      | Implemented?       |
|-----------------------------------------------------------------------------|------------------------------------------------------------|------------------------------------------------------|--------------------|
| drawCard_drawnCardIsNotExplodingKitten_AddsCardToPlayerHand                 | drawn card type ≠ EXPLODING_KITTEN                         | addCardToCurrentPlayer called with drawn card        | :white_check_mark: |
| drawCard_drawnCardIsExplodingKitten_playerHasDefuse_ExecutesDefuseAction    | drawn card = EXPLODING_KITTEN, player has DEFUSE           | setPendingAction called; DefuseAction executes       | :white_check_mark: |
| drawCard_drawnCardIsExplodingKitten_playerHasNoDefuse_EliminatesPlayer      | drawn card = EXPLODING_KITTEN, player has no DEFUSE        | eliminateCurrentPlayer called                        | :white_check_mark: |


### Method under test: `dealCardsAndReturnDeck()`

spaces: numPlayers = { 2, 5,}

cases:
- two players
- five players

| test_Name                                                          | State of the System | Expected output                      | Implemented?       |
|--------------------------------------------------------------------|---------------------|--------------------------------------|--------------------|
| dealCardsAndReturnDeck_twoPlayers_returnsOneExplodingKitten        | two players         | deck contains one exploding kitten   | :white_check_mark: |
| dealCardsAndReturnDeck_twoPlayers_updatesPlayersHands              | two players         | each player's hand is non-empty      | :white_check_mark: |
| dealCardsAndReturnDeck_fivePlayers_returnsFourExplodingKittens     | five players        | deck contains four exploding kittens | :white_check_mark: |
| dealCardsAndReturnDeck_fivePlayers_updatesPlayersHands             | five players        | each player's hand is non-empty      | :white_check_mark: |
| dealCardsAndReturnDeck_createsFullDeck_AssertsDeckMethodsCalled    | two players         | deck methods are asserted            | :white_check_mark: |
