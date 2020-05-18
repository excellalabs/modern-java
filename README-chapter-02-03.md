# Session 4

## Housekeeping

- Welcome anyone new, survey results (remember to record)
- Recap of last session
    - 2nd half of chapter 1 - Java streams, modules, default methods, optionals, pattern matching
    - Recap exercise 1
- Review chapter 2, what struck people most?
- Exercises
- Next time 
    - ? - Deep into bahavior parameterization coding? Incrementally? Real world problem?

# Chapter 2

## Summary: Passing code with behavior parameterization

    - Take apart verbose code to efficiently utilize bahavior parameterization
    - Dives deep via small steps towards exercise we did, from using the old way to predicates
    - Foreshadowing lambdas
    - Code evolution steps:
        - old ways - highly verbose, redundant code
        - predicates - solves much of redundant code issue, not all
        - anonymous functions - solves more of the redundat code issue, highly verbose
        - lambdas - solves the above

## Details: 

* Coping with changing requirements
* Behavior parameterization
* Anonymous classes
* Preview of lambda expressions
* Real-world examples: Comparator, Runnable, and GUI

Analogy: goAndBuy to goAndDo

Example of evolving code to be more flexible for changing requirements

2.1 - Gradually improving into our first exercise
2.1.3 - we've all probably seen this
2.2 - find a better level of abstraction

*Predicates*

Separate the logic of iterating the collection inside the filter-Apples method with the behavior you want to apply to each element of the collection (in this case a predicate)

2.2.1 - passing criteria via boolean expression, wrapped in a predicate object, through an object that implements the test method

Foreshadow: with lambdas, you don't have to wrap the criteria in a predicate. You can pass it directly.

*Quiz 2.1* 

*Anonymous Functions*

With predicates, you're forced to declare multiple classes that implement predicate interface, and then instantiate several predicate objects that you allocate only once.

Listing 2.1 - you can do better. The next evolution is anonymous functions (if not entirely satisfactory).

Anonymous classes 
    - like the local classes (a class defined in a block)
    - They don't have a name. 
    - They allow you to declare and instantiate a class at the same time. 
    - In short, they allow you to create ad hoc implementations.
    - Were often used in the context of GUI applications to create event-handler objects
    - Loads of boilerplate still
    - Can be confusing to use, see Quiz 2.2

2.3.2 for example, creates predicate on the fly using an anonymous function

## EXERCISE: Evolve to anonymous functions


Lambdas

Language designers solved the problems with anonymous functions by introducing lambda expressions, a more concise way to pass code.

2.3.3 example

Even another step of abstraction - generics

You could genericize the type you're passing in code for (so not just apples)

2.3.4 examples - You’ve managed to find the sweet spot between flexibility and conciseness, which wasn’t possible prior to Java 8!

## EXERCISE: Real world examples

Summary bullets

### Chapter 3

* Chapter 3: apply lambdas to further tackle verbosity


Lambdas in a nutshell
The execute-around pattern
Functional interfaces, type inference
Method references
Composing lambdas

NUTSHELL: A kind of anonymous function: it doesn’t have a name, but it has a list of parameters, a body, a return type, and also possibly a list of exceptions that can be thrown.

