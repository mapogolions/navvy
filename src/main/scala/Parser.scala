package navvy.adt

import navvy.adt.{ Result, Success, Failure }
import navvy.functor.Functor
import navvy.functor.FunctorInstances._
import navvy.functor.FunctorSyntax._
import navvy.applicative.Applicative
import navvy.applicative.ApplicativeInstances._
import navvy.applicative.ApplicativeSyntax._


trait Parser[A](val label: String="unknow") { self =>
  // Parses one or more occurrences of p separated by sep
  def sep[B](pb: Parser[B]) = 
    self >> (pb |> self many) map { (x, xs) => x :: xs }
  // Keep only result of the middle parser - between
  def between[B, C](pb: Parser[B])(pc: Parser[C]) = pb |> self <| pc

  // throwing results away
  def <|[B](pb: Parser[B]) = self >> pb map { (a, b) => a }
  def |>[B](pb: Parser[B]) = self >> pb map { (a, b) => b }

  // matching parser zero or one time
  def opt: Parser[Option[A]] = 
    self.map(Some(_)) <|> (Applicative[Parser] pure None)

  def once = self

  def moreOrEq(n: Int): Parser[List[A]] = 
    (self.repeat(n) >> self.many ?? s"more or equals $n").map((xs, ys) => xs ++ ys)

  def more(n: Int): Parser[List[A]] =
    moreOrEq(n + 1) ?? s"more than $n"

  def less(n: Int): Parser[List[A]] =
    Range(0, n).toList.reverse
      .map(x => self.repeat(x)).reduce(_ <|> _) ?? s"less than $n"

  def lessOrEq(n: Int): Parser[List[A]] = 
    less(n + 1) ?? s"less or equals $n"

  def many: Parser[List[A]] = new Parser[List[A]]("many") {
    def apply(source: Source): Result[List[A]] = {
      val (lsOfChar, src) = self anytimes source
      Success(lsOfChar, src)
    }
  } 

  def atLeastOne: Parser[List[A]] = new Parser[List[A]]("atLeastOne") {
    def apply(source: Source) = self oneOrMore source
  }

  def repeat(n: Int): Parser[List[A]] = new Parser[List[A]]("repeat times") {
    def apply(source: Source) = 
      if (n <= 0) Failure(label, s"Unexpected $n", Position from source)
      else self.ntimes(source, n)
  }

  private def ntimes(source: Source, n: Int): Result[List[A]] = {
    @annotation.tailrec
    def loop(src: Source, acc: List[A], count: Int): Result[List[A]] = {
      (count, self apply src) match {
        case (0, _) => Success(acc.reverse, src)
        case (_, Failure(label, err, pos)) => Failure(label, err, pos)
        case (_, Success(h, t)) => loop(t, h :: acc, count - 1)
      }
    }
    loop(source, Nil, n)
  }

  private def oneOrMore(source: Source): Result[List[A]] =
    (self apply source) match {
      case Failure(label, err, pos) => Failure(label, err, pos)
      case Success(h, t) => {
        val (ch, src) = self anytimes t
        Success(h :: ch,  src)
      }
    }

   private def anytimes(source: Source): (List[A], Source) =
    (self apply source) match {
      case Failure(_, _, _) => (Nil, source)
      case Success(h, t) => {
         val (ch, src) = self anytimes t
        (h :: ch, src)
      }
    }

  def >>[B](pb: Parser[B]): Parser[(A, B)] = new Parser[(A, B)]() {
    def apply(source: Source): Result[(A, B)] = (self apply source) match {
      case Failure(label1, err1, pos1) => Failure(label1, err1, pos1)
      case Success(h1, t1) => (pb apply t1) match {
        case Failure(label2, err2, pos2) => Failure(label2, err2, pos2)
        case Success(h2, t2) => Success((h1, h2), t2)
      }
    }
  } ?? s"${self.label} andThen ${pb.label}"

  def ??(label: String): Parser[A] = new Parser[A](label) {
    def apply(source: Source): Result[A] = (self apply source) match {
      case Success(h, t) => Success(h, t)
      case Failure(_, err, pos) => Failure(label, err, pos)
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
  def |>>  = self.map
}
