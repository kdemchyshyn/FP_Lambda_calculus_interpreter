//package generators

import lambdaCalculus.*
import org.scalacheck.*
import org.scalacheck.Gen.lzy

object CollisionGenerator:

  val genCollisionVariableName: Gen[String] = Gen.oneOf("x", "y", "z").suchThat(_.nonEmpty)

  val genCollisionVariable: Gen[Variable] =
    for name <- genCollisionVariableName
    yield Variable(name)

  val genAbstraction: Gen[Abstraction] =
    for
      boundVariable <- genCollisionVariable
      body          <- genLambdaTerm
    yield Abstraction(boundVariable, body)

  val genApplication: Gen[Application] =
    for
      function <- genCollisionLambdaTerm
      argument <- genCollisionLambdaTerm
    yield Application(argument, function)

  lazy val genLambdaTerm: Gen[LambdaTerm] =
    Gen.frequency(3 -> lzy(genCollisionVariable), 1 -> lzy(genAbstraction), 1 -> lzy(genApplication))

  lazy val genCollisionLambdaTerm: Gen[LambdaTerm] =
    Gen.frequency(3 -> lzy(genCollisionVariable), 1 -> lzy(genAbstraction), 1 -> lzy(genApplication))

  val genRule7: Gen[(Variable, LambdaTerm, Variable, LambdaTerm)] =
    for
      x      <- genCollisionVariable
      y      <- genCollisionVariable.suchThat(_ != x)
      nBase <- genCollisionLambdaTerm
      n       = Application(nBase, y)
      pBase <- genCollisionLambdaTerm
      p       = Application(pBase, x)
    yield (x, n, y, p)
