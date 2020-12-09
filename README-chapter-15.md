# Chapter 15. Concepts behind CompletableFuture and reactive programming

This chapter covers

* Threads, Futures, and the evolutionary forces causing Java to support richer concurrency APIs
* Asynchronous APIs
* The boxes-and-channels view of concurrent computing
* CompletableFuture combinators to connect boxes dynamically
* The publish-subscribe protocol that forms the basis of the Java 9 Flow API for reactive programming
* Reactive programming and reactive systems

Two trends are obliging developers to rethink the way software is written:

1. the hardware on which applications run - software needs to exploit multicore processors
1. how applications are structured (particularly how they interact) - microservices and using many 3rd parties (i.e. Twitter API) requires increased network communication
    - you have to contact multiple web services but you don’t want to block your computations and waste billions of precious clock cycles waiting for an answer 

This situation represents the other side of the multitask-programming coin:
- The fork/join framework and parallel streams, discussed in chapter 7, are valuable tools for parallelism; they divide a task into multiple subtasks and perform those subtasks in parallel on different cores, CPUs, or even machines
- Conversely, when you’re dealing with concurrency instead of parallelism, or when your main goal is to perform several loosely related tasks on the same CPUs, keeping their cores as busy as possible to maximize the throughput of your application, you want to avoid blocking a thread and wasting its computational resources while waiting (potentially for quite a while) for a result from a remote service or from interrogating a database.

Java offers two main tool sets for such circumstances. First, as you’ll see in chapters 16 and 17, the Future interface, and particularly its Java 8 CompletableFuture implementation, often provide simple and effective solutions (chapter 16). More recently, Java 9 added the idea of reactive programming, built around the idea of the so-called publish-subscribe protocol via the Flow API, which offers more sophisticated programming approaches.

**Concurrency** is a programming property (overlapped execution) that can occur even for a single-core machine, whereas **parallelism** is a property of execution hardware (simultaneous execution).

Note that chapter 7 dealt with mainly using parallelism in looplike programs. Section 15.2 explores how you can better exploit concurrency for method calls. 

## 15.1. EVOLVING JAVA SUPPORT FOR EXPRESSING CONCURRENCY

- Java has evolved considerably in its support for concurrent programming, largely reflecting the changes in hardware, software systems, and programming concepts over the past 20 years
- Summarizing this evolution can help you understand the reason for the new additions and their roles in programming and system design

Timeline 
* 1996 - Java had locks (via sychronized classes and methods), Runnables and Threads
* 2004 - Java 5 introduced the `java.util.concurrent` package which supported more expressive concurrency, particularly:
    - the `ExecutorService` interface (which decoupled task submission from thread execution)
    - the `Callable<T>` and `Future<T>`, which produced higher-level and result-returning variants of `Runnable` and `Thread` and used generics (also Java 5)
    - addressed multicore processors that started coming out the following year; no one liked working with threads directly
