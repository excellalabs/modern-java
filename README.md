# Modern Programming using *Modern Java in Action*

Below you will find: 
* [Overview](#Overview)
* [Sessions](#Sessions)
* [Environment Setup](#Environment-Setup)

## Overview  

This was a dive deep into modern programming via a book club for [Modern Java in Action](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/). It dives deep into Functional Reactive Programming (FRP), including lambdas, streams, and practical functional and concurrency approaches in depth.  

Chapter notes, code and videos (30 sessions!) are available here.

### Who? 

General programming knowledge is required, but no previous FRP knowledge is required, and even if you're experienced with it, we will dive deep so you may fill in some gaps. 

### Why this topic? 

Functional reactive programming (FRP) is becoming a common way to build systems these days in order to utilize hardware and thrive in a distributed environment, and represents a large shift from modern standard object oriented approaches, so requires some re-training. Modern Java in Action is all about functional reactive programming. It uses Java but the concepts are universally applicable.

This topic encompasses the core skills needed by modern Java engineers.  

### The Format

People read the chapters ahead of time, and we reviewed & discussed key concepts in the sessions. Coding will be reviewed, but actual coding was to be done as a supplement to reinforce the ideas in our own time. Code from the chapters is in this repo, which should be understood and ideally gone through individually from scratch.

### The book

[Modern Java in Action](https://learning.oreilly.com/library/view/modern-java-in/9781617293566/)

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
* [Chapter 12](README-Chapter-12.md)
* [Chapter 13](README-Chapter-13.md)
* [Chapter 14](README-chapter-14.md)
* [Chapter 15](README-chapter-15.md)
* [Chapter 16](README-chapter-16.md)
* [Chapter 17](README-chapter-17.md)
* [Chapter 18](README-chapter-18.md)
* [Chapter 19](README-chapter-19.md)
* [Chapter 20](README-chapter-20.md)

## Environment Setup

1. Clone this repo

### Locally installed Java way 

1. Install Java 11 (or the latest but update app references) & gradle
1. Build & run tests via `./gradlew build`
1. Run via `./gradlew run` 

### Docker way 

1. Install [Docker](https://www.docker.com/products/docker-desktop) 
1. Create & log into Java container via docker-compose by running this script: `./run_app.sh`
1. Build & run tests: `./gradlew build` & run app via `./gradlew run`

## Example Code

Much of the example code from the book is implemented in this repo. Throughout the chapter notes, it is referenced.

The code is implemented in the typical Java structure, starting in `Application.java`, where you can choose to run the console-based or controller-based code.
