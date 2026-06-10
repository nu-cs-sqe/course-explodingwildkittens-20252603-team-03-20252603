# BVA Analysis for GameState

## Public interface for this class

- `GameState(players: List<Player>, deck: Deck)` (constructor) → GameState with ACTIVE status and initialized players
- `isActive()` → boolean
- `endGame()` → void
- `getCurrentPlayer()` → Player
- `advancePlayer()` → void
- `eliminateCurrentPlayer()` → void
- `activePlayerCount()` → int
- `drawFromDeck()` → Card
- `isDeckEmpty()` → boolean
- `insertIntoDeck(card: Card, index: int)` → void
- `shuffleDeck()` → void
- `discardCard(card: Card)` → void
- `addCardToCurrentPlayer(card: Card)` → void
- `removeCardFromCurrentPlayer(card: Card)` → void
- `currentPlayerHasCard(type: CardType)` → boolean
- `getOtherActivePlayers()` → List<Player>
- `peekTopOfDeck(n: int)` → List<Card>
- `getDeckSize()` → int
- `insertPendingCardAt(position: int)` → void
- `turnState()` → TurnState

## Assumptions and notes

- On construction, `status` is `ACTIVE`, `activePlayers` is populated from the passed list (preserving order), and `eliminatedPlayers` is empty.
- `getCurrentPlayer()` returns the player at the front of the queue without removing them; throws `IllegalStateException` if the queue is empty.
- `advancePlayer()` rotates the front player to the back of the queue.
- `eliminateCurrentPlayer()` removes the front player from the active queue and adds them to the eliminated list; throws `IllegalStateException` if only 1 player remains.
- `activePlayerCount()` returns the size of the active players queue.
- `drawFromDeck()` delegates to `deck.drawTop()`; throws `IllegalStateException` if the deck is empty.
- `insertIntoDeck(card, index)` delegates to `deck.insertAt(card, index)`; throws `IndexOutOfBoundsException` for invalid indices.
- `peekTopOfDeck(n)` returns the top n cards without removing them; throws `IllegalArgumentException` if n exceeds deck size.
- `getOtherActivePlayers()` returns an unmodifiable list of all active players except the current one.
- `insertPendingCardAt(position)` retrieves the pending card from `turnState` and inserts it into the deck at the given position; throws `IllegalStateException` if there is no pending action.
- `turnState()` returns the `TurnState` object owned by this `GameState`.



### Method under test: `GameState(players, deck)` (constructor)

spaces: initial field values

cases:
- status initialized to ACTIVE
- activePlayers populated from passed list
- eliminatedPlayers starts empty

| test_Name                                          | State of the System              | Expected output                  | Implemented?       |
|----------------------------------------------------|----------------------------------|----------------------------------|--------------------|
| constructor_InitializesStatusActive                | new GameState with players, deck | isActive() = true                | :white_check_mark: |
| constructor_InitializesActivePlayerCount           | new GameState with 2 players     | activePlayerCount() = 2          | :white_check_mark: |
| constructor_InitializesEliminatedPlayersEmpty      | new GameState with players, deck | eliminated list is empty         | :white_check_mark: |



### Method under test: `isActive()`

spaces: status = {ACTIVE, ENDED}

cases:
- status is ACTIVE → true
- status is ENDED → false

| test_Name                         | State of the System            | Expected output | Implemented?        |
|-----------------------------------|--------------------------------|-----------------|---------------------|
| isActive_StatusActive_ReturnsTrue | status = ACTIVE                | true            | :white_check_mark:  |
| isActive_StatusEnded_ReturnsFalse | status = ENDED (after endGame) | false           | :white_check_mark:  |



### Method under test: `endGame()`

spaces: status = {ACTIVE, already ENDED}

cases:
- game is active → isActive() becomes false
- game already ended → stays false, no throw

