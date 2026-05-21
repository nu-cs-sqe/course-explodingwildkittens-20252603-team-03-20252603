# BVA Analysis for GameController

## Public interface (methods under analysis)

- `playCard(cards: List<Card>)` → void

## Assumptions and notes

- `playCard` delegates validation to `ComboValidator.isValid()`. If invalid, it shows an error message via `display` and returns immediately — no state is changed.
- If valid, cards are removed from the current player's hand and added to the discard pile before the Nope window opens.
- The Nope window is simplified for the terminal version: a single `input.promptNope(others)` call (y/n prompt). This is a known simplification; a timed/chainable window is deferred to a future iteration.
- If the player says Nope (`promptNope` returns `true`), `turnState.incrementNopeCount()` is called and the action is **not** executed.
- If no one Nopes (`promptNope` returns `false`), `nopeCount` stays 0 (even) and the action executes.
- After the window (noped or not), `turnState.clearPendingAction()` is always called.
- `ComboValidator.resolveAction()` is used to obtain the correct `CardAction`; `execute(gameState)` is called on it when the action proceeds.



### Method under test: `playCard(cards: List<Card>)`

spaces: cards validity × Nope response

---

#### Partition 1 — Invalid card selection (ComboValidator rejects)

cases:
- null list → invalid
- empty list → invalid
- single DEFUSE card → invalid (can't be played directly)
- single CAT_CARD alone → invalid (needs a pair)
- 4+ cards → invalid (no combo of that size)

| test_Name                                              | State of the System                        | Expected output                                          | Implemented? |
|--------------------------------------------------------|--------------------------------------------|----------------------------------------------------------|--------------|
| playCard_NullList_ShowsErrorAndNoStateChange           | valid game, null passed                    | display.showMessage called; no cards discarded           | :x:          |
| playCard_EmptyList_ShowsErrorAndNoStateChange          | valid game, empty list passed              | display.showMessage called; no cards discarded           | :x:          |
| playCard_SingleDefuse_ShowsErrorAndNoStateChange       | player holds Defuse card                   | display.showMessage called; card stays in hand           | :x:          |
| playCard_SingleCatCard_ShowsErrorAndNoStateChange      | player holds a cat card                    | display.showMessage called; card stays in hand           | :x:          |

---

#### Partition 2 — Valid single card, not Noped

cases:
- promptNope returns false → action executes, cards discarded, pending cleared

| test_Name                                              | State of the System                        | Expected output                                          | Implemented? |
|--------------------------------------------------------|--------------------------------------------|----------------------------------------------------------|--------------|
| playCard_ValidSingleCard_NotNoped_ExecutesAction       | player holds Skip; promptNope returns false | cards removed from hand, discarded; SkipAction executes | :x:          |
| playCard_ValidSingleCard_NotNoped_ClearsPendingAction  | player holds Skip; promptNope returns false | pendingAction() = Optional.empty() after call            | :x:          |
| playCard_ValidSingleCard_NotNoped_CardsAddedToDiscard  | player holds Skip; promptNope returns false | discard pile size increases by 1                         | :x:          |

---

#### Partition 3 — Valid single card, Noped

cases:
- promptNope returns true → action does NOT execute; nopeCount incremented; cards still discarded

| test_Name                                              | State of the System                        | Expected output                                          | Implemented? |
|--------------------------------------------------------|--------------------------------------------|----------------------------------------------------------|--------------|
| playCard_ValidSingleCard_Noped_ActionNotExecuted       | player holds Skip; promptNope returns true  | SkipAction not executed (turnState unchanged by action)  | :x:          |
| playCard_ValidSingleCard_Noped_IncrementsNopeCount     | player holds Skip; promptNope returns true  | turnState.nopeCount() = 1                                | :x:          |
| playCard_ValidSingleCard_Noped_CardsStillDiscarded     | player holds Skip; promptNope returns true  | cards removed from hand and added to discard pile        | :x:          |
| playCard_ValidSingleCard_Noped_ClearsPendingAction     | player holds Skip; promptNope returns true  | pendingAction() = Optional.empty() after call            | :x:          |

---

#### Partition 4 — Valid 2-cat combo, not Noped

cases:
- 2 matching cat cards → TwoCatAction executes

| test_Name                                              | State of the System                          | Expected output                                          | Implemented? |
|--------------------------------------------------------|----------------------------------------------|----------------------------------------------------------|--------------|
| playCard_TwoCatCombo_NotNoped_ExecutesAction           | player holds 2 matching cats; promptNope=false | TwoCatAction executes; 2 cards discarded               | :x:          |

---

#### Partition 5 — Valid 3-cat combo, not Noped

cases:
- 3 matching cat cards → ThreeCatAction executes

| test_Name                                              | State of the System                          | Expected output                                          | Implemented? |
|--------------------------------------------------------|----------------------------------------------|----------------------------------------------------------|--------------|
| playCard_ThreeCatCombo_NotNoped_ExecutesAction         | player holds 3 matching cats; promptNope=false | ThreeCatAction executes; 3 cards discarded             | :x:          |
