# Chapter 8: Collection API Enhancements

* Using collection factories
    - added in Java 9 
    - new additions to simplify creating small lists, sets and maps
* Learning new idiomatic removal and replacement patterns to use with List and Set
* Learning idiomatic convenience patterns to work with Map

Benefits:

- Addresses verbose and error-prone APi to use at times
- Makes life easier

## 8.1 Collection Factories

Needed a better way to create small collection objects. Here’s one old, overly verbose way:

```
List<String> friends = new ArrayList<>();
friends.add("Raphael");
friends.add("Olivia");
friends.add("Thibaut");
```

This is an improvement, which gives you a list you can update but not add/remove, however still requires unnecessary object allocations behind the scenes:

```
List<String> friends
   = Arrays.asList("Raphael", "Olivia", "Thibaut");
```

For a set, this is a trick (no Arrays.asSet method, so use HashSet constructor which takes a List), but still is overly verbose and requires unnecessary object allocation:

```
Set<String> friends "
   = new HashSet<>(Arrays.asList("Raphael", "Olivia", Thibaut"));
```

or via the Streams API:

```
Set<String> friends
   = Stream.of("Raphael", "Olivia", "Thibaut")
           .collect(Collectors.toSet());
```

Note, Collection literals, i.e. `[3, 4, 5]` to create a list of numbers, aren't available in Java 
    - high maintenance cost 
    - restricts future use of a possible syntax
    - Instead, Java 9 adds support by enhancing the Collection API

### 8.1.1 List Factory

List factories are better:

```
List<String> friends = List.of("Raphael", "Olivia", "Thibaut");
```

Note, list is immutable to prevent unwanted mutations of collections, and null elements are disallowed to prevent unexpected bugs and allow a more compact internal representation

#### Overloading vs. varargs

If you further inspect the List interface, you notice several overloaded variants of List.of:

```
static <E> List<E> of(E e1, E e2, E e3, E e4)
static <E> List<E> of(E e1, E e2, E e3, E e4, E e5)
```

You may wonder why the Java API didn’t have one method that uses varargs to accept an arbitrary number of elements (i.e. `static <E> List<E> of(E... elements`)

- Under the hood, the varargs version allocates an extra array, which is wrapped up inside a list. 
- cost for allocating an array, initializing it, and having it garbage-collected later
- By providing a fixed number of elements (up to ten) through an API, you don’t pay this cost. 
- Note that you can still create `List.of` using more than ten elements, but in that case the varargs signature is invoked

#### What about Streams API?

- You saw in previous chapters that you can use the Collectors.toList() collector to transform a stream into a list
- Unless you need to set up some form of data processing and transformation of the data, use the factory methods
    - simpler to use
    - implementation of the factory methods is simpler and more adequate

### 8.1.2 Set Factory

```
Set<String> friends = Set.of("Raphael", "Olivia", "Thibaut");
```

Note, if you try to add a duplicate you'll get `IllegalArgementException`

### 8.1.3 Map Factory

2 ways

'Map.of`:

```
Map<String, Integer> ageOfFriends
   = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);
```

or `Map.ofEntries` if needing to add more than 10 values, in which case it ises varargs and requires additional object allocations to wrap up a key and value:

```
import static java.util.Map.entry;
Map<String, Integer> ageOfFriends
       = Map.ofEntries(entry("Raphael", 30),
                       entry("Olivia", 25),
                       entry("Thibaut", 26));
```

### Quiz 8.1

## 8.2. Working with List and Set

-Java 8 introduced a few methods into the List and Set interfaces: `removeIf`, `replaceAll`, and `sort`
- All mutate collections, so were added to make mutating code less error prone
- replaceAll 

I.E.:

```
for (Transaction transaction : transactions) {
   if(Character.isDigit(transaction.getReferenceCode().charAt(0))) {
       transactions.remove(transaction);
   }
}
```

- this code may result in a `ConcurrentModificationException`

Instead use `removeIf`:

```
transactions.removeIf(transaction ->
     Character.isDigit(transaction.getReferenceCode().charAt(0)));
```

## 8.3 Working with Map

- Java 8 introduced several default methods supported by the `Map` interface
- To help write more concise code leveraging idiomatic patterns

Shiny new forEach

_Old way_

```
for(Map.Entry<String, Integer> entry: ageOfFriends.entrySet()) {
   String friend = entry.getKey();
   Integer age = entry.getValue();
   System.out.println(friend + " is " + age + " years old");
}
```

since Java 8, Map has supported forEach which accepts a `BiConsumer`:

```
ageOfFriends.forEach((friend, age) -> System.out.println(friend + " is " +
     age + " years old"));
```

### Convenient ways to sort

- Entry.comparingByValue
- Entry.comparingByKey

