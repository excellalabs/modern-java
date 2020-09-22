# Chapter 9: Refactoring, testing, and debugging

This chapter covers

* Refactoring code to use lambda expressions 
    - in existing code
    - on new project, start with lambdas and streams immediately
* Appreciating the impact of lambda expressions on object-oriented design patterns 
    - including strategy, template method, observer, chain of responsibility, and factory
    - more concise using lambdas
* Testing lambda expressions
* Debugging code that uses lambda expressions and the Streams API

Remember, lambdas let you write more concise and flexible code because of
    - express behavior in more compact code
    - pass code as parameters

Let's go through approaches to refactor to **lambdas, method references and streams**

## 9.1.1 Improving code readability

3 refactorings: 

* Refactoring anonymous classes to lambda expressions
* Refactoring lambda expressions to method references
* Refactoring imperative-style data processing to streams

### 9.1.2 From anonymous classes to lambda expressions

Before (1) and after (2):

```
Runnable r1 = new Runnable() {                       1
    public void run(){
        System.out.println("Hello");
    }
};

Runnable r2 = () -> System.out.println("Hello");     2
```

Gotchas:

- this and super are different for anonymous classes and lambda expressions. Inside an anonymous class, this refers to the anonymous class itself, but inside a lambda, it refers to the enclosing class
- anonymous classes are allowed to shadow variables from the enclosing class. Lambda expressions can’t
- converting an anonymous class to a lambda expression can make the resulting code ambiguous in the context of overloading. Indeed, the type of anonymous class is explicit at instantiation, but the type of the lambda depends on its context
- Most IDEs can catch these issues

### 9.1.3. From lambda expressions to method references

- Lambda expressions are great for short code that needs to be passed around
- Consider if method references would improve code readability. A method reduces clutter and a method name states the intent of your code more clearly.
- Use helper static methods such as `comparing` and `maxBy` whenever possible, as they're made for use with method references
- Also helper collectors such as `sum` and `maximum`

### 9.1.4. From imperative data processing to Streams

Try to convert all code that processes a collection with typical data processing patterns with an iterator to use the `Streams API` instead, because recall:

- The Streams API expresses more clearly the intent of a data processing pipeline. 
- It can be optimized behind the scenes, making use of short-circuiting and laziness as well as leveraging your multicore architecture
- Tools like `Lambda-Ficator` can help in refactoring

### 9.1.5. Improving code flexibility

`Behavior parameterization` is encouraged by lambdas, as you can represent different behaviors by passing different lambdas. Here are patterns you can apply immediately to get benefits:

#### Adoption functional interfaces

You need them to use lambda expressions. 2 common code patterns that can be refactored to introduce them are:

#### `Deferred execution` - so logic is only executed if condition is true

_From this, which constructs the log message no matter what:_

```
logger.log(Level.FINER, "Problem: " + generateDiagnostic());
```

_To this functional interface and lambda:_

```
public void log(Level level, Supplier<String> msgSupplier)
```

```
logger.log(Level.FINER, () -> "Problem: " + generateDiagnostic());
```

#### `Execute around`

If you're surrounding different code with the same preparation and cleanup phases, you can often pull that code into a lambda. You can reuse the logic dealing with the preparation and cleanup phases, thus reducing code duplication.

## 9.2. REFACTORING OBJECT-ORIENTED DESIGN PATTERNS WITH LAMBDAS

- Lambda expressions can provide alternative solutions to the problems that the design patterns are tackling, but often with less work and in a simpler way.
 - Many existing object-oriented design patterns can be made redundant or written in a more concise way with lambda expressions.

In this section, we explore five design patterns:

* Strategy
* Template method
* Observer
* Chain of responsibility
* Factory

### 9.2.1. Strategy

A common solution for representing a family of algorithms (strategies) and letting you choose among them at runtime (i.e. when we filtered an inventory with different predicates, such as green apples).

Lambda expressions encapsulate a piece of code (or strategy), which is what the strategy design pattern was created for, so we recommend that you use lambda expressions instead for similar problems where you don't need separate classes for the small amount of logic.

_See before/after code samples in book_

### 9.2.2 Template method

- When you need to represent the outline of an algorithm and have the additional flexibility to change certain parts of it
- Useful when you find yourself saying “I’d love to use this algorithm, but I need to change a few lines so it does what I want.”
- Use lambdas to plug in the functionality without the boilerplate of subclasses

_See before/after code samples in book_

### 9.2.3. Observer

- A common solution when an object (called the subject) needs to automatically notify a list of other objects (called observers) when some event happens (such as a state change)
- I.E., various classes implement the Observer interface and provide specific implementation for a single method
- Lambdas were designed to remove that kind of boilerplate

#### Should you use lambda expressions all the time? 

- No. In the examples we described, lambda expressions work great because the behavior to execute is simple, so they’re helpful for removing boilerplate code. 
- But the logic we're trying to replace with lambdas may be more complex (i.e. there could have state, define several methods) 
- In those situations, you should stick with classes.

