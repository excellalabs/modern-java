# Chapter 11: Using Optional as a better alternative to null

* What’s wrong with null references and why you should avoid them
* From null to Optional: rewriting your domain model in a null-safe way
* Putting optionals to work: removing null checks from your code
* Different ways to read the value possibly contained in an optional
* Rethinking programming given potentially missing values

The idea that null is unavoidable may not be true and biased with historical roots

- British computer scientist Tony Hoare introduced null references back in 1965 while designing ALGOL W, one of the first typed programming languages with heap-allocated records, later saying that he did so “simply because it was so easy to implement.”
- Despite his goal “to ensure that all use of references could be absolutely safe, with checking performed automatically by the compiler,” he decided to make an exception for null references because he thought that they were the most convenient way to model the absence of a value.
- After many years, he regretted this decision, calling it “my billion-dollar mistake.” 
- Many other language implemented it, such as because it was easy or standard
- Null pointers are an outlier, we can do better

## 11.1. HOW DO YOU MODEL THE ABSENCE OF A VALUE?

```
public String getCarInsuranceName(Person person) {
    return person.getCar().getInsurance().getName();
}
```

# Session 17

_[Recording (9/28/20)]()_

## Agenda

- **Housekeeping**: notes & code, expensing food, start recording
- **Recap**
    - Finished Chapter 10 remaining DSL patterns, started Chapter 11 - Optionals
- **Today:** 
    - Finish chapter 11, start 12 - New Date and Time API
    
- **Next time:** finish 12, start 13 - Default methods

What is a person doesn't have a car? Or even is the person is null?

### 11.1.1. Reducing NullPointerExceptions with defensive checking

Verbose and error prone null checks, that increase nesting or exit points

### 11.1.2. Problems with null

* It’s a source of error. 
* It bloats your code
* It’s meaningless. It doesn’t have any semantic meaning, and in particular, it represents the wrong way to model the absence of a value in a statically typed language.
* It breaks Java philosophy. Java always hides pointers from developers except in one case: the null pointer.
* It creates a hole in the type system. null carries no type or other information, so it can be assigned to any reference type. This situation is a problem because when null is propagated to another part of the system, you have no idea what that null was initially supposed to be

### 11.1.3. What are the alternatives to null in other languages?

safe navigation operator, represented by ?

```
def carInsuranceName = person?.car?.insurance?.name
```

It's too common to add null checks without really knowing if they're necessary, sweeping issues under the carpet, and this makes that even worse.

## 11.2. INTRODUCING THE OPTIONAL CLASS

Makes checking null part of the type system, and facilitates proper thinking through when something can be null

Java introduced an Optional in Java 8

If you know that a person may not have a car, for example, the car variable inside the Person class shouldn’t be declared type Car and assigned to a null reference when the person doesn’t own a car; instead, it should be type Optional<Car>, so it contains it

When a value is present, the Optional class wraps it. Conversely, the absence of a value is modeled with an empty optional returned by the method Optional.empty

Semantically, it could be seen as the same thing as null, but in practice, the difference is huge. Trying to dereference a null invariably causes a NullPointerException, whereas Optional.empty() is a valid, workable object of type Optional that can be invoked in useful ways.
    - declaring a variable of type Optional<Car> instead of Car clearly signals that a missing value is permitted
    - help you design more-comprehensible APIs so that by reading the signature of a method, you can tell whether to expect an optional value
    - see code example, **Listing 11.4. Redefining the Person/Car/Insurance data model by using Optional**

### 11.3.1. Creating Optional objects

You can create them in several ways:

```
Optional<Car> optCar = Optional.empty(); // Empty
Optional<Car> optCar = Optional.of(car); // From non-nullable value
Optional<Car> optCar = Optional.ofNullable(car); // From nullable value
```

### 11.3.2. Extracting and transforming values from Optionals with map

You can get the value from an Optional with `get` but we will look at ways that avoid explicit tests to avoid null pointer issues, inspired by similar operations on streams

Instead of: 

```
String name = null;
if(insurance != null){
    name = insurance.getName();
}
```

Use:

```
Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
Optional<String> name = optInsurance.map(Insurance::getName);
```

