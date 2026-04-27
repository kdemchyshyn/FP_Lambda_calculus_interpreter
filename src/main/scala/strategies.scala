import lambdaCalculus.{Abstraction, Application, LambdaTerm}
import scala.annotation.{tailrec, targetName}

object strategies:

  sealed trait Strategy:
    def reductionStep(term: LambdaTerm): Option[LambdaTerm]

  object NormalOrder extends Strategy:

    def reductionStep(term: LambdaTerm): Option[LambdaTerm] =
      term match
        case app: Application =>
          app.left match
            case a: Abstraction => Option(a.body.substitute(a.boundVariable, app.right))
            case _ =>
              reductionStep(app.left) match
                case Some(reducedLeft) =>
                  Option(Application(reducedLeft, app.right))
                case None =>
                  reductionStep(app.right) match
                    case Some(reducedRight) => Some(Application(app.left, reducedRight))
                    case None               => None

        case _ => None

  object ApplicativeOrder extends Strategy:

    def reductionStep(term: LambdaTerm): Option[LambdaTerm] =
      term match
        case app: Application =>
          reductionStep(app.left) match
            case Some(reducedLeft) => Some(Application(reducedLeft, app.right))
            case None =>
              reductionStep(app.right) match
                case Some(reducedRight) => Option(Application(app.left, reducedRight))
                case None =>
                  app.left match
                    case a: Abstraction => Some(a.body.substitute(a.boundVariable, app.right))
                    case _              => None

        case _ => None
