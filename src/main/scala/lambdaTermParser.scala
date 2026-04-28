import lambdaCalculus.*
import scala.util.parsing.combinator.RegexParsers

object lambdaTermParser:

  def parse(input: String): Either[String, LambdaTerm] =
    val parser = ExpressionParser()
    val res    = parser.parseAll(parser.expression, input)

    res match
      case parser.Success(matchedTerm, _) =>
        Right(matchedTerm)
      case parser.Failure(msg, next) =>
        Left(s"Syntax Error: $msg at column ${next.pos.column}")
      case parser.Error(msg, next) =>
        Left(s"Fatal Error: $msg at column ${next.pos.column}")

end lambdaTermParser

class ExpressionParser extends RegexParsers:
  def variable: Parser[Variable] = "[a-z]+".r ^^ { name => Variable(name) }

  def subExpression: Parser[LambdaTerm] = variable | abstraction | "(" ~> expression <~ ")"

  def applicationOrSubExpression: Parser[LambdaTerm] = rep1(subExpression) ^^ { subExpressions =>
    subExpressions.reduceLeft((function, argument) => Application(function, argument))
  }

  def abstraction: Parser[Abstraction] = "\\" ~> variable ~ ("." ~> expression) ^^ { case boundVariable ~ body =>
    Abstraction(boundVariable, body)
  }

  def expression: Parser[LambdaTerm] = abstraction | applicationOrSubExpression
