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

    def concatenate(symbol: String): Variable = Variable(name + symbol)

  @tailrec
  def genNewVariable(variable: Variable, forbidden: Set[Variable]): Variable =
    if forbidden.contains(variable) then genNewVariable(variable.concatenate("'"), forbidden)
    else variable

  case class Abstraction(boundVariable: Variable, body: LambdaTerm) extends LambdaTerm:
    def freeVariables: Set[Variable] = body.freeVariables - boundVariable

    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm =
      if !freeVariables.contains(variable) then this
      else if !replacement.freeVariables.contains(boundVariable) then
        Abstraction(boundVariable, body.substitute(variable, replacement))
      else
        val newVariable = genNewVariable(boundVariable, (body.freeVariables ++ replacement.freeVariables))
        Abstraction(newVariable, body.substitute(boundVariable, newVariable).substitute(variable, replacement))

  case class Application(left: LambdaTerm, right: LambdaTerm) extends LambdaTerm:
    def freeVariables: Set[Variable] = left.freeVariables ++ right.freeVariables

    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm =
      Application(left.substitute(variable, replacement), right.substitute(variable, replacement))
