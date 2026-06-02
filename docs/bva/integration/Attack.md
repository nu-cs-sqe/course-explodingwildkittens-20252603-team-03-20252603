# BVA Analysis for Attack card Action

## Scope
Integration tests for **attack** card action
These tests test the end to end action of attack card

# Design decisions
- we suppressed the checkstyleLength check on this test class since our test methods are asserting
  many method calls and setting up expectations such that it cannot be contained in only 20 lines
  as the checkstyle expects.

## Assumptions and Notes
- Assumes system is already launched
- Assumes the current Player is playing a turn

## UserStory being verified
- player was not initially attacked
  - player clicks attack
  - player does not draw
  - next player has to play twice
- player was initially attacked
  - player clicks attack
  - player does not draw
  - this player is not forced to complete their turns left --> game advances player right after
  - next player has to play the remaining turns of this user + 2

## CardAction under test:
spaces: _game logic_, boolean

cases:
- player was not initially attacked; attacks; not noped; next person is attacked
- player was not initially attacked; attacks; noped; next person is not attacked
- player was initially attacked; attacks on first turn; not noped; next person is attacked (4)
- player was initially attacked; attacks on first turn;  noped; next person is not attacked
- player was initially attacked; attacks on second turn; not noped; next person is attacked (3)
- player was initially attacked; attacks on second turn;  noped; next person is not attacked
- past two players were attacked; attacks on first turn; not noped; next person is attacked --attacks stack (6)
- past two players were attacked; attacks on first turn;  noped; next person is not attacked

| test_Name                                                        | State of the System                                   | Expected output          | Implemented?       |
|------------------------------------------------------------------|-------------------------------------------------------|--------------------------|--------------------|
| attack_NotInitiallyAttacked_NotNoped_NextPlayerAttacked          | not initially attacked, not noped                     | nextplayer attacked      | :white_check_mark: |
| attack_NotInitiallyAttacked_Noped_NextPlayerNotAttacked          | not initially attacked, noped                         | next player not attacked | :x:                |
| attack_InitiallyAttacked_FirstTurn_NotNoped_NextPlayerAttacked   | initially attacked, attacks on first turn, not noped  | next player attacked (4) | :x:                |
| attack_InitiallyAttacked_FirstTurn_Noped_NextPlayerNotAttacked   | initially attacked, attacks on first turn, noped      | next player not attacked | :x:                |
| attack_InitiallyAttacked_SecondTurn_NotNoped_NextPlayerAttacked  | initially attacked, attacks on second turn, not noped | next player attacked (3) | :x:                |
| attack_InitiallyAttacked_SecondTurn_Noped_NextPlayerNotAttacked  | initially attacked, attacks on second turn, noped     | next player not attacked | :x:                |
| attack_ThreeStackedAttacks_FirstTurn_NotNoped_NextPlayerAttacked | two stacked attacks, attacks on first turn, not noped | next player attacked (6) | :x:                |
| attack_ThreeStackedAttacks_FirstTurn_Noped_NextPlayerNotAttacked | two stacked attacks, attacks on first turn, noped     | next player not attacked | :x:                |
