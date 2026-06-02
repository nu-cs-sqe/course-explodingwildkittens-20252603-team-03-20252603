# BVA Analysis for skip card Action

## Scope

Integration tests for **skip** card action
These tests test the end to end action of skip card

## Assumptions and Notes
- Assumes system is already launched
- Assumes the current Player is playing a turn

# CardAction under test:
spaces: _game logic_, boolean

cases:
- user plays a turn; card is not noped; skip skips draw
- user plays a turn; card is noped; does not skip
- user plays a turn; card is noped twice, skip skips draw
- user plays a turn; card is noped thrice, does not skip


| test_Name                      | State of the System                       | Expected output | Implemented?       |
|--------------------------------|-------------------------------------------|-----------------|--------------------|
| skip_IsNotNoped_SkipsDraw      | user plays a card, action is not noped    | skips           | :white_check_mark: |
| skip_IsNopedOnce_DoesNotSkip   | user plays a card, action is noped        | not skipped     | :white_check_mark: |
| skip_IsNopedTwice_SkipsDraw    | user plays a card, action is noped twice  | skip            | :white_check_mark: |
| skip_IsNopedThrice_DoesNotSkip | user plays a card, action is noped thrice | not skipped     | :white_check_mark: |
