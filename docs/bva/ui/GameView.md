# BVA Analysis for GameView

## Public interface for this class

- `GameView()` (constructor) → terminal view using standard input and standard output with UTF-8 encoding
- `GameView(scanner: Scanner, output: PrintStream)` (constructor) → view with injected scanner and output stream for tests
- `showGameState(gameState: GameState)` → void
- `showPlayerHand(player: Player)` → void
- `showMessage(message: String)` → void
- `showWinner(player: Player)` → void
- `showCurrentPlayer(player: Player)` → void
- `promptCardSelection(player: Player)` → List<Card>
- `promptNumPlayers()` → int
- `promptNope(player: Player)` → boolean
- `promptInsertPosition(deckSize: int)` → int
- `promptTargetSelection(candidates: List<Player>)` → Player
- `promptCardType()` → CardType
- `promptRestart()` → boolean
- `promptPlayerChoice()` → PlayerChoice

## Class size and structure

Clean Code recommends keeping classes under about 200 lines. `GameView` implements both `IGameDisplay` and `IPlayerInput`, so a single file would exceed that limit while still using small methods (repo checkstyle caps each method at 20 lines).

We split responsibilities instead of treating one large class as an exception:

- `GameView` (~105 lines) — coordinates the two interfaces and delegates work
- `TerminalDisplayWriter` — all terminal output
- `TerminalInputReader` — all terminal input parsing and validation

User-facing text lives in `src/main/resources/labels.properties` and is formatted with `MessageFormat` (aligned with the project i18n approach; no string concatenation in view code).

## Assumptions and notes

- Display methods write to the injected `PrintStream` via `TerminalDisplayWriter`; they do not mutate game state.
- Input methods read from the injected `Scanner` via `TerminalInputReader` and return parsed values without applying game rules.
- User-facing strings use `ResourceBundle` (`labels.properties`) with `MessageFormat` (no string concatenation in view code).
- `promptNope` prompts a single player (controller calls once per opponent during the Nope window).
- `readYesNo` accepts only `yes` or `no`; other answers are reprompted.
- `promptCardSelection` ignores out-of-range or non-numeric tokens and returns only valid hand cards.
- `readIntInRange` reprompts until the value is within the inclusive `[min, max]` bounds.
- `promptPlayerChoice` returns `DONE_PLAYING_CARDS` for any choice other than `1`.

### Method under test: `showMessage()`

spaces: message content (empty vs non-empty)

| test_Name                         | State of the System | Expected output              | Implemented?       |
|-----------------------------------|---------------------|------------------------------|--------------------|
| showMessage_NonEmpty_PrintsMessage | message = "Hello"   | stdout contains "Hello"      | :white_check_mark: |
| showMessage_Empty_PrintsBlankLine  | message = ""        | stdout contains blank line   | :white_check_mark: |

### Method under test: `showWinner()`

spaces: player name

| test_Name                              | State of the System | Expected output                 | Implemented?       |
|----------------------------------------|---------------------|---------------------------------|--------------------|
| showWinner_NamedPlayer_PrintsWinnerLine | player name "Alice" | stdout contains "Alice wins!" | :white_check_mark: |

### Method under test: `showCurrentPlayer()`

spaces: player name

| test_Name                                   | State of the System | Expected output                      | Implemented?       |
|---------------------------------------------|---------------------|--------------------------------------|--------------------|
| showCurrentPlayer_NamedPlayer_PrintsTurnHeader | player name "Bob"   | stdout contains "Bob's turn"       | :white_check_mark: |

### Method under test: `showGameState()`

spaces: deck size, discard size, active player count, turns remaining

| test_Name                              | State of the System                          | Expected output                                      | Implemented?       |
|----------------------------------------|----------------------------------------------|------------------------------------------------------|--------------------|
| showGameState_ActiveGame_PrintsSummary | deck size 2, discard 1, 2 players, 1 turn    | stdout contains deck, discard, active, turn fields  | :white_check_mark: |

### Method under test: `showPlayerHand()`

spaces: empty hand vs cards; peek cards empty vs present

| test_Name                                    | State of the System        | Expected output                          | Implemented?       |
|----------------------------------------------|----------------------------|------------------------------------------|--------------------|
| showPlayerHand_EmptyHand_PrintsHeaderOnly    | player with empty hand     | header only, no numbered cards           | :white_check_mark: |
| showPlayerHand_WithCards_PrintsNumberedCards | hand has one SKIP card     | stdout contains "1. SKIP"                | :white_check_mark: |

## Method under test: `showPeekCards()`

spaces: peek cards empty vs present