### 9.2.4. Chain of responsibility

- Common solution to create a chain of processing objects/operations. One object does some work and passes result to another object.
- Generally implemented by defining an abstract class representing a processing object that defines a field to keep track of a successor, and a method to do the work and pass to the successor:

```
public abstract class ProcessingObject<T> {
    protected ProcessingObject<T> successor;
    public void setSuccessor(ProcessingObject<T> successor){
        this.successor = successor;
    }
    public T handle(T input) {
        T r = handleWork(input);
        if(successor != null){
            return successor.handle(r);
        }
        return r;
    }
    abstract protected T handleWork(T input);
}
```

Implementing it this way:

```
public class HeaderTextProcessing extends ProcessingObject<String> {
    public String handleWork(String text) {
        return "From Raoul, Mario and Alan: " + text;
    }
}
public class SpellCheckerProcessing extends ProcessingObject<String> {
    public String handleWork(String text) {
        return text.replaceAll("labda", "lambda");       1
    }
}

// Now you can connect two processing objects to construct a chain of operations:

ProcessingObject<String> p1 = new HeaderTextProcessing();
ProcessingObject<String> p2 = new SpellCheckerProcessing();
p1.setSuccessor(p2);                                              1
String result = p1.handle("Aren't labdas really sexy?!!");
System.out.println(result);    
```

Vs using lambdas:

```
UnaryOperator<String> headerProcessing =
    (String text) -> "From Raoul, Mario and Alan: " + text;
UnaryOperator<String> spellCheckerProcessing =
    (String text) -> text.replaceAll("labda", "lambda"); 
Function<String, String> pipeline =
    headerProcessing.andThen(spellCheckerProcessing); 
String result = pipeline.apply("Aren't labdas really sexy?!!");
```

### 9.2.5. Factory

- Create objects without exposing the instantiation logic to the client
- Typically you'd create a Factory class with method responsible for creation of the right object
- Instead, you could use method references and a HashMap to avoid the case statement
- This technique doesn’t scale well if the factory method needs to take multiple arguments to pass to the product constructors. You’d have to provide a functional interface other than a simple Supplier.

_See before/after code samples in book_

# Session 15

_[Recording (9/14/20)](https://excella.zoom.us/rec/share/Kwo3cZyJV0-IEjeePINrKnCvdFSx3OPEMrItYDHT2LHoYBdjsjQ2D7eJPhI2sp8.14eEXts2EZwUEdcV)_

## Agenda

- **Housekeeping**: notes & code, expensing food, start recording
- **Recap**
    - Chapter 9 except testing and debugging
- **Today:** 
    - Chapter 9 testing and debugging, start chapter 10 - Domain-specific languages using lambdas
- **Next time:** finish 10, start 11

## 9.3. Testing Lambdas

- Lambdas don't have names to call so can be trickier to test
- Test the lambda as you do when calling methods
- Focus on the behavior of the method using the lambda
    - lambdas should encapsulate pieces of one-off behavior used by another method
- Put complex lambdas into separate methods like a method reference, and test that

### Testing Higher-order functions

- test its behavior with difference lambdas
- for example, test a filter method by passing in different filtering lambdas and ensures the results are correct
- If the method under test returns a function, you can test that behavior by treating it as an instance of a functional interface (as shown earlier with a Comparator)

## 9.4 Debugging

2 main tools: examining the stack trace & logging

### 9.4.1 Examining the stack trace

- due to the fact that lambda expressions don’t have names, stack traces can be slightly puzzling, as the compiler has to make up names like $0

```
Exception in thread "main" java.lang.NullPointerException
    at Debugging.lambda$main$0(Debugging.java:6)                         1
    at Debugging$$Lambda$5/284720968.apply(Unknown Source)
    at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline
     .java:193)
    at java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators
     .java:948)
...
```

### 9.4.2. Logging information

- It can be hard to see what part of a stream acted on what, such as if you are debugging by logging results with a `forEach`
- The `peek` operation can help. It executes an action on each element of a stream as it’s consumed. It doesn’t consume the whole stream the way forEach does.

Example: in the following code, you use peek to print the intermediate values before and after each operation in the stream pipeline:

```
List<Integer> numbers = Arrays.asList(2, 3, 4, 5);
List<Integer> result =
  numbers.stream()
         .peek(x -> System.out.println("from stream: " + x))    1
         .map(x -> x + 17)
         .peek(x -> System.out.println("after map: " + x))      2
         .filter(x -> x % 2 == 0)
         .peek(x -> System.out.println("after filter: " + x))   3
         .limit(3)
         .peek(x -> System.out.println("after limit: " + x))    4
         .collect(toList());
```

* 1 Print the current element consumed from the source
* 2 Print the result of the map operation.
* 3 Print the number selected after the filter operation.
* 4 Print the number selected after the limit operation.

## Summary

[Continue to next chapter](README-chapter-10.md)
