# BVA Analysis for Exploding Kitten draw

## Scope

Integration tests for the **Exploding Kitten** draw flow
These tests verify the end-to-end behaviour when a player draws an Exploding Kitten card

## Assumptions and Notes
- Assumes system is already launched
- Assumes the current player is playing a turn and chooses to draw

## UserStory being verified
- Player draws a card from the deck
- If the card is an Exploding Kitten:
  - If the player has a Defuse card, the Defuse is discarded and the Exploding Kitten is reinserted into the deck at a chosen position — the player survives
  - If the player has no Defuse card, the player is eliminated: their hand is cleared to the discard pile and the Exploding Kitten is discarded
  - The eliminated player is removed from the active player queue and never takes another turn
  - When only one player remains, endGame() is triggered and the last active player is declared the winner

# CardAction under test:
spaces: hasDefuse = {true, false}, isLastPlayerStanding = {true, false}

cases:
- player draws EK; has Defuse; survives; Defuse discarded; EK reinserted into deck
- player draws EK; no Defuse; player eliminated; hand cleared to discard pile
- player draws EK; no Defuse; eliminated player never reappears in queue on subsequent turns
- player draws EK; no Defuse; EK ends up in discard pile, not the deck
- player draws EK; no Defuse; last player remaining; endGame triggered; winner identified

| test_Name                                               | State of the System                        | Expected output                              | Implemented?       |
|---------------------------------------------------------|--------------------------------------------|----------------------------------------------|--------------------|
| explodingKitten_HasDefuse_PlayerSurvivesAndEKReinserted | has Defuse card                            | survives; Defuse discarded; EK back in deck  | :white_check_mark: |
| explodingKitten_NoDefuse_PlayerEliminated               | no Defuse; 2 players                       | isActive=false; hand cleared                 | :white_check_mark: |
| explodingKitten_NoDefuse_EliminatedPlayerRemovedFromQueue | no Defuse; 3 players                     | eliminated player never becomes current      | :white_check_mark: |
| explodingKitten_NoDefuse_EKInDiscardPile                | no Defuse; 2 players                       | EK in discard pile; not in deck              | :white_check_mark: |
| explodingKitten_LastPlayerRemaining_GameEndsAndWinnerIdentified | no Defuse; last player remaining | game status ENDED; winner is last player     | :white_check_mark: |