| test_Name                                    | State of the System   | Expected output      | Implemented?       |
|----------------------------------------------|-----------------------|----------------------|--------------------|
| endGame_ActiveGame_SetsStatusEnded           | status = ACTIVE       | isActive() = false   | :white_check_mark: |
| endGame_AlreadyEnded_RemainsEnded            | status = ENDED        | isActive() = false   | :white_check_mark: |



### Method under test: `getCurrentPlayer()`

spaces: activePlayers size = {0, 1, 2+}

cases:
- empty queue → throw IllegalStateException
- one player → returns that player
- two or more players → returns the front player, not the second

| test_Name                                               | State of the System | Expected output           | Implemented?       |
|---------------------------------------------------------|---------------------|---------------------------|--------------------|
| getCurrentPlayer_EmptyQueue_ThrowsIllegalStateException | 0 active players    | IllegalStateException     | :white_check_mark: |
| getCurrentPlayer_OnePlayer_ReturnsThatPlayer            | 1 active player     | that player               | :white_check_mark: |
| getCurrentPlayer_MultiplePlayer_ReturnsFrontPlayer      | 2+ active players   | front player (not second) | :white_check_mark: |



### Method under test: `advancePlayer()`

spaces: activePlayers size = {1, 2+}

cases:
- one player → same player remains current after advance
- two or more players → front player moves to back; second player becomes current

| test_Name                                                    | State of the System | Expected output                            | Implemented?       |
|--------------------------------------------------------------|---------------------|--------------------------------------------|--------------------|
| advancePlayer_OnePlayer_SamePlayerRemainsCurrentPlayer       | 1 active player     | getCurrentPlayer() returns same player     | :white_check_mark: |
| advancePlayer_MultiplePlayers_NextPlayerBecomesCurrentPlayer | 2+ active players   | getCurrentPlayer() returns second player   | :white_check_mark: |
| advancePlayer_MultiplePlayers_PreviousCurrentMovesToBack     | 2+ active players   | prior front player is now at back of queue | :white_check_mark: |



### Method under test: `getNextPlayer()`

spaces: activePlayers size = {0, 1, 2, 3+}

cases:
- size = 0 (N/A — eliminateCurrentPlayer guards against dropping below 1 active player)
- size = 1 → throws IllegalStateException (lower boundary: only one player, no next exists)
- size = 2 → returns second player (lower boundary of valid range; exactly one other player)
- size = 3+ → returns the second player in queue, not the third

note: getNextPlayer reads the queue without modifying it; getCurrentPlayer() is unchanged after the call

| test_Name                                                            | State of the System | Expected output                         | Implemented?       |
|----------------------------------------------------------------------|---------------------|-----------------------------------------|--------------------|
| getNextPlayer_OnePlayer_ThrowsIllegalStateException                  | 1 active player     | IllegalStateException                   | :white_check_mark: |
| getNextPlayer_TwoPlayers_ReturnsSecondPlayer                         | 2 active players    | second player returned                  | :white_check_mark: |
| getNextPlayer_MultiplePlayers_ReturnsSecondNotThirdPlayer            | 3 active players    | second player returned, not third       | :white_check_mark: |



### Method under test: `eliminateCurrentPlayer()`

spaces: activePlayers size = {1, 2, 3+}

cases:
- one player → throw IllegalStateException (cannot eliminate the sole remaining winner)
- two players (boundary: eliminating one leaves exactly 1 winner) → activePlayerCount = 1; next player becomes current; eliminated list grows
- three or more players → activePlayerCount decrements by 1; next player becomes current; eliminated list grows

