# Chapter 7: Parallel data processing and performance

## Summary 

* Processing data in parallel with parallel streams
* Performance analysis of parallel streams
* The fork/join framework
* Splitting a stream of data using a Spliterator

## Chapter Overview 

- In the last three chapters, you’ve seen how the new Streams interface lets you *manipulate collections of data in a declarative way*
- the *shift from external to internal iteration* enables the native Java library to gain control over processing the elements of a stream
- relieves Java developers from explicitly implementing optimizations necessary to speed up the processing of collections of data
- the most important benefit is the possibility of executing a pipeline of operations on these collections that automatically makes use of the multiple cores on your computer
- It's easy to declaratively turn a sequential stream into a parallel one

Old way of parallizing

1. explicitly split the data structure containing your data into subparts
1. assign each of these subparts to a different thread 
1. synchronize them opportunely to avoid unwanted race conditions
1. wait for the completion of all threads
1. combine the partial results

    *Note, Java 7 introduced a framework called fork/join to perform these operations more consistently and in a less error-prone way*

Under the hood

- see how parallel streams work under the hood by employing the fork/join framework introduced in Java 7
- it’s important to know how parallel streams work internally, because if you ignore this aspect, you could obtain unexpected (and likely wrong) results by misusing them
- we’ll see that the way a parallel stream gets divided into chunks, before processing the different chunks in parallel, can in some cases be the origin of these incorrect and apparently unexplainable results
- For this reason, you’ll learn how to take control of this splitting process by implementing and using your own Spliterator

## 7.1 Parallel Streams

- You can turn a collection into a **parallel stream** by using the **parellelStream** method in place of stream
- A parallel stream splits its elements into **multiple chunks**, processing each on a **different thread**
- So, you can automatically partition the workload on all the cores of your multicore processor, keeping all of them equally busy

**EXAMPLE:** 

Write a method accepting a number n as argument and returning the sum of the numbers from one to n. 

A straightforward (perhaps naïve) approach is to generate an infinite stream of numbers, limiting it to the passed numbers, and then reduce the resulting stream with a BinaryOperator that sums two numbers, as follows:

```
public long sequentialSum(long n) {
    return Stream.iterate(1L, i -> i + 1)
                 .limit(n)
                 .reduce(0L, Long::sum);
}
```

In more traditional Java terms, this code is equivalent to its iterative counterpart:

```
public long iterativeSum(long n) {
    long result = 0;
    for (long i = 1L; i <= n; i++) {
        result += i;
    }
    return result;
}
```

This operation is a good candidate to use parallelization, especially for large values of n.

Where do you start? Do you synchronize on the result variable? How many threads do you use? Who does the generation of numbers? Who adds them up?

Don’t worry about all of this. It’s a much simpler problem to solve if you adopt parallel streams!

7.1.1. Turning a sequential stream into a parallel one

You can make the former functional reduction process (summing) run in parallel by turning the stream into a parallel one; call the method parallel on the sequential stream:

```
public long parallelSum(long n) {
    return Stream.iterate(1L, i -> i + 1)
                 .limit(n)
                 .parallel()
                 .reduce(0L, Long::sum);
}
```

- the stream is now internally divided into multiple chunks
- so the reduction operation can work on the various chunks independently and in parallel
- the same reduction operation combines the values resulting from the partial reductions of each substream, producing the result of the reduction process on the whole initial stream
- expect a significant performance improvement when running on a multicore processor

*[DIAGRAM](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/07fig01_alt.jpg)*

Internally, 

- a boolean flag is set to signal that you want to run in parallel all the operations that follow the invocation to parallel
- Similarly, you can turn a parallel stream into a sequential one by invoking the method sequential on it
- the last call to parallel or sequential wins and affects the pipeline globally, so you using both in the same pipeline won't help achieve greater control

## Configuring the thread pool used by parallel streams

