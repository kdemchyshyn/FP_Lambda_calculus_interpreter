import lambdaTerm.*
import org.scalacheck.*
import org.scalacheck.Prop.*
import generators.given
object LambdaTermParserSpecification extends Properties("LambdaTermParser"):

  property("LambdaTerm -> String -> LambdaTerm should be identical") = forAll: (term: LambdaTerm) =>
    Right(term) == LambdaTermParser.parse(term.toString)

  property("Variable -> String -> Variable should be identical") = forAll: (term: Variable) =>
    Right(term) == LambdaTermParser.parse(term.toString)

  property("Application -> String -> Application should be identical") = forAll: (term: Application) =>
    Right(term) == LambdaTermParser.parse(term.toString)

  property("Abstraction -> String -> Abstraction should be identical") = forAll: (term: Abstraction) =>
    Right(term) == LambdaTermParser.parse(term.toString)

  property("LambdaTerm -> String without non-necessary parenthesis -> LambdaTerm should be identical") = forAll: (term: LambdaTerm) =>
    Right(term) == LambdaTermParser.parse(term.toSource)

  property("Variable -> String without non-necessary parenthesis -> Variable should be identical") = forAll: (term: Variable) =>
    Right(term) == LambdaTermParser.parse(term.toSource)

  property("Application -> String without non-necessary parenthesis -> Application should be identical") = forAll: (term: Application) =>
    Right(term) == LambdaTermParser.parse(term.toSource)

  property("Abstraction -> String without non-necessary parenthesis -> Abstraction should be identical") = forAll: (term: Abstraction) =>
    Right(term) == LambdaTermParser.parse(term.toSource)

  property("Parser should return failure for random string") = forAll: (str: String) =>
    LambdaTermParser.parse(str).isLeft

end LambdaTermParserSpecification

