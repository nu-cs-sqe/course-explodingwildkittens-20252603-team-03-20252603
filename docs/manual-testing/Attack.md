# Manual Testing Checklist for Attack

## UserStory
- player plays attack
- not forced to finish turns
- not forced to draw
- next player forced to play x + 2 cards where x is the number of remaining turns

## Verified Actions via manual Testing
- [x] Game moves to next player after playing attack
- [x] Current player is not forced to draw
- [x] Next player is forced to play 2 turns (default attack)
- [x] Current player is not forced to finish remaining turns
- [x] Attacks stack
  - [x] Next player is forced to play 2 turns (default attack)
  - [x] double attack on first turn --> 4 turns
  - [x] double attack on second turn --> 3 turns
- [x] Attack is blocked by nope
- [x] Attack works correctly when only 2 players remain



## Bugs. Note whether it needs to be fixed (**CRUCIAL**) or is a minor nitpick(**MINOR**).
### CRUCIAL -- needs to be fixed urgently
- No crucial logic bugs related to attack.


### MINOR -- nitpicks; small optimizations
- [ ] When the same player is re-prompted to choose between playing a card and done playing,
it did not show the player's index so it was confusing. -- nitpick
- [ ] display the number of turns and space out the output better so the user can tell what turn it is.
- [ ] dispaly a message like 'You have been attacked x turns' to make the game more understandable
- [ ] in case of an invalid card selection, 
let the user know the card is not yet played and they have to reselect
- [ ] be clear when asking for play a card versus done playing
so that the user can know to just say "DONE_PLAYING" if done
- [ ] ability to go back if i see my cards are not good 
(without having to click a invalid card selection just to get to go back to choose DONE_PLAYING)