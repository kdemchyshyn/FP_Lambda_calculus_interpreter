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

  property("Parser should return failure for random string") = forAll: (str: String) =>
    LambdaTermParser.parse(str).isLeft

end LambdaTermParserSpecification