- you may wonder where the threads used by the parallel stream come from, how many there are, and how you can customize the process
- `ForkJoinPool` is used internally, which by default has as many threads as you have processors, as returned by `Runtime.getRuntime().available-Processors()`
- You **can change the size of this pool**:

```
System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism",
     "12");
```

- this is a global setting, so it will affect all the parallel streams in your code
- you can't specify this for a single parallel stream
- the default is meaningful and shouldn't be changed without good reason

# Session 11

_[Recording](https://excella.zoom.us/rec/play/78V4d7quq243S92dsQSDAqUvW9S1L6us1SAX_PcLmEi1AXACZ1qgYrMRZufM9AmYloDFn7xnhc9UyGUj?startTime=1595885606000)_

## Agenda

- **Recap** (START RECORING)
    - Finished chapter 6
    - Started chapter 7
- **Today:** 
    - Chapter 7
    
- **Next time:** Finish 7, start/finish 8

## 7.1.2 Measuring stream performance

How to measure?

- You can use **Java Microbenchmark Harness (JMH)** to create reliable microbenchmarks for languages targeting the JVM. 
- There are many factors to consider like warm-up time required by HotSpot to optimize the bytecode, and the overhead introduced by the garbage collector
- Add JMH dependencies to your application: `jmh-core` and `jmh-generator-annprocess` from `org.openjdk.jmh`, and plug-in

Then you can benchmark a method like this:

```
@BenchmarkMode(Mode.AverageTime)                     1
@OutputTimeUnit(TimeUnit.MILLISECONDS)               2
@Fork(2, jvmArgs={"-Xms4G", "-Xmx4G"})               3
public class ParallelStreamBenchmark {
    private static final long N= 10_000_000L;

    @Benchmark                                       4
    public long sequentialSum() {
        return Stream.iterate(1L, i -> i + 1).limit(N)
                     .reduce( 0L, Long::sum);
    }

    @TearDown(Level.Invocation)                      5
    public void tearDown() {
        System.gc();
    }
}
```

1. Measures the average time taken to the benchmarked method
1. Prints benchmark results using milliseconds as time unit
1. Executes the benchmark 2 times to increase the reliability of 1. results, with 4Gb of heap space
1. The method to be benchmarked
1. Tries to run the garbage collector after each iteration of the 1. benchmark

When you compile this class, a second JAR file named benchmarks is generated that you can run as follows:

```
java -jar ./target/benchmarks.jar ParallelStreamBenchmark
```

- They configured the benchmark to use an oversized heap to avoid any influence of the garbage collector as much as possible
- For the same reason, they tried to enforce the garbage collector to run after each iteration of the benchmark 
- Despite all these precautions, it has to be noted that the results should be taken with a grain of salt as many factors will influence the execution time, such as how many cores your machine supports

When launched, 

- JMH executes 20 warm-up iterations of the benchmarked method to allow HotSpot to optimize the code, and then 20 more iterations that are used to calculate the final result 
- These 20+20 iterations are the default behavior of JMH, but you can change both values either through other JMH specific annotations or, even more conveniently, by adding them to the command line using the -w and -i flags

Executing it on a computer equipped with an Intel i7-4600U 2.1 GHz quad-core, it prints the following result:

```
Benchmark                                Mode  Cnt    Score    Error  Units
ParallelStreamBenchmark.sequentialSum    avgt   40  121.843 ±  3.062  ms/op
```

You should expect that the iterative version using a traditional for loop runs much faster because it works at a much lower level and, more importantly, doesn’t need to perform any boxing or unboxing of the primitive values:

```
@Benchmark
public long iterativeSum() {
    long result = 0;
    for (long i = 1L; i <= N; i++) {
        result += i;
    }
    return result;
}
```

```
Benchmark                                Mode  Cnt    Score    Error  Units
ParallelStreamBenchmark.iterativeSum     avgt   40    3.278 ±  0.192  ms/op
```

- The iterative version is almost 40 times faster than the one using the sequential stream for the reasons we anticipated

Let’s do the same with the version using the parallel stream, also adding that method to our benchmarking class. We obtained the following outcome:

```
Benchmark                                Mode  Cnt    Score    Error  Units
ParallelStreamBenchmark.parallelSum      avgt   40  604.059 ± 55.288  ms/op
```

The parallel version of the summing method isn’t taking any advantage of our quad-core CPU and is around five times slower than the sequential one. How can you explain this unexpected result? Two issues are mixed together:

- iterate generates boxed objects, which have to be unboxed to numbers before they can be added.
- iterate is difficult to divide into independent chunks to execute in parallel.
    - you have to keep a mental model that some stream operations are more parallizable than others
    - the `iterate` operation is hard to split into chunks that can be executed independently, because the input of one function application always depends on the result of the previous, shown in this [diagram](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/07fig02.jpg)

So, in this case:

- the reduction process isn’t proceeding as depicted in figure 7.1: the whole list of numbers isn’t available at the beginning of the reduction process, making it impossible to efficiently partition the stream in chunks to be processed in parallel
- By flagging the stream as parallel, you’re adding the overhead of allocating each sum operation on a different thread to the sequential processing
- This demonstrates how parallel programming can be tricky and sometimes counterintuitive 
- When misused (for example, using an operation that’s not parallel-friendly, like iterate) it can worsen the overall performance of your programs, 
- So, it’s mandatory to understand what happens behind the scenes when you invoke the parallel method

## Using more specialized methods

How can you use your multicore processors and streams to perform a parallel sum in an efficient way?

`LongStream.rangeClosed` was mentioned in Chapter 5. This method has 2 benefits compared to `iterate`:

- It works on primitive long numbers directly so there’s no boxing and unboxing overhead
- It produces ranges of numbers, which can be easily split into independent chunks 
- For example, the range 1–20 can be split into 1–5, 6–10, 11–15, and 16–20

Example: 

```
@Benchmark
public long rangedSum() {
    return LongStream.rangeClosed(1, N)
                     .reduce(0L, Long::sum);
}
```

The time output is:

```
Benchmark                                Mode  Cnt    Score    Error  Units
ParallelStreamBenchmark.rangedSum        avgt   40    5.315 ±  0.285  ms/op
```

- much faster than earlier sequential version because boxing is avoided
- choosing the right data structures if often more important than parallelizing the algorithm 
- this approach will also allow the parallel version to be faster than the sequential version
- so, using the right data structure *and* making it work in parallel yields the best performance
- note, this way is also 20% faster than the imperative counterpart, demonstrating functional style allows us to use parallelism of multicore CPUs in a simpler way that can yield better performance

Parallelism overhead

- keep in mind it's not free
- parallelization process itself requires:
    - recursively partition the stream 
    - assign the reduction operation of each substream to a different thread
    - then combine the results of these operations in a single value 
- moving data between multiple cores is also more expensive than you might expect
- it’s important that work to be done in parallel on another core takes longer than the time required to transfer the data from one core to another 
- in general, there are many cases where it isn’t possible or convenient to use parallelization

## 7.1.3 Using parallel streams correctly

Before you use a parallel stream to make your code faster, you have to be sure that you’re using it correctly; it’s not helpful to produce a result in less time if the result will be wrong. Let’s look at a common pitfall.

Main cause of parallel stream misuse is the use of algorithms that mutate some shared state, i.e.:

```
public long sideEffectSum(long n) {
    Accumulator accumulator = new Accumulator();
    LongStream.rangeClosed(1, n).forEach(accumulator::add);
    return accumulator.total;
}
public class Accumulator {
    public long total = 0;
    public void add(long value) { total += value; }
}
```

- quite common
- irretrievably broken because it’s fundamentally sequential
- data race on every access 
- if fixed with synchronization, you’ll lose all parallelism

### EXAMPLE: run `SideEffect parallel sum`

- each execution returns a different result
- all distant from the correct value of 50000005000000
- multiple threads are concurrently accessing the accumulator 
- executing total += value
- despite its appearance, isn’t an atomic operation
- method invoked inside the forEach block has the side effect of changing the mutable state of an object shared among multiple threads
- avoid shared mutable state

# Session 12

_[Recording](https://excella.zoom.us/rec/play/78V4d7quq243S92dsQSDAqUvW9S1L6us1SAX_PcLmEi1AXACZ1qgYrMRZufM9AmYloDFn7xnhc9UyGUj?startTime=1595885606000)_

## Agenda

- **Recap** (START RECORING)
    - Got to here in chapter 7
- **Today:** 
    - Finish chapter 7, do 8 if time
    
- **Next time:** Finish 8, start 9

## 7.1.4 Using parallel streams effectively

- Can't make one prescription like, use parallel on any dataset of a particular size
- Depends on context

There is qualitative advice:

- Measure
- Watch out for boxing
- Some operations aren't natural for parallel, like `limit` and `findFirst`, that rely on order of the elements
    - You can turn an ordered stream into an unordered stream by invoking the method unordered on it
    - For instance, if you need N elements of your stream and you’re not necessarily interested in the first N ones, calling limit on an unordered parallel stream may execute more efficiently 
- Consider total computation cost of pipeline
    -  With N being the number of elements to be processed and Q the approximate cost of processing one of these elements through the stream pipeline, the product of N*Q gives a rough qualitative estimation of this cost
    - A higher value for Q implies a better chance of good performance when using a parallel stream
- For a small amount of data, choosing a parallel stream is almost never a winning decision 
    - The advantages of processing in parallel only a few elements aren’t enough to compensate for the additional cost introduced by the parallelization process
- Take into account how well the data structure underlying the stream decomposes
    - For instance, an ArrayList can be split much more efficiently than a LinkedList, because the first can be evenly divided without traversing it
    - Also, the primitive streams created with the range factory method can be decomposed quickly
    - Finally, as you’ll learn in section 7.3, you can get full control of this decomposition process by implementing your own Spliterator
- The characteristics of a stream, and how the intermediate operations through the pipeline modify them, can change the performance of the decomposition process
    - For example, a SIZED stream can be divided into two equal parts, and then each part can be processed in parallel more effectively, but a filter operation can throw away an unpredictable number of elements
- Consider whether a terminal operation has a cheap or expensive merge step (for example, the combiner method in a Collector)
    - the cost caused by the combination of the partial results generated by each substream can outweigh the performance benefits of a parallel stream

### See Table 7.1. Stream sources and decomposability
 
## 7.2. THE FORK/JOIN FRAMEWORK

- designed to recursively split a parallelizable task into smaller tasks and then combine the results of each subtask to produce the overall result
- implementation of the ExecutorService interface, which distributes those subtasks to worker threads in a thread pool
- it's called `ForkJoinPool`

### 7.2.1. Working with RecursiveTask

_How to define a task and subtask_

To submit tasks to this pool, you have to create a subclass of `RecursiveTask<R>`, where R is the type of the result produced by the parallelized task (and each of its subtasks) or of RecursiveAction if the task returns no result (it could be updating other nonlocal structures, though). 

To define `RecursiveTasks` you need only implement its single abstract method, compute:

```
protected abstract R compute();
```

This method defines 
- logic of splitting the task at hand into subtasks 
- the algorithm to produce the result of a single subtask when it’s no longer possible or convenient to further divide it

For this reason an implementation of this method often resembles the following pseudocode:

```
if (task is small enough or no longer divisible) {
    compute task sequentially
} else {
    split task in two subtasks
    call this method recursively possibly further splitting each subtask
    wait for the completion of all subtasks
    combine the results of each subtask
}
```

- no precise criteria for deciding whether a given task should be further divided or not, but there are various heuristics that you can follow to help you with this decision

[DIAGRAM](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/07fig03_alt.jpg)


// todo
