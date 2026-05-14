# Coding Standards Document

## Development Cycle
In order to commit make sure:
- BVA design is done
- Unit tests are added
- All tests are passing
- You have a descriptive commit message

In order to request for merge (PR):
- check generated reports and fix errors:
  - checkstyle report: [main.html](../../build/reports/checkstyle/main.html) and [test.html](../../build/reports/checkstyle/test.html)
  - code coverage: [index.html](../../build/reports/jacoco/index.html) and [index.html](../../build/reports/pitest/index.html) --> Jacoco and PiTest Mutants
  - spotbugs: [spotbugs.html](../../build/reports/spotbugs/spotbugs.html) and [spotbugsTest.html](../../build/reports/spotbugs/spotbugsTest.html) --> spotbugs.
- ensure the class/feature you are adding has zero errors and is 100% covered on the above reports
- ensure all tests are passing.
- ensure the PR is descriptive.

## Coding Standards
1. Naming
- names should answer why they exist (be descriptive)
- Single-letter names can ONLY be used as local variables inside short methods.
- Pick one word per abstract concept and stick with it — don't mix fetch, retrieve, and get across classes
- Classes should have noun names
- Methods should have verb names

2. Functions
- Use no more than 3 arguments
- No more than 20 lines long
- No more than 120 characters wide
- Functions should do one thing only

3. Comments
- Avoid comments as much as possible.
- Delete commented-out code

4. Formatting
- If one function calls another, keep them vertically close — caller above callee.

5. Objects and Data Structures
- Keep internals private (avoid setters and getters)

6. Error Handling
- Use exceptions not status codes
- Don't return null
- Don't pass null into functions

7. Unit Tests
- write a failing test before production code; write only enough test to fail; write only enough production code to pass
- One concept per test