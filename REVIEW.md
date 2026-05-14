# Code Review Guidelines

Reviews are based on Clean Code principles and Java-specific best practices.
Checkstyle and SpotBugs handle mechanical style enforcement automatically —
this file focuses on design, logic, and correctness issues that static analysis cannot catch.

---

## What to always flag

### Naming
- Class or interface names that are not nouns or noun phrases (e.g. `DataProcessor` is fine; `ProcessData` is not)
- Method names that are not verbs or verb phrases
- Inconsistent synonyms across the codebase — flag if the same concept is called by different names (e.g. `fetch` vs `retrieve` vs `get`)
- Names that require a comment to explain what they do — the name itself should be self-explanatory

### Functions / Methods
- Methods that do more than one thing — if a non-restatement sub-function could be extracted, flag it
- Boolean (flag) arguments — these should be split into two separate methods
- Side effects: methods that do something beyond what their name implies
- Returning error codes instead of throwing exceptions
- Switch statements outside of an Abstract Factory — repeated switch/instanceof chains should use polymorphism
- Methods mixing levels of abstraction — high-level logic and low-level detail in the same function

### Classes
- Classes with more than one reason to change (SRP violation)
- Low cohesion: a subset of methods that only uses a subset of instance variables should likely be its own class
- Classes that cannot be described in 25 words without "and", "or", "if", or "but" — too many responsibilities
- Depending on concrete implementations instead of interfaces (violates Dependency Inversion)
- Open/Closed violations: adding new behavior requires modifying existing code instead of extending it
- Classes should be at most 200 lines long. If exceeded, should consider splitting responsibilities (SRP) 

### Object Design
- Law of Demeter violations: chaining calls like `a.getB().getC().doSomething()`
- Hybrid objects: classes that mix business logic with plain data (pick one)
- Public fields on objects that should encapsulate behavior
- Mutable objects returned directly from getters (breaks encapsulation)

### Error Handling
- Returning `null` — use Optional, a Special Case object, or throw an exception
- Passing `null` as a method argument
- Catch blocks that swallow exceptions silently (empty catch or just `e.printStackTrace()`)
- Checked exceptions that leak implementation details across abstraction layers — consider wrapping
- Missing context in exception messages — should include what failed and relevant state

### Comments
- Commented-out code — should be deleted; version control preserves history
- Comments that restate what the code already says
- Missing explanation for non-obvious decisions — if intent isn't clear from the code, a comment is warranted
- TODO comments that are vague or have been sitting for more than one sprint

### Concurrency (if applicable)
- Shared mutable state without synchronization
- Synchronized blocks that are larger than necessary
- Ignoring or suppressing `InterruptedException`

### Testing
- Tests that assert on more than one concept
- Tests that depend on execution order or shared mutable state
- Tests without clear Arrange / Act / Assert structure
- Missing tests for boundary conditions and error paths
- Mocks used where a simple stub would do — over-mocking makes tests brittle

### Java-Specific
- Using `==` to compare Strings or objects instead of `.equals()`
- `instanceof` chains that should be replaced with polymorphism or pattern matching
- Resource leaks: streams, connections, or readers not closed in a `finally` block or try-with-resources
- `Optional` used as a method parameter or field (only appropriate as a return type)
- Catching `Exception` or `Throwable` instead of a specific exception type
- Raw types (e.g. `List` instead of `List<String>`)

---

## What to skip

- Line length, indentation, whitespace, and brace style — enforced by Checkstyle
- Naming format (casing, prefixes) — enforced by Checkstyle
- Method length over 20 lines — enforced by Checkstyle
- Methods with 3+ parameters — enforced by Checkstyle
- Magic numbers — enforced by Checkstyle
- Wildcard imports and unused imports — enforced by Checkstyle
- Empty catch blocks — enforced by Checkstyle
- `equals` without `hashCode` — enforced by Checkstyle
- Null dereferences, resource leaks, and `==` on objects — caught by SpotBugs
- Generated code (e.g. files ending in `Generated.java`, Lombok-generated methods, protobuf output)
- Pre-existing issues not introduced by this PR — flag as pre-existing only if blocking

---

## Severity guidance

- **High**: correctness bugs, Law of Demeter violations, null returns, concurrency issues, SRP violations
- **Normal**: missing abstraction, poor naming intent, error handling gaps, Dependency Inversion violations
- **Nit**: comment quality, minor naming clarity, test structure improvements
- **Pre-existing**: issues present before this PR that were not introduced by the author