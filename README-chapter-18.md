# Chapter 18. Functional Thinking

**Session 27**

_[Recording]()_

**Agenda**

- **Housekeeping**: notes & code, expensing food, start recording
- **Recap**
    - 17 coding
- **Today:** 
    - chapter 18 key concepts - Thinking functionally 
- **For Next time:** 
    - chapter 19 - Functional programming techniques
    
This chapter covers

* Why functional programming?
* What defines functional programming?
* Declarative programming and referential transparency
* Guidelines for writing functional-style Java
* Iteration versus recursion

First, review concepts behind functional programming—such as side effects, immutability, declarative programming, and referential transparency 

Then, relate these concepts to Java 8. 

### 18.1.1. Shared mutable data

Think about implementing and maintaining systems

Pure functions, or side-effect free

_Side effect_ - not totally enclosed within the function itself
- Modifying a data structure in place, including assigning to any field, apart from initialization inside a constructor (such as setter methods)
- Throwing an exception
- Performing I/O operations such as writing to a file

Can now use multicore parallelism without using locking

### 18.1.2. Declarative programming

- What vs how - provide rules, expect system to decide how
- Avoid internal iteration; leave finer details to the libraries 
- Reads closer to the problem statement 

### 18.1.3 Why functional programming?

_Pass arguments, get results_

## 18.2. WHAT’S FUNCTIONAL PROGRAMMING?

Programming with functions 

Declarative 

Referential transparency

Shouldn't throw exceptions - as signals would be sent outside the input/output control flow 

Partial functions (i.e, square root when input is negative) - use Optionals 
 
### 18.2.1. Functional-style Java

- you can’t completely program in pure functional style in Java. Java’s I/O model consists of side-effecting methods, for example
- it’s possible to write core components of your system as though they were purely functional

To be regarded as functional style, 
- a function or method can mutate only local variables
- all fields are final
- all fields of reference type refer transitively to other immutable objects
- a function or method shouldn’t throw any exceptions


### 18.2.2. Referential transparency

- the restrictions on no visible side effects (no mutating structure visible to callers, no I/O, no exceptions) encode the concept
- always returns the same result value when it’s called with the same argument value
- great property for program understanding
- encompasses save-instead-of-recompute optimization for expensive or long-lived operations, a process that goes by the name memoization or caching

### 18.2.3. Object-oriented vs. functional-style programming

- changes in hardware (such as multicore) and programmer expectation (such as database-like queries to manipulate data) are pushing Java software-engineering styles more functional
- At one end of the spectrum is the extreme object-oriented view: everything is an object, and programs operate by updating fields and calling methods that update their associated object. At the other end of the spectrum lies the referentially transparent functional-programming style of no (visible) mutation

### 18.2.4. Functional style in practice

## 18.3. RECURSION VS. ITERATION

- promoted in functional programming to let you think in terms of what-to-do style
- Pure functional programming languages typically don’t include iterative constructs (hidden invitations to use mutation)
- programs can be rewritten to prevent iteration by using recursion, which doesn’t require mutability

Listing 18.1. Iterative factorial
```
static long factorialIterative(long n) {
    long r = 1;
    for (int i = 1; i <= n; i++) {
        r *= i;
    }
    return r;
}
```

Listing 18.2. Recursive factorial
```
static long factorialRecursive(long n) {
    return n == 1 ? 1 : n * factorialRecursive(n-1);
}
```

Listing 18.3. Stream factorial
```
static long factorialStreams(long n){
    return LongStream.rangeClosed(1, n)
                     .reduce(1, (long a, long b) -> a * b);
}
```
 
Recursion is expensive though. Every time a recursive function is called, a new stack frame is created on the call stack to hold the state of each function call until the recursion is done. It uses memory proportional to its input.

Tail-call optimization is the solution. The basic idea is that you can write a recursive definition of factorial in which the recursive call is the last thing that happens in the function (or the call is in a tail position). This different form of recursion style can be optimized to run fast. The next listing provides a tail-recursive definition of factorial.
 
Listing 18.4. Tail-recursive factorial 
```
static long factorialTailRecursive(long n) {
  return factorialHelper(1, n);
}
static long factorialHelper(long acc, long n) {
  return n == 1 ? acc : factorialHelper(acc * n, n-1);
}
```

_See Figure 18.5._

- Java doesn’t support this kind of optimization
 
Summary

[Continue to next chapter](README-chapter-19.md)