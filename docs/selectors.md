## Selectors

```sh
once :: Parser[A] -> Parser[A]

Example:
'b'.once | "boom"     // Success('b', ...)
tab.once | "\t hello" // Success('\t', ...)
```

```sh
many :: Parser[A] -> Parser[List[A]]

Example:
'b'.many | ""     // Success(Nil, ...)
digit.many | "123.45" // Success(List('1', '2', '3'), ...)
```

```sh
atLeastOne :: Parser[A] -> Parser[List[A]]

Example:
'b'.atLeastOne | "text"     // Failure
(digit >> ';'.once).atLeastOne | "1;2;3" // Success( List(('1', ';'), ('2', ';')), ... )
```

```sh
more :: Parser[A] -> Int-> Parser[List[A]]

Example:
'b'.more(2) | "bbbb"     // Success(List('b', 'b', 'b', 'b'), ...)
digit.many | "123.45" // Success(List('1', '2', '3'), ...)
```