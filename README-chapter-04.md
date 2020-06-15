## Session 7

- **Welcome, Recap (START RECORDING)** 
    - Last session: chapter 3 exercise
- **Run through & discuss** - chapter 4 review and discussion & quizzes as a group
- **Next time** - Chapter 5, some of 6

# Chapter 4: Introducing Streams (streams programming with the Java Streams API)

Chapter concepts: 

* What is a stream?
* Collections versus streams
* Internal versus external iteration
* Intermediate versus terminal operations

*Collections*

- Most programs make and *process collections of data*, including finding, filtering, grouping, counting
- Most databases let you do these things *declaratively*, so you don't have to implement *how*. 
- Welcome streams, for now you can think of them as *fancy iterators over collections of data*.
- With streams, the threading model is decoupled from the query itself. Because you are providing a recipe for a query, it could be executed sequentially or in parallel. 

4.1 example - before and after

## Core concepts

- You chain together several building-block operations to express a complicated data-processing pipeline
- You create a chain by linking sorted, map, and collect operations - **lambdas!**

[**Figure 4.1**](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/04fig01_alt.jpg)

Characteristics of streams: 

- Declarative — More concise and readable
- Composable — Greater flexibility
- Parallelizable — Better performance

*See exercise code*

Rest of chapter...
- patterns such as filtering, slicing, finding, matching, mapping, and reducing
- create streams from different sources, such as from a file
- generate streams with an infinite number of elements

### Collections

Start the conversation with collections - it's the simplest way to begin working with streams.

- Collections in Java 8 support a new `stream` method, that returns a stream
- You can also get streams in various other ways, for example, generating stream elements from a numeric range or from I/O resources

=8-0 interface definition: java.util.stream.Stream

*Docs* - [https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)

### Definition breakdown

A sequence of elements from a source that supports data-processing operations.

*Sequence of elements*
- Provides an interface to a sequenced set of values of a specific element type (like a collection)
- Because collections are data structures, they’re mostly about storing and accessing elements 
    - with specific time/space complexities 
    - for example, an ArrayList versus a LinkedList
- But, streams are about expressing computations such as filter, sorted, and map, which you saw earlier
- Collections are about data; streams are about computations

*Source*
- Streams consume from a data-providing source such as collections, arrays, or I/O resources
- Generating a stream from an ordered collection preserves the ordering

*Data-processing operations*
- Streams support database-like operations and common operations from functional programming languages to manipulate data
- Such as: filter, map, reduce, find, match, sort, and so on
- Stream operations can be executed either sequentially or in parallel


*2 important characteristics*
- **Pipelining** 
    - Many stream operations return a stream themselves, allowing operations to be chained to form a larger pipeline. 
    - This enables certain optimizations such as laziness and short-circuiting
    - A pipeline of operations can be viewed as a database-like query on the data source (yes, like Linq)

- **Internal iteration** 
    - stream operations do the iteration behind the scenes for you
    - in contrast to collections, which are iterated explicitly using an iterator

**EXAMPLE:**

```
import static java.util.stream.Collectors.toList;

List<String> threeHighCaloricDishNames =
  menu.stream()                                         1
      .filter(dish -> dish.getCalories() > 300)         2
      .map(Dish::getName)                               3
      .limit(3)                                         4
      .collect(toList());                               5
System.out.println(threeHighCaloricDishNames);          6
```

1. Gets a stream from menu (the list of dishes)
1. Creates a pipeline of operations: first filter high-calorie dishes
1. Gets the names of the dishes
1. Selects only the first three
1. Stores the results in another List
1. Gives results [pork, beef, chicken]

- All these operations except collect return another stream
    - so they can be connected to form a pipeline
    - can be viewed as a query on the source
- The collect operation 
    - starts processing the pipeline to return a result
    - Returns something other than a stream
    - No result is produced, no element from menu is even selected, until collect is invoked
    - Think of it as if the method invocations in the chain are queued up until collect is called


*4 common stream operations:*

* *filter* 
    - Takes a lambda to exclude certain elements from the stream. 
    - For example, select dishes that have more than 300 calories by passing it this lambda: `d -> d.getCalories() > 300`
* *map* 
    - Takes a lambda to transform an element into another one or to extract information. 
    - For example, you extract the name for each dish by passing the method reference `Dish::getName` which is equivalent to the lambda `d -> d.getName()`
* *limit* — Truncates a stream to contain no more than a given number of elements
* *collect* — Converts a stream into another form. In this above example you convert the stream into a list. 
    - You can see collect as an operation that takes as an argument various recipes for accumulating the elements of a stream into a summary result. 
    - I.E. toList() describes a recipe for converting a stream into a list
    - How collect works in chapter 6

[*Nice diagram*](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/04fig02_alt.jpg)

- You use a much more declarative style to process the data where you say what needs to be done
- You don’t implement the stream operation (i.e. filter) functionalities - they’re available through the Streams library
- the Streams API has more flexibility to decide how to optimize this pipeline
    - For example, the filtering, extracting, and truncating steps could be merged into a single pass and stop as soon as three dishes are found

### Conceptual differences between Collection and Stream APIs

- The difference between collections and streams has to do with when things are computed.
- A collection is an in-memory data structure that holds all the values the data structure currently has
- Every element in the collection has to be computed before it can be added to the collection - they're all stored in memory
- A stream is a conceptually fixed data structure, and it's elements are computed on demand. This = significant programming benefits.

For example,
- a stream containing all the prime numbers (2, 3, 5, 7, 11, . . .) even though there are an infinite number of them. 
- A user will extract only the values they require from a stream 
- The elements are produced invisibly to the user only as and when required 
- This is a form of a producer-consumer relationship
- Like a lazy constructed collection - computed by when asked for; demand-driving; just-in-time
- Collections are eagerly constructed (fill your warehouse before you start selling)

### Additional concepts

- stream can be traversed only once, then it is "consumed"
- External vs internal iteration
    - Collection requires a foreach, etc loop to iterate - external iteration
    - Streams does the iteration for you if you provide a function of what needs to be done - internal iteration
    - Thus the processing of items could be transparently done in parallel or in a different order that may be more optimized. 
        - These optimizations are difficult if you iterate the collection externally as you’re used to doing in Java
    - The internal iteration in the Streams library can automatically choose a data representation and implementation of parallelism to match your hardware

### Quiz 4.1: External vs. internal iteration

### 2 categories of operations

Operations to piece things together, and ones that close a stream

- intermediate operations 
    - i.e. filter, map, limit
    - intermediate operations don’t perform any processing until a terminal operation is invoked
    - they can usually be merged and processed into a single pass by the terminal operation
- terminal operations 
    - i.e. collect
    - produce non-stream values like List, Integer or void

4.4.1 Example

- the limit operation and a technique called short-circuiting
- despite the fact that filter and map are two separate operations, they were merged into the same pass 
    - compiler experts call this technique loop fusion

*So,* working with streams involves 3 thinkgs:
* A data source (such as a collection) to perform a query on
* A chain of intermediate operations that form a stream pipeline
* A terminal operation that executes the stream pipeline and produces a result

The idea behind a stream pipeline is similar to the builder pattern (see http://en.wikipedia.org/wiki/Builder_pattern)

### Summary bullets

### Next chapter
- *An extensive look at the various operations supported by the Streams API*
- filtering, slicing, finding, matching, mapping, and reducing, which can be used to express sophisticated data-processing queries

[Continue to Chapter 5](README-chapter-05.md)
