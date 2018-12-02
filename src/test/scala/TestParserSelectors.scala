import org.junit.Test
import org.junit.Assert._
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.equalTo

import navvy.adt._
import navvy.ops._
import navvy.testkit.ops._
import navvy.functor.Functor
import navvy.functor.FunctorInstances._
import navvy.functor.FunctorSyntax._
import navvy.applicative.Applicative
import navvy.applicative.ApplicativeInstances._
import navvy.applicative.ApplicativeSyntax._


class TestParserSelectors {
  @Test
  def TestBetween: Unit = {
    ('a'.between('b'.once)('c'.once) | "bac-...").test(
      (elem: Char, src: Source) => assertEquals(elem, 'a'),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }

  @Test
  def TestSep: Unit = {
    ('?'.sep(whitespace) | "? ? ?...").test(
      (elem: List[Char], src: Source) => assertEquals(elem, List('?', '?', '?')),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }

  @Test
  def TestOpt: Unit = {
    ('a'.parse.opt | "alloha").test(
      (elem: Option[Char], src: Source) => assertEquals(elem, Some('a')),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    ("ll".parse.opt | "lloha").test(
      (elem: Option[String], src: Source) => assertEquals(elem, Some("ll")),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    (pint.opt | "233h3").test(
      (elem: Option[Int], src: Source) => assertEquals(elem, Some(233)),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }

  @Test
  def TestAtLeastOne: Unit = {
    ("w".atLeastOne | "www.google.com").test(
      (elem: List[String], src: Source) => assertEquals(elem, List("w", "w", "w")),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    ('a'.atLeastOne | "abus").test(
      (elem: List[Char], src: Source) => assertEquals(elem, 'a' :: Nil),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }

  @Test
  def TestMany: Unit = {
    (digit.many | "123hello").test(
      (elem: List[Char], src: Source) => assertEquals(elem, List('1', '2', '3')),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    ("www".many | "file://").test(
      (elem: List[String], src: Source) => assertEquals(elem, Nil),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    ('b'.many | "").test(
      (elem: List[Char], src: Source) => assertEquals(elem, Nil),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }

  @Test
  def TestOnce: Unit = {
    ('a'.once | "alive").test(
      (elem: Char, src: Source) => assertEquals(elem, 'a'),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    ("fun".once | "functor").test(
      (elem: String, src: Source) => assertEquals(elem, "fun"),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }

  @Test
  def TestParseString: Unit = {
    val parseABC = "ABC".parse
    (parseABC | "ABCDE").test(
      (elem: String, src: Source) => assertEquals(elem, "ABC"),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }
}
