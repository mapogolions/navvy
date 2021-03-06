## Simple library for parsing


### [Combinators](./docs/combinators.md)

- `>>` - *combining two parsers in sequence: the `and then` combinator*
- `<|>` - *choosing between two parsers: the `or else` combinator*
- `|>` - *keeps only the result of the right side parser*
- `<|` - *keeps only the result of the left side parser*
- `between` - *keeps only the result of the middle parser*
- `after` - *alias |>. Reverse logic*
- `before` - *alias <|. Reverse logic*

### [Basic built-in primitives](./docs/primitives.md)

- `whitespace` - *any of `' '`, `\t`, `\n`*
- `whitespaces` - *at least one of the  whitespaces*
- `letter` - *any of the letter*
- `letterOrDigit`- *any char which being letter or digit*
- `upper` - *any letter in upper case*
- `lower` - *any letter in lower case*
- `space` - *corresponding with space letter - `' '`*
- `tab` - *corresponding with tab letter - `\t`*
- `newline` - *corresponding with newline letter - `\n`*
- `digit` - *any of the digits*
- `digits` - *at least one of the digit*
- `pint` - *the same as digit, but result converts to integer*
- `float` - *any of the float digits*
- `whatever` - *whatever - (black hole)*

### [Selectors](./docs/selectors.md)

- `once` - *matches one occurances of the specified parser*
- `parse` - *alias `once`*
- `many` - *matches zero or more occurences of the specified parser*
- `atLeastOne` - *matches at least one occurences of the specified parser*
- `more` - *matches more than `n` times of the specified parser*
- `moreOrEq` - *matches more or equal than `n` times of the specified parser*
- `less` - *matches less than `n` times of the specified parser*
- `lessOrEq` - *matches less of equal `n` times of the specified parser*
- `opt` - *matches an optional occurrences of the specified parser*
- `times` - *matches `n` occurances of the specified parser*
- `sep` - *parses zero or more occurences of a parser with a separator*



## sbt project cross-compiled with Dotty and Scala 2

### Usage

This is a normal sbt project, you can compile code with `sbt compile` and run it
with `sbt run`, `sbt console` will start a Dotty REPL. For more information on
cross-compilation in sbt, see <https://www.scala-sbt.org/1.x/docs/Cross-Build.html>.

For more information on the sbt-dotty plugin, see the
[dotty-example-project](https://github.com/lampepfl/dotty-example-project/blob/master/README.md).
