## Selectors


### once

`once :: Parser[A] -> Parser[A]`
> *Matches one occurances of the specified parser*

Examples:
```sh
'b'.once | "boom"     // Success('b', ...)
tab.once | "\t hello" // Success('\t', ...)
```
-----------

### many

`many :: Parser[A] -> Parser[List[A]]`
> *Matches zero or more occurences of the specified parser*

Examples:
```scala
'b'.many | ""     // Success(Nil, ...)
digit.many | "123.45" // Success(List('1', '2', '3'), ...)
```
------------

### atLeastOne

`atLeastOne :: Parser[A] -> Parser[List[A]]`
> *Matches at least one occurences of the specified parser*

Examples:
```scala
'b'.atLeastOne | "text"     // Failure
(digit >> ';'.once).atLeastOne | "1;2;3" // Success( List(('1', ';'), ('2', ';')), ... )
```
---------------

### more

`more :: Parser[A] -> Int-> Parser[List[A]]`
> *Matches more than `n` times of the specified parser*

Examples:
```scala
'b'.more(2) | "bbbb"     // Success(List('b', 'b', 'b', 'b'), ...)
digit.more | "123.45" // Success(List('1', '2', '3'), ...)
```
--------------

### moreOrEq

`moreOrEq :: Parser[A] -> Int-> Parser[List[A]]`
> *Matches more or equal than `n` times of the specified parser*

Examples:
```scala
'b'.moreOrEq(2) | "bb"     // Success(List('b', 'b'), ...)
digit.moreOrEq | "123.45" // Success(List('1', '2', '3'), ...)
```
----------------

### less

`less :: Parser[A] -> Int-> Parser[List[A]]`
> *Matches less than `n` times of the specified parser*

Examples:
```scala
'b'.less(2) | "bb"     // Success(List('b'), ...)
```
---------------

### lessOrEq

`lessOrEq :: Parser[A] -> Int-> Parser[List[A]]`
> *Matches less of equal `n` times of the specified parser*

Examples:
```scala
'b'.lessOrEq(2) | "bb"     // Success(List('b', 'b'), ...)
```
------------------
