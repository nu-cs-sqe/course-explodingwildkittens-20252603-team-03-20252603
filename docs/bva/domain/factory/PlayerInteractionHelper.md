# BVA Analysis for PlayerInteractionHelper

## Public interface for this class
- stealRandomCard(from, to) → void
- stealNamedCard(from, to, type) → void
- pickTarget(candidates) → Player
- pickCardType() → CardType
- giveCard(from, to) → void

## Assumptions and notes
- stealRandomCard does nothing if `from`'s hand is empty; no exception is thrown.
- stealNamedCard does nothing if `from` does not have the named type; no exception is thrown.
- pickTarget and pickCardType delegate entirely to domain.input.IPlayerInput — no additional BVA needed beyond the delegation itself.
- giveCard prompts `from` to choose a card via domain.input.IPlayerInput.promptCardSelection and transfers the first chosen card.
- giveCard does nothing if the prompted selection is empty.
- giveCard does nothing if the selected card is not in `from`'s hand.



### Method under test: `stealRandomCard()`
spaces: collection (from hand size)

cases:
- empty: no card transferred
- one element: that card transferred
- more than one element: one random card transferred
- max size (N/A)

| test_Name                                          | State of the System  | Expected output                          | Implemented?       |
|----------------------------------------------------|----------------------|------------------------------------------|--------------------|
| stealRandomCard_FromEmptyHand_NoTransfer           | from has 0 cards     | no card moved                            | :white_check_mark: |
| stealRandomCard_FromOneCard_TransfersThatCard      | from has 1 card      | from loses it, to gains it               | :white_check_mark: |
| stealRandomCard_FromManyCards_TransfersOneCard     | from has 3 cards     | from loses exactly 1, to gains exactly 1 | :white_check_mark: |



### Method under test: `stealNamedCard()`
spaces: presence of named type in from hand

cases:
- type not present: no transfer
- exactly one of type: that card transferred
- more than one of type: one card of that type transferred
- empty hand (N/A — covered by type not present)

| test_Name                                          | State of the System  | Expected output                    | Implemented?       |
|----------------------------------------------------|----------------------|------------------------------------|--------------------|
| stealNamedCard_TypeNotPresent_NoTransfer           | from has no SKIP     | no card moved                      | :white_check_mark: |
| stealNamedCard_OneOfType_TransfersThatCard         | from has 1 SKIP      | from loses SKIP, to gains SKIP     | :white_check_mark: |
| stealNamedCard_MultipleOfType_TransfersOne         | from has 2 SKIP      | from loses 1 SKIP, to gains 1 SKIP | :white_check_mark: |



### Method under test: `giveCard()`
spaces: from hand size after promptCardSelection

cases:
- promptCardSelection returns empty list: no transfer
- promptCardSelection returns one card: that card transferred
- promptCardSelection returns many (N/A — only first card is taken)

| test_Name                                   | State of the System                       | Expected output            | Implemented?       |
|---------------------------------------------|-------------------------------------------|----------------------------|--------------------|
| giveCard_SelectionEmpty_NoTransfer          | from prompted, returns no card            | no card moved              | :white_check_mark: |
| giveCard_SelectionOneCard_TransfersThatCard | from prompted, returns 1 card             | from loses it, to gains it | :white_check_mark: |
| giveCard_SelectedCardNotInHand_NoTransfer   | selection returns card not in from's hand | no card moved              | :white_check_mark: |
