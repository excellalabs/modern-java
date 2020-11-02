# Chapter 14. The Java Module System

_This chapter covers:_

* The evolutionary forces causing Java to adopt a module system
* The main structure: module declarations and requires and exports directives
* Automatic modules for legacy Java Archives (JARs)
* Modularization and the JDK library
* Modules and Maven builds
* A brief summary of module directives beyond simple requires and exports

- main and most-discussed new feature introduced with Java 9 
- developed within project Jigsaw, and its development took almost a decade
- why you should care as a developer what a module system is, what the new Java Module System is intended for and how you can benefit from it

## 14.1. THE DRIVING FORCE: REASONING ABOUT SOFTWARE

You want to work with a software project that’s easy to reason about because this makes you more productive when you introduce changes in your code base. 

In the following sections, we highlight two design principles that help produce software that’s easier to reason about: separation of concerns and information hiding.

### 14.1.1. Separation of concerns

Modules group classes, allowing you to express visibility relationships between classes in your application, with finer-grained control of which classes can see which other classes and allow this control to be checked at compile time.

- Allowing work on individual parts in isolation, which helps team collaboration
- Facilitating reuse of separate parts
- Easier maintenance of the overall system

### 14.1.2. Information hiding

- encourages hiding implementation details
- language structure to allow the compiler to check that classes and packages were available only for the intended purposes

## 14.2. WHY THE JAVA MODULE SYSTEM WAS DESIGNED

- limitations of modularity before Java 9
- background about the JDK library and explain why modularizing it was important

### 14.2.1. Modularity limitations

- built-in support in Java to help produce modular software projects was somewhat limited before Java 9
- Java has had three levels at which code was grouped: classes, packages, and JARs. For classes, Java has always had support for access modifiers and encapsulation
- There was little encapsulation at the package and JAR levels
- If you want classes and interfaces from one package to be visible to another package, you have to declare them as public, so accessible by everyone else

_Issues with Class path:_

- class path has no notion of versioning for the same class. 
- You can’t, for example, specify that the class JSONParser from a parsing library should belong to version 1.0 or version 2.0, so you can’t predict what will happen if the same library with two different versions is available on the class path
- Common situation in large applications, as you may have different versions of the same libraries used by different components of your application
- class path doesn’t support explicit dependencies (jar hell); all the classes inside different JARs are merged into one bag of classes on the class path. In other words, the class path doesn’t let you declare explicitly that one JAR depends on a set of classes contained inside another JAR. 
    - makes it difficult to reason about the class path and to see if anything's missing or there are conflicts
    - Java 9 module system consistently enables all such errors to be detected at compile time

### 14.2.2. Monolithic JDK

- Classes you're not using are included
- Via the module system, new structuring constructs were required to allow you to choose what parts of the JDK you need and how to reason about the class path, and to provide stronger encapsulation to evolve the platform

### 14.2.3. Comparison with OSGi

- powerful module system in Java before 9, wasn’t formally part of the Java platform

## 14.3. JAVA MODULES: THE BIG PICTURE

- a new unit of Java program structure: the module
- introduced with the `module` keyword + name & body, called a module descriptor, which lives in a special `module-info.java` file, which is compiled to `module-info.class`
    - Use clauses, including the most important, `requires` and `exports`. The former clause specifies what other modules your modules need to run, and exports specifies everything that your module wants to be visible for other modules to use

A `module descriptor` describes and encapsulates one or more packages (and typically lives in the same folder as these packages), but in simple use cases, it exports (makes visible) only one of these packages.

_see Figure 14.2. Core structure of a Java module descriptor (module-info.java)_

# Session 20

_[Recording (11/2/20)]()_

## Agenda

- **Housekeeping**: notes & code, expensing food, start recording
- **Recap**
    - Finished chapter 13 - Default methods, most of chapter 14 - Java Module System
- **Today:** 
    - Summary of 14, Chapter 15 - Concepts behind CompletableFuture and reactive programming
- **Next time:** Finish 15, start 16 - CompletableFuture: composable asynchronous programming

## 14.4. DEVELOPING AN APPLICATION WITH THE JAVA MODULE SYSTEM

In this section, you get an overview of the Java 9 Module System by building a simple modular application from the ground up. 

## Summary 

[Continue](README-chapter-15.md)