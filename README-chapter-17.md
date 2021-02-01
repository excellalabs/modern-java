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
- involved engineers from Netflix, Red Hat, Twitter, Lightbend, and other companies
- produced the definition of four interrelated interfaces representing the minimal set of features that any Reactive Streams implementation has to provide
- These interfaces are now part of Java 9, nested within the new java.util.concurrent.Flow class, and implemented by many third-party libraries, including Akka Streams (Lightbend), Reactor (Pivotal), RxJava (Netflix), and Vert.x (Red Hat)

### 17.2.1. Introducing the Flow class

Java 9 adds one new class for reactive programming: java.util.concurrent.Flow. This class contains only static components and can’t be instantiated. The Flow class contains four nested interfaces to express the publish-subscribe model of reactive programming as standardized by the Reactive Streams project:

Publisher
Subscriber
Subscription
Processor

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

    Step 1: Define simple class for reported temperature (done for you in tempInfo/)
    
    