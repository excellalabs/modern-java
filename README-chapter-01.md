# Chapter 1

# Session 1

## Club Intro
   
- Review [Club Overview](README.md)
- Time & Space: Monday at 5, #modern-java-book-club
- Session format: summary, key points & into their details, exercises / try out in app
- Presenting
- Github repo: https://github.com/excellalabs/modern-java

## Summary - Chapter 1

Chapter 1: History and an overview of the paradigm shifts

*Key concepts*
- History of Java, Java 8-11
- Why languages evolve - track changing hardware or programmer expectations
- Some drivers of change, 
    - more cores 
    - big data to process
    - bigger scale
    - improve concurrency

- Overview and key benefits of **3 programming concepts**: 
    - functional-style programming 
    - stream processing 
    - passing code as parameters

- At first, these changes might feel a little uncomfortable, but once you get used to them, you’ll love them
- Java 8 added functions as a new form of value, to exploit parallel programming on multicore processors. 
    - They are also very useful in themselves.
- Programs using these concepts are said to be written in functional-programming style
    - this phrase means “writing programs that pass functions around as first-class values.” 
- Additions notable things added:
    - modules to better support component-based systems
    - default methods to enable adding these features without having to change everything

## History

Java 8 - streams API, techniques for passing code to methods, default methods in interfaces
- The main changes in Java 8 reflect a move away from classical object orientation, which often focuses on mutating existing values, and toward the functional-style programming spectrum in which what you want to do in broad-brush terms is considered prime and separated from how you can achieve this
- Changes to Java 8 were in many ways more profound than any other changes to Java in its history.

Java 9 - reactive programming added, adjustments to type inference, Java modules

Java 10 - type inferance for local variables

Java 11 - richer syntax for lambda expressions, new async HTTP client library

## Drivers of change

Keep in mind the idea of the language ecosystem and the consequent evolve-or-wither pressure on languages. Although Java may be supremely healthy at the moment, we can recall other healthy languages such as COBOL that failed to evolve.

Drivers: 
- multi core processors 
    - The vast majority of Java programs only use one core
    - Since Java 1 the best practice was to use threads, locks and a memory model if needed, but were often too difficult to use relibably in non-specialist project teams
    - Simplier way to think about it starting in Java 8
- Big data to process
- Bigger scale
- Improve concurrency
- More effective and concise language. Write code closer to the problem statement, like: 

```
File[] hiddenFiles = new File(".").listFiles(new FileFilter() {
    public boolean accept(File file) {
        return file.isHidden();                        1
    }
});
```

to 

```
File[] hiddenFiles = new File(".").listFiles(File::isHidden);
```

- Java 8 method reference :: syntax - meaning “use this method as a value”
- methods are no longer second-class values
- reads closer to the problem statement


## Passing code to methods

- Allows a new way to express behavior parameterization
- Allows additional techniques referred to as functional-style programming

- Functions are first-class values
    - can be passed as functional values
    - anonymous functions (lambdas)
    - better ways in dealing with null

- Example: 1.3.2

### Lambdas (anonymous functions)

1.3.3

- You don’t need to write a method definition that’s used only once
- The code is crisper and clearer because you don’t need to search to find the code you’re passing. 
- If such a lambda exceeds a few lines in length (so that its behavior isn’t instantly clear), you should instead use a method reference to a method with a descriptive name instead of using an anonymous lambda. Code clarity should be your guide.

# Session 2 

## Housekeeping

- Rounds
- RSVP on eventbot, you can download ics to put series on your calendar
- Q: exercises in tests, what would be useful in building out in a full app?
- Next week: hack session 
- Recap of last session. Any items you walk to talk through?

## Streams 

Streams, introduced in Java 8, address boilerplate and obscure code, and difficulty exploiting multicore.

They provide access to sequences of data items. They generalize many aspects of collections, but often enables more readable code and allows elements of a stream to be processed in parallel.

*Key Concepts*

- Resembles the way you might think in database query languages—you express what you want in a higher-level manner, and the implementation (here the Streams library) chooses the best low-level execution mechanism
- As a result, it avoids the need for you to write code that uses synchronized, which is not only highly error-prone but also more expensive than you may realize on multicore CPUs
- Higher level of abstraction
- Get concurrency without coding with threads, almost for free

*Details:*