| test_Name                                                             | State of the System | Expected output                            | Implemented?       |
|-----------------------------------------------------------------------|---------------------|--------------------------------------------|--------------------|
| eliminateCurrentPlayer_OnePlayer_ThrowsIllegalStateException          | 1 active player     | IllegalStateException                      | :white_check_mark: |
| eliminateCurrentPlayer_TwoPlayers_ActiveCountBecomesOne               | 2 active players    | activePlayerCount() = 1                    | :white_check_mark: |
| eliminateCurrentPlayer_TwoPlayers_NextPlayerBecomesCurrentPlayer      | 2 active players    | getCurrentPlayer() returns second player   | :white_check_mark: |
| eliminateCurrentPlayer_TwoPlayers_PlayerMovesToEliminatedList         | 2 active players    | eliminated list contains eliminated player | :white_check_mark: |
| eliminateCurrentPlayer_MultiplePlayers_ActiveCountDecremented         | 3+ active players   | activePlayerCount() decrements by 1        | :white_check_mark: |
| eliminateCurrentPlayer_MultiplePlayers_NextPlayerBecomesCurrentPlayer | 3+ active players   | getCurrentPlayer() returns next player     | :white_check_mark: |
| eliminateCurrentPlayer_MultiplePlayers_PlayerMovesToEliminatedList    | 3+ active players   | eliminated list contains eliminated player | :white_check_mark: |



### Method under test: `activePlayerCount()`

spaces: activePlayers size = {0, 1, 2+}

cases:
- 0 players → 0
- 1 player → 1
- 2+ players → exact count

| test_Name                                      | State of the System | Expected output | Implemented?       |
|------------------------------------------------|---------------------|-----------------|--------------------|
| activePlayerCount_NoPlayers_ReturnsZero        | 0 active players    | 0               | :white_check_mark: |
| activePlayerCount_OnePlayer_ReturnsOne         | 1 active player     | 1               | :white_check_mark: |
| activePlayerCount_MultiplePlayers_ReturnsCount | 2+ active players   | count           | :white_check_mark: |



### Method under test: `drawFromDeck()`

spaces: deck size = {0, 1, 2+}

cases:
- empty deck → throw IllegalStateException
- one card → returns a card; deck becomes empty
- more than one card → returns top card; deck size decrements by 1

| test_Name                                          | State of the System | Expected output               | Implemented?       |
|----------------------------------------------------|---------------------|-------------------------------|--------------------|
| drawFromDeck_EmptyDeck_ThrowsIllegalStateException | empty deck          | IllegalStateException         | :white_check_mark: |
| drawFromDeck_OneCard_ReturnsCard                   | deck with 1 card    | non-null card                 | :white_check_mark: |
| drawFromDeck_OneCard_EmptiesDeck                   | deck with 1 card    | deck is empty after draw      | :white_check_mark: |
| drawFromDeck_MultipleCards_ReturnsTopCard          | deck with 2+ cards  | non-null card                 | :white_check_mark: |
| drawFromDeck_MultipleCards_DeckSizeDecrementsBy1   | deck with 2+ cards  | getDeckSize() decrements by 1 | :white_check_mark: |



### Method under test: `isDeckEmpty()`

spaces: deck size = {0, 1+}

cases:
- empty deck → true
- non-empty deck → false

| test_Name                             | State of the System | Expected output | Implemented?       |
|---------------------------------------|---------------------|-----------------|--------------------|
| isDeckEmpty_EmptyDeck_ReturnsTrue     | empty deck          | true            | :white_check_mark: |
| isDeckEmpty_NonEmptyDeck_ReturnsFalse | deck with 1+ cards  | false           | :white_check_mark: |



### Method under test: `insertIntoDeck(card, index)`

spaces: deck size = {0, 1, 2+}; index = {-1, 0, within bounds, out of bounds}

cases:
- index out of bounds → throw IndexOutOfBoundsException
- index 0 on empty deck → inserts at front
- index within bounds → inserts at position
- index one past last valid position → throw IndexOutOfBoundsException

