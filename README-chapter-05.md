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

Select and skip elements

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

//todo: rest




[Continue to Chapter 6](README-chapter-06.md)
