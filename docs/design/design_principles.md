# Design Principles — Exploding Kittens

## Principles & How They Apply

---

### 1. Encapsulation
Bundle data and the methods that operate on it together in a class.

- `GameState` encapsulates all mutable game state (deck, discard pile, players, turn flags) and exposes controlled methods like `getCurrentPlayer()` and `advancePlayer()` — callers never manipulate the fields directly.
- `Player` bundles a player's hand and elimination status with methods like `addCard()`, `removeCard()`, and `hasCard()`.
- `Deck` owns its card list and exposes only the operations needed: `shuffle()`, `drawTop()`, `insertAt()`, `peekTop()`.

---

### 2. Delegation
One object passes work to another instead of doing it itself.

- `GameController` does not directly shuffle the deck or check a player's hand — it delegates to `Deck.shuffle()` and `Player.hasCard()`.
- `GameController` delegates all display logic to `GameView`, keeping game logic separate from presentation.
- `GameController` delegates persistence to `GameRepository` rather than handling save/load itself.

---

### 3. Information Hiding
Hide implementation details. Only expose what others need to know.

- `Deck` hides how cards are stored internally (e.g. `ArrayList` vs `LinkedList`). Callers just call `drawTop()`.
- `Player` hides how the hand is implemented. Other classes never access `hand` directly — they go through `addCard()` / `removeCard()` / `hasCard()`.
- `GameState` hides the logic for advancing the turn index (`advancePlayer()` skips eliminated players internally).

---

### 4. Encapsulate What Varies
Separate the parts that change frequently from the parts that don't.

- Card actions vary per card type (Skip, Attack, Shuffle, etc.). These should each be encapsulated behind a common `CardAction` interface so new card types can be added without touching `GameController`.
- The Nope mechanic (timer, nope_count parity) varies in complexity — it is isolated in its own logic and `GameController` just awaits a boolean result.
- Win/loss conditions and player elimination logic are isolated in `GameState` / `RemovePlayer` flow, not scattered through the controller.

---

### 5. Favor Composition Over Inheritance
Use "has-a" relationships instead of "is-a" / `extends`.

- `GameState` **has-a** `Deck`, **has-a** list of `Player`s, **has-a** discard pile. No inheritance used.
- `Player` **has-a** list of `Card`s.
- `GameController` **has-a** `GameState`, **has-a** `GameView`, **has-a** `GameRepository`.
- No deep class hierarchies — card behavior differences are handled via composition (each card action is a separate object, not a subclass of `Card`).

---

### 6. Program to Interface, Not Implementation
Depend on abstract types, not concrete classes.

- `GameController` depends on `IGameView` and `IGameRepository` interfaces, not `GameView` or `GameRepository` directly. This allows swapping a CLI view for a GUI view without changing the controller.
- Card actions are referenced via a `CardAction` interface, not their concrete classes (`SkipAction`, `AttackAction`, etc.).

---

### 7. Loosely Coupled Designs
Classes should depend on each other as little as possible.

- `Player` has no knowledge of `GameState` or `Deck` — it only manages its own hand.
- `Card` knows nothing about the game — it just holds a `CardType` and name.
- `GameView` only receives data to display — it does not reach into `GameState` or call `GameController` methods.
- `GameRepository` only knows about `GameState` — it is not coupled to the controller or view.

---

### 8. Open-Closed Principle
Classes should be open for extension but closed for modification.

- New card types (e.g. a future "Reverse" card) can be added by creating a new `CardAction` implementation without modifying `GameController`.
- New display modes (CLI, GUI, web) can be added by implementing `IGameView` without touching existing code.
- New persistence strategies (file, database) can be added by implementing `IGameRepository`.

---

### 9. Hollywood Principle
High-level components control low-level ones — not the other way around.

- `GameController` drives the entire game loop. It tells `GameView` when to display and `GameState` when to advance — neither calls back into `GameController`.
- `GameView` is passive: it receives instructions from `GameController` rather than pulling state on its own.

---

### 10. Principle of Least Knowledge
Objects should not know about the internal structure of other objects.

- Bad: `gameController.getGameState().getDeck().drawTop()`
- Good: `gameController.drawCard()` — the controller encapsulates the multi-step operation.
- `Player` does not reach into another `Player`'s hand directly. For Favor/Two Same Cards, `GameController` mediates the card transfer between players.
- `Card` does not know about `Player` or `Deck`. It is just data.

---

### 11. SOLID

**Single Responsibility Principle**
Each class has exactly one reason to change:
- `GameState` — changes only if the data needed to represent game state changes.
- `GameView` — changes only if the display format changes.
- `GameController` — changes only if the game flow logic changes.
- `GameRepository` — changes only if the persistence strategy changes.
- `Deck` — changes only if deck behavior changes.
- `Player` — changes only if player data or hand behavior changes.

**Open/Closed Principle**
Already covered in principle #8 above.

**Liskov Substitution Principle**
Any concrete implementation of `IGameView` (e.g. `CliGameView`, `GuiGameView`) can be substituted without breaking `GameController`. Any `CardAction` implementation can be substituted for another.

**Interface Segregation Principle**
Interfaces are kept small and specific:
- `IGameView` — only display methods.
- `IGameRepository` — only `save()` and `load()`.
- `CardAction` — only `execute(GameState)`.
No class is forced to implement methods it doesn't need.

**Dependency Inversion Principle**
- `GameController` depends on `IGameView` and `IGameRepository` abstractions, not concrete classes.
- Both high-level (`GameController`) and low-level (`GameView`, `GameRepository`) depend on the same abstractions — neither depends directly on the other.

---

### 12. Low Coupling and High Cohesion

**Low coupling:** Each class depends on as few other classes as possible.
- `Card` has zero dependencies.
- `Player` only depends on `Card` and `CardType`.
- `Deck` only depends on `Card`.
- `GameState` depends on `Deck`, `Player`, `Card`, and the two enums.
- `GameController` depends on `GameState`, `IGameView`, and `IGameRepository`.

**High cohesion:** Everything inside a class relates to its single purpose.
- `Deck` only contains deck operations — nothing about players or game flow.
- `Player` only contains player-related state — nothing about the deck or game rules.
- `GameView` only contains display logic — no game logic.
