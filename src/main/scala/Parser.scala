package navvy.adt

import navvy.adt.{ Result, Success, Failure }
import navvy.functor.Functor
import navvy.functor.FunctorInstances._
import navvy.functor.FunctorSyntax._
import navvy.applicative.Applicative
import navvy.applicative.ApplicativeInstances._
import navvy.applicative.ApplicativeSyntax._


trait Parser[A](val label: String="unknow") { self =>
  def sep[B](pb: Parser[B]) = 
    self >> (pb |> self many) map { (x, xs) => x :: xs }

  def after[B](pb: Parser[B]) = pb |> self
  def before[B](pb: Parser[B]) = self <| pb
  def between[B, C](pb: Parser[B])(pc: Parser[C]) = pb |> self <| pc

  def <|[B](pb: Parser[B]) = self >> pb map { (a, b) => a }
  def |>[B](pb: Parser[B]) = self >> pb map { (a, b) => b }

  def opt: Parser[Option[A]] = 
    self.map(Some(_)) <|> (Applicative[Parser] pure None)
  
  def once = self

  def moreOrEq(n: Int): Parser[List[A]] = 
    (self.times(n) >> self.many ?? s"more or equals $n").map((xs, ys) => xs ++ ys)

  def more(n: Int): Parser[List[A]] = moreOrEq(n + 1) ?? s"more than $n"

  def less(n: Int): Parser[List[A]] =
    Range(0, n).toList.reverse
      .map(x => self.times(x)).reduce(_ <|> _) ?? s"less than $n"

  def lessOrEq(n: Int): Parser[List[A]] = less(n + 1) ?? s"less or equals $n"

  def times(n: Int): Parser[List[A]] = new Parser[List[A]]("times") {
    def apply(source: Source): Result[List[A]] = {
      @annotation.tailrec
      def loop(acc: List[A], source: Source, count: Int): Result[List[A]] = {
        (count, self apply source) match {
          case (1, Failure(lbl, err, pos)) => Failure(label, err, pos)
          case (_, Failure(lbl, err, pos)) => Failure(label, err, pos)
          case (1, Success(elem, src)) => Success(elem :: acc, src)
          case (_, Success(elem, src)) => loop(elem :: acc, src, count - 1)
        }
      }
      if (n <= 0) Failure(label, s"Invalid n = $n", Position from source)
      else loop(Nil, source, n)
    }
  }

  def atLeastOne: Parser[List[A]] = new Parser[List[A]]("at least one") {
    def apply(source: Source) = self apply source match {
      case Failure(_, err ,pos) => Failure(label, err ,pos)
      case Success(elem, src) => self.many.apply(src).map(xs => elem :: xs)
    }
  }

  def many: Parser[List[A]] = new Parser[List[A]]("many") {
    def apply(source: Source): Result[List[A]] = {
      @annotation.tailrec
      def loop(acc: List[A], source: Source): (List[A], Source) = {
        self apply source match {
          case Failure(_, _, _) => (acc.reverse, source)
          case Success(elem, src) => loop(elem :: acc, src)
        }
      }
      val (ls, src) = loop(Nil, source)
      Success(ls, src)
    }
  }

  def >>[B](pb: Parser[B]): Parser[(A, B)] = new Parser[(A, B)]() {
    def apply(source: Source): Result[(A, B)] = (self apply source) match {
      case Failure(label1, err1, pos1) => Failure(label1, err1, pos1)
      case Success(h1, t1) => (pb apply t1) match {
        case Failure(label2, err2, pos2) => Failure(label2, err2, pos2)
        case success => success.map((h1, _))
      }
    }
  } ?? s"${self.label} andThen ${pb.label}"

  def ??(label: String): Parser[A] = new Parser[A](label) {
    def apply(source: Source): Result[A] = (self apply source) match {
      case Failure(_, err, pos) => Failure(label, err, pos)
      case Success(h, t) => Success(h, t)
    }
  }

  def <|>[B](pb: Parser[B]): Parser[A | B] = new Parser[A | B]() {
    def apply(source: Source): Result[A | B] = (self apply source) match {
      case Success(h, t) => Success(h, t)
      case Failure(_, _, _) => (pb apply source)
    }
  } ?? s"${self.label} orElse ${pb.label}"

  def lift2[B, C](f: A => B => C)(pb: Parser[B]) =
    (Applicative[Parser] pure f) ap self ap pb
  
  def lift3[B, C, D](f: A => B => C => D)(pb: Parser[B])(pc: Parser[C]) =
    (Applicative[Parser] pure f) ap self ap pb ap pc

  def apply(source: Source): Result[A]
  def | (text: String) = apply (Source from text)
  def |>>[B](f: A => B) = self.map(f)
}
