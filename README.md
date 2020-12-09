# Modern Programming using *Modern Java in Action*

Below you will find: 
* [Overview](#Overview)
* [Sessions](#Sessions)
* [Environment Setup](#Environment-Setup)

## Overview 

### Modern programming anyone? 

Let's dive deep into modern programming, using the book Modern Java in Action (feel free to expense or get via O'Reilly Learning). We will dive deep into Functional Reactive Programming (FRP), including lambdas, streams, and practical functional and concurrency approaches in depth. We will build a cloud native app along the way to reinforce the concepts and deepen our knowledge. 

### Who? 

We will start from scratch but go fast, so no previous FRP knowledge is required, and even if you're experienced with it, we will dive deep so you may fill in some gaps. We won't be going over fundamental programming concepts (types, loops, OO, etc) so experience there is required, but not in Java.

### Why this topic? 

Functional reactive programming is becoming the main way to programming these days, and represents a large shift from modern standard object oriented approches. There is new material to learn, and while we all learn as we go, it's useful to step back and go over all the material, to know what's there and to dig in deeper. The book Modern Java in Action is all about FRP. It uses Java but the concepts are universally applicable. 

### Outline:

- Part 1: Programming has changed - with functional reactive programming (passing code with behavior parameterization, and lambda expressions)
    - 1-3 (5 sessions)
- Part 2: Using streams when working with data, with parallel benefits
    - 4-10 (? sessions)
- Part 3: Modern language constructs to improve code - Optionals, default functions, etc.
    - 11-14 (? sessions)
- Part 4: Reactive programming for more concise concurrency
    - 15-17 (? sessions)
- Part 5: Working with functional programming techniques
    - 18-21 (? sessions)

## Sessions

- **Mondays at 5:30**
- *See **#modern-java-in-action** for invite/zoom & upcoming agenda*

### Session Agendas & Notes:

* Chapter 1
    * [Session 1](README-chapter-01.md#Session-1)
    * [Session 2](README-chapter-01.md#Session-2)
    * [Session 3](README-chapter-01.md#Session-3)
* Chapter 2 
    * [Session 4](README-chapter-02.md#Session-4)
* Chapter 3 
    * [Session 5](README-chapter-03.md#Session-5)
    * [Session 6](README-chapter-03.md#Session-6)
* Chapter 4
    * [Session 7](README-chapter-04.md#Session-7)
* Chapter 5
    * [Session 8](README-chapter-05.md#Session-8)
    * [Session 9](README-chapter-05.md#Session-9)
* Chapter 6-7
    * [Session 10](README-chapter-06.md#Session-10)
    * [Session 11](README-chapter-07.md#Session-11)
    * [Session 12](README-chapter-07.md#Session-12)
    * [Session 13](README-chapter-07.md#Session-13)    
* Chapter 8, 9, 10, 11
    * [Session 14 - 8, start 9](README-chapter-08.md#Session-14)
    * [Session 15 - finish 9, start 10](README-chapter-09.md#Session-15)
    * [Session 16 - finish 10, start 11](README-chapter-10.md#Session-16)
* [Chapter 12](Readme-Chapter-12.md)
* [Chapter 13](Readme-Chapter-13.md)
* [Chapter 14](Readme-chapter-14.md)

## Environment Setup

1. Install Java 11 (or the latest but update app references) & gradle

1. Build via `./gradlew build`

1. Run via `./gradlew run` 

### Docker way 

1. Install [Docker](https://www.docker.com/products/docker-desktop) 
1. Clone this repo
1. Make sure you can do the following:

    1. Start Java container: `./run_app.sh`
    1. Inside the container:
    
        1. Log into the container
        1. Build & run tests: `./gradlew build`
