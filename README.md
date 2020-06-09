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

## Environment Setup

1. Install [docker](https://www.docker.com/products/docker-desktop) 
1. Clone this repo (`https://github.com/excellalabs/modern-java`)
1. Make sure you can do the following:

    - Start docker container: `./run_app.sh`
    - Compile the app: `./gradlew clean build`
    - Running tests: `./gradlew test`
