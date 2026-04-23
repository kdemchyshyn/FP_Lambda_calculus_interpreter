import scala.annotation.{tailrec, targetName}
import scala.collection.immutable

object lambdaCalculus:

  sealed trait LambdaTerm:
    def freeVariables: Set[Variable]
    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm

  case class Variable(name: String) extends LambdaTerm:
    def freeVariables: Set[Variable] = Set(this)

    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm =
      if this == variable then replacement
      else this

  case class Abstraction(boundVariable: Variable, body: LambdaTerm) extends LambdaTerm:
    def freeVariables: Set[Variable] = ???
    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm = ???

  case class Application(left: LambdaTerm, right: LambdaTerm) extends LambdaTerm:
    def freeVariables: Set[Variable] = ???
    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm = ???
