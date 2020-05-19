# Chapter 3: Apply lambdas to further tackle verbosity

# Session 5

Agenda

- Recap last session, exercise
- 

Points covered:

* Lambdas in a nutshell, where you can use them
* The execute-around pattern
* Functional interfaces, type inference
* Method references
* Composing lambdas

NUTSHELL: A kind of anonymous function: it doesn’t have a name, but it has a list of parameters, a body, a return type, and also possibly a list of exceptions that can be thrown.

`(parameters) -> body acting on parameters and is return value`

*Expression-style lambda*
`(Apple apple1, Apple apple2) -> a.getWeight().compareTo(a2.getWeight());`

*Block-style lambda*
```
(Apple apple1, Apple apple2) -> {
    System.out.println(apple1.getWeight());
    System.out.println(apple2.getWeight());
}
``` 

They let you represent behavior or pass code in a concise way.

- Predicates, strategy pattern too verbose
- Anonymous functions hard to read, cumbersome, and still too verbose

We'll use lambdas the rest of the book, along with some other goodies:

- Type inference
- Important Java interfaces available
- Introducing method references (which go hand-in-hand with lambda expressions)

Listing 3.1. Valid lambda expressions in Java 8+

Quiz 3.1: Lambda syntax

Table 3.1 This provides a list of example lambdas with examples of use cases.

*Where can you use them?*
- You can use a lambda expression in the context of a functional interface. 
- In the predicate exercise, you can pass a lambda as second argument to the method filter because it expects an object of type Predicate<T>, which is a functional interface

*Functional interfaces*
A functional interface is an interface that specifies exactly one abstract method

You provide the implementation of the abstract method of a functional interface directly inline

The whole expression is a concrete implementation of the the functional interface

The signature of the functional interface's abstract method is called a *functional descriptor* (3.2.2)

3.2.1 examples in the Java API

*Default methods*
- Interfaces can now also have default methods (a method with a body that provides some default implementation for a method in case it isn’t implemented by a class)
- An interface is still a functional interface if it has many default methods as long as it specifies only one abstract method

Quiz 3.2: identify Functional interface

Compare valid styles: 

```
Runnable r1 = () -> System.out.println("Hello World 1");         
Runnable r2 = new Runnable() {                                   
    public void run() {
        System.out.println("Hello World 2");
    }
};
public static void process(Runnable r) {
    r.run();
}
process(r1);                                                    
process(r2);                                                     
process(() -> System.out.println("Hello World 3"));   
```

Quiz 3.3: Where can you use lambdas? (HINT: Look at Java's Runnable and Callable interfaces)

```
public interface Runnable {
    public void run();
}
```

```
public interface Callable<V> {
    V call() throws Exception;
}
```

*@FunctionalInterface annotation*

- used to indicate that the interface is intended to be a functional interface and is therefore useful for documentation. 
- the compiler will return a meaningful error it isn’t a functional interface
    
    I.E. “Multiple non-overriding abstract methods found in interface Foo” 

- isn’t mandatory, but it’s good practice to use it when an interface is designed for that purpose

*Execute-around pattern*

TALK THROUGH EXERCISE 3.3?

Most common functional interfaces:
- Predicate
- Consumer
- Function

Quiz 3.4 - which Java functional interface?

Table 3.2. Longer list of common functional interfaces added in Java 8

Table 3.3 Examples

3.5. TYPE CHECKING, TYPE INFERENCE, AND RESTRICTIONS - be aware

*METHOD REFERENCES* - let you reuse existing method definitions and pass them like lambdas

Quiz 3.6: Method references

Constructor references

Composing comparators, predicates, functions

Similar ideas from math

EXERCISE: Refactor chapter 2 quiz

- Refactor stragtegy patterm to lambda, remove extra classes
- Leverage @FunctionalInterface
- Use method references?
- Anything from Java functional interfaces?
