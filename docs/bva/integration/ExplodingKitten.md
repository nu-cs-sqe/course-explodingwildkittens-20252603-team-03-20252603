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
- player draws EK; no Defuse; player eliminated; hand cleared; EK discarded; game ends; winner identified
- player draws EK; no Defuse; eliminated player never reappears in queue on subsequent turns

| test_Name                                                 | State of the System  | Expected output                                                        | Implemented?       |
|-----------------------------------------------------------|----------------------|------------------------------------------------------------------------|--------------------|
| explodingKitten_HasDefuse_PlayerSurvivesAndEKReinserted   | has Defuse card      | survives; Defuse discarded; EK back in deck at top                     | :white_check_mark: |
| explodingKitten_NoDefuse_PlayerEliminatedAndGameEnds      | no Defuse; 2 players | eliminated; hand cleared; EK in discard; game ended; winner is player2 | :white_check_mark: |
| explodingKitten_NoDefuse_EliminatedPlayerRemovedFromQueue | no Defuse; 3 players | eliminated player never becomes current on subsequent turns            | :white_check_mark: |
