# Chapter 5: Collecting data with streams

* Creating and using a collector with the Collectors class
* Reducing streams of data to a single value
* Summarization as a special case of reduction
* Grouping and partitioning data
* Developing your own custom collectors

- We already used the `collect` terminal operation on streams mainly to combine all the elements of a stream into a List
- In this chapter, you’ll discover that `collect` is a reduction operation, like `reduce`, that takes as an argument various recipes for accumulating the elements of a stream *into a summary result* 
- These recipes are defined by a new Collector interface, so it’s important to distinguish `Collection, Collector, and collect`

Here are some example queries you’ll be able to do using `collect` and `collectors`:

- Group a list of transactions by currency to obtain the sum of the values of all transactions with that currency (returning a `Map<Currency, Integer>`)
- Partition a list of transactions into two groups: expensive and not expensive (returning a `Map<Boolean, List<Transaction>>`)
Create multilevel groupings, such as grouping transactions by cities and then further categorizing by whether they’re expensive or not (returning a `Map<String, Map<Boolean, List<Transaction>>>`)

### EXAMPLE 6.1 - *Harder to read than to write!*

## Collectors in a nutshell

- a recipe for how to build a summary of the elements in the stream
- imperative code quickly becomes harder to read, maintain, and modify due to the number of deeply nested loops and conditions required 
- In comparison, the functional-style version, as you’ll discover in section 6.3, can be easily enhanced with an additional collector

## 6.1.1. Collectors as advanced reductions

- invoking the collect method on a stream triggers a reduction operation (parameterized by a Collector) on the elements of the stream itself
- does internally what you had done iteratively in example 6.1 
- traverses each element of the stream and lets the Collector process them

//todo: rest

