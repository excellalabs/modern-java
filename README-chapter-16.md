# Chapter 16. CompletableFuture: composable asynchronous programming

**SESSION 23**

_[Recording ()]()_

**Agenda**

- **Housekeeping**: notes & code, expensing food, start recording
- **Recap**
    - Finished chapter 15, started 16 - CompleteableFutures and Reactive-style programming
- **Today:** 
    - Do chapter 16, maybe start 17
    - Areas people thought were interesting, hard to understand 
- **Next time:** 16 - CompletableFuture: composable asynchronous programming, start 17 - Reactive programming

This chapter covers

- Creating an asynchronous computation and retrieving its result
- Increasing throughput by using nonblocking operations
- Designing and implementing an asynchronous API
- Consuming asynchronously a synchronous API
- Pipelining and merging two or more asynchronous operations
- Reacting to the completion of an asynchronous operation


## 16.1. SIMPLE USE OF FUTURES

- The Future interface was introduced in Java 5 to model a result made available at some point in the future
- A query to a remote service won’t be available immediately when the caller makes the request, for example. 
- The Future interface models an asynchronous computation and provides a reference to its result that becomes available when the computation itself is completed
- Triggering a potentially time-consuming action inside a Future allows the caller Thread to continue doing useful work instead of waiting for the operation’s result
- Friendlier than working with lower-level Threads

_Listing 16.1. Executing a long-lasting operation asynchronously in a Future_

```
ExecutorService executor = Executors.newCachedThreadPool();         1
Future<Double> future = executor.submit(new Callable<Double>() {    2
        public Double call() {
            return doSomeLongComputation();                         3
        }});
doSomethingElse();                                                  4
try {
    Double result = future.get(1, TimeUnit.SECONDS);                5
} catch (ExecutionException ee) {
    // the computation threw an exception
} catch (InterruptedException ie) {
    // the current thread was interrupted while waiting
} catch (TimeoutException te) {
    // the timeout expired before the Future completion
}
```

- 1 Create an ExecutorService allowing you to submit tasks to a - thread pool.
- 2 Submit a Callable to the ExecutorService.
- 3 Execute a long operation asynchronously in a separate thread.
- 4 Do something else while the asynchronous operation is - progressing.
- 5 Retrieve the result of the asynchronous operation, blocking if it isn’t available yet but waiting for 1 second at most before timing out.

-[Figure 16.1. Using a Future to execute a long operation asynchronously](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/16fig01_alt.jpg)_


### 16.1.1. Understanding Futures and their limitations

- Hard to write consise, concurrent code, for example to express dependencies among results of a Future

Declaratively, it’s easy to specify, “When the result of the long computation is available, please send its result to another long computation, and when that’s done, combine its result with the result from another query.” 

Implementing this specification with the operations available in a Future is a different story, which is why it would be useful to have more declarative features in the implementation, such as these:

- Combining two asynchronous computations both when they’re independent and when the second depends on the result of the first
- Waiting for the completion of all tasks performed by a set of Futures
- Waiting for the completion of only the quickest task in a set of Futures (possibly because the Futures are trying to calculate the same value in different ways) and retrieving its result
- Programmatically completing a Future (that is, by providing the result of the asynchronous operation manually)
- Reacting to a Future completion (that is, being notified when the completion happens and then being able to perform a further action with the result of the Future instead of being blocked while waiting for its result)


### 16.1.2. Using CompletableFutures to build an asynchronous application

To explore the CompletableFuture features, in this section you incrementally develop a best-price-finder application that contacts multiple online shops to find the lowest price for a given product or service. Along the way, you learn several important skills:

- How to provide an asynchronous API for your customers (useful if you’re the owner of one of the online shops).
- How to make your code nonblocking when you’re a consumer of a synchronous API. 
    - You discover how to pipeline two subsequent asynchronous operations, merging them into a single asynchronous computation. 
    - This situation arises, for example, when the online shop returns a discount code along with the original price of the item you wanted to buy. You have to contact a second remote discount service to find out the percentage discount associated with this discount code before calculating the actual price of that item.
- How to reactively process events representing the completion of an asynchronous operation and how doing so allows the best-price-finder application to constantly update the best-buy quote for the item you want to buy as each shop returns its price, instead of waiting for all the shops to return their respective quotes. This skill also averts the scenario in which the user sees a blank screen forever if one of the shops’ servers is down.

## 16.2. IMPLEMENTING AN ASYNCHRONOUS API

Homework, implement it in this solution

Let's start off...
