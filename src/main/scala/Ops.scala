package navvy

import navvy.adt._
import navvy.functor.Functor
import navvy.functor.FunctorInstances._
import navvy.functor.FunctorSyntax._
import navvy.applicative.Applicative
import navvy.applicative.ApplicativeInstances._
import navvy.applicative.ApplicativeSyntax._

object ops {
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
  def tab = '\t'.parse ?? "tab"
  def newline = '\n'.parse ?? "newline"
  def whitespaces = whitespace.atLeastOne ?? "whitespaces"
  def whatever = satisfy { _ => true } ("whatever")

  // digits
  def digit = satisfy { _ isDigit } ("digit")
  def digits = digit.atLeastOne ?? "any of [0-9]"
  def pint = {
    def resultToInt[A](sign: Option[A], i: Int): Int = sign match {
      case None => i
      case Some(_) => -i
    }
    (('-' opt) >> digits.map(_ mkString("") toInt)).map(resultToInt) ?? "pint"
  }

  def float = {
    def resultToFloat(input: (((Option[Char], String), Char), String)) = {
      val (((sign, digit1), point), digit2) = input
      val number = s"${digit1}.${digit2}".toDouble
      sign match {
        case None => number
        case Some(ch) => -number
      }
    }
    (('-'.opt) >> digits.map(_ mkString("")) >> '.'.once >> digits.map(_ mkString("")))
      .map(resultToFloat) ?? "float"
  }

  // special
  def email = {
    val local = letterOrDigit.atLeastOne.map(xs => xs.mkString(""))
    val domain = ('@'.once >> 
                  ("mail.ru".once <|> "gmail.com".once <|> "yandex.ru".once)
                 ).map(_ + _)
    (local >> domain).map(_ + _)
  }
  def tel = (digits >> '-'.once >> digits)
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
    def skip(n: Int=1) = parse skip n
    def times(n: Int) = parse times n
    def sep[B] = parse.sep[B]
    def between[B, C] = parse.between[B, C]
    def opt = parse opt
    def atLeastOne: Parser[List[Char]] = parse atLeastOne
    def many: Parser[List[Char]] = parse many
    def once: Parser[Char] = parse
    def parse: Parser[Char] = satisfy { x: Char => x == ch } (s"$ch")
  }

  implicit class StringOps(str: String) {
    def skip(n: Int=1) = parse skip n
    def times(n: Int) = parse times n
    def sep[B] = parse.sep[B]
    def between[B, C] = parse.between[B, C]
    def opt = parse opt
    def atLeastOne: Parser[List[String]] = parse atLeastOne
    def many: Parser[List[String]] = parse many
    def once: Parser[String] = parse
    def parse: Parser[String] =
      str.toList.map(_ parse).sequence.map(_ mkString("")) ?? s"$str"
  }
  
  implicit class ListOfCharsOps(ls: List[Char]) {
    def anyOf = ls.map(_ parse).choice ?? s"Any of ${ls.mkString("|")}"
  }
  
  implicit class ParserOps[A](pa: Parser[A]) {
    def skip(n: Int=1) = whatever.times(n) |> pa
  }

  implicit class ListOfParserOps[A, B](ls: List[Parser[A]]) {
    def choice: Parser[A] = ls reduce(_ <|> _)
    def sequence: Parser[List[A]] = ls match {
      case Nil => Applicative[Parser] pure Nil
      case x::xs => x.lift2({ y: A => ys: List[A] => y :: ys })(xs.sequence)
    }
  }
}