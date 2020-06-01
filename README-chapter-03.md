## *Session 5*

Agenda

- **Welcome & Recap** 
    - Last session: lambdas in chapter 2, exercise (behavior parameterization evolution, lambdas are the current solution)
- **Run through & discuss** - Chapter 3 - Lambda Expressions
- **Exercises** - lambdas and functional interface visual quizzes
- **Next time** -  
    - 3.7 Exercise - refactoring previous code using behavior parameterization, anonymous classes, lambda expressions, and method references
    - Chapter 4 - Intro to Streams quizzes (2)
- *START RECORDING!*

# Chapter 3: Apply lambdas to further tackle verbosity

Points covered:

* Lambdas in a nutshell, where you can use them
* The execute-around pattern
* Functional interfaces, type inference
* Method references
* Composing lambdas

Lambdas in a NUTSHELL: A kind of anonymous function: it doesn’t have a name, but it has a list of parameters, a body, a return type, and also possibly a list of exceptions that can be thrown.

## General lambda syntax 

    (parameters) -> body acting on parameters and is return value

*Expression-style lambdas*

    (Apple apple) -> apple.getWeight().compareTo(a2.getWeight());

    a -> a.getWeight().compareTo(a2.getWeight());
    
    (Apple apple1, Apple apple2) -> apple1.getWeight().compareTo(apple2.getWeight());

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

We'll use lambdas the rest of the book, along with some related concepts:

- Type inference
- Important Java interfaces available
- Introducing method references (which go hand-in-hand with lambda expressions)

* **Listing 3.1**. Valid lambda expressions in Java 8+

### **Quiz 3.1: Lambda syntax**

### **Table 3.1** This provides a list of example lambdas with examples of use cases

*Where can you use them?*
 
- You can use a lambda expression in the **context of a functional interface** (more to come)
- In the predicate exercise, you can pass a lambda as second argument to the method filter because it expects an object of type Predicate<T>, which is a functional interface

## Functional interfaces

- A functional interface is an interface that specifies exactly one abstract method
- You provide the implementation of the abstract method of a functional interface directly inline
- The whole expression is a concrete implementation of the the functional interface
- The signature of the functional interface's abstract method is called a *functional descriptor* (3.2.2)

### **3.2.1 examples** in the Java API

*Default methods*

- Interfaces can now also have default methods (a method with a body that provides some default implementation for a method in case it isn’t implemented by a class)
- An interface is still a functional interface if it has many default methods as long as it specifies only one abstract method

### **Quiz 3.2**: identify Functional interface

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

### **Quiz 3.3:** Where can you use lambdas? (HINT: Look at Java's Runnable and Callable interfaces)

Some Java functional interfaces you've seen so far: 

```
public interface Comparator<T> {                           1
    int compare(T o1, T o2);
}
public interface Runnable {                                2
    void run();
}
public interface ActionListener extends EventListener {    3
    void actionPerformed(ActionEvent e);
}
public interface Callable<V> {                             4
    V call() throws Exception;
}
public interface PrivilegedAction<T> {                     5
    T run();
}
```

*@FunctionalInterface annotation*

- used to indicate that the interface is intended to be a functional interface and is therefore useful for documentation. 
- the compiler will return a meaningful error it isn’t a functional interface
    
    I.E. “Multiple non-overriding abstract methods found in interface Foo” 

- isn’t mandatory, but it’s good practice to use it when an interface is designed for that purpose

*Execute-around pattern*

A pattern where setup and cleanup phases are always similar and surround the important code doing the processing

*Most common functional interfaces:*

- Predicate
- Consumer
- Function

### **Quiz 3.4** - which Java functional interface?

**Real world: composable.java**

### **Table 3.2** Longer list of common functional interfaces added in Java 8

### **Table 3.3** Examples

Additional concepts:

- 3.5. TYPE CHECKING, TYPE INFERENCE, AND RESTRICTIONS - be aware
- METHOD REFERENCES - let you reuse existing method definitions and pass them like lambdas
- Composing comparators, predicates, functions
- Similar ideas from math

## Method references

- Shorthand for lambdas calling a specific method
- If the basic idea is that if a lambda represents “call this method directly,” it’s best to refer to the method by name rather than by a description of how to call it

## Table 3.4. Examples of lambdas and method reference equivalents

## Quiz 3.6 - Method references

## Quiz 3.7 - Constructor references

## Session 6

### EXERCISE: 3.7 PUTTING LAMBDAS AND METHOD REFERENCES INTO PRACTICE

- To wrap up this chapter and our discussion on lambdas, we’ll continue with our initial problem of sorting a list of Apples with different ordering strategies 
- Progressively evolve a naïve solution into a concise solution, using all the concepts and features explained so far: behavior parameterization, anonymous classes, lambda expressions, and method references
- Pull the latest and go to the **instructions** in the file `src/test/java/com/excella/reactor/chapter03/SortingTest.java`
- Good luck! 

[*Continue to chapter 4*](README-chapter-04.md)
