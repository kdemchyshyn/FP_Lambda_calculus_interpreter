import scala.annotation.{tailrec, targetName}
import scala.collection.immutable

object lambda_terms:

  sealed trait Term:
    def freeVariables: Set[Variable]
    def substitute(variable: Variable, replacement: Term): Term

  case class Variable(name: String) extends Term:
    def freeVariables: Set[Variable] = Set(this)

    def substitute(variable: Variable, replacement: Term): Term =
      if this == variable then replacement
      else this

  case class Abstraction(boundVariable: Variable, body: Term) extends Term:
    def freeVariables: Set[Variable] = ???
    def substitute(variable: Variable, replacement: Term): Term = ???

  case class Application(left: Term, right: Term) extends Term:
    def freeVariables: Set[Variable] = ???
    def substitute(variable: Variable, replacement: Term): Term = ???
