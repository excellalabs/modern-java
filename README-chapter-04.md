# Chapter 4: Introducing Streams (streams programming with the Java Streams API)

* What is a stream?
* Collections versus streams
* Internal versus external iteration
* Intermediate versus terminal operations

*Collections*
- Most programs make and *process collections of data*, including finding, filtering, grouping, counting
- Most databases let you do these things *declaratively*, so you don't have to implement *how*. 
- Welcome streams, for now you can think of them as *fancy iterators over collections of data*.

4.1 example - before and after

*Parallelism benefits intro*

The threading model is decoupled from the query itself. Because you are providing a recipe for a query, it could be executed sequentially or in parallel. 

## Core concepts

- You chain together several building-block operations to express a complicated data-processing pipeline
- You create a chain by linking sorted, map, and collect operations - **lambdas!**

[**Figure 4.1**](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/04fig01_alt.jpg)

Streams are: 

- Declarative— More concise and readable
- Composable— Greater flexibility
- Parallelizable— Better performance

Rest of chapter...
- patterns such as filtering, slicing, finding, matching, mapping, and reducing
- create streams from different sources, such as from a file
- generate streams with an infinite number of elements

NEXT TIME: 4.2 to the end of chapter 4
