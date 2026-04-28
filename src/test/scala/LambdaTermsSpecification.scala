import CollisionGenerator.*
import evaluator.*
import evaluator.Result
import generators.given
import lambdaCalculus.*
import org.scalacheck.*
import org.scalacheck.Prop.*
import org.scalacheck.Test.Parameters
import strategies.*

object LambdaTermModel extends Properties("LambdaTermModel"):

  property("Equal objects should have the same name.") = forAll: (v1: Variable, v2: Variable) =>
    (v1 == v2) == (v1.name == v2.name)

  property("Abstraction should correctly store the bound variable and the body term.") = forAll:
    (v: Variable, t: LambdaTerm) =>
      val abs = Abstraction(v, t)
      (abs.boundVariable == v) && (abs.body == t)

  property("Application should correctly link the left (function) and right (argument) terms.") = forAll:
    (l: LambdaTerm, r: LambdaTerm) =>
      val appl = Application(l, r)
      (appl.left == l) && (appl.right == r)

  property("Immutability") = forAll: (t: LambdaTerm, v: Variable, replacement: LambdaTerm) =>
    val before = t.toString
    t.substitute(v, replacement)
    val after = t.toString
    before == after

end LambdaTermModel

object FreeVariables extends Properties("Free Variables"):

  property("FV of a single variable: The set of free variables for a variable x should contain only x") = forAll:
    (v: Variable) =>
      val term = v.freeVariables
      (term.size == 1) && term.contains(v)

  property("FV of an identity abstraction: The set of free variables for λx.x should be empty") = forAll:
    (v: Variable) => Abstraction(v, v).freeVariables.isEmpty

  property("FV of an abstraction with a different body: The set of free variables for λx. y should contain only y") =
    forAll: (t: LambdaTerm, v: Variable) =>
      val termFV = Abstraction(v, t).freeVariables
      !termFV.contains(v) && termFV.subsetOf(t.freeVariables)

  property(
    "FV of an application: The set of free variables for (x y) should be the union of free variables of x and y"
  ) = forAll: (x: LambdaTerm, y: LambdaTerm) =>
    Application(x, y).freeVariables == x.freeVariables.union(y.freeVariables)

  property("FV with shadowing: The set of free variables for λx. (λx. x) should be empty due to shadowing") = forAll:
    (v: Variable) => Abstraction(v, Abstraction(v, v)).freeVariables.isEmpty

end FreeVariables