- 1.4
- Multicore computers
- Multithreading is difficult
- Boilerplate - streams operators like filter and group are common so benefit from being standardized 

### Parallelism & no shared mutable data 

Magic bullets:
- the library handles partitioning—breaking down a big stream into several smaller streams to be processed in parallel for you
- this parallelism almost for free from streams, works only if the methods passed to library methods like filter don’t interact (for example, by having mutable shared objects). 
    - Sometimes these are referred to as pure functions or side-effect-free functions or stateless functions.

In contrast, in the imperative programming paradigm you typically describe a program in terms of a sequence of statements that mutate state. 

The no-shared-mutable-data requirement means that a method is perfectly described solely by the way it transforms arguments to results; in other words, it behaves as a mathematical function and has no (visible) side effects.

These are cornerstones of what’s generally described as the paradigm of functional programming.

## Modules & default methods

Historically, Java had little support for systems built from components, apart from a JAR file containing a set of Java packages with no particular structure.

Evolving interfaces to such packages was hard—changing a Java interface meant changing every class that implements it

- Java 9 provides a module system that provide you with syntax to define modules containing collections of packages—and keep much better control over visibility and namespaces. 
    - Modules enrich a simple JAR-like component with structure, both as user documentation and for machine checking;

- Java 8 added default methods to support evolvable interfaces, so these things could be added without having to change all existing classes that use them. 
    - An interface can now contain method signatures for which an implementing class doesn’t provide an implementation. 
    - The missing method bodies are given as part of the interface, hence default implementation. 
    - More in chapter 13. 

## Other good ideas from functional programming

We've got these, which are **exploited by the streams API**:
- [x] using methods and lambdas as first-class values
- [x] calls to functions or methods can be efficiently and safely executed in parallel in the absence of mutable shared state

Two more:
- [ ] Optionals
- [ ] Pattern matching

### Optionals

Common functional languages provide more constructs to help the programming, includingh *avoiding null*. 

 Tony Hoare, one of the giants of computer science, said this in a presentation at QCon London 2009:

    I call it my billion-dollar mistake. It was the invention of the null reference in 1965.
    
    I couldn’t resist the temptation to put in a null reference, simply because it was so easy to implement.
    

Optionals
- if used correctly, helps avoid null pointer exceptions
- a container that may or may not contain a value
- includes methods to explicitly deal with the case where a value is not present, thus avoiding NPE

### Pattern Matching

Normally you'd use if/else or switch statements
- Allows using against many more data types
- For more complex data types, pattern matching can express programming ideas more concisely compared to using if-then-else
- Need third-party library (i.e. Vavr) to get fully pattern matching in Java

Example: under 1.6

# Session 3 

## House cleaning 

- Welcome anyone new
- Survey results
- Next time: 
    - Chapters 2 & 3 with reduced time on concepts (behavior parameterization, lambda expressions, 105 minutes)
    - Deep into bahavior parameterization coding? Incrementally? Real world problem?
- Recap of last session (2nd half of chapter 1 - Java streams, modules, default methods, optionals, pattern matchinbg)

## Summary of Chapter 1

See book's last-page summary bullets

# Exercises

A) Get the Spring Boot starter app (with exercises) running that's included in this repo: 
    
1. Clone `https://github.com/excellalabs/modern-java`
1. Install [docker](https://www.docker.com/products/docker-desktop) 
1. Make sure you can do the following:

    - Start docker container: `./run_app.sh`
    - Compile the app: `./gradlew clean build`
    - Running tests: `./gradlew test`

    
B) Refactor redundant code using predicates and lambdas: 

The client just asked you to change their production system. They asked you to change some code in their e-commerce store that filters fruit they sell, to make it easier to read and maintain since they're changing in more because of COVID-19. 

Change some existing code that filters apples by color and weight using two separate functions, so one function can do both. 
    
1. Open the test class `Chapter01Exercises` & ensure the tests pass, located in `src/test/java/com/excella/reactor/chapter01`
1. Refactor the `filterByColor` and `filterByWeight` functions that are in there into one function, passing the filtering criteria as a parameter. 
1. Get the tests to pass. The solution is located in `Chapter01ExercisesCOMPLETED`. 

BONUS: If you're done already, try `Quiz 2.2` in chapter 2 to see if you like that approach more
