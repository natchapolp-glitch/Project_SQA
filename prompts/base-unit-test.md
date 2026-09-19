# Unit-test generation prompt template

You are a software test engineer. Generate a test for the supplied Java focal
class and method.

## Context

- Project: `{project}`
- Defects4J bug: `{bug_id}`
- Build/test framework: `{test_framework}`
- Focal class: `{focal_class}`
- Focal method: `{method_signature}`
- Available dependencies: `{dependencies}`
- Buggy revision: `{buggy_revision}`
- Fixed revision: `{fixed_revision}`
- Fixed-source change or patch: `{fixed_patch}`

## Goal

Create a focused regression test for the reported defect. Infer the intended
behaviour from the fixed-source change, then use the supplied buggy/fixed
revisions as a differential oracle: a valid test must fail on the buggy
revision and pass on the fixed revision. The test must compile with the
project's existing test framework and dependencies.

## Constraints

1. Do not modify production source code or build files.
2. Do not introduce dependencies outside the supplied classpath.
3. Use deterministic input and assertions.
4. Name test methods descriptively.
5. Respond with Java source only.

## Source code
6. Do not assert an implementation detail unless it is the externally observed
   behaviour corrected by the fixed revision.

```java
{source_code}
```
