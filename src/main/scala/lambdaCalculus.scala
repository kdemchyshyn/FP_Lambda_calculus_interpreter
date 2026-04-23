import scala.annotation.{tailrec, targetName}
import scala.collection.immutable

object lambdaCalculus:

  sealed trait LambdaTerm:
    def freeVariables: Set[Variable]
    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm
    def toString: String
    def toSource: String

  end LambdaTerm

  case class Variable(name: String) extends LambdaTerm:
    def freeVariables: Set[Variable] = Set(this)

    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm =
      if this == variable then replacement
      else this

    override def toString: String = name
    def toSource: String = name

  end Variable

  case class Abstraction(boundVariable: Variable, body: LambdaTerm) extends LambdaTerm:
    def freeVariables: Set[Variable] = ???
    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm = ???
    override def toString: String = s"(\\$boundVariable.${body.toString})"
    def toSource: String = s"\\$boundVariable.${body.toSource}"

  end Abstraction


  case class Application(left: LambdaTerm, right: LambdaTerm) extends LambdaTerm:
    def freeVariables: Set[Variable] = ???
    def substitute(variable: Variable, replacement: LambdaTerm): LambdaTerm = ???
    override def toString: String = s"(${left.toString} ${right.toString})"

    def toSource: String =
      val functionStr = left match
        case _: Abstraction => s"(${left.toSource})"
        case _ => left.toSource

      val argumentStr = right match
        case _: Variable => right.toSource
        case _ => s"(${right.toSource})"

      s"$functionStr $argumentStr"

  end Application

