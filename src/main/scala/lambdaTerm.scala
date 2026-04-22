object lambdaTerm:
  trait LambdaTerm:
    def toString: String

  end LambdaTerm
  case class Variable(name: String) extends LambdaTerm:
    override def toString: String = name
  end Variable

  case class Abstraction(boundVariable: Variable, body: LambdaTerm) extends LambdaTerm:
    override def toString: String = s"\\$boundVariable.${body.toString}"
  end Abstraction

  case class Application(function: LambdaTerm, argument: LambdaTerm) extends LambdaTerm:
    override def toString: String = s"(${function.toString} ${argument.toString})"
  end Application

end lambdaTerm


