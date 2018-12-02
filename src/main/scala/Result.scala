package navvy.adt

import navvy.adt._


sealed trait Result[+A] { self =>
  def echo = println(self toString)
}

case class Success[A](
  val elem: A, 
  val source: Source
) extends Result[A] {
  // override def toString = s"Success(${elem},${source})"
  override def toString = s"$elem"
}

case class Failure(
  val label: String, 
  val err: String,
  val pos: Position
) extends Result[Nothing] {
  override def toString = {
    val where = s"Row: ${pos.row} Column: ${pos.col} "
    val what = s"Error parsing ${label}\n"
    val cause = s"${pos.line}\n${" " * (pos.col + 1)}^ ${err}"
    s"$where $what $cause"
  }
}
