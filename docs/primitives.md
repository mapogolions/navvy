## Basic built-in primitives


`digit :: Parser[Char]`
> *Any of the digits*

Examples:
```scala
digit | "1. Some text"  // Success('1', ...)
```
------------

`digits :: Parser[List[Char]]`
> *At least one of the digit*

Examples:
```scala
digits | "1234text"  // Success(List('1', '2', '3', '4'), ...)
```
------------

`float :: Parser[Double]`
> *Any of the float digits*

Examples:
```scala
float | "-23.34N" // Success(-23.34, ...)
```
------------

`pint :: Parser[Int]`
> *The same as digit, but result is converted to integer*

Examples:
```scala
pint | "-23.34N" // Success(-23, ...)
```
------------

`whitespace :: Parser[Char]`
> *Any of `' '`, `\t`, `\n`*

Examples:
```scala
whitespace | " \t\ntext" // Success(' ', ...)
```
------------

`whitespaces :: Parser[List[Char]]`
> *At least one of the  whitespaces*

Examples:
```scala
whitespaces | " \t\ntext" // Success(List(' ', '\t', '\n'), ...)
```
--------------

`letter :: Parser[Char]`
> *Any of the letter*

Examples:
```scala
letter | "$" // Failure(...)
```
-----------

`letterOrDigit :: Parser[Char]`
> *Any char which being letter or digit*

Examples:
```scala
letterOrDigit | "34text" // Success('3', ...)
letterOrDigit | "t3ext"  // Success('t', ...)
```
-----------

`upper :: Parser[Char]`
> *Any letter in upper case*

Examples:
```scala
upper | "Text" // Success('T', ...)
```
---------------

`lower :: Parser[Char]`
> *Any letter in lower case*

Examples:
```scala
lower | "tEXT" // Success('t', ...)
```
------------

`space :: Parser[Char]`
> *Corresponding with space letter - `' '`*

Examples:
```scala
space | "    " // Success(' ', ...)
```
-----------

`tab :: Parser[Char]`
> *Corresponding with tab letter - `\t`*

Examples:
```scala
tab | "\t\ntext" // Success('\t', ...)
```
------------

`newline :: Parser[Char]`
> *Corresponding with newline letter - `\n`*

Examples:
```scala
newline | "\ntext" // Success('\n', ...)
```
------------

`whatever :: Parser[List[Char]]`
> *Whatever*

Examples:
```scala
whatever | "" // Success(Nil, ...)
```