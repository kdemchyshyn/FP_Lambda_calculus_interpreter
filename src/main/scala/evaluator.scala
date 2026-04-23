import lambdaCalculus.Term
import strategies.Strategy

import scala.annotation.{tailrec, targetName}
import scala.collection.immutable

object evaluator:

  enum Result:
    case Success(term: Term)
    case Timeout(lastTerm: Term)
  
  object Evaluator:
    //@tailrec uncomment when recursion is ready
    def evaluate(term: Term, strategy: Strategy, limit: Int): Result = ???
