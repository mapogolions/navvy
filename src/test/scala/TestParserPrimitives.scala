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


class TestParserPrimitives {
  @Test
  def TestUppserCase: Unit = {
    (upper | "Albus").test(
      (elem: Char, src: Source) => assertEquals(elem, 'A'),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    (upper.atLeastOne | "OOps").test(
      (elem: List[Char], src: Source) => assertEquals(elem, List('O', 'O')),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }
  
  @Test
  def TestPint: Unit = {
    (pint | "123hello").test(
      (elem: Int, src: Source) => assertEquals(elem, 123),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    (pint | "-1020").test(
      (elem: Int, src: Source) => assertEquals(elem, -1020),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }

  @Test
  def TestDigit: Unit = {
    (digit.atLeastOne | "134").test(
      (elem: List[Char], src: Source) => assertEquals(elem, List('1', '3', '4')),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    (digit.many | "text").test(
      (elem: List[Char], src: Source) => assertEquals(elem, Nil),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }

  @Test
  def TestWhitespace: Unit = {
    (whitespace | " text").test(
      (elem: Char, src: Source) => assertEquals(elem, ' '),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    (whitespace | "\t text").test(
      (elem: Char, src: Source) => assertEquals(elem, '\t'),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    (whitespace | "\n\t text").test(
      (elem: Char, src: Source) => assertEquals(elem, '\n'),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    ((whitespace |> digit).many | " 1 2 3 ").test(
      (elem: List[Char], src: Source) => 
        assertEquals(elem, List('1', '2', '3')),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )

    (('-'.once <| digit).many | "-1-2-3-").test(
      (elem: List[Char], src: Source) => 
        assertEquals(elem, List('-', '-', '-')),
      (label: String, err: String, pos: Position) => assertFail("Should be success")
    )
  }

  @Test
  def TestWhitespaces: Unit = {
    (whitespaces | " \t\n").test(
      (elem: List[Char], src: Source) => assertEquals(elem, List(' ', '\t', '\n')),
      (lbl: String, e: String, pos: Position) => assertFail("Should be Success")
    )
    
    (whitespaces | "").test(
      (elem: List[Char], src: Source) => assertFail("Should be Failure"),
      (lbl: String, e: String, pos: Position) => {
        assertEquals(lbl, "whitespaces")
        assertEquals((pos.row, pos.col), (0, 0))
      }
    )
  }
}