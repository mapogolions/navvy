package navvy

import scala.language.implicitConversions
import navvy.ops._

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
    ('a'.once | "abs" echo)
    'a'.once >> 'b'.once |>> { (x, y) => x + y } | "abba" echo
  }
}
