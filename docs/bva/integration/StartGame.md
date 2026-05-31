# BVA Analysis for Start A Game Integration Tests

## Scope

Integration tests for the **Start A Game** user story end-to-end through `GameController`. These tests use real (non-mocked) `DeckFactory`, `GameState`, `Player`, and `Deck` instances; only `IGameDisplay` and `IPlayerInput` are stubbed to control prompts and inject the player count.

## Classes under test (integrated)

- `GameController` — orchestrates setup via `startGame()`
- `DeckFactory` — builds the shuffled play deck and defuse cards
- `GameState` — holds the active player queue and deck after initialization
- `Player` — holds each player's hand

## Assumptions and notes

- `startGame()` re-prompts via `display.showMessage` if `promptNumPlayers()` returns a value outside [2, 5].
- `DeckFactory.buildDeck()` returns a shuffled deck containing all action/cat cards plus exactly `numPlayers − 1` Exploding Kitten cards; no Defuse cards are included in the play deck.
- `DeckFactory.buildDefuseCards()` supplies one Defuse per player; each player receives 7 cards drawn from the deck and 1 Defuse from this method (8 cards total).
- `GameState` is constructed after dealing; `isActive()` returns `true` on construction.
- The first player provided to `GameState` occupies the head of the `activePlayers` queue; `getCurrentPlayer()` returns that player.
- All players start with `isActive() == true` (none are eliminated at setup).
- Invalid player count handling (re-prompt loop) cross-references unit-level tests in `GameController.md` — integration tests here verify the same behavior holds with real collaborators rather than mocks.

---

## Method under test: `GameController.startGame()` (full integration)

spaces: numPlayers = {−1, 1, 2, 3, 4, 5, 6}

cases:
- numPlayers = 2 (min valid): game initializes; all post-setup invariants hold
- numPlayers = 3 (interior): game initializes; all post-setup invariants hold
- numPlayers = 4 (interior): game initializes; all post-setup invariants hold
- numPlayers = 5 (max valid): game initializes; all post-setup invariants hold
- numPlayers = 1 (one below min): `showMessage` called; re-prompt; valid count then given
- numPlayers = 6 (one above max): `showMessage` called; re-prompt; valid count then given
- numPlayers = −1 (well below min): `showMessage` called; re-prompt; valid count then given

---

### Partition 1 — GameState is active after setup

| test_Name                                | State of the System          | Expected output       | Implemented?       |
|------------------------------------------|------------------------------|-----------------------|--------------------|
| startGame_TwoPlayers_GameStateIsActive   | promptNumPlayers() returns 2 | isGameActive() = true | :white_check_mark: |
| startGame_ThreePlayers_GameStateIsActive | promptNumPlayers() returns 3 | isGameActive() = true | :white_check_mark: |
| startGame_FourPlayers_GameStateIsActive  | promptNumPlayers() returns 4 | isGameActive() = true | :white_check_mark: |
| startGame_FivePlayers_GameStateIsActive  | promptNumPlayers() returns 5 | isGameActive() = true | :white_check_mark: |

---

### Partition 2 — Each player has exactly 8 cards in hand (7 random + 1 Defuse)

| test_Name                                        | State of the System          | Expected output                   | Implemented? |
|--------------------------------------------------|------------------------------|-----------------------------------|--------------|
| startGame_TwoPlayers_EachPlayerHasEightCards     | promptNumPlayers() returns 2 | every player's hand size = 8      | :x:          |
| startGame_ThreePlayers_EachPlayerHasEightCards   | promptNumPlayers() returns 3 | every player's hand size = 8      | :x:          |
| startGame_FourPlayers_EachPlayerHasEightCards    | promptNumPlayers() returns 4 | every player's hand size = 8      | :x:          |
| startGame_FivePlayers_EachPlayerHasEightCards    | promptNumPlayers() returns 5 | every player's hand size = 8      | :x:          |

---

### Partition 3 — Each player's hand contains exactly 1 Defuse card