* 2011 - Java 7 added java.util.concurrent.Recursive-Task to support fork/join implementation of divide-and-conquer algorithms
* 2014 - Java 8 added support for parallel streams
* 2017 - Java 9 provided explicit support for distributed asynchronous programming. 
    - mental model and toolkit for various web services and combining their information in real time for a user or to expose it as a further web service. This process is called reactive programming, and Java 9 provides support for it via the publish-subscribe protocol (specified by the java.util.concurrent.Flow interface
    - A key concept of CompletableFuture and java.util.concurrent.Flow is to provide programming structures that enable independent tasks to execute concurrently wherever possible and in a way that easily exploits as much as possible of the parallelism provided by multicore or multiple machines

### 15.1.1. Threads and higher-level abstractions

A single-CPU computer can support multiple users because its operating system allocates a process to each user. 
- it gives these processes separate virtual address spaces so that two users feel like they’re the only users of the computer. 
- The operating system furthers this illusion by waking periodically to share the CPU among the processes. 
- A process can request that the operating system allocate it one or more threads—processes that share an address space as their owning process and therefore can run tasks concurrently and cooperatively.

If your program doesn’t use threads, it’s effectively using only one of the processor cores.

### 15.1.2. Executors and thread pools

Before looking at additional abstractions for threads, we visit the (Java 5) idea of ExecutorServices and the thread pools on which these further abstractions are built.

Java 5 provided the Executor framework and the idea of thread pool as a higher-level idea capturing the power of threads, which allow Java programmers to decouple task submission from task execution.

- Java threads access operating-system threads directly. The problem is that operating-system threads are expensive to create and to destroy (involving interaction with page tables), and moreover, only a limited number exist. Exceeding the number of operating-system threads is likely to cause a Java application to crash mysteriously, so be careful not to leave threads running while continuing to create new ones.
- The number of operating system (and Java) threads will significantly exceed the number of hardware threads[2], so all the hardware threads can be usefully occupied executing code even when some operating-system threads are blocked or sleeping

_Thread pools and why they’re better_

- The Java ExecutorService provides an interface where you can submit tasks (Callable or Runnable) and obtain their results later
- It contains the number of threads specified when created, storing them in a thread pool, from which unused threads are taken to run submitted tasks. The threads are returned to the pool when their tasks terminate.
- It is cheap to submit thousands of tasks to a thread pool while keeping the number of tasks to a hardware-appropriate number

_Thread pools and why they’re worse_

Thread pools are better than explicit thread manipulation in almost all ways, but you need to be aware of two “gotchas:”

1. A thread pool with k threads can execute only k tasks concurrently. Any further task submissions are held in a queue and not allocated a thread until one of the existing tasks completes
    - This situation is generally good, in that it allows you to submit many tasks without accidentally creating an excessive number of threads, but you have to be wary of tasks that sleep or wait for I/O or network connections. In the context of blocking I/O, these tasks occupy worker threads but do no useful work while they’re waiting
    - try to avoid submitting tasks that can block (sleep or wait for events) to thread pools
1. Java typically waits for all threads to complete before allowing a return from main to avoid killing a thread executing vital code. Therefore, it’s important in practice and as part of good hygiene to shut down every thread pool before exiting the program.

### 15.1.3. Other abstractions of threads: non-nested with method calls

Concurrency used in this chapter differ from those used in chapter 7 (parallel Stream processing and the fork/join framework). They are richer forms of concurrency in which threads created (or tasks spawned) by a user’s method call may outlive the call - often called an **asynchronous method**. 

Dangers

- Ongoing thread runs concurrently with the code following the method call and therefore requires careful programming to avoid data races
    - risks a seeming application crash by never terminating due to a forgotten thread
- If the Java main() method returns before the ongoing thread has terminated, it will either a) wait for all such outstanding threads before exiting, or b) kill the outstanding threads and exit. 
    - risks interrupting a sequence of I/O operations writing to disk, thereby leaving an external data in an inconsistent state

Therefor, ensure that your program keeps track of all threads it creates and joins them all before exiting (including shutting down any thread pools).

### 15.1.4. What do you want from threads?

For effective parallelism, you need to ensure enough tasks are available to occupy all the hardware threads, which means structuring your program to have many smaller tasks (but not too small because of the cost of task switching). 

You saw how to do this for loops and divide-conquer algorithms in chapter 7, using parallel stream processing and fork/join, but in the rest of this chapter (and in chapters 16 and 17), you see how to do it for method calls without writing swaths of boilerplate thread-manipulation code.

## 15.2. SYNCHRONOUS AND ASYNCHRONOUS APIS

- Chapter 7 showed you that Java 8 Streams give you a way to exploit parallel hardware
- This exploitation happens in two stages: 
    1. You replace external iteration (explicit for loops) with internal iteration (using Stream methods)
    1. Then you can use the parallel() method on Streams to allow the elements to be processed in parallel by the Java runtime library instead of rewriting every loop to use complex thread-creation operations. 
- An additional advantage is that the runtime system is much better informed about the number of available threads when the loop is executed than is the programmer, who can only guess
- Situations other than loop-based computations can also benefit from parallelism, namely **asychronous APIs**. 

Consider this synchronous code:

```
int y = f(x);
int z = g(x);
System.out.println(y + z);
```

- If you know that f and g don’t interact, or you don’t care, you want to execute f and g in separate CPU cores
- This makes the total execution time only the maximum of that of the calls to f and g instead of the sum
- All you need to do is run the calls to f and g in separate threads, though that complicates the code

