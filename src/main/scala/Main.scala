package navvy

import scala.language.implicitConversions
import navvy.ops._


object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
    /* ('a'.once | "abs" echo)
    val res = startsWith("http://google.com".once)("http:/".once)
    res | "" echo */
    // 'a'.once >> 'b'.once |>> { (x, y) => x + y } | "abba" echo
    // ('a'.once <|> whatever).many | "alloha" echo
  }
}
