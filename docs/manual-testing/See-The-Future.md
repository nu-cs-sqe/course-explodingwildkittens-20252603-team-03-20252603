# Manual Testing Checklist: See The Future

## User Story
- Player plays See The Future
- System shows player the top 3 cards (or less if deck has < 3 cards)
- Deck is unchanged after viewing
- Player confirms done viewing and cards are cleared

## Verified Actions via Manual Testing
- [x] Top 3 cards are shown to the player
- [x] Deck order is unchanged after seeing the future
- [x] Game moves back to PlayATurn after viewing
- [x] See The Future is blocked by nope

## Bugs. Note whether it needs to be fixed (**CRUCIAL**) or is a minor nitpick (**MINOR**).
### CRUCIAL -- needs to be fixed urgently
- [ ] decide on whether we want to clear the output after viewing or just leave it there for the player

### MINOR -- nitpicks; small optimizations
- [ ] when displaying the cards, display and leave 
space for the instructions afterward to choose between play a card v done playing
- [ ] if nopes, display a statement saying action was noped moving back to current turn 
or do you want ot play a different card