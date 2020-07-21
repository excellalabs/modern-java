# Session 10

_[Recording](https://excella.zoom.us/rec/share/78stI6332URJUIGSxXicWPV9Bp_vaaa81CZP__NbxUzNZVmAGPPMQYpUkkOxfWWW)_

## Agenda

- **Recap** (START RECORING)
    - Went through exercise
    - Last part of chapter 5 
    - Introduced chapter 6
- **Today:** 
    - Chapter 6, start 7
    
- **Next time:** Finish Chapter 7, start 8?

# Chapter 6: Collecting data with streams

Summary 

* Creating and using a collector with the Collectors class
* Reducing streams of data to a single value
* Summarization as a special case of reduction
* Grouping and partitioning data
* Developing your own custom collectors

Overview

- We already used the `collect` terminal operation on streams mainly to combine all the elements of a stream into a List
- In this chapter, you’ll discover that `collect` is a reduction operation, like `reduce`, that takes as an argument various recipes for accumulating the elements of a stream *into a summary result* 
- These recipes are defined by a new Collector interface, so it’s important to distinguish `Collection, Collector, and collect`
- Can get back a map of new keys and values
- _Note_, unique to Java. Not higher level operation like map, flatMap

Here are some example queries you’ll be able to do using `collect` and `collectors`:

- Group a list of transactions by currency to obtain the sum of the values of all transactions with that currency (returning a `Map<Currency, Integer>`)
- Partition a list of transactions into two groups: expensive and not expensive (returning a `Map<Boolean, List<Transaction>>`)
Create multilevel groupings, such as grouping transactions by cities and then further categorizing by whether they’re expensive or not (returning a `Map<String, Map<Boolean, List<Transaction>>>`)

### EXAMPLE 6.1 - *Harder to read than to write!*

## Collectors in a nutshell

**A recipe for how to build a summary of the elements in the stream.**

- Imperative code quickly becomes harder to read, maintain, and modify due to the number of deeply nested loops and conditions required 
- In comparison, the functional-style version, as you’ll discover in section 6.3, can be easily enhanced with an additional collector
- Check out the [Collector docs](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html)

### 6.1.1. Collectors as advanced reductions

- invoking the collect method on a stream triggers a reduction operation (parameterized by a Collector) on the elements of the stream itself
- does internally what you had done iteratively in example 6.1 
- traverses each element of the stream and lets the Collector process them
- Typically, the Collector applies a transforming function to the element. 
    - Quite often this is the identity transformation, which has no effect (for example, as in toList). 
- The function then accumulates the result in a data structure that forms the final output of this process.
- The implementation of the methods of the `Collector` interface defines how to perform a reduction operation on a stream, such as the one in our currency example. 
     - We’ll investigate how to create customized collectors in sections 6.5 and 6.6.

BASIC EXAMPLE: 

```
List<Transaction> transactions =
    transactionStream.collect(Collectors.toList());
```

### 6.1.2. Predefined collectors

We’ll mainly explore the features of the predefined collectors, those that can be created from the factory methods (such as `groupingBy`) provided by the `Collectors` class. 

These offer three main functionalities:

- **Reducing and summarizing stream elements to a single value**
    - combine all items in a stream to a single result
    - handy in a variety of use cases, such as finding the total amount of the transacted values in the list of transactions in the previous example, or summing all calories

```
Comparator<Dish> dishCaloriesComparator =
    Comparator.comparingInt(Dish::getCalories);
Optional<Dish> mostCalorieDish =
    menu.stream()
        .collect(maxBy(dishCaloriesComparator));
```

- **Grouping elements**
    - multiple levels of grouping or combining different collectors to apply further reduction operations on each of the resulting subgroups
- **Partitioning elements**
    - special case of grouping, using a predicate as a grouping function

## 6.2. REDUCING AND SUMMARIZING

### 6.2.2. Summarization

The Collectors class provides a specific factory method for summing: 

`Collectors.summingInt`

```
int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
```

1. While traversing the stream, each dish is mapped into its number of calories
1. That number is added to an accumulator starting from an initial value (in this case the value is 0)

**See Figure 6.2**

Note, if you didn't use the above specialized collector, it would look like this:

```
int totalCalories = menu.stream().collect(reducing(
                                   0, Dish::getCalories, (i, j) -> i + j));
```

It takes three arguments:

- starting value of the reduction operation and will also be the value returned in the case of a stream with no elements
- function to transform a dish into an int representing its calorie content
- BinaryOperator that aggregates two items into a single value of the same type

- `Collectors.summingLong` and Collectors.summingDouble methods behave similarly
- `Collectors.averagingInt`, `averagingLong` and `averagingDouble` are also available to calculate the average of the same set of numbers

```
double avgCalories =
    menu.stream().collect(averagingInt(Dish::getCalories));
```

Often though, you may want to retrieve two or more of these results, and possibly you’d like to do it in a single operation
- In this case, you can use the collector returned by the `summarizingInt` factory method. 
- For example, you can count the elements in the menu and obtain the sum, average, maximum, and minimum of the calories contained in each dish with a single summarizing operation:

```
IntSummaryStatistics menuStatistics =
        menu.stream().collect(summarizingInt(Dish::getCalories));

> IntSummaryStatistics{count=9, sum=4300, min=120,
                     average=477.777778, max=800}
```

This **collector** gathers all that information in a class called `IntSummaryStatistics` that provides convenient getter methods to access the results

The **collector** returned by the `joining` factory method concatenates into a single string, all strings resulting from invoking the toString method on each object in the stream:

```
String shortMenu = menu.stream().map(Dish::getName).collect(joining(", "));

> pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon
```

## 6.2.4. Generalized summarization with reduction

EXAMPLE: find the highest-calorie dish using the one-argument version of reducing as follows:

```
Optional<Dish> mostCalorieDish =
    menu.stream().collect(reducing(
        (d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
```

- you can think of a collector created with the one-argument reducing factory method as a particular case of the three-argument method
- uses the first item in the stream as a starting point
- an identity function (a function that returns its input argument as is) as a transformation function

### Collect vs. reduce

- You may wonder what the differences between the collect and reduce methods of the stream interface are, because often you can obtain the same results using either method
- For instance, you can achieve what is done by the toList Collector using the reduce method as follows:

```
Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();
List<Integer> numbers = stream.reduce(
                               new ArrayList<Integer>(),
                               (List<Integer> l, Integer e) -> {
                                         l.add(e);
                                         return l; },
                               (List<Integer> l1, List<Integer> l2) -> {
                                         l1.addAll(l2);
                                         return l1; });
```

- This solution has two problems: a semantic one and a practical one
- The semantic problem lies in the fact that the reduce method is meant to combine two values and produce a new one; it’s an immutable reduction. 
- In contrast, the collect method is designed to mutate a container to accumulate the result it’s supposed to produce. 
- This means that the previous snippet of code is misusing the reduce method, because it’s mutating in place the List used as accumulator. 
- Using the reduce method with the wrong semantic is also the cause of a practical problem: this reduction process can’t work in parallel, because the concurrent modification of the same data structure operated by multiple threads can corrupt the List itself
- In this case, if you want thread safety, you’ll need to allocate a new List every time, which would impair performance by object allocation. 
- This is the main reason why the collect method is useful for expressing reduction working on a mutable container but crucially in a parallel-friendly way

You can further simplify the previous sum example using the reducing collector by using a reference to the sum method of the Integer class instead of the lambda expression you used to encode the same operation. This results in the following:

```
int totalCalories = menu.stream().collect(reducing(0,        1
                              Dish::getCalories,             2
                              Integer::sum));                3
```

1 Initial value
2 Transformation function
3 Aggregating function



- functional programming in general (and the new API based on functional-style principles added to the Collections framework in Java 8 in particular) often provides multiple ways to perform the same operation
- collectors are somewhat more complex to use than the methods directly available on the Streams interface, but in exchange they offer higher levels of abstraction and generalization and are more reusable and customizable.
- explore the largest number of solutions possible to the problem at hand, but always choose the most specialized one that’s general enough to solve it
- This is often the best decision for both readability and performance reasons. 

For instance, 
- to calculate the total calories in our menu, we’d prefer the following solution because it’s the most concise and most readable. 
- At the same time, it’s also the one that performs best, because IntStream lets us avoid all the auto-unboxing operations, or implicit conversions from Integer to int, that are useless in this case

```
int totalCalories = menu.stream().mapToInt(Dish::getCalories).sum();
```

### Quiz 6.1: Joining strings with reducing

test your understanding of how reducing can be used as a generalization of other collectors

## 6.3 GROUPING

- can be cumbersome, verbose, and error-prone when implemented with an imperative style

```
Map<Dish.Type, List<Dish>> dishesByType =
                      menu.stream().collect(groupingBy(Dish::getType));
```

This will result in the following Map:

```
{FISH=[prawns, salmon], OTHER=[french fries, rice, season fruit, pizza],
 MEAT=[pork, beef, chicken]}
```

- you pass to the groupingBy method a `Function`
- We call this Function a _classification function_ specifically because it’s used to classify the elements of the stream into different groups
- the result of this grouping operation is a Map having as map key the value returned by the classification function, and as corresponding map value a list of all the items in the stream having that classified value
    - In the menu-classification example, a key is the type of dish, and its value is a list containing all the dishes of that type

__Figure 6.4__

It isn’t always possible to use a method reference as a classification function, because you may wish to classify using something more complex than a simple property accessor. 

For example, you could decide to classify as “diet” all dishes with 400 calories or fewer, set to “normal” the dishes having between 400 and 700 calories, and set to “fat” the ones with more than 700 calories:

```
public enum CaloricLevel { DIET, NORMAL, FAT }
Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(
         groupingBy(dish -> {
                if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                else return CaloricLevel.FAT;
         } ));
```

### 6.3.1. Manipulating grouped elements

_Further manipulate grouped elements such above_

Suppose you want to filter only the caloric dishes, let’s say the ones with more than 500 calories. You may argue that in this case you could apply this filtering predicate before the grouping like the following:

```
Map<Dish.Type, List<Dish>> caloricDishesByType =
                    menu.stream().filter(dish -> dish.getCalories() > 500)
                                 .collect(groupingBy(Dish::getType));

> {OTHER=[french fries, pizza], MEAT=[pork, beef]}
```

_Do you see the problem there?_

- Because there is no dish of type FISH satisfying our filtering predicate, that key totally disappeared from the resulting map. 
- To workaround this problem the `Collectors` class overloads the `groupingBy` factory method, with one variant also taking a second argument of type `Collector` along with the usual classification function
- In this way, it’s possible to move the filtering predicate inside this second Collector, as follows:

```
Map<Dish.Type, List<Dish>> caloricDishesByType =
      menu.stream()
          .collect(groupingBy(Dish::getType,
                   filtering(dish -> dish.getCalories() > 500, toList())));

> {OTHER=[french fries, pizza], MEAT=[pork, beef], FISH=[]}
```

- The filtering method is another static factory method of the `Collectors` class accepting a `Predicate` to filter the elements in each group and a further `Collector` that is used to regroup the filtered elements
- In this way, the resulting `Map` will also keep an entry for the FISH type even if it maps an empty List

Another even more common way in which it could be useful to manipulate the grouped elements is transforming them through a mapping function

- the Collectors class provides another `Collector` through the mapping method that accepts a mapping function, and another `Collector` used to gather the elements resulting from the application of that function to each of them
- By using it you can, for instance, convert each Dish in the groups into their respective names in this way:

```
Map<Dish.Type, List<String>> dishNamesByType =
      menu.stream()
          .collect(groupingBy(Dish::getType,
                   mapping(Dish::getName, toList())));
```


Note that in this case each group in the resulting Map is a List of Strings rather than one of Dishes as it was in the former examples. You could also use a third Collector in combination with the groupingBy to perform a flatMap transformation instead of a plain map. To demonstrate how this works let’s suppose that we have a Map associating to each Dish a list of tags as it follows:

```
Map<String, List<String>> dishTags = new HashMap<>();
dishTags.put("pork", asList("greasy", "salty"));
dishTags.put("beef", asList("salty", "roasted"));
dishTags.put("chicken", asList("fried", "crisp"));
dishTags.put("french fries", asList("greasy", "fried"));
dishTags.put("rice", asList("light", "natural"));
dishTags.put("season fruit", asList("fresh", "natural"));
dishTags.put("pizza", asList("tasty", "salty"));
dishTags.put("prawns", asList("tasty", "roasted"));
dishTags.put("salmon", asList("delicious", "fresh"));
```

In case you are required to extract these tags for each group of type of dishes you can easily achieve this using the flatMapping Collector:

```
Map<Dish.Type, Set<String>> dishNamesByType =
   menu.stream()
      .collect(groupingBy(Dish::getType,
               flatMapping(dish -> dishTags.get( dish.getName() ).stream(),
                           toSet())));
```

- Here for each Dish we are obtaining a List of tags. 
- We need to perform a flatMap in order to flatten the resulting two-level list into a single one. 
- Also note that this time we collected the result of the flatMapping operations executed in each group into a Set instead of using a List as we did before, in order to avoid repetitions of same tags associated to more than one Dish in the same type. 
- The Map resulting from this operation is then the following:

```
{MEAT=[salty, greasy, roasted, fried, crisp], FISH=[roasted, tasty, fresh,
     delicious], OTHER=[salty, greasy, natural, light, tasty, fresh, fried]}
```

### 6.3.2. Multilevel grouping

- Until this point we only used a single criterion to group the dishes in the menu, for instance by their type or by calories
- if you want to use more than one criterion at the same time you can compose groupings

- The two arguments `Collectors.groupingBy` factory method can be used also to perform a two-level grouping
- Pass a second inner `groupingBy` to the outer `groupingBy`, defining a second-level criterion to classify the stream’s items:

```
Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel =
menu.stream().collect(
      groupingBy(Dish::getType,                                              1
         groupingBy(dish -> {                                                2
                if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                else return CaloricLevel.FAT;
          } )
      )
);
```

1 First-level classification function
2 Second-level classification function

The result of this two-level grouping is a two-level Map like the following:

```
{MEAT={DIET=[chicken], NORMAL=[beef], FAT=[pork]},
 FISH={DIET=[prawns], NORMAL=[salmon]},
 OTHER={DIET=[rice, seasonal fruit], NORMAL=[french fries, pizza]}}
```

- Here the outer Map has as keys the values generated by the first-level classification function: fish, meat, other
- The values of this Map are in turn other Maps, having as keys the values generated by the second-level classification function: normal, diet, or fat. 
- Finally, the second-level Maps have as values the List of the elements in the stream returning the corresponding first and second-level key values when applied respectively to the first and second classification functions: salmon, pizza, and so on. 
- This multilevel grouping operation can be extended to any number of levels, and an n-level grouping has as a result an n-level Map, modeling an n-level tree structure

It helps to think that `groupingBy` works in terms of “buckets.” 
- The first groupingBy creates a bucket for each key
- You then collect the elements in each bucket with the downstream collector and so on to achieve n-level groupings

### 6.3.3. Collecting data in subgroups

The second collector passed to the first groupingBy can be any type of collector, not just another groupingBy

For instance, it’s possible to count the number of Dishes in the menu for each type, by passing the counting collector as a second argument to the groupingBy collector:

```
Map<Dish.Type, Long> typesCount = menu.stream().collect(
                    groupingBy(Dish::getType, counting()));
```

The result is the following Map:

```
{MEAT=3, FISH=2, OTHER=4}
```

To give another example, you could rework the collector you already used to find the highest calorie dish in the menu to achieve a similar result, but now classified by the type of dish:

```
Map<Dish.Type, Optional<Dish>> mostCaloricByType =
    menu.stream()
        .collect(groupingBy(Dish::getType,
                            maxBy(comparingInt(Dish::getCalories))));
```

The result of this grouping is a `Map`, having as keys the available types of Dishes and as values the Optional<Dish>, wrapping the corresponding highest-calorie Dish for a given type:

```
{FISH=Optional[salmon], OTHER=Optional[pizza], MEAT=Optional[pork]}
```

## 6.4 Partitioning 

- Partitioning is a special case of grouping: having a predicate called a partitioning function as a classification function
- The fact that the partitioning function returns a boolean means the resulting grouping Map will have a Boolean as a key type, and therefore, there can be at most two different groups—one for true and one for false:

```
Map<Boolean, List<Dish>> partitionedMenu =
             menu.stream().collect(partitioningBy(Dish::isVegetarian));
```

Returs map:

```
{false=[pork, beef, chicken, prawns, salmon],
 true=[french fries, rice, season fruit, pizza]}
```

Advantages 

- keeping both lists of the stream elements, for which the application of the partitioning function returns true or false. 
    - In the previous example, you can obtain the List of the nonvegetarian Dishes by accessing the value of the key false in the partitionedMenu Map, using two separate filtering operations: one with the predicate and one with its negation
- as you already saw for grouping, the partitioningBy factory method has an overloaded version to which you can pass a second collector

## The Collector interface - how to implement specific reduction operations

## Summary
