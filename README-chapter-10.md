# Chapter 10: Domain-specific languages using lambdas

_This chapter covers_

* What domain-specific languages (DSLs) and their forms are
    - developers forget that a programming language is a language intended to convey a message in the clearest way
    - A most important characteristic of well-written software is clear communications of intention
    - famous computer scientist Harold Abelson stated, “Programs must be written for people to read and only incidentally for machines to execute.”
    - Most important within business logic. Code that can be shared and understood by developers and domain experts is helpful for validating correctness early on.
    - thus, it's common to express business logic through DSL, a small, non-general purpose language tailored, with terminology, for a specific domain.
    - Examples:
        - Maven and Gradle - DSLs for expressing build processes
        - HTML - language tailored to define the structure of a web page
        - SQL (external), Streams API (internal) for data access
    - Rigidity and verbosity made Java bad for implementing a compact DSL, but that changed with lambda support
* The pros and cons of adding a DSL to your API
    - when should consider implementing one?
    - easier to code main logic of software
    - enables writing less, and more concise & fluent code
    - enables better communication and collaboration with domain experts
    - lower-skilled programmers can write code quickly and effectively without having to program against system-level code
    - cons
* The alternatives available on the JVM to a plain Java-based DSL
* Learning from the DSLs present in modern Java interfaces and classes
* Patterns and techniques to implement effective Java-based DSLs
* How commonly used Java libraries and tools use these patterns

## 10.1 A specific language for your domain

**Defined:** A DSL is a custom-built language designed to solve a problem for a specific business domain
- For example, an accounting DSL would include concepts such as bank statements and operations such as reconciling often represented as a a set of classes and methods
- In a way, you can see the DSL as an API created to interface with a specific business domain
- restricts the operations and vocabulary available to a specific domain & only allow users to deal with complexities of the domain

Two reasons should drive you toward the development of a DSL:

- Communication is king. Your code should clearly communicate its intentions and be understandable even by a non-programmer. This way, this person can contribute to validating whether the code matches the business requirements.
- Code is written once but read many times. Readability is vital for maintainability. In other words, you should always code in a way that your colleagues thank you for rather than hate you for!

### 10.1.1. Pros and cons of DSLs

A DSL raises the level of abstraction so you can clarify the business intentions, and it makes the code more readable.

However, the implementation now has to be maintained and tested, so useful to investigate the benefits and costs when deciding to use one.

Benefits:

* Conciseness - An API that conveniently encapsulates the business logic allows you to avoid repetition, resulting in code that’s less verbose
* Readability — 
    - vocabulary of the domain makes the code understandable even by domain non-experts. 
    - Consequently, code and domain knowledge can be shared across a wider range of members of the organization.
* Maintainability 
    — Code written against a well-designed DSL is easier to maintain and modify. 
    - It's code that may changes most frequently
* Higher level of abstraction — The operations available in a DSL work at the same level of abstraction as the domain, thus hiding the details that aren’t strictly related to the domain’s problems
* Focus — Having a language designed for the sole purpose of expressing the rules of the business domain helps programmers stay focused on that specific part of the code. The result is increased productivity.
* Separation of concerns — Expressing the business logic in a dedicated language makes it easier to keep the business-related code isolated from the infrastructural part of the application. The result is code that’s easier to maintain.

Introducing a DSL into your code base can have a few disadvantages:

* Difficulty of DSL design — It’s hard to capture domain knowledge in a concise limited language
* Development cost — 
    - Adding a DSL is a long-term investment with a high up-front cost
    - The maintenance of the DSL and its evolution add further engineering overhead
* Additional indirection layer — it wraps your domain model in an additional layer that has to be as thin as possible to avoid incurring performance problems
* Another language to learn — Nowadays, developers are used to employing multiple languages. Adding a DSL to your project, however, implicitly implies that you and your team have one more language to learn. Worse, if you decide to have multiple DSLs covering different areas of your business domain, combining them in a seamless way could be hard, because DSLs tend to evolve independently.
* Hosting-language limitations — Some general-purpose programming languages (Java is one of them) are known for being verbose and having rigid syntax. These languages make it difficult to design a user-friendly DSL. In fact, DSLs developed on top of a verbose programming language are constrained by the cumbersome syntax and may not be nice to read. The introduction of lambda expression in Java 8 offers a powerful new tool to mitigate this problem.

