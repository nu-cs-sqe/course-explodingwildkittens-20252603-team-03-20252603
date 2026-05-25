# BVA Analysis for Player

## Public interface for this class

- constructor(id, name) → returns a Player with empty hand and peek buffer
- getId() → String
- getName() → String
- getHand() → unmodifiable view of hand
- getPeekCards() → unmodifiable view of peek buffer
- addCard(card) → void
- removeCard(card) → void
- hasCard(type) → boolean
- removeCardOfType(type) → Optional<Card>; removes the first matching card from the hand when present
- storePeek(cards) → void
- clearPeek() → void

## Assumptions and notes

- `Card` uses default `equals` / `hashCode`; `removeCard` removes by object identity (same reference as in the hand).
- `storePeek` replaces the entire peek buffer; `null` argument is treated like an empty list (clears peek, adds nothing).
- `removeCardOfType` removes and returns the first card in hand order that matches the type; otherwise returns an empty Optional and leaves the hand unchanged.

### Method under test: `Player(id, name)` (constructor)

spaces: hand size, peek size, id/name assignment

| test_Name                              | State of the System        | Expected output              | Implemented?       |
|----------------------------------------|-----------------------------|------------------------------|--------------------|
| constructor_SetsIdAndName              | any id and name strings     | getId/getName match          | :white_check_mark: |
| constructor_InitializesEmptyHand       | new player                  | getHand is empty             | :white_check_mark: |
| constructor_InitializesEmptyPeekBuffer | new player                  | getPeekCards is empty        | :white_check_mark: |

### Method under test: `addCard()`

spaces: hand size before add (0 vs ≥1)

| test_Name                        | State of the System | Expected output     | Implemented?       |
|----------------------------------|---------------------|---------------------|--------------------|
| addCard_EmptyHand_AddsCard       | empty hand          | hand size 1, card present | :white_check_mark: |
| addCard_NonEmptyHand_AppendsCard | hand has one card   | hand size 2, order preserved | :white_check_mark: |

### Method under test: `removeCard()`

spaces: card reference in hand or not; hand empty or not

| test_Name                              | State of the System        | Expected output     | Implemented?       |
|----------------------------------------|----------------------------|---------------------|--------------------|
| removeCard_CardInHand_RemovesCard      | same ref in hand           | hand no longer contains card | :white_check_mark: |
| removeCard_CardNotInHand_Unchanged     | different ref, same fields | hand unchanged      | :white_check_mark: |
| removeCard_EmptyHand_DoesNothing       | empty hand, remove call    | still empty         | :white_check_mark: |

### Method under test: `hasCard()`

spaces: empty hand; matching type present; no matching type

| test_Name                           | State of the System   | Expected output | Implemented?       |
|-------------------------------------|-----------------------|-----------------|--------------------|
| hasCard_EmptyHand_ReturnsFalse      | empty hand            | false           | :white_check_mark: |
| hasCard_MatchingType_ReturnsTrue    | hand has that type    | true            | :white_check_mark: |
| hasCard_NoMatchingType_ReturnsFalse | hand has other types  | false           | :white_check_mark: |

### Method under test: `removeCardOfType()`

spaces: no match; single match; multiple matches (first wins)

| test_Name                                    | State of the System      | Expected output | Implemented?       |
|----------------------------------------------|--------------------------|-----------------|--------------------|
| removeCardOfType_NoMatch_ReturnsEmpty        | hand has no such type    | empty Optional, hand unchanged | :white_check_mark: |
| removeCardOfType_OneMatch_RemovesAndReturnsThatCard | one matching card | Optional with that card, hand no longer contains it | :white_check_mark: |
| removeCardOfType_MultipleMatches_RemovesFirstInHandOrder | two matching, order known | first removed, second remains | :white_check_mark: |

### Method under test: `storePeek()`

spaces: null vs non-null list; replace behavior

| test_Name                              | State of the System     | Expected output        | Implemented?       |
|----------------------------------------|-------------------------|------------------------|--------------------|
| storePeek_Null_ClearsPeek              | had peek cards before   | peek empty             | :white_check_mark: |
| storePeek_NonNull_ReplacesPeek         | prior peek + new list   | peek equals new cards  | :white_check_mark: |
| storePeek_SecondCall_ReplacesFirst     | two storePeek in sequence | only second content  | :white_check_mark: |

### Method under test: `clearPeek()`

spaces: peek empty vs non-empty

| test_Name                       | State of the System | Expected output | Implemented?       |
|---------------------------------|---------------------|-----------------|--------------------|
| clearPeek_NonEmpty_ClearsPeek   | peek has cards      | peek empty      | :white_check_mark: |
| clearPeek_AlreadyEmpty_NoThrow  | peek empty          | still empty     | :white_check_mark: |

### Method under test: `getHand()` / `getPeekCards()` (encapsulation)

spaces: mutating returned view

| test_Name                    | State of the System | Expected output              | Implemented?       |
|------------------------------|---------------------|------------------------------|--------------------|
| getHand_IsUnmodifiable       | any player          | add on view throws           | :white_check_mark: |
| getPeekCards_IsUnmodifiable  | any player          | add on view throws           | :white_check_mark: |
