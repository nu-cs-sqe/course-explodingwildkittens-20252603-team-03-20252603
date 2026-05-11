# Design Principles — Exploding Kittens

## Principles & How They Apply

---

### 1. Encapsulation
Bundle data and the methods that operate on it together in a class.

- `GameState` encapsulates all mutable game state (deck, discard pile, players, turn flags) and exposes controlled methods like `getCurrentPlayer()`, `advancePlayer()`, and `eliminateCurrentPlayer()` — callers never manipulate fields directly.
- `Player` bundles a player's hand and peek cards with methods like `addCard()`, `removeCard()`, and `hasCard()` — the hand list is never accessed directly.
- `Deck` owns its card list and exposes only the operations needed: `shuffle()`, `drawTop()`, `insertAt()`.
- `TurnState` bundles all turn-specific flags (`turnsToTake`, `skipDraw`, `isAttacking`, `nopeCount`) with meaningful behavior methods like `enableSkipDraw()`, `startAttack()`, and `reset()`.

---

### 2. Delegation
One object passes work to another instead of doing it itself.

- `GameController` delegates all display logic to `IGameDisplay` and all input collection to `IPlayerInput`.
- `GameController` delegates deck construction to `DeckFactory` — it never builds the deck itself.
- `GameController` delegates card execution to `Card.execute()`, which in turn delegates to the card's `CardAction`.
- `FavorAction`, `TwoCatAction`, and `ThreeCatAction` delegate the actual card stealing logic to `PlayerInteractionHelper` rather than implementing it themselves.

---

### 3. Information Hiding
Hide implementation details. Only expose what others need to know.

- `Deck` hides how cards are stored internally. Callers just call `drawTop()` or `insertAt()`.
- `Player` hides how the hand is implemented. Other classes never access `hand` directly — they go through `addCard()`, `removeCard()`, `hasCard()`.
- `GameState` hides the queue mechanics for turn rotation — callers just call `advancePlayer()` and `eliminateCurrentPlayer()` without knowing how the queue works internally.
- `DeckFactory` hides all card counts, card names, and exploding kitten reinsertion logic behind a single `buildDeck(numPlayers)` call.
- `peekTop()` is removed from `Deck` — the only way to peek is through `SeeTheFutureAction`, preventing any class from peeking the deck without playing the card.

---

### 4. Encapsulate What Varies
Separate the parts that change frequently from the parts that don't.

- Card actions vary per card type. Each is encapsulated behind the `CardAction` interface — adding a new card type means adding a new class, nothing else changes.
- The card stealing behavior shared by `FavorAction`, `TwoCatAction`, and `ThreeCatAction` is extracted into `PlayerInteractionHelper` — if stealing logic changes, it changes in one place.
- Turn state flags that change every turn are extracted into `TurnState`, separate from the stable game data in `GameState`.

---

### 5. Favor Composition Over Inheritance
Use "has-a" relationships instead of "is-a" / `extends`.

- `GameState` **has-a** `Deck`, **has-a** `Queue<Player>`, **has-a** `TurnState`, **has-a** discard pile.
- `Player` **has-a** list of `Card`s and a list of `peekCards`.
- `Card` **has-a** `CardAction` — card behavior is composed in, not inherited.
- `FavorAction`, `TwoCatAction`, and `ThreeCatAction` each **have-a** `PlayerInteractionHelper` rather than extending a shared base class.
- No inheritance hierarchies exist anywhere in the design.

---

### 6. Program to Interface, Not Implementation
Depend on abstract types, not concrete classes.

- `GameController` depends on `IGameDisplay` and `IPlayerInput`, not `GameView` directly. A GUI or web view can be swapped in without touching the controller.
- All card behaviors are referenced via `CardAction`. `GameController` calls `card.execute(gameState)` without knowing which concrete action runs.
- `GameView` implements both `IGameDisplay` and `IPlayerInput` — the controller never knows these are the same concrete class.

---

### 7. Loosely Coupled Designs
Classes should depend on each other as little as possible.