```
class ThreadExample {

    public static void main(String[] args) throws InterruptedException {
        int x = 1337;
        Result result = new Result();

        Thread t1 = new Thread(() -> { result.left = f(x); } );
        Thread t2 = new Thread(() -> { result.right = g(x); });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(result.left + result.right);
    }

    private static class Result {
        private int left;
        private int right;
    }
}
```

Some of the complexity here has to do with transferring results back from the thread. Only final outer-object variables can be used in lambdas or inner classes, but the real problem is all the explicit thread manipulation.

You can simplify this code somewhat by using the Future API interface instead of `Runnable`. Assuming that you previously set up a thread pool as an `ExecutorService` (such as executorService), you can write:

```
public class ExecutorServiceExample {
    public static void main(String[] args)
        throws ExecutionException, InterruptedException {

        int x = 1337;

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> y = executorService.submit(() -> f(x));
        Future<Integer> z = executorService.submit(() -> g(x));
        System.out.println(y.get() + z.get());

        executorService.shutdown();
    }
}
```

- This code is still polluted by the boilerplate code involving explicit calls to submit
- The solution is to change it to an **asynchronous API**, allowing it to return physically before producing its result, which allows it to run in parallel
- Note, asynchronous APIs can naturally implement nonblocking I/O (where the API call merely initiates the I/O operation without waiting for the result, provided that the library at hand, such as Netty, supports nonblocking I/O operations)

There are 2 techniques you can use: 
1. Using Java Futures in a better way. They appeared in Java 5 and were enriched into CompletableFuture in Java 8 to make them composable.
1. A reactive-programming style that uses the Java 9 java.util.concurrent.Flow interfaces, based on the publish-subscribe protocol

We'll see how each affects the function signature.

### 15.2.1. Future-style API

Change the typical synchronous code above to: 

```
Future<Integer> f(int x);
Future<Integer> g(int x);
```

And change the calls to:

```
Future<Integer> y = f(x);
Future<Integer> z = g(x);
System.out.println(y.get() + z.get());
```

- method f returns a Future, which contains a task that continues to evaluate its original body, but the return from f happens as quickly as possible after the call
- Method g similarly returns a future
- the third line uses get() to wait for both Futures to complete and sums their results

Note, 
- You could have left the API and call of g unchanged without reducing parallelism—only introducing Futures for f. - two reasons not to do so in bigger programs:
    - other uses of g may require a Future-style version, so you prefer a uniform API style.
    - to enable parallel hardware to execute your programs as fast as possible, it’s useful to have more and smaller tasks (within reason)

# Session 21

_[Recording (11/9/20)]()_

## Agenda

- **Housekeeping**: notes & code, expensing food, start recording
- **Recap**
    - Finished chapter 14 - Java Module System, started chapter 15 - Concepts behind CompletableFuture and reactive programming
- **Today:** 
    - Continue Chapter 15
- **Next time:** Finish 15, start 16 - CompletableFuture: composable asynchronous programming

### 15.2.2. Reactive-style API

Callback-style programming, changing the the signature of f and g to:

```
void f(int x, IntConsumer dealWithResult);
```

- It doesn't return a value, but a callback (as a lambda) is passed in, which is called when the result is ready
- f returns immediately after spawning the task to evaluate the body, which results in this calling code:

```
public class CallbackStyleExample {
    public static void main(String[] args) {

        int x = 1337;
        Result result = new Result();

        f(x, (int y) -> {
            result.left = y;
            System.out.println((result.left + result.right));
        } );

        g(x, (int z) -> {
            result.right = z;
            System.out.println((result.left + result.right));
        });

    }
}
```

Issues with this code: before this code prints the correct result (the sum of the calls to f and g), it prints the fastest value to complete (and occasionally instead prints the sum twice, as there’s no locking here, and both operands to + could be updated before either of the println calls is executed). There are two answers:
- You could recover the original behavior by invoking println after testing with if-then-else that both callbacks have been called, perhaps by counting them with appropriate locking
- This reactive-style API is intended to react to a sequence of events, not to single results, for which Futures are more appropriate

Reactive style of programming allows methods f and g to invoke their callback dealWithResult multiple times. The original versions of f and g were obliged to use a return that can be performed only once.

Whereas, a Future can be completed only once, and its result is available to get(). In a sense, the reactive-style asynchronous API naturally enables a sequence (which we will later liken to a stream) of values, whereas the Future-style API corresponds to a one-shot conceptual framework.

