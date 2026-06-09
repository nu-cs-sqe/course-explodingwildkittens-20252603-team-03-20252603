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



## Suppressed Warnings

The project uses Checkstyle and SpotBugs for static analysis. A small number of suppressions are applied where a tool rule conflicts with a deliberate design decision.

### Checkstyle

| Suppression | Where | Why |
|---|---|---|
| `checkstyle:ParameterNumber` | `GameController` — two package-private constructors | Checkstyle flags constructors with more than 3 parameters. These constructors require 4 parameters (`GameState`, `IGameDisplay`, `IPlayerInput`, `ComboValidator` / `DeckFactory`) because the UML design mandates each as a distinct injected dependency. Merging any two would violate the design contract. |
| `checkstyle:MethodLength` | All 11 integration test classes (`AttackIntegrationTest`, `ExplodingKittenIntegrationTest`, `FavorIntegrationTest`, `GameControllerTest`, `NopeIntegrationTest`, `PlayATurnIntegrationTest`, `SeeTheFutureIntegrationTest`, `ShuffleIntegrationTest`, `SkipIntegrationTest`, `ThreeCardIntegrationTest`, `TwoCardIntegrationTest`) | Integration tests must set up a full game state and assert many sequential steps in one method. Splitting them would obscure the scenario's narrative flow and make failures harder to diagnose. |

### SpotBugs

| Suppression | Where | Why |
|---|---|---|
| `EI_EXPOSE_REP2` — *may expose internal representation by incorporating reference to mutable object* | `Deck(List, Random)` constructor; `GameController` (3 constructors); `GameView(Scanner, PrintStream)` constructor | These constructors accept external collaborators (scanner, display, input, random seed) that are intentionally shared references — they are injected dependencies, not owned data. Defensive copies would break the injection contract and prevent callers from controlling behaviour during tests. |
| `EI_EXPOSE_REP` — *may expose internal representation by returning reference to mutable object* | `GameState.turnState()` | `TurnState` is a mutable object that callers legitimately need to update. Returning a copy would require mirroring all mutation methods elsewhere and complicates the design without a real safety benefit, since `TurnState` is an internal domain object, not a public API surface. |
| `UUF_UNUSED_FIELD` — *unused field* | `GameState` (class-level); `GameController` (class-level) | SpotBugs flags fields it cannot trace as read across all code paths. These fields are accessed through paths SpotBugs cannot follow statically. They are genuinely used at runtime. |
| `URF_UNREAD_FIELD` — *field is written but never read* | `GameController` (class-level) | Same root cause as `UUF_UNUSED_FIELD` — SpotBugs cannot resolve reads that happen through indirect dispatch or across constructor variants. |
| `UWF_UNWRITTEN_FIELD` — *field is read but never written* | `GameController` (class-level) | Some fields are written only in certain constructors (package-private testing constructors vs. the public production constructor). SpotBugs analyses each constructor path independently and flags fields it sees as unset, but at runtime exactly one constructor runs and all fields are properly initialised. |
| `NP_UNWRITTEN_FIELD` — *possible null pointer dereference of field that has not been written* | `GameController` (class-level) | Follows directly from `UWF_UNWRITTEN_FIELD` — because SpotBugs believes certain fields may never be written, it also warns they could be null at the point of use. The actual constructors guarantee all fields are set before any method reads them. |




## Acknowledgements
Claude AI Usage
Claude was used as a development assistant throughout this project, following the guidelines of the syllabus

Here are all the suppressions with their relative file paths:

---

## Checkstyle Exceptions / Suppressed Errors

### Checkstyle

**`checkstyle:ParameterNumber`**
- **Files:**
    - `src/main/java/ui/GameController.java` (lines 43, 54 — two package-private constructors)
- **Why:** 
Checkstyle flags constructors with more than 3 parameters. 
These constructors need 4 parameters (`GameState`, `IGameDisplay`, `IPlayerInput`, `ComboValidator` / `DeckFactory`) 
for the GameController because the UML class diagram mandates each as a distinct injected dependency and the 
game controller needs these four objects to manage the game state and merging any two would violate 
the design contract and make it hard to test


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
- **Why:** 
Integration tests must set up a full game state and assert many sequential steps in one method. 
Splitting them would obscure the scenario flow and make failures harder to trace.

---

### SpotBugs (FindBugs)

**`EI_EXPOSE_REP2`** — *"May expose internal representation by incorporating reference to mutable object"*
- **Files:**
    - `src/main/java/domain/model/Deck.java` (line 23)
    - `src/main/java/ui/GameController.java` (lines 35, 44, 55)
    - `src/main/java/ui/GameView.java` (line 29)
- **Why:** These constructors accept injected collaborators (scanner, display, input, random) that are intentionally shared references. Defensive copies would break the injection contract and prevent test control over behaviour.

**`EI_EXPOSE_REP`** — *"May expose internal representation by returning reference to mutable object"*
- **Files:**
    - `src/main/java/domain/model/GameState.java` (line 159 — `turnState()` getter)
- **Why:** Callers legitimately need to read and mutate `TurnState` directly i.e updating the nopeCount and turns. 
Returning a copy would require duplicating all mutation methods and complicates the design without a real safety benefit.

**`UUF_UNUSED_FIELD`** — *"Unused field"*
- **Files:**
    - `src/main/java/domain/model/GameState.java` (class-level)
    - `src/main/java/ui/GameController.java` (class-level)
- **Why:** SpotBugs cannot trace all reads across constructor variants and indirect dispatch paths. The fields are genuinely used at runtime.

**`URF_UNREAD_FIELD`** — *"Field is written but never read"*
- **Files:**
    - `src/main/java/ui/GameController.java` (class-level)
- **Why:** Same root cause as `UUF_UNUSED_FIELD` — SpotBugs cannot resolve reads that occur through indirect dispatch or across multiple constructor branches.

**`UWF_UNWRITTEN_FIELD`** — *"Field is read but never written"*
- **Files:**
    - `src/main/java/ui/GameController.java` (class-level)
- **Why:** Some fields are only written in certain constructors (e.g. the testing constructor vs. the production constructor). SpotBugs analyses each path independently and flags fields it sees as unset, but at runtime exactly one constructor runs and all fields are properly initialised.

**`NP_UNWRITTEN_FIELD`** — *"Possible null pointer dereference of field that has not been written"*
- **Files:**
    - `src/main/java/ui/GameController.java` (class-level)
- **Why:** Follows directly from `UWF_UNWRITTEN_FIELD` — because SpotBugs believes certain fields may never be written, it also warns they could be null at the point of use. The actual constructors guarantee all fields are set before any method reads them.