## Combinators

```sh
>> :: Parser[A] -> Parser[B] -> Parser[(A, B)]

Examples:
digit >> '.'.once | "1. Some text" // Success(('1', '.'), ...)
tab >> digits.times(3) | "\t12345" // Success(('\t', List('1', '2', '3')), ...)
```

```sh
<|> :: Parser[A] -> Parser[B] -> Parser[A | B]

Examples:
float <|> pint | "-344.45L" // Success(-344.45, ...)
pint <|> float | "-344.45L" // Success(-344, ...)
```

```sh
|> :: Parser[A] -> Parser[B] -> Parser[B]

Examples:
"hello".once |> "world".once | "helloworld" // Success("world")
'1'.once |> '2'.once | "1234"               // Success('2', ...)
```

```sh
<| :: Parser[A] -> Parser[B] -> Parser[A]

Examples:
"hello".once <| "world".once | "helloworld" // Success("hello")
'1'.once <| '2'.once | "1234"               // Success('1', ...)
```

```sh
between :: Parser[A] -> Parser[B] -> Parser[C] -> Parser[B]

Examples:
'.'.between(digits)(whitespace) | "1. Some text" // Success('.', ...)
```