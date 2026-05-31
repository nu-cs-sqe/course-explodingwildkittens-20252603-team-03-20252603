# BVA Analysis for Deck



## Public interface for this class
- constructor(cards) --> returns a Deck object
- shuffle() --> shuffles the list
- drawtop() --> removes the first Card
- insertAt() --> insert at this index
- size() --> return length of list
- isEmpty() --> boolean if deck is empty
- countCardsByName() --> returns number of cards of this name
- dealCards() --> returns a list of cards
- addToDeck() --> adds cards to the deck



## Assumptions and notes:
- N/A



### Method under test: `shuffle()`
spaces: collection

cases:
- empty()
- one element
- more than one element
- max size (N/A)
- duplicate elements (N/A)
- using first element (N/A)
- using last element (N/A)
- subset is empty (N/A)
- subset has exactly one element (N/A)
- subset has all but one element (N/A)
- subset is same as original (N/A)

| test_Name                                   | State of the System     | Expected output | Implemented?       |
|---------------------------------------------|-------------------------|-----------------|--------------------|
| shuffle_EmptyDeck_Shuffles                  | empty Deck              | void            | :white_check_mark: |
| shuffle_DeckWithOneElement_Shuffles         | deck with one           | void            | :white_check_mark: |
| shuffle_DeckWithMoreThanOneElement_Shuffles | deck with more than one | void            | :white_check_mark: |




### Method under test: `drawTop()`
spaces: collection

cases:
- empty()
- one element
- more than one element
- max size (N/A)
- duplicate elements (N/A)
- using first element (N/A)
- using last element (N/A)
- subset is empty (N/A)
- subset has exactly one element (N/A)
- subset has all but one element (N/A)
- subset is same as original (N/A)

| test_Name                                         | State of the System     | Expected output | Implemented?       |
|---------------------------------------------------|-------------------------|-----------------|--------------------|
| drawTop_EmptyDeck_ThrowsIllegalStateException     | empty Deck              | exception       | :white_check_mark: |
| drawTop_DeckWithOneElement_RemovesOneCard         | deck with one           | one Card        | :white_check_mark: |
| drawTop_DeckWithMoreThanOneElement_RemovesOneCard | deck with more than one | one Card        | :white_check_mark: |




### Method under test: `insertAt()`
spaces: collection, index

cases:
- empty()
- one element
- more than one element
- index is -1
- index is 0
- index is largest valid value
- index is largest valid value + 1
- max size (N/A)
- duplicate elements (N/A)
- using first element (N/A)
- using last element (N/A)
- subset is empty (N/A)
- subset has exactly one element (N/A)
- subset has all but one element (N/A)
- subset is same as original (N/A)

whitebox:
- index on boundary (n ->size of deck); deck with at least one item --> adds one
- index one past boundary(n+1 ->size of deck + 1); deck with at least one item --> exception

| test_Name                                                                                   | State of the System    | Expected output | Implemented?       |
|---------------------------------------------------------------------------------------------|------------------------|-----------------|--------------------|
| insertAt_EmptyDeckIndexZero_AddsOne                                                         | empty deck, index 0    | void            | :white_check_mark: |
| insertAt_EmptyDeckIndexOne_ThrowsIndexOutOfBoundsException                                  | empty deck, index 1    | exception       | :white_check_mark: |
| insertAt_DeckWithOneElementIndexWithinBoundsIndexZero_AddsOne                               | deck with one, index 0 | void            | :white_check_mark: |
| insertAt_DeckWithOneElementIndexWithinBoundsIndexOne_AddsOne                                | deck with one, index 1 | void            | :white_check_mark: |
| insertAt_DeckWithOneElementIndexLargerThanBoundsIndexTwo_ThrowsIndexOutOfBoundsException    | deck with one; index 2 | exception       | :white_check_mark: |
| insertAt_DeckWithMoreThanOneElementWithinBounds_AddsOne                                     | deck with 3; index 1   | void            | :white_check_mark: |
| insertAt_DeckWithMoreThanOneElementLargerThanBounds_ThrowsIndexOutOfBoundsException         | deck with 3; index 4   | exception       | :white_check_mark: |
| insertAt_DeckWithMoreThanOneElementIndexMinusOne_ThrowsIndexOutOfBoundsException            | deck with 3; index -1  | exception       | :white_check_mark: |
| insertAt_DeckWithMoreThanOneElementIndexZero_AddsOne                                        | deck with 3; index 0   | void            | :white_check_mark: |
| insertAt_DeckWithMoreThanOneElementLargestValidIndex_AddsOne                                | deck with 3; index 3   | void            | :white_check_mark: |
| insertAt_DeckWithMoreThanOneElementLargestValidIndexPlusOne_ThrowsIndexOutOfBoundsException | deck with 3; index 4   | exception       | :white_check_mark: |



### Method under test: `size()`
spaces: collection

