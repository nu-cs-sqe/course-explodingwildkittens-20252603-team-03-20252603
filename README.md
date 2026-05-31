![Gradle Build](https://github.com/nu-cs-sqe/course-explodingwildkittens-20252603-team-03-20252603/actions/workflows/main.yml/badge.svg)

[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=23631080)
# Exploding Wildkittens

## Contributors
- Caroline Guerra
- Mercy Muiruri
- Austin Omondi
- Chibueze Anyachebelu

## Dependencies
- JDK 11
- JUnit 5.10
- Gradle 8.10

## Checkstyle Exceptions

### `GameController`: 4-parameter constructor

The linter flags constructors with more than 3 parameters. `GameController` is an intentional exception: its 4 parameters (`GameState`, `IGameDisplay`, `IPlayerInput`, `ComboValidator`) are each distinct collaborators specified in `design.puml`. Merging any two would create a meaningless wrapper. This is the only place in the codebase where the limit is exceeded.

## Acknowledgements
Claude AI Usage
Claude was used as a development assistant throughout this project, following the guidelines of the syllabus
