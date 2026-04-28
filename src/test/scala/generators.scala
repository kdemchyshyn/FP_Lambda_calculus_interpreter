import lambdaCalculus.*
import org.scalacheck.*
import org.scalacheck.Gen.lzy

object generators:

  val genVariableName: Gen[String] = Gen.nonEmptyListOf(Gen.alphaLowerChar).map(_.mkString).suchThat(_.nonEmpty)

  val genVariable: Gen[Variable] =
    for name <- genVariableName
    yield Variable(name)

  val genAbstraction: Gen[Abstraction] =
    for
      boundVariable <- genVariable
      body          <- genLambdaTerm
    yield Abstraction(boundVariable, body)

  val genApplication: Gen[Application] =
    for
      function <- genLambdaTerm
      argument <- genLambdaTerm
    yield Application(argument, function)

  lazy val genLambdaTerm: Gen[LambdaTerm] =
    Gen.frequency(3 -> lzy(genVariable), 1 -> lzy(genAbstraction), 1 -> lzy(genApplication))

  given Arbitrary[Variable]    = Arbitrary(genVariable)
  given Arbitrary[Abstraction] = Arbitrary(genAbstraction)
  given Arbitrary[Application] = Arbitrary(genApplication)
  given Arbitrary[LambdaTerm]  = Arbitrary(genLambdaTerm)
