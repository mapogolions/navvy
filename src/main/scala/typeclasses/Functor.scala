package navvy.functor

import navvy.adt._


trait Functor[F[_]] { self =>
  def map[A, B](fa: F[A])(f: A => B): F[B]
  def lift[A, B](f: A => B): F[A] => F[B] = fa => map(fa)(f)
  def as[A, B](fa: F[A], b: B): F[B] = map(fa)(_ => b)
  def void[A](fa: F[A]): F[Unit] = as(fa, ())
  def fproduct[A, B](fa: F[A])(f: A => B): F[(A, B)] = map(fa)(a => a -> f(a))
  
  def tupleLeft[A, B](fa: F[A], b: B): F[(B, A)] = map(fa)((b, _))
  def tupleRigth[A, B](fa: F[A], b: B): F[(A, B)] = map(fa)((_, b))

  def compose[G[_]: Functor]: Functor[[X] => F[G[X]]] =
    new Functor[[X] => F[G[X]]] {
      override def map[A, B](fga: F[G[A]])(f: A => B): F[G[B]] =
        self.map(fga)(fa => implicitly.map(fa)(f))
    }
}

object Functor {
  def apply[F[_]: Functor]: Functor[F] = implicitly
}

object FunctorSyntax {
  implicit class FunctorOps[F[_]: Functor, A](F: F[A]) {
    def map[B](f: A => B): F[B] = implicitly.map(F)(f)
    def lift[B](f: A => B): () => F[B] = () => implicitly.lift(f)(F)
    def as[B](b: B): F[B] = implicitly.as(F, b)
    def void[B]: F[Unit] = implicitly.void(F)
    def fproduct[B](f: A => B): F[(A, B)] = implicitly.fproduct(F)(f)
    def tupleLeft[B](b: B): F[(B, A)] = implicitly.tupleLeft(F, b)
    def tupleRight[B](b: B): F[(A, B)] = implicitly.tupleRigth(F, b)
  }
}

object FunctorInstances {
  // pa: Parser[A] is just a function String => Result[A]
  implicit val parserFunctor: Functor[Parser] = new Functor[Parser] {
    def map[A, B](fa: Parser[A])(f: A => B): Parser[B] =
      fa match {
        case _ => new Parser[B]() {
          def apply(source: Source): Result[B] = fa(source) match {
            case Success(h, t) => Success(f(h), t)
            case Failure(label,err, pos) => Failure(label, err, pos)
          }
        }
      }
  }
}
