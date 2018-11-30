## Simple library for parsing

### Basic built-in primitives

* `digit` - any digit. Result returns as single char
* `digits` - at least one digit. Result returns as list of chars

## sbt project cross-compiled with Dotty and Scala 2

### Usage

This is a normal sbt project, you can compile code with `sbt compile` and run it
with `sbt run`, `sbt console` will start a Dotty REPL. For more information on
cross-compilation in sbt, see <https://www.scala-sbt.org/1.x/docs/Cross-Build.html>.

For more information on the sbt-dotty plugin, see the
[dotty-example-project](https://github.com/lampepfl/dotty-example-project/blob/master/README.md).