- `Player` has no knowledge of `GameState`, `Deck`, or any other `Player`.
- `Card` only knows about `CardType` and `CardAction` — no knowledge of game flow.
- `Deck` only depends on `Card` — nothing about players or game rules.
- `TurnState` only depends on `Card` (for `pendingAction`) — no knowledge of `GameState` or `GameController`.
- `PlayerInteractionHelper` only depends on `Player` and `Card` — no knowledge of `GameState`.
- Action classes receive `GameState` as a method parameter rather than storing it as a field — parameter coupling is far lighter than field coupling.

---

### 8. Open-Closed Principle
Classes should be open for extension but closed for modification.

- New card types can be added by creating a new `CardAction` implementation and a new `Card` instance — `GameController` and all existing classes are untouched.
- New display modes (CLI, GUI, web) can be added by implementing `IGameDisplay` and `IPlayerInput` without touching existing code.
- New stealing behaviors can be added by extending `PlayerInteractionHelper` or composing a new helper — existing actions are unaffected.

---

### 9. Hollywood Principle
High-level components control low-level ones — not the other way around.

- `GameController` drives the entire game loop. It tells `IGameDisplay` when to display, `GameState` when to advance, and `DeckFactory` when to build the deck.
- `IGameDisplay` and `IPlayerInput` are passive — they wait to be called by `GameController` and never initiate game flow themselves.
- `CardAction` implementations are passive workers — they are called by `Card.execute()`, which is called by `GameController`. They never call back up into the controller.

---

### 10. Principle of Least Knowledge
Objects should not know about the internal structure of other objects.

- Bad: `gameState.getActivePlayers().peek().addCard(card)`
- Good: `gameState.addCardToCurrentPlayer(card)` — `GameState` exposes the operation directly.
- `PlayerInteractionHelper` only calls methods on the `Player` objects passed directly to it — never reaches through them into other objects.
- `GameController` only calls methods on its own direct fields: `gameState`, `display`, `input`, `deckFactory`. It never chains calls through `GameState` to reach `Player` or `Deck` directly.

---

### 11. SOLID

**Single Responsibility Principle**
Each class has exactly one reason to change:
- `GameState` — changes only if the data needed to represent game state changes.
- `TurnState` — changes only if the flags governing turn flow change.
- `GameView` — changes only if the display or input format changes.
- `GameController` — changes only if the game flow logic changes.
- `DeckFactory` — changes only if the rules for building the initial deck change.
- `PlayerInteractionHelper` — changes only if the rules for stealing cards change.

**Open/Closed Principle**
Already covered in principle #8 above.

**Liskov Substitution Principle**
Any concrete implementation of `IGameDisplay` or `IPlayerInput` can be substituted without breaking `GameController`. Any `CardAction` implementation can be substituted for another — `GameController` behaves identically regardless of which card is played.

**Interface Segregation Principle**
Interfaces are kept small and specific:
- `IGameDisplay` — only display methods.
- `IPlayerInput` — only `promptCardSelection()`.
- `CardAction` — only `execute(GameState)`.
  `GameView` implements both display and input interfaces by choice, not by force — a test stub that only needs display implements `IGameDisplay` alone without being forced to implement `promptCardSelection`.

**Dependency Inversion Principle**
- `GameController` depends on `IGameDisplay` and `IPlayerInput` abstractions, not `GameView` directly.
- Both high-level (`GameController`) and low-level (`GameView`) depend on the same abstractions — neither depends directly on the other.

---

### 12. Low Coupling and High Cohesion

**Low coupling:** Each class depends on as few other classes as possible.
- `Card` depends only on `CardType` and `CardAction`.
- `Player` depends only on `Card` and `CardType`.
- `Deck` depends only on `Card`.
- `TurnState` depends only on `Card`.
- `PlayerInteractionHelper` depends only on `Player` and `Card`.
- `GameState` depends on `Deck`, `Player`, `Card`, `TurnState`, and `GameStatus`.
- `GameController` depends on `GameState`, `IGameDisplay`, `IPlayerInput`, and `DeckFactory`.

**High cohesion:** Everything inside a class relates to its single purpose.
- `Deck` only contains deck operations — nothing about players or game flow.
- `Player` only contains player-related state — nothing about the deck or game rules.
- `TurnState` only contains turn-specific flags — nothing about the players or deck.
- `PlayerInteractionHelper` only contains card transfer logic between players — nothing else.
- `DeckFactory` only contains deck construction logic — nothing about game flow.