| test_Name                                                                  | State of the System                | Expected output           | Implemented ?      |
|----------------------------------------------------------------------------|------------------------------------|---------------------------|--------------------|
| insertIntoDeck_InvalidIndex_ThrowsIndexOutOfBoundsException                | any deck, index -1                 | IndexOutOfBoundsException | :white_check_mark: |
| insertIntoDeck_EmptyDeckIndexZero_InsertsCard                              | empty deck, index 0                | deck size = 1             | :white_check_mark: |
| insertIntoDeck_EmptyDeckIndexOne_ThrowsIndexOutOfBoundsException           | empty deck, index 1                | IndexOutOfBoundsException | :white_check_mark: |
| insertIntoDeck_NonEmptyDeckValidIndex_InsertsCard                          | deck with 2+ cards, valid index    | deck size increments      | :white_check_mark: |
| insertIntoDeck_NonEmptyDeckIndexPastBounds_ThrowsIndexOutOfBoundsException | deck with 2+ cards, index past end | IndexOutOfBoundsException | :white_check_mark: |



### Method under test: `shuffleDeck()`

spaces: delegates to deck (not in BVA catalog — boundary cases covered by Deck.md)

cases:
- delegates shuffle to the deck
- calls shuffle() on the deck exactly once

| test_Name                          | State of the System | Expected output             | Implemented?       |
|------------------------------------|---------------------|-----------------------------|--------------------|
| shuffleDeck_DelegatesShuffleToDeck | deck with 2+ cards  | void                        | :white_check_mark: |
| shuffleDeck_CallsShuffleOnDeck     | mocked deck         | deck.shuffle() invoked once | :white_check_mark: |



### Method under test: `discardCard(card)`

spaces: discard pile size = {0, 1+}; null card

cases:
- null card → throw IllegalArgumentException
- empty discard pile → card added; discard size = 1
- non-empty discard pile → card appended; discard size increments by 1

| test_Name                                           | State of the System        | Expected output              | Implemented?       |
|-----------------------------------------------------|----------------------------|------------------------------|--------------------|
| discardCard_NullCard_ThrowsIllegalArgumentException | any state, null card       | IllegalArgumentException     | :white_check_mark: |
| discardCard_EmptyPile_AddsCard                      | empty discard pile         | discard pile size = 1        | :white_check_mark: |
| discardCard_NonEmptyPile_AppendsCard                | discard pile with 1+ cards | discard pile size increments | :white_check_mark: |



### Method under test: `addCardToCurrentPlayer(card)`

spaces: current player hand size = {0, 1+}

cases:
- empty hand → card added; hand size = 1
- non-empty hand → card appended; hand size increments by 1

| test_Name                                       | State of the System           | Expected output      | Implemented?       |
|-------------------------------------------------|-------------------------------|----------------------|--------------------|
| addCardToCurrentPlayer_EmptyHand_AddsCard       | current player has empty hand | hand size = 1        | :white_check_mark: |
| addCardToCurrentPlayer_NonEmptyHand_AppendsCard | current player has 1+ cards   | hand size increments | :white_check_mark: |



### Method under test: `removeCardFromCurrentPlayer(card)`

spaces: card in hand vs not in hand

cases:
- card is in hand → removed; hand size decrements by 1
- card is not in hand → hand unchanged

| test_Name                                               | State of the System            | Expected output         | Implemented?       |
|---------------------------------------------------------|--------------------------------|-------------------------|--------------------|
| removeCardFromCurrentPlayer_CardInHand_RemovesCard      | card is in current player hand | hand no longer has card | :white_check_mark: |
| removeCardFromCurrentPlayer_CardNotInHand_HandUnchanged | card not in hand               | hand unchanged          | :white_check_mark: |



### Method under test: `currentPlayerHasCard(type)`

spaces: matching type in hand = {present, absent}; empty hand

cases:
- empty hand → false
- matching type present → true
- no matching type → false

