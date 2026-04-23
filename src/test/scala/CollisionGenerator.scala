//package generators

import lambdaCalculus.*
import org.scalacheck.*
import org.scalacheck.Gen.lzy

object CollisionGenerator:

  val genCollisionVariableName : Gen[String] = Gen.oneOf("x", "y", "z").suchThat(_.nonEmpty)

  val genCollisionVariable: Gen[Variable] =
    for name <- genCollisionVariableName
      yield Variable(name)

  val genAbstraction: Gen[Abstraction] =
    for
      boundVariable <- genCollisionVariable
      body <- genLambdaTerm
    yield Abstraction(boundVariable, body)

  val genApplication: Gen[Application] =
    for
      function <- genCollisionLambdaTerm
      argument <- genCollisionLambdaTerm
    yield Application(argument, function)

  lazy val genLambdaTerm: Gen[Term] =
    Gen.frequency(
      3 -> lzy(genCollisionVariable),
      1 -> lzy(genAbstraction),
      1 -> lzy(genApplication),
    )

  lazy val genCollisionLambdaTerm: Gen[Term] =
    Gen.frequency(
      3 -> lzy(genCollisionVariable),
      1 -> lzy(genAbstraction),
      1 -> lzy(genApplication),
    )
    
  val genRule7: Gen[(Variable, Term, Variable, Term)] =
    for
      x <- genCollisionVariable
      y <- genCollisionVariable.suchThat(_ != x)
      n_base <- genCollisionLambdaTerm
      n = Application(n_base, y)
      p_base <- genCollisionLambdaTerm
      p = Application(p_base, x)
    yield (x, n, y, p)
