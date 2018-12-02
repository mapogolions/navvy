package navvy.testkit

import navvy.adt._


object ops {
  implicit class ResultOps[A](res: Result[A]) {
    def test(
      f: (elem: A, src: Source) => Unit, 
      g: (label: String, err: String, pos: Position) => Unit
    ) = res match {
        case Success(elem, src) => f(elem, src)
        case Failure(label, err, pos) => g(label, err, pos)
      }
    }
}