### 10.1.2 Different DSL solutions available on the JVM

We quickly explore alternatives to Java for the DSL and describe the circumstances under which they could be appropriate solutions.

_Catagories of DSLs_

- Internal (embedded) - implemented on top of existing hosting language (i.e. in Java within your app)
- External - stand-alone as they're developed from scratch with an independent syntax (i.e. SQL)
- Polyglot DSL - more expressive language such as Scala or Groovy

Lambdas solved the problem of Java being unsuitable for writing DSLs:

```
List<String> numbers = Arrays.asList("one", "two", "three");
numbers.forEach( new Consumer<String>() { # logic
    @Override
    public void accept( String s ) {
        System.out.println(s); # logic
    }
} );
```

Only the 2 marked lines are actually needed anymore:

```
List<String> numbers = Arrays.asList("one", "two", "three");
numbers.forEach(System.out::println);
```

Scala in particular has several features, such as currying and implicit conversion, that are convenient in developing a DSL

_See Scala code examples, of currying and implicit conversion, to get an idea of what's possible beyond Java_

## 10.2. SMALL DSLS IN MODERN JAVA APIS

- the Comparator interface serves as a good example of how lambdas improve the reusability and composability of methods in native Java API

_See code for small example_

### 10.2.1. The Stream API seen as a DSL to manipulate collections

It's characteristics of a well-designed DSL:
- fluent style 
- All intermediate operations are lazy and return another Stream allowing a sequence of operations to be pipelined
- Terminal operation is eager and triggers the computation of the result of the whole pipeline.

### 10.2.2. Collectors as a DSL to aggregate data

Collector interface and explained how to use it to collect, to group, and to partition the items in a Stream. We also investigated the static factory methods provided by the Collectors class to conveniently create different flavors of Collector objects and combine them. It’s time to review how these methods are designed from a DSL point of view.

Multilevel grouping and multifield sorting

```
Map<String, Map<Color, List<Car>>> carsByBrandAndColor =
        cars.stream().collect(groupingBy(Car::getBrand,
                                         groupingBy(Car::getColor)));
```

What do you notice here compared with what you did to concatenate two Comparators? You defined the multifield Comparator by composing two Comparators in a fluent way,

```
Comparator<Person> comparator =
        comparing(Person::getAge).thenComparing(Person::getName);
```

whereas the Collectors API allows you to create a multilevel Collector by nesting the Collectors:

```
Collector<? super Car, ?, Map<Brand, Map<Color, List<Car>>>>
   carGroupingCollector =
       groupingBy(Car::getBrand, groupingBy(Car::getColor));
```

Normally, the fluent style is considered to be more readable than the nesting style, especially when the composition involves three or more components. 
    - This reflects a deliberate design choice caused by the fact that the innermost Collector has to be evaluated first, but logically, it’s the last grouping to be performed. In this case, nesting the Collector creations with several static methods instead of fluently concatenating them allows the innermost grouping to be evaluated first but makes it appear to be the last one in the code.

By looking more closely at the native Java API and the reasons behind its design decisions, you’ve started to learn a few patterns and useful tricks for implementing readable DSLs.

## 10.3. PATTERNS AND TECHNIQUES TO CREATE DSLS IN JAVA

- Create a simple domain model
- Apply patterns to create a DSL on top of it
- _See starter code, Stock, Trader, Order_

### 10.3.1. Method chaining

- First DSL style to explore, most common
- Single chain of method invocations:

```
Order order = forCustomer( "BigBank" )
                  .buy( 80 )
                  .stock( "IBM" )
                      .on( "NYSE" )
                  .at( 125.00 )
                  .sell( 50 )
                  .stock( "GOOGLE" )
                      .on( "NASDAQ" )
                  .at( 375.00 )
              .end();
```

- You need a few builders that create the objects of this domain through a fluent API
- The top-level builder creates and wraps an order, making it possible to add one or more trades to it

_See MethodChainingOrderBuilder code example, Listing 10.6 & code in repo:_

