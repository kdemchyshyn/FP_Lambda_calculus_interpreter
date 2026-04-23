import lambdaCalculus.LambdaTerm
import strategies.Strategy

import scala.annotation.{tailrec, targetName}
import scala.collection.immutable

object evaluator:

  enum Result:
    case Success(term: LambdaTerm)
    case Timeout(lastTerm: LambdaTerm)
  
  object Evaluator:
    //@tailrec uncomment when recursion is ready
    def evaluate(term: LambdaTerm, strategy: Strategy, limit: Int): Result = ???
