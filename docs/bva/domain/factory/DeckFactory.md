# BVA Analysis for DeckFactory

## Public interface for this class
- constructor(numPlayers) --> Deck
- buildDeck() --> Deck
- buildExplodingKittenCards() --> Card[]
- buildDefuseCards() --> Card[]

## Assumptions and notes:
- this class assumes the input (num players) to the constructor is validated by the caller of the class thus does not include that rule in this BVA.
- all card types (except exploding kittens) have fixed card numbers that are nto affected by any input (not even number of players)

### Method under test: `DeckFactory(numPlayers)--constructor`

spaces: number, collection, range

cases:
- low - x (N/A since we can trust the number comingin is validated by the caller of this class)
- low 2
- high 5
- high +x (N/A since we can trust the number coming is validated by the caller of this class)
- empty (N/A fixed return deck size)
- one element (N/A fixed return deck size)
- more than one (N/A fixed return deck size)
- max size (N/A fixed return deck size)
- duplicate elements (N/A fixed return deck size)
- most positive (N/A fixed return deck size)
- most negative (N/A fixed return deck size)
- most positive + 1 (N/A fixed return deck size)
- most negative -1 (N/A fixed return deck size)

| test_Name                                 | State of the System | Expected output        | Implemented?              |
|-------------------------------------------|---------------------|------------------------|---------------------------|
| constructor_TwoPlayers_ReturnsDeckFactory | two players         | expected Deck of cards | :x: or :white_check_mark: |
| constructor_FivePlayers_ReturnsDeckFactory                   | five players        | expected Deck of cards | :white_check_mark:        |


### Method under test: `buildDeck()`
spaces: collection,

cases:
- empty (N/A fixed return deck size)
- one element (N/A fixed return deck size)
- more than one (N/A fixed return deck size)
- max size (N/A fixed return deck size)
- duplicate elements (N/A fixed return deck size)
- most positive (N/A fixed return deck size)
- most negative (N/A fixed return deck size)
- most positive + 1 (N/A fixed return deck size)
- most negative -1 (N/A fixed return deck size)

|                            | State of the System | Expected output | Implemented?              |
|----------------------------|---------------------|-----------------|---------------------------|
| buildDeck_ReturnsCleanDeck | no inputs           | Deck object     | :x: or :white_check_mark: |


### Method under test: `buildExplodingKittenCards()`
spaces: range, collection

cases:
- low - x (N/A since we can trust the number coming in is validated by the caller of this class)
- low 2
- high 5
- high +x (N/A since we can trust the number coming is validated by the caller of this class)
- empty (N/A fixed return  size)
- one element (N/A fixed return  size)
- more than one (N/A fixed return  size)
- max size (N/A fixed return  size)
- duplicate elements (N/A fixed return  size)
- most positive (N/A fixed return  size)
- most negative (N/A fixed return  size)
- most positive + 1 (N/A fixed return  size)
- most negative -1 (N/A fixed return  size)

|                                        | State of the System | Expected output          | Implemented?              |
|----------------------------------------|---------------------|--------------------------|---------------------------|
| buildExplodingKittenCards_ReturnsCards | two players         | 1 exploding kitten card  | :x: or :white_check_mark: |
| buildExplodingKittenCards_ReturnsCards             | five players        | 4 exploding kitten cards | :x: or :white_check_mark: |



### Method under test: `buildDefuseCards()`
spaces: collection,

cases:
- empty (N/A fixed return  size)
- one element (N/A fixed return  size)
- more than one (N/A fixed return  size)
- max size (N/A fixed return  size)
- duplicate elements (N/A fixed return  size)
- most positive (N/A fixed return  size)
- most negative (N/A fixed return  size)
- most positive + 1 (N/A fixed return  size)
- most negative -1 (N/A fixed return  size)

|                                          | State of the System | Expected output | Implemented?              |
|------------------------------------------|---------------------|-----------------|---------------------------|
| buildDefuseCards()_ReturnsSixDefuseCards | no inputs           | 6 defuse cards  | :x: or :white_check_mark: |