| test_Name                                              | State of the System          | Expected output                                     | Implemented? |
|--------------------------------------------------------|------------------------------|-----------------------------------------------------|--------------|
| startGame_TwoPlayers_EachPlayerHasExactlyOneDefuse     | promptNumPlayers() returns 2 | each player's hand has exactly 1 DEFUSE card        | :x:          |
| startGame_ThreePlayers_EachPlayerHasExactlyOneDefuse   | promptNumPlayers() returns 3 | each player's hand has exactly 1 DEFUSE card        | :x:          |
| startGame_FourPlayers_EachPlayerHasExactlyOneDefuse    | promptNumPlayers() returns 4 | each player's hand has exactly 1 DEFUSE card        | :x:          |
| startGame_FivePlayers_EachPlayerHasExactlyOneDefuse    | promptNumPlayers() returns 5 | each player's hand has exactly 1 DEFUSE card        | :x:          |

---

### Partition 4 — Deck contains exactly numPlayers − 1 Exploding Kitten cards after setup

spaces: numPlayers − 1 = {1, 2, 3, 4}

| test_Name                                                   | State of the System          | Expected output                                      | Implemented? |
|-------------------------------------------------------------|------------------------------|------------------------------------------------------|--------------|
| startGame_TwoPlayers_DeckContainsOneExplodingKitten         | promptNumPlayers() returns 2 | deck contains exactly 1 EXPLODING_KITTEN card        | :x:          |
| startGame_ThreePlayers_DeckContainsTwoExplodingKittens      | promptNumPlayers() returns 3 | deck contains exactly 2 EXPLODING_KITTEN cards       | :x:          |
| startGame_FourPlayers_DeckContainsThreeExplodingKittens     | promptNumPlayers() returns 4 | deck contains exactly 3 EXPLODING_KITTEN cards       | :x:          |
| startGame_FivePlayers_DeckContainsFourExplodingKittens      | promptNumPlayers() returns 5 | deck contains exactly 4 EXPLODING_KITTEN cards       | :x:          |

---

### Partition 5 — First player is at the head of the activePlayers queue

spaces: numPlayers = {2, 5} (boundary; interior players do not affect queue ordering)

| test_Name                                              | State of the System                              | Expected output                         | Implemented? |
|--------------------------------------------------------|--------------------------------------------------|-----------------------------------------|--------------|
| startGame_TwoPlayers_FirstPlayerIsCurrentPlayer        | promptNumPlayers() returns 2; player 1 added first | getCurrentPlayer() returns player 1   | :x:          |
| startGame_FivePlayers_FirstPlayerIsCurrentPlayer       | promptNumPlayers() returns 5; player 1 added first | getCurrentPlayer() returns player 1   | :x:          |

---

### Partition 6 — All players have isActive() == true (not eliminated)

spaces: numPlayers = {2, 5} (boundary; the invariant applies identically at all valid counts)

| test_Name                                        | State of the System          | Expected output                           | Implemented? |
|--------------------------------------------------|------------------------------|-------------------------------------------|--------------|
| startGame_TwoPlayers_AllPlayersAreActive         | promptNumPlayers() returns 2 | all 2 players have isActive() = true      | :x:          |
| startGame_FivePlayers_AllPlayersAreActive        | promptNumPlayers() returns 5 | all 5 players have isActive() = true      | :x:          |

---

### Partition 7 — Invalid player count: graceful error handling and re-prompt

spaces: numPlayers = {−1, 1, 6}

note: cross-references unit-level cases in `GameController.md` (`startGame_InvalidNumPlayersBelowMin_*` and `startGame_InvalidNumPlayersAboveMax_*`); integration tests here confirm the same behaviour holds with real collaborators.

| test_Name                                                          | State of the System                            | Expected output                                           | Implemented?       |
|--------------------------------------------------------------------|------------------------------------------------|-----------------------------------------------------------|--------------------|
| startGame_NumPlayersNegative_ShowsErrorAndRepromptsUntilValid      | promptNumPlayers() returns −1, then 2          | showMessage called once; game initializes with 2 players  | :white_check_mark: |
| startGame_NumPlayersBelowMin_ShowsErrorAndRepromptsUntilValid      | promptNumPlayers() returns 1, then 2           | showMessage called once; game initializes with 2 players  | :x:                |
| startGame_NumPlayersAboveMax_ShowsErrorAndRepromptsUntilValid      | promptNumPlayers() returns 6, then 2           | showMessage called once; game initializes with 2 players  | :x:                |