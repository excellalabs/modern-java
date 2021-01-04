**SESSION 24**

_[Recording]()_

**Agenda**

- **Housekeeping**: notes & code, expensing food, start recording
- **Recap**
    - Went over [Other Key Reactive Resources](https://github.com/excellalabs/modern-java/blob/master/README-chapter-15.md#other-key-reactive-resources)
- **Today:** 
    - Go over _Other Key Reactive Resources_
    - _Review_ chapter 16 content, start 17
        - Areas people thought were interesting, hard to understand 
    - Chapter 16 Code exercise (16.2)
- **For Next time:** 
    - We will quickly review through 16 code exercise
    - Finish reading Chapter 17 (Reactive programming)
    - Try 17.2.1 code exercise 
    - Start reading chapter 18
    
# Chapter 16. CompletableFuture: composable asynchronous programming

This chapter covers

- Creating an asynchronous computation and retrieving its result [CODE]
- Increasing throughput by using non-blocking operations
- Designing and implementing an asynchronous API [CODE]
- Consuming asynchronously a synchronous API [CODE]
- Pipelining and merging two or more asynchronous operations [CODE]
- Reacting to the completion of an asynchronous operation [CODE]


## 16.1. SIMPLE USE OF FUTURES

We started with futures...

- The `Future` interface was introduced in Java 5 to model a result made available at some point in the future
- For example, a query to a remote service won’t be available immediately when the caller makes the request 
- The Future interface models an asynchronous computation and provides a reference to its result that becomes available when the computation itself is completed
    - Triggering a potentially time-consuming action inside a Future allows the caller Thread to continue doing useful work instead of waiting for the operation’s result
- Friendlier than working with lower-level Threads

-[Figure 16.1. Using a Future to execute a long operation asynchronously](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/16fig01_alt.jpg)_

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

- 1 Create an ExecutorService allowing you to submit tasks to a thread pool.
- 2 Submit a Callable to the ExecutorService.
- 3 Execute a long operation asynchronously in a separate thread.
- 4 Do something else while the asynchronous operation is progressing.
- 5 Retrieve the result of the asynchronous operation, blocking if it isn’t available yet but waiting for 1 second at most before timing out.

### 16.1.1. Understanding Futures and their limitations

- Hard to write concise, concurrent code, for example to express dependencies among results of a Future
- Conversely, declaratively, it’s easy to specify, “When the result of the long computation is available, please send its result to another long computation, and when that’s done, combine its result with the result from another query.” 
- It would be useful to have more declarative features in the implementation than Futures have, such as these:
    - Combining two asynchronous computations both when they’re independent and when the second depends on the result of the first
    - Waiting for the completion of all tasks performed by a set of Futures
    - Waiting for the completion of only the quickest task in a set of Futures (possibly because the Futures are trying to calculate the same value in different ways) and retrieving its result
    - Programmatically completing a Future (that is, by providing the result of the asynchronous operation manually)
    - Reacting to a Future completion (that is, being notified when the completion happens and then being able to perform a further action with the result of the Future instead of being blocked while waiting for its result)

Let's explore how `CompleteableFutures` offers some of these features...
 
### 16.1.2. Using CompletableFutures to build an asynchronous application

To explore the `CompletableFuture` features, in this section you incrementally develop a best-price-finder application that contacts multiple online shops to find the lowest price for a given product or service. 

Along the way, you learn several **important skills**:

- How to **provide an asynchronous API** for your customers (useful if you’re the owner of one of the online shops).
- How to **make your code nonblocking when you’re a consumer of a synchronous API**. 
- How to **pipeline two subsequent asynchronous operations, merging them into a single asynchronous computation**. 
    - The situation is, the online shop returns a discount code along with the original price of the item you wanted to buy. You have to contact a second remote discount service to find out the percentage discount associated with this discount code before calculating the actual price of that item.
- Learn how to **reactively process events representing the completion of an asynchronous operation** 
    - allows the best-price-finder application to constantly update the best-buy quote for the item you want to buy as each shop returns its price, instead of waiting for all the shops to return their respective quotes. 
    -  also averts the scenario in which the user sees a blank screen forever if one of the shops’ servers is down

## 16.2. IMPLEMENTING AN ASYNCHRONOUS API

Homework, follow steps in this section, and implement it in this solution (see section just above for review)

1. 16.2.1. Converting a synchronous method into an asynchronous one 
      
    For the purposes of learning how to design an asynchronous API, pretend to be on the other side of the barricade. You’re a wise shop owner who realizes how painful this synchronous API is for its users, and you want to rewrite it as an asynchronous API to make your customers’ lives easier. We're simulating when the consumer of this API invokes this method, it remains blocked and then is idle for 1 second while waiting for its synchronous completion. 
            
    Wrap synchronous API:
    1. Create the CompletableFuture that will contain the result of the computation.
    1. Execute the computation asynchronously in a different Thread.
    1. Set the value returned by the long computation on the Future when it becomes available.
    1. Return the Future without waiting for the computation of the result it contains to be completed.
    
    Shop client: 
    1. Query the shop to retrieve the price of a product.
    1. Read the price from the Future or block until it becomes available.

    _(solution in branch `exercise-16.2.1`)_
    
1. 16.2.2. Dealing with errors 

    Key points:
    - clients should use overloaded `get` method to pass timeout
    - propagate the Exception that caused the problem inside the `CompletableFuture` through its `completeExceptionally` method
    - refactor to CompleteableFuture factory method `supplyAsync` which handles errors too

    Exceptions and unhandled, so will remain confined in the thread which is trying to calculate the product price, so kills it. This will block the client waiting for the result forever. 
    
    Handle the exception in the proper way:  
    
    1. If the price calculation completed normally, complete the Future with the price.
    1. Otherwise, complete the Future exceptionally with the Exception that caused the failure.
    
    _(solution in branch `exercise-16.2.2`)_



## SUMMARY

[Continue to next chapter](README-chapter-17.md)