I.E., process the elements of in order based on person's name:

```
Map<String, String> favouriteMovies
       = Map.ofEntries(entry("Raphael", "Star Wars"),
       entry("Cristina", "Matrix"),
       entry("Olivia",
       "James Bond"));

favouriteMovies
  .entrySet()
  .stream()
  .sorted(Entry.comparingByKey())
  .forEachOrdered(System.out::println);  
```

### Note: HashMap and Performance

The internal structure of a HashMap was updated in Java 8 to improve performance
- Entries of a map typically are stored in buckets accessed by the generated hashcode of the key
    - if many keys return the same hashcode, performance deteriorates because buckets are implemented as LinkedLists with O(n) retrieval
    - Nowadays, when the buckets become too big, they’re replaced dynamically with sorted trees, which have O(log(n)) retrieval and improve the lookup of colliding elements

### getOrDefault

```
System.out.println(favouriteMovies.getOrDefault("Olivia", "Matrix")); 
```

### Compute patterns

Sometimes you want to perform an operation conditionally and store the result. Examples include:

- Caching info, i.e. a SHA-256 representation - if alrady calculated, reuse
- Dealing with m aps that store multiple values, so you can ensure to initialize values if absent

Helpers added:

- computeIfAbsent—If there’s no specified value for the given key (it’s absent or its value is null), calculate a new value by using the key and add it to the Map.
- computeIfPresent—If the specified key is present, calculate a new value for it and add it to the Map.
- compute—This operation calculates a new value for a given key and stores it in the Map.

### Remove Patterns

`remove` function saves a lot of code, as we can see in this example of removing an item which involves having to check if it exists:

Old:

```
String key = "Raphael";
String value = "Jack Reacher 2";
if (favouriteMovies.containsKey(key) &&
     Objects.equals(favouriteMovies.get(key), value)) {
   favouriteMovies.remove(key);
   return true;
}
else {
   return false;
}
```

With the `remove` method:

```
favouriteMovies.remove(key, value)
```

### 8.3.6 Replacement Patterns

`Map` has 2 new methods that let you replace entries:

- `replaceAll` — Replaces each entry’s value with the result of applying a BiFunction. This method works similarly to replaceAll on a List, which you saw earlier.
- `Replace` — Lets you replace a value in the Map if a key is present. An additional overload replaces the value only if it the key is mapped to a certain value.

### 8.3.7. Merge

The `merge` method makes it easier to merge the values of multiple Maps, i.e.:

```
Map<String, String> everyone = new HashMap<>(family);
friends.forEach((k, v) ->
   everyone.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));   1
System.out.println(everyone); 
```


## Quiz 8.2

Figure out what the following code does, and think of what idiomatic operation you could use to simplify what it’s doing:

```
Map<String, Integer> movies = new HashMap<>();
movies.put("JamesBond", 20);
movies.put("Matrix", 15);
movies.put("Harry Potter", 5);
Iterator<Map.Entry<String, Integer>> iterator =
            movies.entrySet().iterator();
while(iterator.hasNext()) {
   Map.Entry<String, Integer> entry = iterator.next();
   if(entry.getValue() < 10) {
       iterator.remove();
   }
}
System.out.println(movies);
```

Output: {Matrix=15, JamesBond=20}

## 8.4. IMPROVED CONCURRENTHASHMAP

The `ConcurrentHashMap` class was introduced to provide a more modern HashMap, which is also concurrency friendly. 

ConcurrentHashMap allows concurrent add and update operations that lock only certain parts of the internal data structure. Thus, read and write operations have improved performance compared with the synchronized Hashtable alternative.

### 8.4.1. Reduce and Search

ConcurrentHashMap supports three new kinds of operations, reminiscent of what you saw with streams:

- `forEach` —Performs a given action for each (key, value)
- `reduce` — Combines all (key, value) given a reduction function into a result
- `search` — Applies a function on each (key, value) until the function produces a non-null result

Each kind of operation supports four forms, accepting functions with keys, values, Map.Entry, and (key, value) arguments:

- Operates with keys and values (forEach, reduce, search)
- Operates with keys (forEachKey, reduceKeys, searchKeys)
- Operates with values (forEachValue, reduceValues, searchValues)
- Operates with Map.Entry objects (forEachEntry, reduceEntries, search-Entries)

In this example, you use the reduceValues method to find the maximum value in the map:

```
ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();        1
long parallelismThreshold = 1;
Optional<Integer> maxValue =
   Optional.ofNullable(map.reduceValues(parallelismThreshold, Long::max));
```

Other additions:

- `mappingCount` - to get number of mappings in the map as a long
- `keySet` - to return the `ConcurrentHashMap` as a set

## Summary

[Continue to Chapter 9](README-chapter-09.md)