import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.equalTo

import navvy.adt._
import navvy.ops._
import navvy.functor.Functor
import navvy.functor.FunctorInstances._
import navvy.functor.FunctorSyntax._
import navvy.applicative.Applicative
import navvy.applicative.ApplicativeInstances._
import navvy.applicative.ApplicativeSyntax._


class TestParserCombinators {
  @Test
  def TestKeepRight: Unit = {
    (digit |> digit | "123").test(
      (elem: Char, src: Source) => assertEquals(elem, '2'),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    ('a'.once |> 'b'.once | "abba").test(
      (elem: Char, src: Source) => assertEquals(elem, 'b'),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }
  
  @Test
  def TestKeepLeft: Unit = {
    ('a'.once <| 'b'.once | "abba").test(
      (elem: Char, src: Source) => assertEquals(elem, 'a'),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    (digit  <| digit | "123").test(
      (elem: Char, src: Source) => assertEquals(elem, '1'),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    ('a'.once <| 'b'.atLeastOne | "abba").test(
      (elem: Char, src: Source) => assertEquals(elem, 'a'),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }

  @Test
  def TestMoreDifficultCases: Unit = {
    // Left associative
    val dAndBorF = 'd'.parse >> ('b'.parse <|> 'f'.parse)
    assertEquals(
      dAndBorF | "dfoo" toString,
      "(d,f)"
    )
    assertEquals(
      dAndBorF | "dbar" toString,
      "(d,b)"
    )

    (dAndBorF | "dfoo")
      .test(
        (elem: (Char, Char), src: Source) => assertEquals(elem, ('d', 'f')),
        (label: String, err: String, pos: Position) => assertFail("Should be success")
      )
  }

  @Test
  def TestOrElse: Unit = {
    assertEquals(
      'a'.parse <|> 'b'.parse | "aloha" toString,
      "a"
    )
    assertEquals(
      'a'.parse <|> 'b'.parse | "bloha" toString,
      "b"
    )
    assertEquals(
      'a'.parse <|> 'b'.parse | "abloha" toString,
      "a"
    )
  }

  @Test
  def TestAndThen: Unit = {
    assertEquals(
      'b'.parse >> 'c'.parse | "bcla-la-la" toString,
      "(b,c)"
    )
  }
}