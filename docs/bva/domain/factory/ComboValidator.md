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
- one action card (SKIP, ATTACK, SHUFFLE, SEE_THE_FUTURE, FAVOR): true
- one NOPE: false
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

| test_Name                                             | State of the System         | Expected output | Implemented?       |
|-------------------------------------------------------|-----------------------------|-----------------|--------------------|
| isValid_NullList_ReturnsFalse                         | null passed                 | false           | :white_check_mark: |
| isValid_EmptyList_ReturnsFalse                        | 0 cards                     | false           | :white_check_mark: |
| isValid_OneActionCard_ReturnsTrue                     | 1 SKIP card                 | true            | :white_check_mark: |
| isValid_OneNope_ReturnsFalse                          | 1 NOPE card                 | false           | :white_check_mark: |
| isValid_OneCatCard_ReturnsFalse                       | 1 CAT_CARD                  | false           | :white_check_mark: |
| isValid_OneExplodingKitten_ReturnsFalse               | 1 EXPLODING_KITTEN          | false           | :white_check_mark: |
| isValid_OneDefuse_ReturnsFalse                        | 1 DEFUSE                    | false           | :white_check_mark: |
| isValid_TwoMatchingCatCards_ReturnsTrue               | 2 TACO_CAT cards            | true            | :white_check_mark: |
| isValid_TwoDifferentCatCards_ReturnsFalse             | 1 TACO_CAT + 1 CATTERMELON  | false           | :white_check_mark: |
| isValid_TwoCardsOneNotCat_ReturnsFalse                | 1 TACO_CAT + 1 SKIP         | false           | :white_check_mark: |
| isValid_ThreeMatchingCatCards_ReturnsTrue             | 3 TACO_CAT cards            | true            | :white_check_mark: |
| isValid_ThreeDifferentCatCards_ReturnsFalse           | 3 different cat types       | false           | :white_check_mark: |
| isValid_FourOrMoreCards_ReturnsFalse                  | 4 TACO_CAT cards            | false           | :white_check_mark: |



### Method under test: `resolveAction()`
spaces: card type, collection size

cases:
- 1 SKIP: SkipAction
- 1 ATTACK: AttackAction
- 1 SHUFFLE: ShuffleAction
- 1 SEE_THE_FUTURE: SeeTheFutureAction
- 1 FAVOR: FavorAction
- 1 NOPE: IllegalArgumentException
- 2 matching CAT_CARD: TwoCatAction
- 3 matching CAT_CARD: ThreeCatAction
- invalid combo: IllegalArgumentException
- max size (N/A)

| test_Name                                                 | State of the System    | Expected output                  | Implemented?       |
|-----------------------------------------------------------|------------------------|----------------------------------|--------------------|
| resolveAction_OneSkip_ReturnsSkipAction                   | 1 SKIP card            | SkipAction instance              | :white_check_mark: |
| resolveAction_OneAttack_ReturnsAttackAction               | 1 ATTACK card          | AttackAction instance            | :white_check_mark: |
| resolveAction_OneShuffle_ReturnsShuffleAction             | 1 SHUFFLE card         | ShuffleAction instance           | :white_check_mark: |
| resolveAction_OneSeeFuture_ReturnsSeeTheFutureAction      | 1 SEE_THE_FUTURE card  | SeeTheFutureAction instance      | :white_check_mark: |
| resolveAction_OneFavor_ReturnsFavorAction                 | 1 FAVOR card           | FavorAction instance             | :white_check_mark: |
| resolveAction_OneNope_ThrowsIllegalArgumentException      | 1 NOPE card            | IllegalArgumentException         | :white_check_mark: |
| resolveAction_TwoMatchingCats_ReturnsTwoCatAction         | 2 TACO_CAT cards       | TwoCatAction instance            | :white_check_mark: |
| resolveAction_ThreeMatchingCats_ReturnsThreeCatAction     | 3 TACO_CAT cards       | ThreeCatAction instance          | :white_check_mark: |
| resolveAction_InvalidCombo_ThrowsIllegalArgumentException | invalid combo          | IllegalArgumentException         | :white_check_mark: |
