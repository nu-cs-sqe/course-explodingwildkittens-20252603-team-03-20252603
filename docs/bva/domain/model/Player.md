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
- getCardOfType(type) → Card or null
- storePeek(cards) → void
- clearPeek() → void

## Assumptions and notes

- `Card` uses default `equals` / `hashCode`; `removeCard` removes by object identity (same reference as in the hand).
- `storePeek` replaces the entire peek buffer; `null` argument is treated like an empty list (clears peek, adds nothing).
- `getCardOfType` returns the first card in hand order that matches the type.

### Method under test: `Player(id, name)` (constructor)

spaces: hand size, peek size, id/name assignment

| test_Name                              | State of the System        | Expected output              | Implemented?       |
|----------------------------------------|-----------------------------|------------------------------|--------------------|
| constructor_SetsIdAndName              | any id and name strings     | getId/getName match          | :x: |
| constructor_InitializesEmptyHand       | new player                  | getHand is empty             | :x: |
| constructor_InitializesEmptyPeekBuffer | new player                  | getPeekCards is empty        | :x: |

### Method under test: `addCard()`

spaces: hand size before add (0 vs ≥1)

| test_Name                        | State of the System | Expected output     | Implemented?       |
|----------------------------------|---------------------|---------------------|--------------------|
| addCard_EmptyHand_AddsCard       | empty hand          | hand size 1, card present | :x: |
| addCard_NonEmptyHand_AppendsCard | hand has one card   | hand size 2, order preserved | :x: |

### Method under test: `removeCard()`

spaces: card reference in hand or not; hand empty or not

| test_Name                              | State of the System        | Expected output     | Implemented?       |
|----------------------------------------|----------------------------|---------------------|--------------------|
| removeCard_CardInHand_RemovesCard      | same ref in hand           | hand no longer contains card | :x: |
| removeCard_CardNotInHand_Unchanged     | different ref, same fields | hand unchanged      | :x: |
| removeCard_EmptyHand_DoesNothing       | empty hand, remove call    | still empty         | :x: |

### Method under test: `hasCard()`

spaces: empty hand; matching type present; no matching type

| test_Name                           | State of the System   | Expected output | Implemented?       |
|-------------------------------------|-----------------------|-----------------|--------------------|
| hasCard_EmptyHand_ReturnsFalse      | empty hand            | false           | :x: |
| hasCard_MatchingType_ReturnsTrue    | hand has that type    | true            | :x: |
| hasCard_NoMatchingType_ReturnsFalse | hand has other types  | false           | :x: |

### Method under test: `getCardOfType()`

spaces: no match; single match; multiple matches (first wins)

| test_Name                                    | State of the System      | Expected output | Implemented?       |
|----------------------------------------------|--------------------------|-----------------|--------------------|
| getCardOfType_NoMatch_ReturnsNull            | hand has no such type    | null            | :x: |
| getCardOfType_OneMatch_ReturnsThatCard       | one matching card        | that reference  | :x: |
| getCardOfType_MultipleMatches_ReturnsFirst   | two matching, order known| first in hand   | :x: |

### Method under test: `storePeek()`

spaces: null vs non-null list; replace behavior

| test_Name                              | State of the System     | Expected output        | Implemented?       |
|----------------------------------------|-------------------------|------------------------|--------------------|
| storePeek_Null_ClearsPeek              | had peek cards before   | peek empty             | :x: |
| storePeek_NonNull_ReplacesPeek         | prior peek + new list   | peek equals new cards  | :x: |
| storePeek_SecondCall_ReplacesFirst     | two storePeek in sequence | only second content  | :x: |

### Method under test: `clearPeek()`

spaces: peek empty vs non-empty

| test_Name                       | State of the System | Expected output | Implemented?       |
|---------------------------------|---------------------|-----------------|--------------------|
| clearPeek_NonEmpty_ClearsPeek   | peek has cards      | peek empty      | :x: |
| clearPeek_AlreadyEmpty_NoThrow  | peek empty          | still empty     | :x: |

### Method under test: `getHand()` / `getPeekCards()` (encapsulation)

spaces: mutating returned view

| test_Name                    | State of the System | Expected output              | Implemented?       |
|------------------------------|---------------------|------------------------------|--------------------|
| getHand_IsUnmodifiable       | any player          | add on view throws           | :x: |
| getPeekCards_IsUnmodifiable  | any player          | add on view throws           | :x: |
