package navvy

import scala.language.implicitConversions
import navvy.ops._
import navvy.functor.Functor
import navvy.functor.FunctorInstances._
import navvy.functor.FunctorSyntax._

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
    //(digits.asInt | "1111loha" echo)
    // ('a'.once >> 'b'.once >> 'c'.once).flatten | "abcad" echo
    // tel | "3434-3434" echo
   //  email | "ivanovgmail.com" echo

    // ('-'.opt.once | "344-324234-34" echo)
    // ('e'.opt >> 'e'.opt).many | "hello" echo
    // ('e'.once.repeat(3) >> 'b'.once.repeat(2)).many | "eeebbeeebbeeebb" echo

    // трюк с пропуском в середине
    //('e'.once.repeat(2) <| whatever.repeat(2) >> 'l'.once  | "eeeelo" echo)
    // пропустить первые три символа
    // (whatever.repeat(3) |> 'a'.once | "---alloha" echo)

    // порядковые селоекторы
    // ('e'.once.after('b'.once) | "beahelo" echo)
    // ('e'.once.before("hello".once) | "estopper" echo)
    // ('e'.once.repeat(2).after('b'.atLeastOne) | "bbbeexponential" echo)

    // Zero or one times
    // ('e'.once.zeroOrOne | "" echo)

    // пропускаем первые два символа
    // (whatever >> whatever) |> 'b'.once | "aabus" echo
    // 'e'.once.times(3)
    float | "-324.4545d" echo
  }
}
