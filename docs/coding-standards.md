# Java coding standards

This project follows the [SE Education Java coding standard](https://se-education.org/guides/conventions/java/index.html). These are the main rules to apply when writing or reviewing Java code.

## Naming

- Use lowercase package names.
- Use PascalCase nouns for classes and enums.
- Use camelCase verbs for methods and camelCase names for variables.
- Use `SCREAMING_SNAKE_CASE` for constants.
- Name boolean fields and methods with prefixes such as `is`, `has`, `can`, or `should`.
- Use plural names for collections and arrays.
- Write names and comments in English.

## Layout and formatting

- Use four spaces for indentation; do not use tabs.
- Keep lines at 120 characters or fewer, with 110 characters as a preferred soft limit.
- Use K&R braces and braces for every conditional and loop body, including one-line bodies.
- Put spaces around operators, after commas, and after Java keywords such as `if` and `for`.
- Separate logical units in a method with one blank line.
- Use explicit imports; do not use wildcard imports.
- Keep class members in this order: documentation, class variables, instance variables, constructors, and methods.

## Design and documentation

- Put every class in a named package.
- Declare variables in the smallest scope possible and initialize them where they are declared when practical.
- Keep fields private unless a public field is justified by a data-class design; use methods to preserve encapsulation.
- Add Javadoc to every public class and public method, except getters, setters, and test methods where the behavior is obvious.
- Add Javadoc to nontrivial private methods and fields whose purpose is not obvious.
- Start method summaries with a third-person verb such as `Returns`, `Adds`, or `Calculates`.
- Add an explicit `// Fallthrough` comment when a switch case intentionally falls through.

When this document does not cover a topic, follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
