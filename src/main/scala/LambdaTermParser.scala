import lambdaTerm.*

import scala.util.parsing.combinator.RegexParsers
object LambdaTermParser:
  def parse(input: String): Either[String, LambdaTerm] = {
    val parser = ExpressionParser()
    val res = parser.parseAll(parser.expression, input)

    res match
      case parser.Success(matchedTerm, _) =>
        Right(matchedTerm)
      case parser.Failure(msg, next) =>
        Left(s"Syntax Error: $msg at column ${next.pos.column}")
      case parser.Error(msg, next) =>
        Left(s"Fatal Error: $msg at column ${next.pos.column}")
  }
end LambdaTermParser

class ExpressionParser extends RegexParsers:
  def variable : Parser[Variable] = "[a-z]+".r ^^ { name => Variable(name)}
  def application : Parser[Application] = "(" ~> expression ~ expression <~ ")" ^^ { case  function ~ argument => Application(function, argument) }
  def abstraction : Parser[Abstraction] = "\\" ~> variable ~ ("." ~> expression) ^^ {case boundVariable ~ body => Abstraction(boundVariable, body)}
  def expression : Parser[LambdaTerm] = application | abstraction | variable



