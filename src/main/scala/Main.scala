import lambdaCalculus.*
import evaluator.*
import strategies.*
import lambdaTermParser.*

object Main:
  val limit = 20
  def main(args: Array[String]): Unit =
    println("=== Lambda Calculus Interpreter ===")

    println("Choose reduction strategy:")
    println("1 - Normal Order")
    println("2 - Applicative Order")

    val choice = scala.io.StdIn.readLine("Enter choice (1 or 2): ")

    val strategy = choice match
      case "2" => ApplicativeOrder
      case _ => NormalOrder

    println(s"Selected: ${strategy.getClass.getSimpleName}")

    val input = scala.io.StdIn.readLine("Enter lambda term: ")

    lambdaTermParser.parse(input) match
      case Right(term) =>
        val result = Evaluator.evaluate(term, strategy, limit)

        result match
          case Result.Success(res) =>
            println(s"\nResult: $res")
          case Result.Timeout(last) =>
            println(s"\nStopped (step limit reached). Last term: $last")

      case Left(error) =>
        println(s"\nParsing failed: $error")
