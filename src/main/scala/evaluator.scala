import lambdaCalculus.LambdaTerm
import scala.annotation.{tailrec, targetName}
import scala.collection.immutable
import strategies.Strategy

object evaluator:

  enum Result:
    case Success(term: LambdaTerm)
    case Timeout(lastTerm: LambdaTerm)

  object Evaluator:

    @tailrec
    def evaluate(term: LambdaTerm, strategy: Strategy, limit: Int): Result =
      if limit <= 0 then Result.Timeout(term)
      else
        strategy.reductionStep(term) match
          case Some(next) => evaluate(next, strategy, limit - 1)
          case None       => Result.Success(term)
