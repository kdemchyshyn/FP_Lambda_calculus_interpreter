import lambdaCalculus.LambdaTerm

import scala.annotation.{tailrec, targetName}
import scala.collection.immutable

object strategies:
  sealed trait Strategy:
    def reductionStep(term: LambdaTerm): Option[LambdaTerm]

  object NormalOrder extends Strategy:
    def reductionStep(term: LambdaTerm): Option[LambdaTerm] = ???

  object ApplicativeOrder extends Strategy:
    def reductionStep(term: LambdaTerm): Option[LambdaTerm] = ???
