object lambdaTerm:
  trait LambdaTerm:
    def toString: String
    def toSource: String

  end LambdaTerm
  case class Variable(name: String) extends LambdaTerm:
    override def toString: String = name
    def toSource: String = name
  end Variable

  case class Abstraction(boundVariable: Variable, body: LambdaTerm) extends LambdaTerm:
    override def toString: String = s"(\\$boundVariable.${body.toString})"
    def toSource: String = s"\\$boundVariable.${body.toSource}"
  end Abstraction

  case class Application(function: LambdaTerm, argument: LambdaTerm) extends LambdaTerm:
    override def toString: String = s"(${function.toString} ${argument.toString})"
    def toSource: String =
      val functionStr = function match
        case _ : Abstraction => s"(${function.toSource})"
        case _ => function.toSource

      val argumentStr = argument match
        case _ : Variable => argument.toSource
        case _ => s"(${argument.toSource})"

      s"$functionStr $argumentStr"

  end Application

end lambdaTerm