cases:
- empty()
- one element
- more than one element
- max size (N/A)
- duplicate elements (N/A)
- using first element (N/A)
- using last element (N/A)
- subset is empty (N/A)
- subset has exactly one element (N/A)
- subset has all but one element (N/A)
- subset is same as original (N/A)

| test_Name                                                                   | State of the System     | Expected output | Implemented?       |
|-----------------------------------------------------------------------------|-------------------------|-----------------|--------------------|
| size_EmptyDeck_ReturnsZero                                                  | empty Deck              | 0               | :white_check_mark: |
| size_DeckWithOneElement_ReturnsOne                                          | deck with one           | 1               | :white_check_mark: |
| size_DeckWithMoreThanOneElement_ReturnsSize                                 | deck with more than one | size of deck    | :white_check_mark: |




### Method under test: `isEmpty()`
spaces: collection

cases:
- empty()
- one element
- more than one element
- max size (N/A)
- duplicate elements (N/A)
- using first element (N/A)
- using last element (N/A)
- subset is empty (N/A)
- subset has exactly one element (N/A)
- subset has all but one element (N/A)
- subset is same as original (N/A)

| test_Name                                       | State of the System     | Expected output | Implemented?       |
|-------------------------------------------------|-------------------------|-----------------|--------------------|
| isEmpty_EmptyDeck_ReturnsTrue                   | empty Deck              | true            | :white_check_mark: |
| isEmpty_DeckWithOneElement_ReturnsFalse         | deck with one           | false           | :white_check_mark: |
| isEmpty_DeckWithMoreThanOneElement_ReturnsFalse | deck with more than one | false           | :white_check_mark: |




### Method under test: `countCardsByName()`
spaces: collection

cases:
- empty()
- one element
- more than one element
- one instance of that name
- many instances of that name
- no instances of that name
- max size (N/A)
- duplicate elements (N/A)
- using first element (N/A)
- using last element (N/A)
- subset is empty (N/A)
- subset has exactly one element (N/A)
- subset has all but one element (N/A)
- subset is same as original (N/A)

| test_Name                                                                                | State of the System                                | Expected output | Implemented?       |
|------------------------------------------------------------------------------------------|----------------------------------------------------|-----------------|--------------------|
| countCardsByName_EmptyDeck_ReturnsZero                                                   | empty Deck                                         | 0               | :white_check_mark: |
| countCardsByName_DeckWithOneElementOfThatName_ReturnsOne                                 | deck with one                                      | 1               | :white_check_mark: |
| countCardsByName_DeckWithMoreThanOneElementOfThatName_ReturnsNumberOfInstancesOfThatName | deck with more than one                            | number          | :white_check_mark: |
| countCardsByName_DeckWithMoreThanOneElementButNoElementsOfThatName_ReturnsZero           | deck with more than one element; none of that name | 0               | :white_check_mark: |



### Method under test: `dealCards()`
spaces: collection, count

cases:
- empty()
- one element
- more than one element with more than 'count' cards
- more than one element with less than 'count' cards
- more than one element with 'count' cards
- count -1 (N/A)
- count 0 (N/A)
- count 1 (N/A)
- count >1
- max size (N/A)
- duplicate elements (N/A)
- using first element (N/A)
- using last element (N/A)
- subset is empty (N/A)
- subset has exactly one element (N/A)
- subset has all but one element (N/A)
- subset is same as original (N/A)

| test_Name                                                                 | State of the System                                            | Expected output | Implemented?       |
|---------------------------------------------------------------------------|----------------------------------------------------------------|-----------------|--------------------|
| dealCards_emptyDeck_throwsException                                       | empty Deck                                                     | exception       | :white_check_mark: |
| dealCards_deckWithOneElement_throwsException                              | Deck with one element                                          | exception       | :white_check_mark: |
| dealCards_deckWithMoreThanOneElementMoreThanCountCards_returnsCards       | Deck with more than one element more than count cards, count 7 | returns cards   | :white_check_mark: |
| dealCards_deckWithMoreThanOneElementLessThanCountCards_throwsException    | Deck with more than one element less than count cards          | exception       | :white_check_mark: |
| dealCards_deckWithMoreThanOneElementWithCountCardsSevenCount_returnsCards | Deck with more than one element with count cards, count 7      | returns cards   | :white_check_mark: |



### Method under test: `addToDeck()`
spaces: collection,

cases:
- empty()
- one element
- more than one cards
- max size (N/A)
- duplicate elements (N/A)
- using first element (N/A)
- using last element (N/A)
- subset is empty (N/A)
- subset has exactly one element (N/A)
- subset has all but one element (N/A)
- subset is same as original (N/A)

| test_Name                           | State of the System | Expected output | Implemented?       |
|-------------------------------------|--------------------|-----------------|--------------------|
| addToDeck_emptyCards_addsNoCards    | empty cards        | adds no cards   | :white_check_mark: |
| addToDeck_oneCard_addsCard          | one card           | adds one card   | :white_check_mark: |
| addToDeck_moreThanOneCard_addsCards | more than one card | adds cards      | :white_check_mark: |