- conceptually similar to the map method of Stream you saw in chapters 4 and 5
- You could also think of an Optional object as being a particular collection of data, containing at most a single element. If the Optional contains a value, the function passed as argument to map transforms that value. If the Optional is empty, nothing happens

### 11.3.3. Chaining Optional objects with flatMap

Similarly, rewritting our original null-plagued code from before:

```
public String getCarInsuranceName(Person person) {
    return person.getCar().getInsurance().getName();
}
```

What's wrong with doing it this way?

```
Optional<Person> person = Optional.of(person);
Optional<String> name =
    person.map(Person::getCar)
             .map(Car::getInsurance)
             .map(Insurance::getName);
```

The variable optPerson is of type Optional<Person>, so it’s perfectly fine to call the map method. But getCar returns an object of type Optional<Car> (as presented in listing 11.4), which means that the result of the map operation is an object of type Optional<Optional<Car>>

To solve, you can look at a pattern you’ve used previously with streams: the flatMap method. `flatMap` has the effect of replacing each generated stream with the contents of that stream (see _Figure 11.4. Comparing the flatMap methods of Stream and Optional_)

So knowing that, this is the way to rewrite the code:

```
public String getCarInsuranceName(Optional<Person> person) {
    return person.flatMap(Person::getCar)
                 .flatMap(Car::getInsurance)
                 .map(Insurance::getName)
                 .orElse("Unknown"); // A default value if the resulting Optional is empty
}
```

- You can obtain what you want with an easily comprehensible statement instead of increasing the code complexity with conditional branches
- Allows you to make explicit through the type system something that otherwise would remain implicit in your knowledge of the domain model
    - the first purpose of a language, even a programming language, is communication
- Note, Optionals aren't meant to be serializable, so if you expose a non-optional version for this reason, consider the optional version internally, i.e. a getCarAsOptional field

### 11.3.4. Manipulating a stream of optionals

- The Optional’s stream() method, introduced in Java 9, allows you to convert an Optional with a value to a Stream containing only that value or an empty Optional to an equally empty Stream
- Particularly convenient in a common case: when you have a Stream of Optional and need to transform it into another Stream containing only the values present in the nonempty Optional of the original Stream

Suppose that you’re required to implement a method that’s passed with a List<Person> and that should return a Set<String> containing all the distinct names of the insurance companies used by the people in that list who own a car.

This example results in a long chain:

```
public Set<String> getCarInsuranceNames(List<Person> persons) {
    return persons.stream()
                  .map(Person::getCar)                                 1
                  .map(optCar -> optCar.flatMap(Car::getInsurance))    2
                  .map(optIns -> optIns.map(Insurance::getName))       3
                  .flatMap(Optional::stream)                           4
                  .collect(toSet());                                   5
}
```

1 Convert the list of persons into a Stream of Optional<Car> with the cars eventually owned by them.
2 FlatMap each Optional<Car> into the corresponding Optional<Insurance>.
3 Map each Optional<Insurance> into the Optional<String> containing the corresponding name.
4 Transform the Stream<Optional<String>> into a Stream<String> containing only the present names.
5 Collect the result Strings into a Set to obtain only the distinct values.

- It has an additional complication
    - Each element is also wrapped into an Optional
    - You have to get rid of the empty Optionals and unwrapping the values contained in the remaining ones before collecting the results into a Set
- Use the stream() method of the Optional class instead

```
Stream<Optional<String>> stream = ...
Set<String> result = stream.filter(Optional::isPresent)
                           .map(Optional::get)
                           .collect(toSet());
```

- This method transforms each Optional into a Stream with zero or one elements, depending on whether the transformed Optional is empty
- For this reason, a reference to that method can be seen as a function from a single element of the Stream to another Stream and then passed to the flatMap method invoked on the original Stream
- In this way each element is converted to a Stream and then the two-level Stream of Streams is flattened into a single-level one. This trick allows you to unwrap the Optionals containing a value and skip the empty ones in only one step.

### 11.3.5. Default actions and unwrapping an Optional

Optional class provides several instance methods to read the value contained by an Optional instance:

- `get` - simplest but least safe as throws exception if no element present, so should only be used if a value is ensured
- `orElse` - provide default value when optional doesn't have one
- `orElseGet` - lazy version of `orElse`, because supplier is only invoked if optional contains no value, used when vial - supplier is time-consuming
- `or` - like `orElseGet` but doesn't unwrap the Optional if present, so doesn't perform an action
- `orElseThrow` - like `get` but you can choose the exception
- `ifPresent` - lets you execute the action given as argument if a value is present, otherwise no action taken
- ifPresentOrElse (Java 9) - differs from ifPresent by taking a Runnable that gives an empty-based action to be executed when the Optional is empty

### 11.3.6. Combining two Optionals

Suppose that you have a method that, given a Person and a Car, queries some external services and implements some complex business logic to find the insurance company that offers the cheapest policy for that combination:

```
public Optional<Insurance> nullSafeFindCheapestInsurance(
                              Optional<Person> person, Optional<Car> car) {
    if (person.isPresent() && car.isPresent()) {
        return Optional.of(findCheapestInsurance(person.get(), car.get()));
    } else {
        return Optional.empty();
    }
}
```

Looks too much like old null checking way, so use map and flatMap:

```
public Optional<Insurance> nullSafeFindCheapestInsurance(
                              Optional<Person> person, Optional<Car> car) {
    return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
}
```

- You invoke a flatMap on the first optional, so if this optional is empty, the lambda expression passed to it won’t be executed, and this invocation will return an empty optional
- Conversely, if the person is present, flatMap uses it as the input to a Function returning an Optional<Insurance> as required by the flatMap method. The body of this function invokes a map on the second optional, so if it doesn’t contain any Car, the Function returns an empty optional, and so does the whole nullSafeFindCheapestInsurance method
- Finally, if both the Person and the Car are present, the lambda expression passed as an argument to the map method can safely invoke the original findCheapestInsurance method with them.

### 11.3.7. Rejecting certain values with filter

- The analogies between the Optional class and the Stream interface aren’t limited to the map and flatMap methods. A third method, filter, behaves in a similar fashion on both classes, in that if the optional is already empty, it doesn’t have any effect; otherwise, it applies the predicate to the value contained in the optional

_Quiz 11.2: Filtering an optional_

_Table 11.1 summarizes the methods of the Optional class_

## 11.4. PRACTICAL EXAMPLES OF USING OPTIONAL

- Involves a rethink of how you deal with potentially missing values, in the code you write and in interacting with the Java APIs
- For backward-compatibility reasons, old Java APIs can’t be changed to make proper use of optionals, but all is not lost. 
- You can fix, or at least work around, this issue by adding to your code small utility methods that allow you to benefit from the power of optionals...

### 11.4.1. Wrapping a potentially null value in an Optional

When a method returns null, wrap the value they return with an optional, when you want to safely transform a value that could be null into an optional:

```
Optional<Object> value = Optional.ofNullable(map.get("key"))
```

### 11.4.2. Exceptions vs. Optional

Throwing an exception is another common alternative in the Java API to returning a null when a value can’t be provided

I.E. `parseInt` throws a NumberFormatException if it can't convert to Int. Implement a tiny utility (such as in an OptionalUtility class) method wrapping it, returning an Optional, i.e.:

```
public static Optional<Integer> stringToInt(String s) {
    try {
        return Optional.of(Integer.parseInt(s));         1
    } catch (NumberFormatException e) {
        return Optional.empty();                         2
    }
}
```

### 11.4.3. Primitive optionals and why you shouldn’t use them

Note that like streams, optionals also have primitive counterparts—OptionalInt, Optional-Long, and OptionalDouble—so the method in listing 11.7 could have returned Optional-Int instead of Optional<Integer>

We encouraged the use of primitive streams (especially when they could contain a huge number of elements) for performance reasons, but because an Optional can have at most a single value, that justification doesn’t apply here.
    - We discourage using primitive optionals because they lack the map, flatMap, and filter methods
    - as happens for streams, an optional can’t be composed with its primitive counterpart, so if the method of listing 11.7 returned OptionalInt, you couldn’t pass it as a method reference to the flatMap method of another optional

### 11.4.4. Putting it all together

Read exercise and do related quiz. Code is in the file, [OptionalHarness.java](src/main/java/com/excella/reactor/stocks/OptionalHarness.java).

## Summary

[Continue to next chapter]()