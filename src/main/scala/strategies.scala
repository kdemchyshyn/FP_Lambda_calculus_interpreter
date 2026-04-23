import lambdaCalculus.Term

import scala.annotation.{tailrec, targetName}
import scala.collection.immutable

object strategies:
  sealed trait Strategy:
    def reductionStep(term: Term): Option[Term]

  object NormalOrder extends Strategy:
    def reductionStep(term: Term): Option[Term] = ???

  object ApplicativeOrder extends Strategy:
    def reductionStep(term: Term): Option[Term] = ???
