# Chapter 5: Working with streams

*An extensive look at the various operations supported by the Streams API*

- filtering, slicing, mapping, finding, matching, and reducing
- special cases of streams: numeric streams, streams built from multiple sources such as files and arrays, and infinite streams

## 5.1.x Filtering with a predicate, distinct

- takes an argument as a predicate and returns a boolean
- [Diagram](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/05fig01_alt.jpg)

```
List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
numbers.stream()
       .filter(i -> i % 2 == 0)
       .distinct()
       .forEach(System.out::println);
```

## 5.2 Slicing a stream

*Select and skip elements*

- Using a prediate - `takeWhile` and `dropWhile`

```
List<Dish> slicedMenu2
    = specialMenu.stream()
                 .dropWhile(dish -> dish.getCalories() < 320)
                 .collect(toList()); 
```

## Truncating a stream

With `limit` you can:

```
List<Dish> dishes = specialMenu
                        .stream()
                        .filter(dish -> dish.getCalories() > 300)
                        .limit(3)
                        .collect(toList());
```

With `skip` you can:

```
List<Dish> dishes = menu.stream()
                        .filter(d -> d.getCalories() > 300)
                        .skip(2)
                        .collect(toList());
```

### Quiz 5.1

# Session 8

## Agenda 

- **Recap** (START RECORING)
    - Chapter 4 - intro to streams, started
    - Started chapter 5 - working with streams
        - Filtering - filter(), distinct()
        - Slicing - select & skip
            - takeWhile(), dropWhile()
            - Truncating - limit(), skip()
- **Today:** 
    - Review key concepts of chapter 5 - working with streams, get into 6 - data collection with streams
    - *Poll:* should we do the streams exercises on our own?
- **Next time:** Finish chapter 6, some of chapter 7 - Parallel data processing and performance

## 5.3 Mapping

Select information from certain objects through `map` and `flatMap` methods

### 5.3.1. Applying a function to each element of a stream

- Streams support the `map` method, which takes a function as argument
- The function is applied to each element, mapping it into a new element 
- The word mapping is used because it has a meaning similar to transforming but with the nuance of **“creating a new version of”** rather than “modifying”

EXAMPLE: 

```
List<String> dishNames = menu.stream()
                             .map(Dish::getName)
                             .collect(toList());
```

- the stream outputted by the map method is of type `Stream<String>`

EXAMPLE: 

Given a list of words, you’d like to return a list of the number of characters for each word. How would you do it?

```
List<String> words = Arrays.asList("Modern", "Java", "In", "Action");
List<Integer> wordLengths = words.stream()
                                 .map(String::length)
                                 .collect(toList());
```


### 5.3.2 Flattening streams

How would you return unique characters for a list of words? For example, given the list of words ["Hello," "World"] you’d like to return the list ["H," "e," "l," "o," "W," "r," "d"].

You might think:

```
words.stream()
     .map(word -> word.split(""))
     .distinct()
     .collect(toList());
```

What's wrong? 

- the lambda passed to the map method returns a `String[]` *for each word*
- The stream returned by the map method is of type `Stream<String[]>`
- You want a `Stream<String>` to represent a stream of characters
- See [Figure 5.5. Incorrect use of map to find unique characters from a list of words](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/05fig05_alt.jpg)

The solution is to use `flatMap`:
    - get a stream of characters instead of arrays, you can use `Arrays.stream()` passing an array
    - use `flatMap` to flatten each generated stream
    - see example after diagram above

In a nutshell, the flatMap method lets you replace each value of a stream with another stream and then concatenates all the generated streams into a single stream.

### Quiz 5.2 #2

## 5.4 Finding and matching

- Another common data processing idoim is finding elements that match a property
- Steams API provides allMatch, anyMatch, noneMatch, findFirst, and findAny methods of a stream

5.4.1 Predicate matches at least one element: 

```
if(menu.stream().anyMatch(Dish::isVegetarian)) {
    System.out.println("The menu is (somewhat) vegetarian friendly!!");
}
```

5.4.2. Predicate matches all elements

All: 

```
boolean isHealthy = menu.stream()
                        .allMatch(dish -> dish.getCalories() < 1000);
```

Or nothing:

```
boolean isHealthy = menu.stream()
                        .noneMatch(d -> d.getCalories() >= 1000);
```

