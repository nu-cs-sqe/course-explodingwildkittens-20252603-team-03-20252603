# BVA Analysis for Nope Action

## Scope

Integration tests for **nope** card action
These tests test the end to end action of nope card

## Assumptions and Notes
- Assumes system is already launched
- Assumes the current Player is playing a turn

## User Story Under test
- player plays a card
- every player with a nope card is asked if they want to nope the card
- counts the nope 
- if even:
  - cancels out --> execute the card
- if odd:
  - nope holds --> do not execute the card

## CardAction under test:
spaces: _game logic_, boolean, count

cases:
- 0 nopes; card executed
- 1 nope card not excuted
- 2 nopes card executed
- 3 nopes card not executed
- 3 players, only 1 player with a nope; only one person is prompted

### Note: in each test, assert only the users with nopes are asked to nope and everyone who nopes loses a card.

| test_Name                                               | State of the System                 | Expected output                                      | Implemented?       |
|---------------------------------------------------------|-------------------------------------|------------------------------------------------------|--------------------|
| nope_ZeroNopes_CardExecuted                             | 0 nopes                             | card executed; players don't lose any cards          | :white_check_mark: |
| nope_OneNope_CardNotExecuted                            | 1 nope                              | card not executed; player loses nope card            | :white_check_mark: |
| nope_TwoNopes_CardExecuted                              | 2 nopes                             | card executed; players lose nope cards               | :white_check_mark: |
| nope_ThreeNopes_CardNotExecuted                         | 3 nopes                             | card not executed; players lose nope cards           | :white_check_mark: |
| nope_ThreePlayers_OneLeftWithNope_OnlyOnePlayerPrompted | 3 players; only 1 player has a nope | only that player is prompted; player loses nope card | :white_check_mark: |