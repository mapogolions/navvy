package navvy

import navvy.adt._
import navvy.functor.Functor
import navvy.functor.FunctorInstances._
import navvy.functor.FunctorSyntax._
import navvy.applicative.Applicative
import navvy.applicative.ApplicativeInstances._
import navvy.applicative.ApplicativeSyntax._

object ops {
  // for draft
  def startWith = lift({ token: String => token.startsWith })
  def add = lift({ a: Int => b: Int => a + b })
  def lift[A, B, C](f: A => B => C)(pa: Parser[A])(pb: Parser[B]) =
    Applicative[Parser].pure(f).ap(pa).ap(pb)

  def parserThreeDigitsAsInt = parseThreeDigitsAsStr map { _.toInt }
  def parseThreeDigitsAsStr = {
    def transform(xs: ((Char, Char), Char)): String = 
      xs match { case ((a, b), c) => s"${a}${b}${c}" }
    parseThreeDigits.map(transform)
  }

  def parseThreeDigits = parseDigit >> parseDigit >> parseDigit
  def parseLowerCase = Range('a', 'z').toList.map(_ toChar) anyOf
  def parseUpperCase = Range('A', 'Z').toList.map(_ toChar) anyOf
  def parseDigit = List('0', '1', '2', '3', '4', '5', '6', '7', '8', '9') anyOf

  // characters
  def letter = satisfy { _ isLetter } ("letter")
  def letterOrDigit = satisfy { _ isLetterOrDigit } ("letter or digit")
  def upper = satisfy { _ isUpper } ("upper case")
  def lower = satisfy { _ isLower } ("lower case")
  def whitespace = satisfy { _ isWhitespace } ("whitespace")
  def space = ' '.parse ?? "space"
  def whitespaces = whitespace atLeastOne
  def whatever = satisfy { _ => true } ("whatever")

  // digits
  def digit = satisfy { _ isDigit } ("digit")
  def digits = digit.atLeastOne ?? "Any of [0-9]"
  def pint = {
    def negate[A](sign: Option[A], i: Int): Int = sign match {
      case None => i
      case Some(_) => -i
    }
    ('-' opt) >> digits.map(_ mkString("") toInt) map negate
  }

  // special
  def email = {
    val nickname = letterOrDigit.atLeastOne.map(xs => xs.mkString(""))
    val domain = ('@'.once >> 
                  ("mail.ru".once <|> "gmail.com".once <|> "yandex.ru".once)
                 ).map(_ + _)
    (nickname >> domain).map(_ + _)
  }
  def tel1 = (digits >> '-'.once >> digits)
    .map( (xs, ys) => xs._1 ++ List(xs._2) ++ ys)
    .map(_ mkString(""))

  private def satisfy[A >: Char](p: A => Boolean)(label: String) =
    new Parser[A](label) {
      def apply(source: Source) = source.char match {
        case (src, None) => 
          Failure(label, "No more input", Position from src)
        case (src, Some(ch)) =>
          if (p(ch)) Success(ch, src)
          else Failure(label, s"Unexpected $ch", Position from source)
      }
  }

  implicit class CharOps(ch: Char) {
    def sep[B] = parse.sep[B]
    def between[B, C] = parse.between[B, C]
    def opt = parse opt
    def lessOrEq(n: Int) = parse lessOrEq n
    def less(n: Int) = parse less n
    def more(n: Int) = parse more n
    def moreOrEq(n: Int) = parse moreOrEq n
    def repeat(n: Int) = parse repeat n
    def atLeastOne: Parser[List[Char]] = parse atLeastOne
    def many: Parser[List[Char]] = parse many
    def once: Parser[Char] = parse
    def parse: Parser[Char] = satisfy { x: Char => x == ch } (s"$ch")
  }

  implicit class StringOps(str: String) {
    def sep[B] = parse.sep[B]
    def between[B, C] = parse.between[B, C]
    def opt = parse opt
    def lessOrEq(n: Int) = parse lessOrEq n
    def less(n: Int) = parse less n
    def more(n: Int) = parse more n
    def moreOrEq(n: Int) = parse moreOrEq n
    def repeat(n: Int) = parse repeat n
    def atLeastOne: Parser[List[String]] = parse atLeastOne
    def many: Parser[List[String]] = parse many
    def once: Parser[String] = parse
    def parse: Parser[String] =
      str.toList.map(_ parse).sequence map { _ mkString("") }
  }

  implicit class ParserListOps[A](pa: Parser[List[A]]) {
    def count = pa.map(xs => xs.length)
    def string(delim: String="") = pa.map(xs => xs.mkString(delim))
    def int = pa.map(_ mkString("") toInt)
  }
  
  implicit class ListOfCharsOps(ls: List[Char]) {
    def anyOf = ls.map(_ parse).choice ?? s"Any of ${ls.mkString("|")}"
  }
 
  implicit class ListOfParserOps[A, B](ls: List[Parser[A]]) {
    def choice: Parser[A] = ls reduce(_ <|> _)
    def sequence: Parser[List[A]] = {
      def cons[A] =  lift({ x: A => xs: List[A] => x :: xs })
      ls match {
        case Nil => Applicative[Parser] pure Nil
        case x::xs => cons(x)(xs sequence)
      }
    }
  }
}