## Combinators


`>> :: Parser[A] -> Parser[B] -> Parser[(A, B)]`
> *Combining two parsers in sequence: the `and then` combinator*

Examples:
```sh
digit >> '.'.once | "1. Some text"
tab >> digits.times(3) | "\t12345"
```
------------

`<|> :: Parser[A] -> Parser[B] -> Parser[A | B]`
> *Choosing between two parsers: the `or else` combinator*

Examples:
```sh
float <|> pint | "-344.45L" // Success(-344.45, ...)
pint <|> float | "-344.45L" // Success(-344, ...)
```
-------------

`|> :: Parser[A] -> Parser[B] -> Parser[B]`
> *Keeps only the result of the right side parser*

Exmaples:
```sh
"hello".once |> "world".once | "helloworld" // Success("world")
'1'.once |> '2'.once | "1234"               // Success('2', ...)
```
--------------

`<| :: Parser[A] -> Parser[B] -> Parser[A]`
> *Keeps only the result of the left side parser*

Examples:
```sh
"hello".once <| "world".once | "helloworld" // Success("hello")
'1'.once <| '2'.once | "1234"               // Success('1', ...)
```
---------------

`between :: Parser[A] -> Parser[B] -> Parser[C] -> Parser[B]`
> *Keeps only the result of the middle parser*

Examples:
```sh
'.'.between(digits)(whitespace) | "1. Some text" // Success('.', ...)
```
--------------

`after :: Parser[A] -> Parser[B] -> Parser[A]`

Examples:
```sh
whitespace.after(digit) | "1 hello" // Success(' ', ...)
```
--------------

`before :: Parser[A] -> Parser[B] -> Parser[A]`

Examples:
```sh
digit.before(whitespace) | "1 hello" // Success('1', ...)
```
--------------