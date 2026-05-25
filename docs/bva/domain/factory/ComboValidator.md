# BVA Analysis for ComboValidator

## Public interface for this class
- isValid(cards) → boolean
- resolveAction(cards) → CardAction

## Assumptions and notes
- null input to isValid returns false (per Clean Code Ch.7: do not pass null).
- Cat cards require exactly 2 or 3 matching cards to form a valid combo; mixed types are not valid.
- EXPLODING_KITTEN and DEFUSE cannot be played from hand as normal actions.
- resolveAction throws IllegalArgumentException for any invalid combo.



### Method under test: `isValid()`
spaces: collection size, card types

cases:
- null: false
- empty (0 cards): false
- one action card (SKIP, ATTACK, SHUFFLE, SEE_THE_FUTURE, FAVOR, NOPE): true
- one CAT_CARD: false
- one EXPLODING_KITTEN: false
- one DEFUSE: false
- two same CAT_CARD type: true
- two different CAT_CARD types: false
- two cards where one is not a CAT_CARD: false
- three same CAT_CARD type: true
- three different CAT_CARD types: false
- four or more cards: false
- max size (N/A)

| test_Name                                             | State of the System         | Expected output | Implemented? |
|-------------------------------------------------------|-----------------------------|-----------------|--------------|
| isValid_NullList_ReturnsFalse                         | null passed                 | false           | :x:          |
| isValid_EmptyList_ReturnsFalse                        | 0 cards                     | false           | :x:          |
| isValid_OneActionCard_ReturnsTrue                     | 1 SKIP card                 | true            | :x:          |
| isValid_OneCatCard_ReturnsFalse                       | 1 CAT_CARD                  | false           | :x:          |
| isValid_OneExplodingKitten_ReturnsFalse               | 1 EXPLODING_KITTEN          | false           | :x:          |
| isValid_OneDefuse_ReturnsFalse                        | 1 DEFUSE                    | false           | :x:          |
| isValid_TwoMatchingCatCards_ReturnsTrue               | 2 TACO_CAT cards            | true            | :x:          |
| isValid_TwoDifferentCatCards_ReturnsFalse             | 1 TACO_CAT + 1 CATTERMELON  | false           | :x:          |
| isValid_TwoCardsOneNotCat_ReturnsFalse                | 1 TACO_CAT + 1 SKIP         | false           | :x:          |
| isValid_ThreeMatchingCatCards_ReturnsTrue             | 3 TACO_CAT cards            | true            | :x:          |
| isValid_ThreeDifferentCatCards_ReturnsFalse           | 3 different cat types       | false           | :x:          |
| isValid_FourOrMoreCards_ReturnsFalse                  | 4 TACO_CAT cards            | false           | :x:          |



### Method under test: `resolveAction()`
spaces: card type, collection size

cases:
- 1 SKIP: SkipAction
- 1 ATTACK: AttackAction
- 1 SHUFFLE: ShuffleAction
- 1 SEE_THE_FUTURE: SeeTheFutureAction
- 1 FAVOR: FavorAction
- 1 NOPE: NopeAction
- 2 matching CAT_CARD: TwoCatAction
- 3 matching CAT_CARD: ThreeCatAction
- invalid combo: IllegalArgumentException
- max size (N/A)

| test_Name                                                 | State of the System    | Expected output                  | Implemented? |
|-----------------------------------------------------------|------------------------|----------------------------------|--------------|
| resolveAction_OneSkip_ReturnsSkipAction                   | 1 SKIP card            | SkipAction instance              | :x:          |
| resolveAction_OneAttack_ReturnsAttackAction               | 1 ATTACK card          | AttackAction instance            | :x:          |
| resolveAction_OneShuffle_ReturnsShuffleAction             | 1 SHUFFLE card         | ShuffleAction instance           | :x:          |
| resolveAction_OneSeeFuture_ReturnsSeeTheFutureAction      | 1 SEE_THE_FUTURE card  | SeeTheFutureAction instance      | :x:          |
| resolveAction_OneFavor_ReturnsFavorAction                 | 1 FAVOR card           | FavorAction instance             | :x:          |
| resolveAction_OneNope_ReturnsNopeAction                   | 1 NOPE card            | NopeAction instance              | :x:          |
| resolveAction_TwoMatchingCats_ReturnsTwoCatAction         | 2 TACO_CAT cards       | TwoCatAction instance            | :x:          |
| resolveAction_ThreeMatchingCats_ReturnsThreeCatAction     | 3 TACO_CAT cards       | ThreeCatAction instance          | :x:          |
| resolveAction_InvalidCombo_ThrowsIllegalArgumentException | invalid combo          | IllegalArgumentException         | :x:          |
