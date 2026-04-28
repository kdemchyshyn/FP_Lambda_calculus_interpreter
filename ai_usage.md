## AI USAGE for planning the api:
- https://gemini.google.com/share/5b3c1d4b822c

## AI USAGE for making the README.md
(don't have a chat link, this conversation is not saved in chat)
- Student:

```
i need to have a 
a short README file with:
names of all group members,
a short description of the solution,
instructions for how to build and run the project,
instructions for how to run the tests.
and it also should have small parser documnetation

can you suggest me a md template
give it to me in copy-pastable txt format so i can easily copy it to myself

the task is described below:
Project Requirements
1. Functional programming requirement
The solution must be implemented in a functional style only.
This means:
no mutable state in the interpreter logic;
no use of var;
no in-place updates of data structures;
lambda terms must be represented as immutable data.
Using Scala 3 features such as case classes, sealed traits / enums, pattern matching, and recursive functions is expected.
2. Lambda term model
You must define an immutable syntax tree for lambda terms.
At minimum, your model must support:
variables
abstractions
applications
A typical model would contain three node types corresponding to these three forms.
3. Free variables
Your solution must include a function that computes the set of free variables of a lambda term.
This is required because the substitution algorithm depends on free-variable analysis.
4. Capture-avoiding substitution
You must implement substitution according to the following rules.
Let [N/x]P mean “substitute term N for variable x in term P”.
[N/x]x ≡ N
[N/x]a ≡ a, for every variable a ≠ x
[N/x](P Q) ≡ ([N/x]P [N/x]Q)
[N/x](λx.P) ≡ λx.P
[N/x](λy.P) ≡ λy.P, if y ≠ x and x ∉ FV(P)
[N/x](λy.P) ≡ λy.[N/x]P, if y ≠ x, x ∈ FV(P), and y ∉ FV(N)
[N/x](λy.P) ≡ λz.[N/x]([z/y]P), if y ≠ x, x ∈ FV(P), y ∈ FV(N), and z ∉ FV(N) ∪ FV(P)
Rule 7 requires alpha-conversion.
Here, z must be a fresh variable that does not occur free in either N or P.
Your implementation must be capture-avoiding. Substitutions that accidentally change the meaning of a term because of variable capture will be considered incorrect.
5. Beta-reduction
You must implement beta-reduction using the standard rule:
(λx.P) N → [N/x]P
Your interpreter must be able to perform:
one reduction step, and
repeated reduction until a result is reached.
6. Reduction strategy
For this assignment, normal order reduction means:
leftmost-outermost reduction
continued until a normal form is reached, or until evaluation is stopped by a step limit
This reduction strategy is required for the baseline solution.
7. Non-termination control
Some lambda terms do not terminate under reduction.
Because non-termination cannot be detected in general, your solution must include a practical mechanism to stop evaluation, such as:
a configurable maximum number of reduction steps
If the step limit is reached, your interpreter must return a clear result indicating that evaluation was stopped before normal form was reached.
8. Testing
Your solution must include automated tests.
Tests must cover the required functionality, including:
the lambda-term model;
computation of free variables;
all substitution rules;
at least one case where alpha-conversion is necessary to avoid variable capture;
beta-reduction;
normal-order reduction;
the step-limit mechanism for a non-terminating term.
Manual testing alone is not sufficient.
Grading Levels
15 points
To receive 15 points, your submission must satisfy all of the following:
the interpreter is implemented in a functional style only;
the lambda-term syntax tree is implemented using immutable case classes or an equivalent immutable Scala 3 representation;
free variables are computed correctly;
substitution is implemented correctly according to the rules above;
alpha-conversion is handled correctly where needed;
normal order reduction is implemented correctly;
the solution includes a mechanism to stop non-terminating evaluation;
the implementation is covered by automated tests.
20 points
To receive 20 points, your submission must satisfy all 15-point requirements, and in addition:
implement at least one more reduction strategy;
provide a configurable way to choose the reduction strategy.
Examples of acceptable additional strategies include:
applicative order;
call-by-value;
another well-defined strategy that is clearly documented.
The chosen strategies must be implemented clearly and tested.
25 points
To receive 25 points, your submission must satisfy all 20-point requirements, and in addition:
implement a parser for a string representation of lambda terms.
The parser must:
parse variables, abstractions, and applications;
support parentheses where needed;
document the concrete syntax accepted by the parser.
You may choose a Unicode syntax using λ or an ASCII syntax such as \x. x, but the syntax must be clearly described in the repository documentation and covered by tests.
Submission Requirements
Each student or group must submit the project as a private GitHub repository.
The repository must contain:
the full source code;
automated tests;
a short README file with:
names of all group members,
a short description of the solution,
instructions for how to build and run the project,
instructions for how to run the tests.
The submitted version is considered final at the submission deadline.
Students are not allowed to add or modify work after submission unless explicitly requested by the instructor.
Your project should build and test in a standard Scala 3 environment.
```

- AI:
Here is a complete, copy-pastable Markdown template for your README. I've tailored it specifically to the 25-point requirements of your Scala 3 Lambda Calculus interpreter project, including placeholders for the extra reduction strategy and the parser syntax.

```markdown
# Lambda Calculus Interpreter

## Group Members
* [Member 1 Name]
* [Member 2 Name]
* [Member 3 Name]

## Project Description
This project is a pure functional interpreter for the untyped Lambda Calculus, implemented in Scala 3. The solution strictly adheres to functional programming principles, utilizing immutable data structures (case classes/enums) and pure functions without mutable state or in-place updates.

**Key Features:**
* **Immutable Syntax Tree:** Models variables, abstractions, and applications.
* **Capture-Avoiding Substitution:** Implements comprehensive substitution rules, including alpha-conversion to prevent variable capture using fresh variables.
* **Reduction Strategies:** * **Normal Order:** Leftmost-outermost reduction.
  * **[Insert Strategy 2]:** [Briefly describe your chosen second strategy, e.g., Applicative Order / Call-by-Value].
* **Non-Termination Control:** Includes a configurable step-limit mechanism to prevent infinite loops during the reduction of non-terminating terms (e.g., the Omega combinator).
* **String Parser:** Converts string representations of lambda terms into the internal AST.

## Parser Documentation
The interpreter includes a parser that accepts a specific syntax for lambda terms. 

**Concrete Syntax:**
* **Variables:** Lowercase letters (e.g., `x`, `y`, `z`).
* **Abstractions (Functions):** Represented using `[λ or \]`, followed by the bound variable, a dot `.`, and the body. 
  * *Example:* `\x. x` or `λx. x` *(Update this based on whether you chose ASCII or Unicode)*.
* **Applications:** Two terms separated by a space. 
  * *Example:* `(M N)`.
* **Parentheses:** Supported and often required to enforce precedence and disambiguate terms.

**Examples of valid inputs:**
* Identity function: `\x. x`
* Application: `(\x. x) y`
* Complex term: `\x. (\y. x y)`

*(Note: Update the syntax examples above if your parser expects specific spacing, Unicode characters, or different grouping rules).*

## Prerequisites
* **Scala 3**
* **sbt** (Scala Build Tool)

## How to Build and Run
To compile the project and run the main application (which demonstrates the parsing and evaluation of terms):

1. Open your terminal and navigate to the project's root directory.
2. Compile the project:
   ```bash
   sbt compile
   ```
3. Run the application:
   ```bash
   sbt run
   ```
   *(If your app accepts command-line arguments, describe them here, e.g., `sbt "run '\x.x' normal 100"`)*

## How to Run the Tests
The project includes a comprehensive suite of automated tests covering the AST model, free variable computation, substitution rules (including alpha-conversion), beta-reduction, both reduction strategies, and the parser.

To run the full test suite:
```bash
sbt test
```
```

- Student:

```
can you help me make a good documentation of the parser:
import lambdaCalculus.*
import scala.util.parsing.combinator.RegexParsers

object lambdaTermParser:

  def parse(input: String): Either[String, LambdaTerm] =
    val parser = ExpressionParser()
    val res    = parser.parseAll(parser.expression, input)

    res match
      case parser.Success(matchedTerm, _) =>
        Right(matchedTerm)
      case parser.Failure(msg, next) =>
        Left(s"Syntax Error: $msg at column ${next.pos.column}")
      case parser.Error(msg, next) =>
        Left(s"Fatal Error: $msg at column ${next.pos.column}")

end lambdaTermParser

class ExpressionParser extends RegexParsers:
  def variable: Parser[Variable] = "[a-z]+".r ^^ { name => Variable(name) }

  def subExpression: Parser[LambdaTerm] = variable | abstraction | "(" ~> expression <~ ")"

  def applicationOrSubExpression: Parser[LambdaTerm] = rep1(subExpression) ^^ { subExpressions =>
    subExpressions.reduceLeft((function, argument) => Application(function, argument))
  }

  def abstraction: Parser[Abstraction] = "\\" ~> variable ~ ("." ~> expression) ^^ { case boundVariable ~ body =>
    Abstraction(boundVariable, body)
  }

  def expression: Parser[LambdaTerm] = abstraction | applicationOrSubExpression
  ```

- AI:

Here is a clean, well-structured documentation section for your parser. It accurately reflects the logic in your Scala code, including the specific rules for variables (`[a-z]+`), the ASCII lambda syntax (`\`), left-associative applications, and the `Either` error-handling wrapper. 

You can copy and paste this directly into your README:

```markdown
## Parser Documentation

The project includes a robust parser built with Scala's `RegexParsers` combinators (`scala.util.parsing.combinator`). It takes a string representation of a lambda term and constructs the corresponding abstract syntax tree (AST).

### Concrete Syntax & Grammar Rules

The parser strictly accepts the following grammar:

* **Variables:** Consist of one or more lowercase English letters (regex: `[a-z]+`). 
    * *Examples:* `x`, `y`, `foo`, `argument`
* **Abstractions (Functions):** Denoted by a backslash `\`, followed by a bound variable, a dot `.`, and the body expression. 
    * *Examples:* `\x.x`, `\var.body`
* **Applications:** Expressed by separating two or more sub-expressions with whitespace. Application is **left-associative**, meaning `x y z` is parsed as `(x y) z`.
    * *Examples:* `x y`, `(\x.x) y`
* **Parentheses:** Used to group expressions, enforce precedence, or disambiguate terms. 
    * *Examples:* `x (\y.y)`, `(\x.x \y.y) z`

### Usage and Error Handling

The parser is exposed via the `lambdaTermParser.parse` method, which safely handles invalid syntax without throwing runtime exceptions. It returns an `Either[String, LambdaTerm]`:

* **`Right(LambdaTerm)`**: Returned upon a successful parse, containing the fully constructed immutable syntax tree.
* **`Left(String)`**: Returned upon encountering invalid syntax, providing a descriptive error message indicating the exact column number where the failure occurred.

### Examples

| String Input | Parsed AST Output (Conceptual representation) |
| :--- | :--- |
| `"x"` | `Variable("x")` |
| `"\x.x"` | `Abstraction(Variable("x"), Variable("x"))` |
| `"x y"` | `Application(Variable("x"), Variable("y"))` |
| `"\x.x y"` | `Abstraction(Variable("x"), Application(Variable("x"), Variable("y")))` |
| `"(\x.x) y"` | `Application(Abstraction(Variable("x"), Variable("x")), Variable("y"))` |
```

***

Your parser implementation looks solid, especially the left-associative reduction for applications. Do you want to easily add support for parsing the actual Unicode `λ` character alongside the backslash `\` before finalizing this?

- Student:
```
reformat example table so i can put in in reamde more easily and add more examples for each type: variable, abstraction, application as well as putting parantheses when they are implicit
```

- AI:
Here are two ways to format the examples depending on how you want them to look in your README. Both are in ready-to-copy code blocks. 

### Option 1: Standard Markdown Table
This is the cleanest visual option for GitHub READMEs. Just copy the block below and paste it directly into your file.

```markdown
### Examples of Valid Inputs

| Lambda Term (Input) | Conceptual AST Output | Description |
| :--- | :--- | :--- |
| `x` | `Variable("x")` | A single variable. |
| `\x.x` | `Abstraction(Variable("x"), Variable("x"))` | The Identity combinator. |
| `x y` | `Application(Variable("x"), Variable("y"))` | Simple application. |
| `x y z` | `Application(Application(Variable("x"), Variable("y")), Variable("z"))` | Demonstrates left-associativity. |
| `\x. x y` | `Abstraction(Variable("x"), Application(Variable("x"), Variable("y")))` | Demonstrates greedy scope of abstractions. |
| `(\x. x) y` | `Application(Abstraction(Variable("x"), Variable("x")), Variable("y"))` | Applying an abstraction to a variable using parentheses. |
| `\f.\x. f (f x)`| `Abstraction(Variable("f"), Abstraction(Variable("x"), Application(Variable("f"), Application(Variable("f"), Variable("x")))))`| Church numeral 2 (complex nested parsing). |
```

### Option 2: Bulleted List
If your Markdown viewer does not support tables well, or if you prefer a vertical layout because the AST outputs are long, use this list format instead:

```markdown
### Examples of Valid Inputs

* **Single variable:**
  * **Input:** `x`
  * **AST:** `Variable("x")`

* **The Identity combinator:**
  * **Input:** `\x.x`
  * **AST:** `Abstraction(Variable("x"), Variable("x"))`

* **Simple application:**
  * **Input:** `x y`
  * **AST:** `Application(Variable("x"), Variable("y"))`

* **Demonstrates left-associativity:**
  * **Input:** `x y z`
  * **AST:** `Application(Application(Variable("x"), Variable("y")), Variable("z"))`

* **Demonstrates greedy scope of abstractions:**
  * **Input:** `\x. x y`
  * **AST:** `Abstraction(Variable("x"), Application(Variable("x"), Variable("y")))`

* **Applying an abstraction to a variable using parentheses:**
  * **Input:** `(\x. x) y`
  * **AST:** `Application(Abstraction(Variable("x"), Variable("x")), Variable("y"))`

* **Church numeral 2 (complex nested parsing):**
  * **Input:** `\f.\x. f (f x)`
  * **AST:** `Abstraction(Variable("f"), Abstraction(Variable("x"), Application(Variable("f"), Application(Variable("f"), Variable("x")))))`
```


(In another chat)
- Student:

```
i heard you can make cool EBNF or backusnaur or whatever documnetation for cli commands and parsers
can you suggest such documentation for this small lambda calculus parser
and explain +- of such documentation as well as does it make sence to have such documentation for my parser

the parsers code in scala:
import lambdaCalculus.*
import scala.util.parsing.combinator.RegexParsers

object lambdaTermParser:

  def parse(input: String): Either[String, LambdaTerm] =
    val parser = ExpressionParser()
    val res    = parser.parseAll(parser.expression, input)

    res match
      case parser.Success(matchedTerm, _) =>
        Right(matchedTerm)
      case parser.Failure(msg, next) =>
        Left(s"Syntax Error: $msg at column ${next.pos.column}")
      case parser.Error(msg, next) =>
        Left(s"Fatal Error: $msg at column ${next.pos.column}")

end lambdaTermParser

class ExpressionParser extends RegexParsers:
  def variable: Parser[Variable] = "[a-z]+".r ^^ { name => Variable(name) }

  def subExpression: Parser[LambdaTerm] = variable | abstraction | "(" ~> expression <~ ")"

  def applicationOrSubExpression: Parser[LambdaTerm] = rep1(subExpression) ^^ { subExpressions =>
    subExpressions.reduceLeft((function, argument) => Application(function, argument))
  }

  def abstraction: Parser[Abstraction] = "\\" ~> variable ~ ("." ~> expression) ^^ { case boundVariable ~ body =>
    Abstraction(boundVariable, body)
  }

  def expression: Parser[LambdaTerm] = abstraction | applicationOrSubExpression
  ```

  - AI:
  Here is the EBNF (Extended Backus-Naur Form) documentation for your Lambda Calculus parser. 

### **EBNF Documentation for your Parser**

EBNF uses specific symbols: `|` means "or", `{ ... }` means "repeated zero or more times", and literal strings are enclosed in quotes. Based on your Scala `RegexParsers` code, here is the exact grammar:

```ebnf
Expression     ::= Abstraction | ApplicationChain
ApplicationChain ::= SubExpression { SubExpression }
Abstraction    ::= "\" Variable "." Expression
SubExpression  ::= Variable | Abstraction | "(" Expression ")"
Variable       ::= lowercase_letter { lowercase_letter }

lowercase_letter ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" 
                   | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" 
                   | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"
```
*(Note: I named `applicationOrSubExpression` as `ApplicationChain` here for readability, as it represents one or more sub-expressions grouped together.)*

---

### **Pros and Cons (+/-) of EBNF Documentation**



**The Pros (+):**
* **Language Agnostic:** Anyone can read your EBNF and understand the syntax of your language without needing to know a single drop of Scala. 
* **Standardized Communication:** It is the universal language of computer science for describing syntax. If you want to write a paper, a formal spec, or ask for help from parsing experts, EBNF is what they will expect to read.
* **Portability:** If you ever decide to rewrite your parser in Rust, Python, or Haskell, the EBNF serves as a perfect blueprint. You won't have to reverse-engineer your Scala code.

**The Cons (-):**
* **Maintenance Overhead:** You now have two sources of truth: your code and your documentation. If you add a new feature (like `let` bindings) to your code and forget to update the EBNF, they drift out of sync.
* **Doesn't Capture Semantics well:** EBNF tells you *what* text is valid, but it struggles to cleanly explain *how* that text is processed. For example, your EBNF doesn't explicitly show that applications are *left-associative*. Your Scala code does this explicitly with `.reduceLeft()`, but the EBNF leaves that detail ambiguous to the reader.

---

### **Does it make sense to have this for your parser?**

**Yes, but strictly for your `README.md`.** The domain of Lambda Calculus is deeply rooted in formal mathematics and logic. People exploring your repository will likely expect a formal mathematical definition of the syntax you are parsing. Putting that EBNF block at the top of your documentation provides instant, frictionless context.

However, you don't need to go overboard integrating it into your code comments. You are using Scala's `RegexParsers` (parser combinators). The fundamental philosophy of parser combinators is that **the code itself acts as an executable grammar**. 

Look at your code:
```scala
def abstraction: Parser[Abstraction] = "\\" ~> variable ~ ("." ~> expression)
```
Look at the EBNF:
```ebnf
Abstraction ::= "\" Variable "." Expression
```
They are practically identical. Scala's combinator library is effectively an internal Domain Specific Language (DSL) for EBNF. Because your code is already so highly declarative, heavily commenting it with EBNF would be redundant. Keep the EBNF in the repository documentation for the non-Scala readers, and let the beautifully clean combinator code speak for itself internally!