object Substitution extends Properties("Substitution"):

  property("Rule 1: Same variable substitution: [N/x]x should return N") = forAll: (x: Variable, n: LambdaTerm) =>
    x.substitute(x, n) == n

  property("Rule 2: Different variable substitution: [N/x]a should return a when a != x") = forAll:
    (n: LambdaTerm, x: Variable, a: Variable) => (a != x) ==> (a.substitute(x, n) == a)

  property("Rule 3: Application substitution: [N/x](P Q) should return ([N/x]P [N/x]Q)") = forAll:
    (n: LambdaTerm, x: Variable, appl: Application) =>
      appl.substitute(x, n) == Application(appl.left.substitute(x, n), appl.right.substitute(x, n))

  property("Rule 4: Shadowed abstraction substitution: [N/x](λx.P) should return λx.P (no changes)") = forAll:
    (n: LambdaTerm, x: Variable, p: LambdaTerm) => Abstraction(x, p).substitute(x, n) == Abstraction(x, p)

  property("Rule 5: Constant abstraction substitution: [N/x](λy.P) should return λy.P if x is not free in P") = forAll:
    (n: LambdaTerm, x: Variable, p: LambdaTerm, y: Variable) =>
      (!p.freeVariables.contains(x)) ==> (Abstraction(y, p).substitute(x, n) == Abstraction(y, p))

  property("Rule 6: Capture-free abstraction substitution: [N/x](λy.P) should return λy.[N/x]P if y is not free in N") =
    forAll: (n: LambdaTerm, x: Variable, p: LambdaTerm, y: Variable) =>
      (!n.freeVariables.contains(y)) ==> (Abstraction(y, p).substitute(x, n) == Abstraction(y, p.substitute(x, n)))

  property("Rule 7: Alpha-conversion to avoid capture: [N/x](λy. P) should rename y to a fresh z if y is free in N") =
    forAll(genRule7) { (x, n, y, p) =>
      ((y != x) && p.freeVariables.contains(x) && n.freeVariables.contains(y)) ==> {
        val res = Abstraction(y, p).substitute(x, n)
        res match
          case Abstraction(z, newBody) =>
            (z != y) && (z != x) && !n.freeVariables.contains(z) && !p.freeVariables.contains(z) && (newBody != p)
          case _ => false
      }
    }

  property("Substitution preserves free variable invariant") = forAll: (p: LambdaTerm, x: Variable, n: LambdaTerm) =>
    val resultFV = p.substitute(x, n).freeVariables
    val expectedFV =
      if p.freeVariables.contains(x) then (p.freeVariables - x) ++ n.freeVariables
      else p.freeVariables
    resultFV == expectedFV

  property("Substitution does nothing if variable is not free in term") = forAll:
    (p: LambdaTerm, x: Variable, n: LambdaTerm) =>
      (!p.freeVariables.contains(x)) ==> {
        p.substitute(x, n) == p
      }

  property("Substituting a variable with itself does not change the term") = forAll: (p: LambdaTerm, x: Variable) =>
    p.substitute(x, x) == p

end Substitution

object BetaReductionAndEvaluator extends Properties("Beta-Reduction & Evaluator"):

  override def overrideParameters(p: Parameters): Parameters =
    p.withMinSuccessfulTests(50).withMaxDiscardRatio(100).withMinSuccessfulTests(1000)

  property("Single-step beta-reduction: (λx. x) a should reduce to a in exactly one step") = forAll:
    (x: Variable, a: LambdaTerm) =>

      // (λx. x x) (λx. x x)
      val omega = Application(Abstraction(x, Application(x, x)), Abstraction(x, Application(x, x)))
      // (λx. x)
      val id = Abstraction(x, x)
      NormalOrder.reductionStep(Application(id, a)).contains(a)

  property("Identity application: Evaluator should reduce (λx. x) z to z") = forAll:
    (x: Variable, a: LambdaTerm, z: Variable) =>
      val omega = Application(Abstraction(x, Application(x, x)), Abstraction(x, Application(x, x)))
      val id    = Abstraction(x, x)

      Evaluator.evaluate(Application(id, z), NormalOrder, 10) match
        case Result.Success(res) => res == z
        case _                   => false

  property("Nested reduction: Evaluator should handle multiple reduction steps until normal form is reached") = forAll:
    (x: Variable, y: Variable, a: Variable, b: Variable) =>
      // (λx. λy. x y) a b  -> (λy. a y) b -> a b
      (x != y && x != a && x != b && y != a && y != b) ==> {
        val term = Application(Application(Abstraction(x, Abstraction(y, Application(x, y))), a), b)
        Evaluator.evaluate(term, NormalOrder, 10) match
          case Result.Success(res) => res == Application(a, b)
          case _                   => false
      }

  property("Normal Order: Avoiding infinite loops - reduce (λx. y) Ω to y even if Ω is non-terminating") = forAll:
    (x: Variable, y: Variable) =>
      (x != y) ==> {
        val omega = Application(Abstraction(x, Application(x, x)), Abstraction(x, Application(x, x)))
        val term  = Application(Abstraction(x, y), omega)
        Evaluator.evaluate(term, NormalOrder, 10) match
          case Result.Success(res) => res == y
          case _                   => false
      }

  property("Normal form is irreducible (NormalOrder)") = forAll: (term: LambdaTerm) =>
    Evaluator.evaluate(term, NormalOrder, 20) match
      case Result.Success(res) =>
        NormalOrder.reductionStep(res).isEmpty
      case _ => true

  property("Applicative Order: Must reduce arguments before applying functions") = forAll: (v: Variable) =>
    val id   = Abstraction(v, v)
    val term = Application(id, Application(id, v))
    // Applicative order: id (id v) -> id v
    val step = ApplicativeOrder.reductionStep(term)
    step.contains(Application(id, v))

  property("Applicative Order: Non-termination - (λx. y) Ω should result in Timeout") = forAll:
    (x: Variable, y: Variable) =>
      (x != y) ==> {
        val omega = Application(Abstraction(x, Application(x, x)), Abstraction(x, Application(x, x)))
        val term  = Application(Abstraction(x, y), omega)
        Evaluator.evaluate(term, ApplicativeOrder, 5) match
          case Result.Timeout(_) => true
          case _                 => false
      }

  property("Applicative Order: Reduction to Normal Form") = forAll: (v: Variable) =>
    val id   = Abstraction(v, v)
    val term = Application(id, v)
    Evaluator.evaluate(term, ApplicativeOrder, 5) match
      case Result.Success(res) => res == v
      case _                   => false

  property("Beta-Reduction: Multiple applications - ((λx.λy. x) a) b should reduce to 'a'") = forAll:
    (x: Variable, y: Variable, a: Variable, b: Variable) =>
      (x != y && x != a && x != b && y != a && y != b) ==> {
        val const = Abstraction(x, Abstraction(y, x))
        val term  = Application(Application(const, a), b)
        Evaluator.evaluate(term, NormalOrder, 10) match
          case Result.Success(res) => res == a
          case _                   => false
      }

  property("Reduction step returns None for variables") = forAll: (v: Variable) =>
    NormalOrder.reductionStep(v).isEmpty

  property("NormalOrder and ApplicativeOrder behave differently on Omega") = forAll: (x: Variable, y: Variable) =>
    (x != y) ==> {
      val omega = Application(Abstraction(x, Application(x, x)), Abstraction(x, Application(x, x)))

      val term = Application(Abstraction(x, y), omega)

      val normal      = Evaluator.evaluate(term, NormalOrder, 10)
      val applicative = Evaluator.evaluate(term, ApplicativeOrder, 10)

      (normal, applicative) match
        case (Result.Success(_), Result.Timeout(_)) => true
        case _                                      => false
    }

end BetaReductionAndEvaluator

object NonTerminationAndStepLimit extends Properties("Non-termination & Step Limit"):

  override def overrideParameters(p: Parameters): Parameters =
    p.withMinSuccessfulTests(50).withMaxDiscardRatio(100).withMinSuccessfulTests(1000)

  property("Step limit reached for Omega: Evaluator should stop reducing Ω after max steps") = forAll: (x: Variable) =>
    val omega = Application(Abstraction(x, Application(x, x)), Abstraction(x, Application(x, x)))
    val limit = 5
    Evaluator.evaluate(omega, NormalOrder, limit) match
      case Result.Timeout(_) => true
      case _                 => false

  property("Step limit reporting: Result should clearly indicate evaluation stopped due to limit") = forAll:
    (x: Variable) =>
      val omega = Application(Abstraction(x, Application(x, x)), Abstraction(x, Application(x, x)))
      Evaluator.evaluate(omega, NormalOrder, 2).isInstanceOf[Result.Timeout]

  property("Zero step limit: Evaluator should return the original term if limit is zero") = forAll: (x: Variable) =>
    val omega = Application(Abstraction(x, Application(x, x)), Abstraction(x, Application(x, x)))
    Evaluator.evaluate(omega, NormalOrder, 0) match
      case Result.Timeout(t) => t == omega
      case _                 => false
end NonTerminationAndStepLimit
