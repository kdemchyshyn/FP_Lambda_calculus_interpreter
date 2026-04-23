import lambdaCalculus.*
import org.scalacheck.*
import org.scalacheck.Prop.*
import generators.given
import CollisionGenerator.*

object LambdaTermModel extends Properties("LambdaTermModel"):

  property("Equal objects should have the same name.") = forAll: (v1: Variable, v2: Variable) =>
    (v1 == v2) == (v1.name == v2.name)

  property("Abstraction should correctly store the bound variable and the body term.") = forAll: (v: Variable, t: LambdaTerm) =>
    val abs = Abstraction(v, t)
    (abs.boundVariable == v) && (abs.body == t)

  property("Application should correctly link the left (function) and right (argument) terms.") = forAll: (l: LambdaTerm, r: LambdaTerm) =>
    val appl = Application(l, r)
    (appl.left == l) && (appl.right == r)

  property("Immutability") = forAll: (t: LambdaTerm, v: Variable, replacement: LambdaTerm) =>
    val before = t.toString
    t.substitute(v, replacement)
    val after = t.toString
    before == after

end LambdaTermModel

object FreeVariables extends Properties("Free Variables"):

  property("FV of a single variable: The set of free variables for a variable x should contain only x") = forAll: (v: Variable) =>
    val term = v.freeVariables
    (term.size == 1) && term.contains(v)

  property("FV of an identity abstraction: The set of free variables for λx.x should be empty") = forAll: (v: Variable) =>
    Abstraction(v, v).freeVariables.isEmpty

  property("FV of an abstraction with a different body: The set of free variables for λx. y should contain only y") = forAll: (t: LambdaTerm, v: Variable) =>
    val termFV = Abstraction(v, t).freeVariables
    !termFV.contains(v) && termFV.subsetOf(t.freeVariables)

  property("FV of an application: The set of free variables for (x y) should be the union of free variables of x and y") = forAll: (x: LambdaTerm, y:Term) =>
    Application(x, y).freeVariables == x.freeVariables.union(y.freeVariables)

  property("FV with shadowing: The set of free variables for λx. (λx. x) should be empty due to shadowing") = forAll: (v: Variable) =>
    Abstraction(v, Abstraction(v, v)).freeVariables.isEmpty

end FreeVariables

object Substitution extends Properties("Substitution"):

  property("Rule 1: Same variable substitution: [N/x]x should return N") = forAll: (x: Variable, n: LambdaTerm) =>
    x.substitute(x, n) == n

  property("Rule 2: Different variable substitution: [N/x]a should return a when a != x") = forAll: (n: LambdaTerm, x: Variable, a: Variable) =>
    (a != x) ==> (a.substitute(x, n) == a)

  property("Rule 3: Application substitution: [N/x](P Q) should return ([N/x]P [N/x]Q)") = forAll: (n: LambdaTerm, x: Variable, appl: Application) =>
    appl.substitute(x, n) == Application(appl.left.substitute(x, n), appl.right.substitute(x, n))

  property("Rule 4: Shadowed abstraction substitution: [N/x](λx.P) should return λx.P (no changes)") = forAll: (n: LambdaTerm, x: Variable, p: LambdaTerm) =>
    Abstraction(x, p).substitute(x, n) == Abstraction(x, p)

  property("Rule 5: Constant abstraction substitution: [N/x](λy.P) should return λy.P if x is not free in P") = forAll: (n: LambdaTerm, x: Variable, p: LambdaTerm, y: Variable) =>
    (!p.freeVariables.contains(x)) ==> (Abstraction(y, p).substitute(x, n) == Abstraction(y, p))

  property("Rule 6: Capture-free abstraction substitution: [N/x](λy.P) should return λy.[N/x]P if y is not free in N") = forAll: (n: LambdaTerm, x: Variable, p: LambdaTerm, y: Variable) =>
    (!n.freeVariables.contains(y)) ==> (Abstraction(y, p).substitute(x, n) == Abstraction(y, p.substitute(x, n)))

  property("Rule 7: Alpha-conversion to avoid capture: [N/x](λy. P) should rename y to a fresh z if y is free in N") = forAll(genRule7) {(x, n, y, p) => ((y != x) && p.freeVariables.contains(x) && n.freeVariables.contains(y)) ==> {
    val res = Abstraction(y, p).substitute(x, n)
    res match
      case Abstraction(z, newBody) =>
        (z != y) && (z != x) && !n.freeVariables.contains(z) && !p.freeVariables.contains(z)
      case _ => false
  }}

end Substitution

//object BetaReductionAndEvaluator extends Properties("Beta-Reduction & Evaluator"):
//  property("Single-step beta-reduction: (λx. x) a should reduce to a in exactly one step") = ???
//
//  property("Identity application: Evaluator should reduce (λx. x) z to z") = ???
//
//  property("Nested reduction: Evaluator should handle multiple reduction steps until normal form is reached") = ???
//
//  property("Normal Order: Leftmost-outermost priority should be picked first") = ???
//
//  property("Normal Order: Avoiding infinite loops - reduce (λx. y) Ω to y even if Ω is non-terminating") = ???
//end BetaReductionAndEvaluator
//
//object NonTerminationAndStepLimit extends Properties("Non-termination & Step Limit"):
//  property("Step limit reached for Omega: Evaluator should stop reducing Ω after max steps") = ???
//
//  property("Step limit reporting: Result should clearly indicate evaluation stopped due to limit") = ???
//
//  property("Zero step limit: Evaluator should return the original term if limit is zero") = ???
//end NonTerminationAndStepLimit