| test_Name                                             | State of the System            | Expected output | Implemented?       |
|-------------------------------------------------------|--------------------------------|-----------------|--------------------|
| currentPlayerHasCard_EmptyHand_ReturnsFalse           | current player hand is empty   | false           | :white_check_mark: |
| currentPlayerHasCard_MatchingType_ReturnsTrue         | current player has that type   | true            | :white_check_mark: |
| currentPlayerHasCard_NoMatchingType_ReturnsFalse      | current player lacks that type | false           | :white_check_mark: |



### Method under test: `getOtherActivePlayers()`

spaces: activePlayers size = {2, 3+}

cases:
- two players → list with the one other player
- three or more players → list with all players except current

| test_Name                                                        | State of the System | Expected output                    | Implemented?       |
|------------------------------------------------------------------|---------------------|------------------------------------|--------------------|
| getOtherActivePlayers_TwoPlayers_ReturnsListWithOnePlayer        | 2 active players    | list containing the other player   | :white_check_mark: |
| getOtherActivePlayers_MultiplePlayers_ReturnsAllButCurrent       | 3+ active players   | list of all players except current | :white_check_mark: |



### Method under test: `peekTopOfDeck(n)`

spaces: n = {0, within deck size, exceeds deck size}; deck size = {0, 1, 2+}

cases:
- n = 0 → empty list
- n within deck size → returns top n cards without removing them
- n exceeds deck size → throw IllegalArgumentException

| test_Name                                                     | State of the System       | Expected output          | Implemented?       |
|---------------------------------------------------------------|---------------------------|--------------------------|--------------------|
| peekTopOfDeck_ZeroCards_ReturnsEmptyList                      | any deck, n = 0           | empty list               | :white_check_mark: |
| peekTopOfDeck_NWithinDeckSize_ReturnsTopNCards                | deck with 3+ cards, n = 3 | list of top 3 cards      | :white_check_mark: |
| peekTopOfDeck_NExceedsDeckSize_ThrowsIllegalArgumentException | deck with 2 cards, n = 3  | IllegalArgumentException | :white_check_mark: |



### Method under test: `getDeckSize()`

spaces: deck size = {0, 1, 2+}

cases:
- 0 → 0
- 1 → 1
- 2+ → exact count

| test_Name                              | State of the System | Expected output | Implemented?           |
|----------------------------------------|---------------------|-----------------|------------------------|
| getDeckSize_EmptyDeck_ReturnsZero      | empty deck          | 0               | :white_check_mark:     |
| getDeckSize_OneCard_ReturnsOne         | deck with 1 card    | 1               | :white_check_mark:     |
| getDeckSize_MultipleCards_ReturnsCount | deck with 2+ cards  | count           | :white_check_mark:     |



### Method under test: `insertPendingCardAt(position)`

spaces: pendingAction = {present, absent}; position = {valid, out of bounds}

cases:
- no pending action → throw IllegalStateException
- valid position → pending card inserted into deck at that position
- out of bounds position → throw IndexOutOfBoundsException (propagated from deck)

| test_Name                                                               | State of the System                    | Expected output           | Implemented?       |
|-------------------------------------------------------------------------|----------------------------------------|---------------------------|--------------------|
| insertPendingCardAt_NoPendingAction_ThrowsIllegalStateException         | no pending action in turnState         | IllegalStateException     | :white_check_mark: |
| insertPendingCardAt_ValidPosition_InsertsCardIntoDeck                   | pending action set, valid position     | card in deck at position  | :white_check_mark: |
| insertPendingCardAt_InvalidPosition_ThrowsIndexOutOfBoundsException     | pending action set, invalid position   | IndexOutOfBoundsException | :white_check_mark: |



### Method under test: `turnState()`

spaces: returns field (not in BVA catalog)

cases:
- returns the TurnState owned by this GameState

| test_Name                         | State of the System | Expected output             | Implemented?       |
|-----------------------------------|---------------------|-----------------------------|--------------------|
| turnState_ReturnsNonNullTurnState | any GameState       | non-null TurnState instance | :white_check_mark: |