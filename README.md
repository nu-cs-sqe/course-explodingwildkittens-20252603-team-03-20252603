![Gradle Build](https://github.com/nu-cs-sqe/course-explodingwildkittens-20252603-team-03-20252603/actions/workflows/main.yml/badge.svg)

[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=23631080)
# Exploding Wildkittens
This is a digital implementation of 
[Exploding Kittens](https://cdn.shopify.com/s/files/1/0345/9180/1483/files/Exploding-Kittens_Grab-N-Game_Instructions_2023.pdf?v=1712786226) 
(Original Edition, 2–5 players).


## Contributors
- Caroline Guerra
- Mercy Muiruri
- Austin Omondi
- Chibueze Anyachebelu



## Dependencies
- JDK 11
- JUnit 5.10
- Gradle 8.10



## Running the Game
To run the game you either:
1. click the run <| button on the Main.java file in `src/main/java/ui/Main.java`
2. or run via gradle using these commands in your terminal

// verbose option
``` 
./gradlew run 
```
OR 

// removes unnecessary gradle build statements so the CLI game commands are more visible
``` 
./gradlew run --console=plain
```



## Special Design Decisions
`GameController`: 4-parameter constructor

The linter flags constructors with more than 3 parameters. `GameController` is an intentional exception: its 4 parameters (`GameState`, `IGameDisplay`, `IPlayerInput`, `ComboValidator`) are each distinct collaborators specified in `design.puml`. Merging any two would create a meaningless wrapper. This is the only place in the codebase where the limit is exceeded.



## Code Standards
This project enforces the following Checkstyle rules (build fails on any violation) for all files both
src and test files.

| Rule                      | Limit                       | Notes                                         |
|---------------------------|-----------------------------|-----------------------------------------------|
| Line length               | Max 120 characters          | URLs and imports exempt                       |
| Indentation               | Tabs only                   | No spaces for indentation                     |
| Method length             | Max 20 lines                | Empty lines not counted                       |
| Method parameters         | Max 3                       | See exception below                           |
| Classes per file          | 1                           | One top-level class per file                  |
| Empty catch blocks        | Not allowed                 | Except variables named `expected` or `ignore` |
| Magic numbers             | Not allowed                 | `-1, 0, 1, 2` are exempt                      |
| Unused/redundant imports  | Not allowed                 |                                               |
| `equals()` / `hashCode()` | Must be overridden together |                                               |
| Boolean expressions       | Must be simplified          |                                               |
---

## Checkstyle Exceptions / Suppressed Errors

### Checkstyle

**`checkstyle:ParameterNumber`**
- **Files:**
    - `src/main/java/ui/GameController.java` (lines 43, 54 — two package-private constructors)
- **Why:** Checkstyle flags constructors with more than 3 parameters. Both constructors take 4 because each parameter is a distinct dependency required by `design.puml`: `GameState` holds live game data, `IGameDisplay` handles all output, `IPlayerInput` reads player decisions, and `ComboValidator`/`DeckFactory` handles card logic and deck construction. None of these can be merged without violating the class responsibilities defined in the UML.

**`checkstyle:MethodLength`**
- **Files:**
    - `src/test/java/ui/AttackIntegrationTest.java`
    - `src/test/java/ui/ExplodingKittenIntegrationTest.java`
    - `src/test/java/ui/FavorIntegrationTest.java`
    - `src/test/java/ui/GameControllerTest.java`
    - `src/test/java/ui/NopeIntegrationTest.java`
    - `src/test/java/ui/PlayATurnIntegrationTest.java`
    - `src/test/java/ui/SeeTheFutureIntegrationTest.java`
    - `src/test/java/ui/ShuffleIntegrationTest.java`
    - `src/test/java/ui/SkipIntegrationTest.java`
    - `src/test/java/ui/ThreeCardIntegrationTest.java`
    - `src/test/java/ui/TwoCardIntegrationTest.java`
- **Why:** Each integration test simulates a complete game scenario end-to-end: it stubs player input, starts the game, plays one or more cards, and then asserts the resulting game state. Each assertion step depends on the game state left by the previous step, so the entire scenario must live in one method. Splitting it into smaller helpers would hide which step caused a failure and make the test harder to read as a game narrative.

---

### SpotBugs (FindBugs)

**`EI_EXPOSE_REP2`** — *"May expose internal representation by incorporating reference to mutable object"*
- **Files:**
    - `src/main/java/domain/model/Deck.java` (line 23)
    - `src/main/java/ui/GameController.java` (lines 35, 44, 55)
    - `src/main/java/ui/GameView.java` (line 29)
- **Why:** SpotBugs warns that storing a passed-in mutable object directly (rather than copying it) lets the caller mutate the object after construction. Here that is intentional: tests inject a stubbed `Scanner`, a mock `IGameDisplay`, or a seeded `Random` so they can control exactly what the class reads and outputs. If the constructor copied these objects, the test's stub would be disconnected and tests would lose control over the class's behaviour.

**`EI_EXPOSE_REP`** — *"May expose internal representation by returning reference to mutable object"*
- **Files:**
    - `src/main/java/domain/model/GameState.java` (line 159 — `turnState()` getter)
- **Why:** `GameController` calls methods on the returned `TurnState` directly — e.g. `turnState.setPendingAction()`, `turnState.incrementNopeCount()`, and `turnState.clearPendingAction()` — to track what card is being played and whether it has been noped. If `turnState()` returned a copy, those mutations would be applied to the copy and discarded, so the real game state would never update.

**`UUF_UNUSED_FIELD`** — *"Unused field"*
- **Files:**
    - `src/main/java/domain/model/GameState.java` (class-level)
    - `src/main/java/ui/GameController.java` (class-level)
- **Why:** In `GameController`, the public constructor (`GameController(IGameDisplay, IPlayerInput, ComboValidator)`) never assigns `gameState` or `deckFactory` — both are set later by `startGame()`. SpotBugs sees the public constructor path and flags those fields as never assigned. In `GameState`, all six fields are read by its methods, but the package-private constructor (`GameState(List, Deck, TurnState)`) uses a different initialization path, which confuses the analyser into thinking some fields are never used.

**`URF_UNREAD_FIELD`** — *"Field is written but never read"*
- **Files:**
    - `src/main/java/ui/GameController.java` (class-level)
- **Why:** The third constructor (`GameController(IGameDisplay, IPlayerInput, ComboValidator, DeckFactory)`) assigns `deckFactory`, but SpotBugs does not connect that assignment to the reads in `dealCardsAndReturnDeck()` (lines 107–109), which is called from `startGame()` rather than the constructor itself. Because the write and the read are in different methods, SpotBugs treats the field as written but never read.

**`UWF_UNWRITTEN_FIELD`** — *"Field is read but never written"*
- **Files:**
    - `src/main/java/ui/GameController.java` (class-level)
- **Why:** The public constructor never writes `gameState`, yet methods like `isGameActive()` and `playATurn()` read it. SpotBugs flags this as a field that is read but potentially never written. In practice, `startGame()` always sets `gameState` before any of those methods are called, so the field is never null at the point of use.

**`NP_UNWRITTEN_FIELD`** — *"Possible null pointer dereference of field that has not been written"*
- **Files:**
    - `src/main/java/ui/GameController.java` (class-level)
- **Why:** Because SpotBugs believes `gameState` may never be written (see `UWF_UNWRITTEN_FIELD`), it also warns that calls like `gameState.isActive()` (line 138) and `gameState.getCurrentPlayer()` (line 149) could throw a `NullPointerException`. The expected call order is always `startGame()` first, which sets `gameState` before any other method can run, so null is never reached.


---
## Acknowledgements
Claude AI Usage
Claude was used as a development assistant throughout this project, following the guidelines of the syllabus

Here are all the suppressions with their relative file paths:
