## Combinators

```sh
>> :: Parser[A] -> Parser[B] -> Parser[(A, B)]
```

```sh
<|> :: Parser[A] -> Parser[B] -> Parser[A | B]
```

```sh
|> :: Parser[A] -> Parser[B] -> Parser[B]
```

```sh
<| :: Parser[A] -> Parser[B] -> Parser[A]
```

```sh
between :: Parser[A] -> Parser[B] -> Parser[C] -> Parser[B]
```