- Those all use `short-circuiting`.
    - `allMatch, noneMatch, findFirst, and findAny` don’t need to process the whole stream to produce a result
    - `limit` too
    - useful such as for infinite streams


### 5.4.3. Finding an element

- `findAny`

```
Optional<Dish> dish =
  menu.stream()
      .filter(Dish::isVegetarian)
      .findAny();
```

*A tast of Optional (more in chapter 11):*

- The `Optional<T>` class (java.util.Optional) is a container class to represent the existence or absence of a value. 
- Instead of returning null, which is well known for being error-prone, the Java 8 library designers introduced `Optional<T>`.

There are a few methods available in Optional that force you to explicitly check for the presence of a value or deal with the absence of a value:

- `isPresent()` returns true if Optional contains a value, false otherwise
- `ifPresent(Consumer<T> block)` executes the given block if a value is present (`Consumer` functional interface mentioned in chapter 3 lets you pass a lambda that takes an argument of type T and returns void)
- `T get()` returns the value if present; otherwise it throws a NoSuchElementException
- `T orElse(T other)` returns the value if present; otherwise it returns a default value

EXAMPLE: 

```
menu.stream()
    .filter(Dish::isVegetarian)
    .findAny()                                                  1
    .ifPresent(dish -> System.out.println(dish.getName()); 
```

*findFirst and findAny*

- You may wonder why we have both findFirst and findAny
- The answer is parallelism
- Finding the first element is more constraining in parallel
- If you don’t care about which element is returned, use `findAny` because it’s less constraining when using parallel streams

## 5.5 Reducing

- The terminal operations seen so far either return a boolean (allMatch and so on), void (forEach), or an Optional object (findAny and so on). 
- You’ve also been using collect to combine all elements in a stream into a List

In this section, you’ll see how you can combine elements of a stream to express more complicated queries using reduction (a stream is reduced to a value) 

In functional programming-language jargon, this is referred to as a fold because you can view this operation as repeatedly folding a long piece of paper (your stream) until it forms a small square.

```
int sum = 0;
for (int x : numbers) {
    sum += x;
}
```

To:

```
int sum = numbers.stream().reduce(0, (a, b) -> a + b);
```

- abstracts procedural pattern, can pass the the formula on how to reduce
- `reduce` takes 2 arguments:
    - An initial value (0 here)
    - A `BinaryOperator<T>` to combine two elements and produce a new value; here you use the lambda `(a, b) -> a + b`
    - you can easily multiply, etc by passing a different lambda

**In-depth look with Figure 5.7** - Using reduce to sum the numbers in a stream


There's an overloaded variant of reduce that doesn’t take an initial value, but it returns an Optional object
    - handles the case where stream has no elements, indicating the sum may be absent

```
Optional<Integer> sum = numbers.stream().reduce((a, b) -> (a + b));
```

### 5.5.2. Maximum and minimum

Reduce operation will use the new value with the next element of the stream to produce a new maximum until the whole stream is consumed

```
Optional<Integer> max = numbers.stream().reduce(Integer::max);
```

### Quiz 5.3: Reducing

### Benefit of the reduce method and parallelism

- NOTE: A chain of map and reduce is commonly known as the `map-reduce` pattern, made famous by Google’s use of it for web searching because it can be easily parallelized.
- iterative summation (for loop), which is the mutable-accumulator pattern, is a dead end for parallelization
- `reduce` provides you a new pattern
- use `parallelStream` instead of stream
- Caveats: 
    - the lambda passed to reduce can’t change state (for example, instance variables) 
    - the operation needs to be associative and commutative so it can be executed in any order
- there are built-in methods for *common reduction patterns*, like `sum` and `max`
- We’ll investigate a more complex form of reductions using the collect method in the next chapter
        *For example, instead of reducing a stream into an Integer, you can also reduce it into a Map if you want to group dishes by types*

### Stream operations: stateless vs. stateful

Operations like `map` and `filter` take each element from the input stream and produce zero or one result in the output stream. These operations are in general **stateless**.

Operations like `reduce, sum, and max` need to have **internal state** to accumulate the result
- The internal state is of bounded size no matter how many elements are in the stream being processed
- The state is small

Operations such as `sorted or distinct`:
- seem at first to behave like filter or map—all take a stream and produce another stream (an intermediate operation) — but there’s a crucial difference
- They require knowing the previous history to do their job
- For example, sorting requires all the elements to be buffered before a single item can be added to the output stream
- the storage requirement of the operation is unbounded

