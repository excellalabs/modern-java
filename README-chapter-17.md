# Chapter 17. Reactive programming

Reactive programming is programming with asynchronous data streams.
In a way, this isn't anything new. Event buses or your typical click events are really an asynchronous event stream, on which you can observe and do some side effects. Reactive is that idea on steroids. 
- You are able to create data streams of anything, not just from click and hover events. 
- Streams are cheap and ubiquitous, anything can be a stream: variables, user inputs, properties, caches, data structures, etc. 
- For example, imagine your Twitter feed would be a data stream in the same fashion that click events are. You can listen to that stream and react accordingly.

This chapter covers

- Defining reactive programming and discussing the principles of the Reactive Manifesto
- Reactive programming at the application and system levels
- Showing example code using reactive streams and the Java 9 Flow API
- Introducing RxJava, a widely used reactive library
- Exploring the RxJava operations to transform and combine multiple reactive streams
- Presenting marble diagrams that visually document operations on reactive streams

## 17.1 Reactive Manifesto

- The Reactive Manifesto (https://www.reactivemanifesto.org)—developed in 2013 and 2014 by Jonas Bonér, Dave Farley, Roland Kuhn, and Martin Thompson—formalized a set of core principles for developing reactive applications and systems.
- Responsive, Resilient, Elastic, Message-driven, defined in previous chapter and again here

[Figure 17.1](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/17fig01_alt.jpg)

### 17.1.1 Reactive at application level

- the main feature of reactive programming for application-level components allows tasks to be executed asynchronously
- you process _streams of event_ in an _asynchronous and non-blocking way_
- essential for maximizing use rate of modern multicore CPUs, and more precisely, of the threads competing for their use
- the reactive frameworks and libraries share threads (relatively expensive and scarce resources) among lighter constructs such as futures; actors; and (more commonly) event loops dispatching a sequence of callbacks intended to aggregate, transform, and manage the events to be processed.
- Benefits: 
    - cheaper than using threads
    - raise the abstraction level of implementing concurrent and asynchronous applications

The most important thing to pay attention to when using these thread-multiplexing strategies is to never perform blocking operations inside the main event loop
    - include as blocking operations all I/O-bound operations such as accessing a database or the file system or calling a remote service that may take a long or unpredictable time to complete

See example with [Figure 17.2](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/kindle_split_031.html)
- 3 streams, 2 threads
- Writing a file to disk blocks a thread
- Most frameworks (i.e. RxJava, Akka) overcome this by allowing blocking operations to be executed on a separate, dedicated thread pool
    - all threads in main pool are free to run uninterrupted
    - this also makes tuning the size and config of your thread pools with finer granularity, and monitor their performance more precisely

One other big aspect of reactive programming is having your applications perform efficiently in isolation...

### 17.1.2 Reactive at system level

A reactive system is a software architecture that allows multiple applications to work as a single coherent, resilient platform 
- also allows these applications to be sufficiently decoupled so when one of them fails, it doesn’t bring down the whole system

The main difference between reactive **applications** and **systems** is that 
- the former type usually perform computations based on ephemeral streams of data and are called event-driven
- the latter type are intended to compose the applications and facilitate communication. Systems with this property are often referred to as being message-driven.
- the other important distinction between messages and events is the fact that messages are directed toward a defined single destination, whereas events are facts that will be received by the components that are registered to observe them. 
    - In reactive systems, it’s also essential for these messages to be asynchronous to keep the sending and the receiving operations decoupled from the sender and receiver, respectively. 
    - This decoupling is a requirement for full isolation between components and is fundamental for keeping the system responsive under both failures (resilience) and heavy load (elasticity)
        - Resilience includes isolating failures within the components where they happen, preventing them from cascading
    - Elasticity's main enabler is **location transparency** - which allows communication among any components no matter where each may reside
        - Location-agnostic scaling shows another difference between reactive applications (asynchronous and concurrent and decoupled in time) and reactive systems (which can become decoupled in space through location transparency).


## 17.2. REACTIVE STREAMS AND THE FLOW API

The requirements and behavior that the Reactive Manifesto laid out were condensed into the [Reactive Streams project](www.reactive-streams.org)
- Involved engineers from Netflix, Red Hat, Twitter, Lightbend, and other companies
- Produced the definition of four interrelated interfaces representing the minimal set of features that any Reactive Streams implementation has to provide
- These interfaces are now part of Java 9, nested within the new java.util.concurrent.Flow class, and implemented by many third-party libraries, including Akka Streams (Lightbend), Reactor (Pivotal), RxJava (Netflix), and Vert.x (Red Hat)
- The core idea of the Flow API is asynchronous stream processing via the publish-subscribe protocol

### 17.2.1. Introducing the Flow class

Java 9 adds one new class for reactive programming: java.util.concurrent.Flow. This class contains only static components and can’t be instantiated. The Flow class contains four nested interfaces to express the publish-subscribe model of reactive programming as standardized by the Reactive Streams project:

* Publisher
* Subscriber
* Subscription
* Processor

- allows interrelated interfaces and static methods to establish flow-controlled components 
    - Publishers produce items consumed by one or more Subscribers, each managed by a Subscription
    - Publisher is a provider of a potentially unbounded number of sequenced events, but it’s constrained by the backpressure mechanism to produce them according to the demand received from its Subscriber(s)
    - Subscriber to register itself as a listener of the events issued by the Publisher; flow control, including backpressure, between Publishers and Subscribers is managed by a Subscription

See [Listing 17.1-17.3](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/kindle_split_031.html)

1. onSubscribe is always invoked as the first event
1. An arbitrary number of onNext signals follow
1. The stream of events can go on forever, or it can be terminated by an onComplete callback to signify that no more elements will be produced or by an onError if the Publisher experiences a failure

- When a Subscriber registers itself on a Publisher, the Publisher’s first action is to invoke the onSubscribe method to pass back a Subscription object. 
- The Subscription interface declares two methods, ` void request(long n)` and `void cancel()` 
    - The Subscriber can use the first method to notify the Publisher that it’s ready to process a given number of events; the second method allows it to cancel the Subscription, thereby telling the Publisher that it’s no longer interested in receiving its events.

The Java 9 Flow specification defines a set of rules through which the implementations of these interfaces should cooperate.
- The Publisher must send the Subscriber a number of elements no greater than that specified by the Subscription’s request method
- The Subscriber must notify the Publisher that it's ready to receive and process n elements
- The Subscription is shared by exactly one Publisher and Subscriber, and represents the unique relationship between them

## 17.2.1 Creating your first reactive application

Homework!

1. 17.2.2 _Create a reactive application directly on top of the Java 9 Flow API_
 
    - Get a feeling for how the four interfaces discussed in the preceding sections work together
    - Write a simple temperature-reporting program using reactive principles. 
    
    This program has two components:
       
    `TempInfo`, which mimics a remote thermometer (constantly reporting randomly chosen temperatures between 0 and 99 degrees Fahrenheit, which is appropriate for U.S. cities most of the time)

    `TempSubscriber`, which listens to these reports and prints the stream of temperatures reported by a sensor installed in a given city
The first step is defining a simple class that conveys the currently reported temperature, as shown in the following listing.

    Steps:
    
    1. Check out branch `exercise-17.2.2` as it has the simple class for reported temperature in `tempInfo/`, which is also where the rest of the code goes
    1. **Implement a Subscription** for the temperatures of a given town that sends a temperature report whenever this report is requested by its Subscriber (in Listing 17.6)
        1. Loops once per request made by the Subscriber
        1. Sends the current temperature to the Subscriber
        1. In case of a failure while fetching the temperature propagates the error to the Subscriber
        1. If the subscription is canceled, send a completion (onComplete) signal to the Subscriber.
    1. **Create a Subscriber** that, every time it gets a new element, prints the temperatures received from the Subscription and asks for a new report (in Listing 17.7)
        1. Stores the subscription and sends a first request
        1. Prints the received temperature and requests a further one
        1. Prints the error message in case of an error
    1. **Create a client** to work with it, by creating a `Publisher` and then subscribe to it by using `TempScriber`
        1. Creates a new Publisher, of temperatures in New York and subscribes the TempSubscriber to it
        1. Returns a Publisher that sends a TempSubscription to the Subscriber that subscribes to it
    
    _POP QUIZ_ 17.1: What is wrong with the above code that causes it to error out? Hint: StackOverflow (not the site)
    
    ** Session 26
    
    _[Recording]()_
    
    **Agenda**
    
    - **Housekeeping**: notes & code, expensing food, start recording
    - **Recap**
        - 17 key concepts, started 17 coding
    - **Today:** 
        - Finish 17 coding
        - If time, start chapter 18
    - **For Next time:** 
        - chapter 18 - Thinking functionally
        - chapter 19 - Functional programming techniques
    
    _Notice_, the methods on the foundational interfaces:
    
    * Publisher
        - subscribe(Subscriber<? super T> subscriber)
    * Subscriber
        - onSubscribe(Subscription subscription)
        - onNext(T item)
        - onComplete()
        - onError(Throwable throwable)
    * Subscription
        - request(long n)
        - cancel()
    * Processor implements Publisher and Subscriber 
   
   ```
             Publisher                           Subscriber
      
      --->   - subscribe(subscriber)
             - new Subscription.request(n) 
                                           --->  - onSubscribe(subscription)
                                           --->  - onNext(item)
                                           --->  - onCompelete()
                                           --->  - onError(throwable)                 
    ```
    
1. 17.2.3 Implement a Processor

    Both a `Subscriber` and a `Publisher`, it’s intended to 
        1. subscribe to a Publisher
        1. republish the data that it receives after transforming that data
    
    Steps:
        
    1. implement a Processor that subscribes to a Publisher that emits temperatures in Fahrenheit and republishes them after converting them to Celsius (answer: 17.10)

## 17.3. USING THE REACTIVE LIBRARY RXJAVA

- RxJava provides two classes, both of which implement Flow.Publisher:
    - `io.reactivex.Flowable`, which includes the reactive pull-based backpressure feature of Java 9 Flow (using request)
    - `io.reactivex.Observable`, which didn’t support backpressure. 
        - simpler to program and more appropriate for user-interface events (such as mouse movements)
        - use when you have a stream of <1000 events or GUI events
    
- Here we'll implement an `Observable` since we worked with Flowable above.
    - In RxJava, the `Observable` plays the role of the `Publisher` in the Flow API
    - So the `Observer` similarly corresponds to Flow’s `Subscriber` interface
    
Implementations in RxJava

* Publisher = Observable = Flowable (backpressure)
    - Single, Completeable
* Subscriber = Observer
    - Maybe
* Subscription?
* Processor?

Hierarchy

```
RxJava 

    Observable 
    /        \
Single     Completable


RxJava 2

    Flowable
    /      \
 Maybe     Completable


Reactor 

   Flux
  /
Mono / Completeable?
```  

- `Observable` - generic ReactiveX building block that emits values over time
- `Single` - use when you have a task-oriented Observable and you expect a single value, i.e. DB fetch operation
- `Completeable` - when you don't care about the return value or there isn't any. Terminal events only, onError & onCompleted, i.e. updating cache

`Maybe` 
- introduced in RxJava 2
- can be thought of as a method that returns Optional of some type, Optional and can either have some value exactly like Single or have return nothing - just like Completable. 0 or 1 elements. 
- when we want to mark that an Observable might not have a value and will just complete
- I.E.:
    - fetch from a cache - we'll not necessarily have a value so we will complete when not, and will call onNext to try to get the value. 
    - handling non-null values in a stream with RxJava2

`Flowable`
- introduced in RxJava 2
- use instead of `Observable` when needing backpressure support


### 17.3.1. Creating and using an Observable
    
Do coding in seection in `src/main/java/com/excella/modernjava/tempinfo/rxjava`

### 17.3.2






