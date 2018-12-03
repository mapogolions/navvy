## Combinators


`>> :: Parser[A] -> Parser[B] -> Parser[(A, B)]`
> *Combining two parsers in sequence: the `and then` combinator*

Examples:
```scala
digit >> '.'.once | "1. Some text" // Success(('1', '.'), ...)
tab >> digit | "\t12345" // Success(('\t', '1'), ...)
```
------------

`<|> :: Parser[A] -> Parser[B] -> Parser[A | B]`
> *Choosing between two parsers: the `or else` combinator*

Examples:
```scala
float <|> pint | "-344.45L" // Success(-344.45, ...)
pint <|> float | "-344.45L" // Success(-344, ...)
```
-------------

`|> :: Parser[A] -> Parser[B] -> Parser[B]`
> *Keeps only the result of the right side parser*

Exmaples:
```scala
"hello".once |> "world".once | "helloworld" // Success("world")
'1'.once |> '2'.once | "1234"               // Success('2', ...)
```
--------------

`<| :: Parser[A] -> Parser[B] -> Parser[A]`
> *Keeps only the result of the left side parser*

Examples:
```scala
"hello".once <| "world".once | "helloworld" // Success("hello")
'1'.once <| '2'.once | "1234"               // Success('1', ...)
```
---------------

`between :: Parser[A] -> Parser[B] -> Parser[C] -> Parser[B]`
> *Keeps only the result of the middle parser*

Examples:
```scala
'.'.between(digits)(whitespace) | "1. Some text" // Success('.', ...)
```
--------------

`after :: Parser[A] -> Parser[B] -> Parser[A]`

Examples:
```scala
whitespace.after(digit) | "1 hello" // Success(' ', ...)
```
--------------

`before :: Parser[A] -> Parser[B] -> Parser[A]`

Examples:
```scala
digit.before(whitespace) | "1 hello" // Success('1', ...)
```