### [Table 5.1: Intermediate and Terminal Strem Operations](https://learning.oreilly.com/library/view/Modern+Java+in+Action/9781617293566/kindle_split_016.html#ch05table01)

### HOMEWORK EXERCISE: 5.6. PUTTING IT ALL INTO PRACTICE

### 5.7 Primitive stream specializations

What is the problem with this code? 

```
int calories = menu.stream()
                   .map(Dish::getCalories)
                   .reduce(0, Integer::sum);
```

- There's an insidious boxing cost 
    - Behind the scenes each Integer needs to be unboxed to a primitive before performing the summation
- So, the Streams API also supplies primitive stream specializations that support specialized methods to work with streams of numbers
    - `IntStream, DoubleStream, and LongStream`
    - avoid hidden boxing costs
    - they have  methods to perform common numeric reductions, such as sum and max
    - methods to convert back to a stream of objects when necessar
    - additional complexity of these specializations isn’t inherent to streams - it reflects the complexity of boxing—the (efficiency-based) difference between int and Integer, etc

The most common methods you’ll use to convert a stream to a specialized version are mapToInt, mapToDouble, and mapToLong

EXAMPLE 

```
int calories = menu.stream()
                   .mapToInt(Dish::getCalories)
                   .sum();

Stream<Integer> newStream = calories.boxed();
```

- mapToInt extracts all the calories from each dish (represented as an Integer) and returns an IntStream as the result (rather than a Stream<Integer>). 
- the sum method defined on the IntStream interface calculates the sum of calories
- Note that if the stream were empty, sum would return 0 by default
- convert back to general stream (each int will be boxed to an Integer) with the the method `boxed` so you can do things with the other data again


### HOMEWORK (OPTIONAL) EXERCISE: 5.7.3. Putting numerical streams into practice: Pythagorean triples

## 5.8. BUILDING STREAMS

- create a stream from a sequence of values, from an array, from a file, and even from a generative function to create infinite streams
- `Stream.of`

```
Stream<String> stream = Stream.of("Modern ", "Java ", "In ", "Action");
stream.map(String::toUpperCase).forEach(System.out::println);
```

### 5.8.2. Stream from nullable

- In Java 9, a new method was added that lets you create a stream from a nullable object 
- You may encounter a situation where you extracted an object that may be null and then needs to be converted into a stream (or an empty stream for null)
- For example, the method System.getProperty returns null if there is no property with the given key. 
- To use it together with a stream, you’d need to explicitly check for null as follows:

```
String homeValue = System.getProperty("home");
Stream<String> homeValueStream
    = homeValue == null ? Stream.empty() : Stream.of(value);
```

Using Stream.ofNullable you can rewrite this code more simply:

```
Stream<String> homeValueStream
    = Stream.ofNullable(System.getProperty("home"));
```

This pattern can be particularly handy in conjunction with `flatMap` and a stream of values that may include nullable objects:

```
Stream<String> values =
   Stream.of("config", "home", "user")
         .flatMap(key -> Stream.ofNullable(System.getProperty(key)));
```

### 5.8.3. Streams from arrays

```
int[] numbers = {2, 3, 5, 7, 11, 13};
int sum = Arrays.stream(numbers).sum();
```

### 5.8.4. Streams from files

- Java’s NIO API (non-blocking I/O), which is used for I/O operations such as processing a file, has been updated to take advantage of the Streams API 
- Many static methods in java.nio.file.Files return a stream
- For example, a useful method is `Files.lines`, which returns a stream of lines as strings from a given file

Using what you’ve learned so far, you could use this method to find out the number of unique words in a file as follows:

*SEE BOOK*

### 5.8.5. Streams from functions: creating infinite streams

- Streams API provides two static methods to generate a stream from a function: `Stream.iterate` and `Stream.generate`
- They create an infinite stream, a stream that doesn’t have a fixed size like when you create a stream from a fixed collection
- Streams produced by iterate and generate create values on demand given a function 
- Can calculate values forever
- It’s generally sensible to use `limit(n)` on such streams to avoid printing an infinite number of values.

```
Stream.iterate(0, n -> n + 2)
      .limit(10)
      .forEach(System.out::println);
```

### HOMEWORK QUIZ 5.4: Fibonacci tuples series

*Summary*

[Continue to Chapter 6](README-chapter-06.md)