_Async code is usually more complex_
- You shouldn’t thoughtlessly use either API for every method. The APIs do keep code simpler (and use higher-level constructs) than explicit thread manipulation does.
- Careful use of these APIs for method calls that
    1. cause long-running computations (perhaps longer than several milliseconds) will make your program faster without the explicit ubiquitous use of threads polluting your program
    1. wait for a network or for input from a human can significantly improve the efficiency of your application. There’s the additional benefit that the underlying system can use threads effectively without clogging up.

### 15.2.3. Sleeping (and other blocking operations) considered harmful

- Tasks sleeping in a thread pool consume resources by blocking other tasks from starting to run. They can’t stop tasks already allocated to a thread, as the operating system schedules these tasks.
- any blocking operation can do the same thing (waiting for another task to do something, such as invoking get() on a Future; and waiting for external interactions such as reads from networks, database servers, or human interface devices such as keyboards)
- Solution: break your task into two parts — before and after — and ask Java to schedule the after part only when it won’t block

Compare code A, shown as a single task:

```
work1();
Thread.sleep(10000);         1
work2();
```

1 Sleep for 10 seconds.

With code B:

```
public class ScheduledExecutorServiceExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService
            = Executors.newScheduledThreadPool(1);

        work1();
        scheduledExecutorService.schedule(
    ScheduledExecutorServiceExample::work2, 10, TimeUnit.SECONDS);    

        scheduledExecutorService.shutdown();
    }

    public static void work1(){
        System.out.println("Hello from Work1!");
    }

    public static void work2(){
        System.out.println("Hello from Work2!");
    }
}
```

Schedule a separate task for work2() 10 seconds after work1() finishes.

Think of both tasks being executed within a thread pool.

- A executing: first, it’s queued to execute in the thread pool, and later, it starts executing. Halfway through, however, it blocks in the call to sleep, occupying a worker thread for 10 whole seconds doing nothing. Then it executes work2() before terminating and releasing the worker thread. - B, by comparison, executes work1() and then terminates — but only after having queued a task to do work2() 10 seconds later
- Code A offers a precious thread while it sleeps, whereas code B queues another task to execute instead of sleeping
- Tasks occupy valuable resources when they start executing, so you should aim to keep them running until they complete and release their resources. Instead of blocking, a task should terminate after submitting a follow-up task to complete the work it intended to do.
- This guideline applies to I/O too. I.E., a task should issue a nonblocking “start a read” method call and terminate after asking the runtime library to schedule a follow-up task when the read is complete
- This design pattern may seem to lead to lots of hard-to-read code. But the Java CompletableFuture interface (section 15.4 and chapter 16) abstracts this style of code within the runtime library, using combinators instead of explicit uses of blocking get() operations on Futures
- code B is better than A whenever you have more than a few tasks that might sleep or otherwise block

### 15.2.4. Reality check

- If you’re designing a new system, designing it with many small, concurrent tasks so that all possible blocking operations are implemented with asynchronous calls is probably the way to go if you want to exploit parallel hardware
- Pragmatically, we suggest that you try to identify situations that would benefit from Java’s enhanced concurrency APIs, and use them without worrying about making every API asynchronous
- Look at newer libraries such as Netty, which provides a uniform blocking/nonblocking API for network servers

### 15.2.5. How do exceptions work with asynchronous APIs?

- In Future-based and reactive-style asynchronous APIs, the conceptual body of the called method executes in a separate thread, and the caller’s execution is likely to have exited the scope of any exception handler placed around the call 
- Unusual behavior that would trigger an exception needs to perform an alternative action 

#### In the CompletableFuture implementation of Futures
the API includes provision for exposing exceptions at the time of the get() method and also provides methods such as exceptionally() to recover from exceptions, which we discuss in chapter 16

### For reactive-style asynchronous APIs
you have to modify the interface by introducing an additional callback, which is called instead of an exception being raised, as the existing callback is called instead of a return being executed. To do this, include multiple callbacks in the reactive API, as in this example:

```
void f(int x, Consumer<Integer> dealWithResult,
              Consumer<Throwable> dealWithException);
```

Then the body of f might perform

```
dealWithException(e);
```