- wraps an order with a builder so it can be chained
- methods like buy and sell are methods of the order builder, returning another builder
- methods that facilitate chaining (i.e. `buy` `stock` `at` `end`)
- having multiple builder classes—and in particular, two different trade builders—is made to force the user of this DSL to call the methods of its fluent API in a predetermined sequence, ensuring that a trade has been configured correctly before the user starts creating the next one
-  the parameters used to set an order up are scoped inside the builder. This approach minimizes the use of static methods and allows the names of the methods to act as named arguments, thus further improving the readability of this style of DSL. 
- the fluent DSL resulting from this technique has the least syntactic noise possible

_Cons_

 - the main issue in method chaining is the verbosity required to implement the builders. A lot of glue code is necessary to mix the top-level builders with the lower-level ones. 
 - Another evident disadvantage is the fact that you have no way to enforce the indentation convention that you used to underline the nesting hierarchy of the objects in your domain

# Session 16

_[Recording (9/21/20)](https://excella.zoom.us/rec/share/w5AJ3XFcR0chx6V9Jdst3GVqDxxQQ4m9A6zBh4hasGZOpP1edzxs36T-QtXSaIaU.bVzg01_a16O3-BE7)_

## Agenda

- **Housekeeping**: notes & code, expensing food, start recording
- **Recap**
    - Chapter 9 testing and debugging, Chapter 10 (DSLs) through method chaining
- **Today:** 
    - Finish chapter 10, start 11 - Optionals
- **Next time:** finish 11, start 12 - New Date and Time API

### 10.3.2. Using nested functions

Populates the domain model by using functions that are nested within other functions:

```
Order order = order("BigBank",
                    buy(80,
                        stock("IBM", on("NYSE")),
                        at(125.00)),
                    sell(50,
                         stock("GOOGLE", on("NASDAQ")),
                         at(375.00))
                   );
```

- code required to implement this DSL style is far more compact than method chaining
- NestedFunctionOrderBuilder in the following listing shows that it’s possible to provide an API with this DSL style to your users. (In this listing, we implicitly assume that all its static methods are imported.)
- another advantage of this technique compared with method chaining is that the hierarchy structure of your domain objects (an order contains one or more trades, and each trade refers to a single stock in the example) is visible by the way in which the different functions are nested

_See code **Listing 10.8. An order builder providing a nested-function DSL**_

Cons:

- Resulting DSL requires a lot of parentheses
- The list of arguments that have to be passed to the static methods is rigidly predetermined
- If the objects of your domain have some optional fields, you need to implement different overloaded versions of those methods, which allows you to omit the missing parameters
- The meanings of the different arguments are defined by their positions rather than their names
    - You can mitigate this last problem by introducing a few dummy methods, as you did with the at() and on() methods in your NestedFunctionOrderBuilder, the only purpose of which is to clarify the role of an argument

Haven't leveraged lambdas yet...

### 10.3.3. Function sequencing with lambda expressions

- employs a sequence of functions defined with lambda expressions
- you need to develop several builders that accept lambda expressions and to populate the domain model by executing them

```
Order order = order( o -> {
    o.forCustomer( "BigBank" );
    o.buy( t -> {
        t.quantity( 80 );
        t.price( 125.00 );
        t.stock( s -> {
            s.symbol( "IBM" );
            s.market( "NYSE" );
        } );
    });
    o.sell( t -> {
        t.quantity( 50 );
        t.price( 375.00 );
        t.stock( s -> {
            s.symbol( "GOOGLE" );
            s.market( "NASDAQ" );
        } );
    });
} );
```

- These builders keep the intermediate state of the objects to be created the way you did in the DSL implementation by using method chaining
- As you did in the method-chaining pattern, you have a top-level builder to create the order, but this time, the builder takes Consumer objects as parameters so that the user of the DSL can use lambda expressions to implement them

_See code **Listing 10.10. An order builder providing a function-sequencing DSL**_

Pros:

- Combines two positive characteristics of the two previous DSL styles. 
- Like the method-chaining pattern it allows to define the trading order in a fluent way. 
- Similarly to the nested-function style, it preserves the hierarchy structure of our domain objects in the nesting level of the different lambda expressions

Cons:

- Requires a lot of setup code 
- Using the DSL itself is affected by the noise of the Java 8 lambda-expression syntax

Approaches summary:

- Choosing among these three DSL styles is mainly a matter of taste
- It requires some experience to find the best fit for the domain model for which you want to create a domain language
- It’s possible to combine two or more of these styles in a single DSL, as you see in the next section

### 10.3.4. Putting it all together

- You could combine approaches
- For example the nested-function pattern with the lambda approach
    - Each trade is created by a Consumer of a TradeBuilder that’s implemented by a lambda expression
    - You can write the body of the lambda expression through which the trade will be populated in the most compact way possible:

```
Order order =
        forCustomer( "BigBank",                           1
                     buy( t -> t.quantity( 80 )           2
                                .stock( "IBM" )           3
                                .on( "NYSE" )
                                .at( 125.00 )),
                     sell( t -> t.quantity( 50 )
                                 .stock( "GOOGLE" )
                                 .on( "NASDAQ" )
                                 .at( 125.00 )) );
```

1 Nested function to specify attributes of the top-level order
2 Lambda expression to create a single trade
3 Method chaining in the body of the lambda expression that populates the trade object

_See code **Listing 10.12. An order builder providing a DSL that mixes multiple styles**_

- An example of combining approaches for a readable DSL
- Minor drawback: the resulting DSL appears to be less uniform than one that uses a single technique, so users of this DSL probably will need more time to learn it
- The Comparator and Stream APIs show, using method references can further improve the readability of many DSLs....

### 10.3.5. Using method references in a DSL

- Add a feature to domain model to calculate taxes

_See code **Listing 10.13, 14, 15, 16**_

- 10.13, 14 adds feature, but too hard to know what the arguments are
- 10.15 - fix by implementing small TaxCalculator DSL that fluently defines the taxes to be applied, but too verbose
- 10.16 - fix 

## 10.4 Real-world Java 8 DSL

- _Table 10.1. summarized DSLs patterns with their pros and cons_
- Let's look at how these patterns are employed in 3 well-known Java libraries

### 10.4.1. jOOQ

- a Java library providing a nice DSL to write and execute SQL queries in a type-safe way
- A source-code generator reverse-engineers the database schema, which allows the Java compiler to type-check complex SQL statements
- uses method chaining pattern
    - characteristics of this pattern (allowing optional parameters and requiring certain methods to be called only in a predetermined sequence) are essential to mimic the syntax of a well-formed SQL query

```
SELECT * FROM BOOK
WHERE BOOK.PUBLISHED_IN = 2016
ORDER BY BOOK.TITLE
```

can be written using the jOOQ DSL like this:

```
create.selectFrom(BOOK)
      .where(BOOK.PUBLISHED_IN.eq(2016))
      .orderBy(BOOK.TITLE)
```

- Can use with the Streams API allowing you to manipulate in memory, with a single fluent statement, the data resulting from the execution of the SQL query
- See **Listing 10.17. Selecting books from a database by using the jOOQ DSL**

### 10.4.2. Cucumber

- Behavior-driven development (BDD) is an extension of test-driven development that uses a simple domain-specific scripting language made of structured statements that describe various business scenarios
- Cucumber, like other BDD frameworks, translates these statements into executable test cases
- Can be used both as runnable tests and as acceptance criteria for a given business feature
- Script that defines the test scenario is written with an external DSL that has a limited number of keywords and lets you write sentences in a free format (i.e. Given, When, Then) that are matched by regular expressions
- See **Listing 10.18. Implementing a test scenario by using Cucumber annotations**, refactored version
    - Demonstrates how to effectively combine an external DSL with an internal one and shows that lambdas allow you to write more compact, readable code

### 10.4.3. Spring Integration

- extends the dependency-injection-based Spring programming model to support the well-known Enterprise Integration Patterns
    - promote the adoption of an asynchronous, message-driven architecture
    - enables lightweight remoting, messaging, and scheduling within Spring-based applications
    - implements all the most common patterns necessary for message-based applications, such as channels, endpoints, pollers, and channel interceptors
     - Endpoints are expressed as verbs in the DSL to improve readability, and integration processes are constructed by composing these endpoints into one or more message flows
- see **Listing 10.19. Configuring a Spring Integration flow by using the Spring Integration DSL**
    - primarily uses method chaining
    - fits well with the main purpose of the IntegrationFlow builder: creating a flow of message-passing and data transformations
    - also uses function sequencing with lambda expressions for the top-level object to be built (and in some cases also for inner, more-complex method arguments)

## Summary

[Continue to next chapter](README-chapter-11.md)
