# Lambda Calculus Interpreter

## Group Members
* [ Shon Kucherenko ]
* [ Anna Prokopchenko ]
* [ Anastasiia Chaika ]
* [ Kateryna Demchyshyn ]

## Project Description
### Fufilled all of the necessary requirements for 25 points:
- the interpreter is implemented in a functional style only
- the lambda-term syntax tree is implemented using immutable case classes or an equivalent immutable Scala 3 representation
- free variables are computed correctly
- substitution is implemented correctly according to the rules above
- alpha-conversion is handled correctly where needed
- normal order and application order reduction is implemented correctly + configurable way to choose between them
- the solution includes a mechanism to stop non-terminating evaluation
- the implementation is covered by automated tests
- parser for a string representation of lambda terms

### Project implementation overview & Project features:
**Core Data Model:**
The Lambda Calculus AST is modeled using a `sealed trait LambdaTerm`. Used Scala 3 `case classes` (`Variable`, `Abstraction`, and `Application`) to ensure immutability and enable exhaustive pattern matching across the interpreter. 

**Capture-Avoiding Substitution & Alpha-Conversion:**
Substitution is implemented as a recursive method directly on the `LambdaTerm` types. To prevent variable capture (Rule 7), the implementation automatically performs alpha-conversion. It generates fresh variables by iteratively appending a `'` symbol (`genNewVariable`) until it finds a variable name that does not exist in the sets of free variables.

**Evaluation Engine & Strategies:**
Reduction logic is defined as a separate `Strategy` trait with two objects extending it `Normal Order (Call-by-name)` and `Applicative Order (Call-by-value)`. 
The `Evaluator` object uses a `@tailrec` function to apply these strategies. It includes a `limit` parameter that decrements with every beta-reduction step. If a term is non-terminating the evaluator halts and returns a `Result.Timeout` containing the last computed term, preventing stack overflows or infinite loops.

**Testing:**
Used **ScalaCheck** for property based testing

## Parser Documentation
Parser built with Scala's `RegexParsers` combinators (`scala.util.parsing.combinator`). It takes a string representation of a lambda term and constructs the corresponding abstract syntax tree (AST).

### EBNF
The parser recognizes a context-free grammar for untyped lambda calculus. Below is the formal specification in Extended Backus-Naur Form (EBNF):

```ebnf
Expression       ::= Abstraction | ApplicationChain
ApplicationChain ::= SubExpression { SubExpression }
Abstraction      ::= "\" Variable "." Expression
SubExpression    ::= Variable | Abstraction | "(" Expression ")"
Variable         ::= lowercase_letter { lowercase_letter }

lowercase_letter ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" 
                   | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" 
                   | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"
```

- the parser expects an <expression> as an input

### Less formal explanation 
The parser expect in input a valid expression which consists of:

* **Variables:** Consist of one or more lowercase English letters (regex: `[a-z]+`).
  * *Examples:* `x`, `y`, `foo`, `argument`
* **Abstractions (Functions):** Denoted by a backslash `\`, followed by a bound variable, a dot `.`, and the body expression.
  * *Examples:* `\x.x`, `\var.body`
* **Applications:** Expressed by separating two or more sub-expressions with whitespace. Application is **left-associative**, meaning `x y z` is parsed as `(x y) z`.
  * *Examples:* `x y`, `(\x.x) y`
* **Parentheses:** Used to group expressions, enforce precedence, or disambiguate terms. The parser correctly parses expressions with fully explicit parentheses, as well as those omitting unnecessary parentheses based on standard left-associativity. (`x y z`, `(x y) z`, `((x y) z)` are all valid representations of the same lambda term)
  * *Other Examples:* `x (\y.y)`, `(\x.x \y.y) z`

### More Examples

| Lambda Term (Input) | AST Output | Description |
| :--- | :--- | :--- |
| `x` | `Variable("x")` | A single variable. |
| `\x.x` | `Abstraction(Variable("x"), Variable("x"))` | The Identity combinator. |
| `x y` | `Application(Variable("x"), Variable("y"))` | Simple application. |
| `x y z` | `Application(Application(Variable("x"), Variable("y")), Variable("z"))` | Demonstrates left-associativity. |
| `\x. x y` | `Abstraction(Variable("x"), Application(Variable("x"), Variable("y")))` | Demonstrates greedy scope of abstractions. |
| `(\x. x) y` | `Application(Abstraction(Variable("x"), Variable("x")), Variable("y"))` | Applying an abstraction to a variable using parentheses. |
| `\f.\x. f (f x)`| `Abstraction(Variable("f"), Abstraction(Variable("x"), Application(Variable("f"), Application(Variable("f"), Variable("x")))))`| Church numeral 2 |


### Usage and Error Handling
The parser is exposed via the `lambdaTermParser.parse` method, which safely handles invalid syntax without throwing runtime exceptions. It returns an `Either[String, LambdaTerm]`:

* **`Right(LambdaTerm)`**: Returned upon a successful parse, containing the fully constructed immutable syntax tree.
* **`Left(String)`**: Returned upon encountering invalid syntax, providing a descriptive error message indicating the exact column number where the failure occurred.


## How to Build and Run
Open sbt shell or bash in root directory of the project and:
```bash
sbt compile
```
```bash
sbt run
```
or
```bash
compile
```
```bash
run
```

## AI USAGE
- read in the ai_uasge.md