| test_Name                                             | State of the System      | Expected output                        | Implemented?       |
|-------------------------------------------------------|--------------------------|----------------------------------------|--------------------|
| showPeekCards_WithPeekCards_PrintsPeekSection         | peek buffer has cards    | stdout contains "Peek cards:"          | :white_check_mark: |
| showPeekCards_WithNoPeekCards_DoesNotPrintPeekSection | peek buffer has no cards | stdout does not contains "Peek cards:" | :white_check_mark: |


### Method under test: `promptNumPlayers()`

spaces: valid integer; invalid then valid input

| test_Name                                      | State of the System              | Expected output | Implemented?       |
|------------------------------------------------|----------------------------------|-----------------|--------------------|
| promptNumPlayers_ValidInput_ReturnsValue         | scanner input "3"                | returns 3       | :white_check_mark: |
| promptNumPlayers_InvalidThenValid_ReturnsValue | scanner input "x" then "4"       | returns 4       | :white_check_mark: |

### Method under test: `promptCardSelection()`

spaces: empty input; single index; multiple indices; out-of-range index

| test_Name                                           | State of the System              | Expected output        | Implemented?       |
|-----------------------------------------------------|----------------------------------|------------------------|--------------------|
| promptCardSelection_EmptyInput_ReturnsEmptyList     | hand has cards, input ""         | empty list             | :white_check_mark: |
| promptCardSelection_SingleIndex_ReturnsCard         | hand has cards, input "1"        | list size 1            | :white_check_mark: |
| promptCardSelection_MultipleIndices_ReturnsCards    | hand has 2 cards, input "1,2"    | list size 2            | :white_check_mark: |
| promptCardSelection_OutOfRangeIndex_ReturnsEmptyList | hand size 1, input "9"        | empty list             | :white_check_mark: |

### Method under test: `promptNope()`

spaces: null player; yes answer; no answer; invalid then yes

| test_Name                              | State of the System     | Expected output | Implemented?       |
|----------------------------------------|-------------------------|-----------------|--------------------|
| promptNope_NullPlayer_ThrowsIllegalArgumentException | player is null | throws IllegalArgumentException | :white_check_mark: |
| promptNope_YesAnswer_ReturnsTrue         | one player, input "yes" | true            | :white_check_mark: |
| promptNope_NoAnswer_ReturnsFalse         | one player, input "no"  | false           | :white_check_mark: |
| promptNope_InvalidThenYes_ReturnsTrue  | input "maybe" then "yes" | true           | :white_check_mark: |

### Method under test: `promptInsertPosition()`

spaces: value below min; value in range

| test_Name                                         | State of the System    | Expected output | Implemented?       |
|---------------------------------------------------|------------------------|-----------------|--------------------|
| promptInsertPosition_OutOfRangeThenValid_ReturnsValue | deckSize 2, "5" then "1" | returns 1     | :white_check_mark: |
| promptInsertPosition_MinBoundary_ReturnsZero      | deckSize 3, input "0"  | returns 0       | :white_check_mark: |

### Method under test: `promptTargetSelection()`

spaces: invalid then valid selection

| test_Name                                         | State of the System              | Expected output      | Implemented?       |
|---------------------------------------------------|----------------------------------|----------------------|--------------------|
| promptTargetSelection_InvalidThenValid_ReturnsPlayer | 2 candidates, "9" then "2"     | second candidate     | :white_check_mark: |

### Method under test: `promptCardType()`

spaces: valid index maps to enum value

| test_Name                                   | State of the System | Expected output              | Implemented?       |
|---------------------------------------------|---------------------|------------------------------|--------------------|
| promptCardType_FirstOption_ReturnsFirstType | input "1"           | CardType.EXPLODING_KITTEN    | :white_check_mark: |

### Method under test: `promptRestart()`

spaces: yes vs no answer

| test_Name                         | State of the System | Expected output | Implemented?       |
|-----------------------------------|---------------------|-----------------|--------------------|
| promptRestart_YesAnswer_ReturnsTrue | input "yes"         | true            | :white_check_mark: |
| promptRestart_NoAnswer_ReturnsFalse | input "n"           | false           | :white_check_mark: |

### Method under test: `promptPlayerChoice()`

spaces: play card option vs done option

| test_Name                                          | State of the System | Expected output                    | Implemented?       |
|----------------------------------------------------|---------------------|------------------------------------|--------------------|
| promptPlayerChoice_PlayCardOption_ReturnsPlayCard  | input "1"           | PlayerChoice.PLAY_CARD             | :white_check_mark: |
| promptPlayerChoice_DoneOption_ReturnsDonePlaying   | input "2"           | PlayerChoice.DONE_PLAYING_CARDS    | :white_check_mark: |
