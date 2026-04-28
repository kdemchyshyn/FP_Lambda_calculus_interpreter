import scala.annotation.{tailrec, targetName}
import scala.collection.immutable

object lambdaCalculus:

  sealed trait LambdaTerm:
    def freeVariables: Set[Variable]
    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm
    def toString: String
    def toSource: String

  case class Variable(name: String) extends LambdaTerm:
    def freeVariables: Set[Variable] = Set(this)

    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm =
      if this == variable then replacement
      else this
    override def toString: String = name
    def toSource: String          = name

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
    override def toString: String = s"(\\$boundVariable.${body.toString})"
    def toSource: String          = s"\\$boundVariable.${body.toSource}"

    def apply(argument: LambdaTerm): LambdaTerm =
      val freshVariable =
        genNewVariable(boundVariable, body.freeVariables ++ argument.freeVariables ++ Set(boundVariable))

      body.substitute(boundVariable, freshVariable).substitute(freshVariable, argument)

  case class Application(left: LambdaTerm, right: LambdaTerm) extends LambdaTerm:
    def freeVariables: Set[Variable] = left.freeVariables ++ right.freeVariables

    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm =
      Application(left.substitute(variable, replacement), right.substitute(variable, replacement))

    override def toString: String = s"(${left.toString} ${right.toString})"

    def toSource: String =
      val functionStr = left match
        case _: Abstraction => s"(${left.toSource})"
        case _              => left.toSource

      val argumentStr = right match
        case _: Variable => right.toSource
        case _           => s"(${right.toSource})"

      s"$functionStr $argumentStr"

    def tryApplying(strategy: LambdaTerm => Option[LambdaTerm]): Option[LambdaTerm] =
      strategy(this)