If there are multiple callbacks, instead of supplying them separately, you can equivalently wrap them as methods in a single object. The Java 9 Flow API, for example, wraps these multiple callbacks within a single object (of class Subscriber<T> containing four methods interpreted as callbacks). Here are three of them:

```
void    onComplete()
void    onError(Throwable throwable)
void    onNext(T item)
```

Separate callbacks indicate when a value is available (onNext), when an exception arose while trying to make a value available (onError), and when an onComplete callback enables the program to indicate that no further values (or exceptions) will be produced. 

For the preceding example, the API for f would now be

```
void f(int x, Subscriber<Integer> s);
```

and the body of f would now indicate an exception, represented as Throwable t, by performing

```
s.onError(t);
```

- Compare this API containing multiple callbacks with reading numbers from a file or keyboard device. 
    - a device is a producer rather than a passive data structure, it produces a sequence of “Here’s a number” or “Here’s a malformed item instead of a number” items, and finally a “There are no more characters left (end-of-file)” notification
- Common to call these messages or events

## 15.3. THE BOX-AND-CHANNEL MODEL

- A great way to think about concurrent systems
- Helps structure thoughts and code
- It raises the level of abstraction for constructing a larger system

_Example: Calculating f(x) + g(x)_

- You want to call method or function p with argument x, pass its result to functions q1 and q2, call method or function r with the results of these two calls, and then print the result
- See Figure 15.7

Issues with this:
- many tasks might be waiting (with a call to get()) for a Future to complete
- as discussed in section 15.1.2, the result may be underexploitation of hardware parallelism or even deadlock 
- it tends to be hard to understand such large-scale system structure well enough to work out how many tasks are liable to be waiting for a get()

Solution is using **combinators** with `CompletableFuture`, i.e. similar to what we saw with composing functions:

```
Function<Integer, Integer> myfun = add1.andThen(dble);
```

- combinators often can execute code more efficiently than hand-coding the threading
- combinators work for Futures and reactive stream in addition to functions
- box-and-channel diagrams help you change perspective from directly programming concurrency to allowing combinators to do the work internally

## COMPLETABLEFUTURE AND COMBINATORS FOR CONCURRENCY

- One problem with the Future interface is that it’s an interface, encouraging you to think of and structure your concurrent coding tasks as Futures, without as much structured support
- Java 8 brings the ability to compose Futures, using the CompletableFuture implementation of the Future interface. 
- Why call it CompletableFuture rather than ComposableFuture? 
    - an ordinary Future is typically created with a Callable, which is run, and the result is obtained with a get()
    - But a CompletableFuture allows you to create a Future without giving it any code to run
    - a complete() method allows some other thread to complete it later with a value so that get() can access that value

To sum f(x) and g(x) concurrently, you can write:

```
public class CFComplete {

    public static void main(String[] args)
        throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int x = 1337;

        CompletableFuture<Integer> a = new CompletableFuture<>();
        executorService.submit(() -> a.complete(f(x)));
        int b = g(x);
        System.out.println(a.get() + b);

        executorService.shutdown();
    }
}
```

This can waste processing resources (recall section 15.2.3) by having a thread blocked waiting for a get if g(x) takes longer. `CompletableFuture` lets you avoid this.

**Quiz 15.1:**

## 15.5. PUBLISH-SUBSCRIBE AND REACTIVE PROGRAMMING

Recap: 

- The mental model for a Future and CompletableFuture is that of a computation that executes independently and concurrently. The result of the Future is available with get() after the computation completes. Thus, Futures are one-shot, executing code that runs to completion only once.
- By contrast, the mental model for reactive programming is a Future-like object that, over time, yields multiple results

With Streams: 
- hard to express Stream-like operations that can split a sequence of values between two processing pipelines (think fork) or process and combine items from two separate streams (think join)
    - Streams have linear processing pipelines
- Java 9 models reactive programming with interfaces available inside `java.util.concurrent.Flow` and encodes what’s known as the publish-subscribe model

Three main concepts in this model:
- A publisher to which a subscriber can subscribe
- The connection is known as a subscription
- Messages (also known an events) are transmitted via the connection

See _Figure 15.9_
- Multiple components can subscribe to a single publisher, a component can publish multiple separate streams, and a component can subscribe to multiple publishers

### 15.5.1. Example use for summing two flows

...