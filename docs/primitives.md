## Basic build-in primitives (signatures)

```sh
digit :: Parser[Char]

digit | "1. Some text"  // Success('1', ...)
```

```sh
digits :: Parser[List[Char]]

digits | "1234text"  // Success(List('1', '2', '3', '4'), ...)
```

```sh
float :: Parser[Double]

float | "-23.34N" // Success(-23.34, ...)
```

```sh
pint :: Parser[Int]

pint | "-23.34N" // Success(-23, ...)
```

```sh
whitespace :: Parser[Char]

whitespace | " \t\ntext" // Success(' ', ...)
```

```sh
whitespaces :: Parser[List[Char]]

whitespaces | " \t\ntext" // Success(List(' ', '\t', '\n'), ...)
```

```sh
letter :: Parser[Char]

letter | "$" // Failure(...)
```

```sh
letterOrDigit :: Parser[Char]

letterOrDigit | "34text" // Success('3', ...)
letterOrDigit | "t3ext"  // Success('t', ...)
```

```sh
upper :: Parser[Char]

upper | "Text" // Success('T', ...)
```

```sh
lower :: Parser[Char]

lower | "tEXT" // Success('t', ...)
```

```sh
space :: Parser[Char]

space | "    " // Success(' ', ...)
```

```sh
tab :: Parser[Char]

tab | "\t\ntext" // Success('\t', ...)
```

```sh
newline :: Parser[Char]

newline | "\ntext" // Success('\n', ...)
```

```sh
whatever :: Parser[List[Char]]

whatever | "" // Success(Nil, ...)
```