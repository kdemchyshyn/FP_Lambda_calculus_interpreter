import lambdaCalculus.{Abstraction, Application, LambdaTerm}
import scala.annotation.{tailrec, targetName}

object strategies:

  sealed trait Strategy:
    def reductionStep(term: LambdaTerm): Option[LambdaTerm]

    def tryBetaReduce(left: LambdaTerm, right: LambdaTerm): Option[LambdaTerm] =
      left match
        case a: Abstraction => Some(a.apply(right))
        case _              => None

    def reduceLeft(
      left: LambdaTerm,
      right: LambdaTerm,
      strategy: LambdaTerm => Option[LambdaTerm],
    ): Option[LambdaTerm] =
      val reduced = strategy(left)

      reduced match
        case value: Some[LambdaTerm] => Some(Application(value.value, right))
        case None                    => None

    def reduceRight(
      left: LambdaTerm,
      right: LambdaTerm,
      strategy: LambdaTerm => Option[LambdaTerm],
    ): Option[LambdaTerm] =
      val reduced = strategy(right)

      reduced match
        case value: Some[LambdaTerm] => Some(Application(left, value.value))
        case None                    => None

  object NormalOrder extends Strategy: // Call by name

    def reductionStep(term: LambdaTerm): Option[LambdaTerm] =
      term match
        case app: Application =>
          tryBetaReduce(app.left, app.right).orElse(reduceLeft(app.left, app.right, reductionStep))

        case a: Abstraction =>
          val reducedBody = reductionStep(a.body)

          reducedBody match
            case reduced: Some[LambdaTerm] => Some(Abstraction(a.boundVariable, reducedBody.get))
            case None                      => None

        case _ => None

  object ApplicativeOrder extends Strategy: // Call by value

    def reductionStep(term: LambdaTerm): Option[LambdaTerm] =
      term match
        case app: Application =>
          reduceLeft(app.left, app.right, reductionStep)
            .orElse(
              reduceRight(app.left, app.right, reductionStep)
                .orElse(tryBetaReduce(app.left, app.right))
            )

        case _ => None
