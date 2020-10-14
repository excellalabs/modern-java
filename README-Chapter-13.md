# Chapter 13. Default methods

## What's covered & overview

* What default methods are
* Evolving APIs in a compatible way
* Usage patterns for default methods
* Resolution rules

- Any (nonabstract) class that must provide an implementation for each method causes a problem when library designers need to update an interface to add a new method
- Indeed, existing concrete classes (which may not be under the interface designers’ control) need to be modified
- Java 8 provides 2 solutions:
    - static methods allowed inside interfaces
    - default methods that allows you to provide a default implementation for methods in an interface
    - two examples you’ve seen are sort in the List interface and stream in the Collection interface

```
default void sort(Comparator<? super E> c){
    Collections.sort(this, c);
}
```

... so can call `sort` directly

```
List<Integer> numbers = Arrays.asList(3, 5, 1, 2, 6);
numbers.sort(Comparator.naturalOrder());   

```

- Are interfaces like abstract classes now? Yes and no; we'll explore more later.
- The main users of default methods are library designers, though they are also used to achieve a form of multiple inheritance in Java
- It gives a means of evolving interfaces without modifying existing implementations
- It also gives a flexible mechanism for multiple inheritance of behavior; a class can inherit default methods from several interfaces

## 13.1. EVOLVING APIS

Different types of compatibilities: binary, source, and behavioral

Binary compatibility - existing binaries running without errors continue to link (which involves verification, preparation, and resolution) without error after introducing a change. Adding a method to an interface is binary compatible, for example, because if it’s not called, existing methods of the interface can still run without problems.

Source compatibility means that an existing program will still compile after introducing a change. Adding a method to an interface isn’t source compatible; existing implementations won’t recompile because they need to implement the new method.

Behavioral compatibility means running a program after a change with the same input results in the same behavior. Adding a method to an interface is behavioral compatible because the method is never called in the program (or gets overridden by an implementation).

## 13.2. DEFAULT METHODS IN A NUTSHELL

 - An interface can contain method signatures for which an implementing class doesn’t provide an implementation
 - The missing method bodies are given as part of the interface (hence, default implementations) rather than in the implementing class

Abstract classes vs. interfaces in Java 8

- both can contain abstract methods and methods with a body
- a class can extend only from one abstract class, but a class can implement multiple interfaces.
- an abstract class can enforce a common state through instance variables (fields). An interface can’t have instance variables

 ## 13.3. USAGE PATTERNS FOR DEFAULT METHODS

Besides evolving an API, you may also want to use default methods for: optional methods and multiple inheritance of behavior.

### 13.3.1. Optional methods

Some classes that implement an interface leave some method implementation empty, since they don't want them. Default methods let you provide default implementation, so concrete classes don't need to provide an empty one. 

### 13.3.2. Multiple inheritance of behavior

It wasn't possible before in Java, but now you can compose interfaces with implementations via default methods when creating a concrete class: 

```
public interface Rotatable {
    void setRotationAngle(int angleInDegrees);
    int getRotationAngle();
    default void rotateBy(int angleInDegrees){                            1
        setRotationAngle((getRotationAngle () + angleInDegrees) % 360);
    }
}

public interface Moveable {
    int getX();
    int getY();
    void setX(int x);
    void setY(int y);
    default void moveHorizontally(int distance){
        setX(getX() + distance);
    }
    default void moveVertically(int distance){
        setY(getY() + distance);
    }
}

public interface Resizable {
    int getWidth();
    int getHeight();
    void setWidth(int width);
    void setHeight(int height);
    void setAbsoluteSize(int width, int height);
    default void setRelativeSize(int wFactor, int hFactor){
        setAbsoluteSize(getWidth() / wFactor, getHeight() / hFactor);
    }
}

public class Monster implements Rotatable, Moveable, Resizable {

}

...

Monster m = new Monster();        1
m.rotateBy(180);                  2
m.moveVertically(10);   

```

The monster is all the things.

## RESOLUTION RULES

 - in Java a class can extend only one parent class but implement multiple interfaces, but now with default methods, a class could inherit more than 1 method with the same signature
- the Java compiler resolves such potential conflicts

### 13.4.1. Three resolution rules to know

- Classes always win. A method declaration in the class or a superclass takes priority over any default method declaration in an interface
- Otherwise, subinterfaces win: the method with the same signature in the most specific default-providing interface is selected. (If B extends A, B is more specific than A.)
- Finally, if the choice is still ambiguous, the class inheriting from multiple interfaces has to explicitly select which default method implementation to use by overriding it and calling the desired method explicitly

### 13.4.2. Most specific default-providing interface wins

- If there are no methods in the class or superclass, the method with the most specific default-providing interface is selected. 
- From the example in the section, the compiler has a choice between the hello method from interface A and the hello method from interface B. Because B is more specific, the program prints "Hello from B" again.

_Quiz 13.2: Remember the resolution rules_

### 13.4.3. Conflicts and explicit disambiguation

If C inherits from both A and B interfaces, but B no longer extends A, then there is no interface hierarchy that can be selected from. So you have to explicitly call the method:

```
public class C implements B, A {
    void hello(){
        B.super.hello();               1
    }
}
```

### 13.4.4. Diamond problem

See the section for the famous diamond inheritance problem.

## SUMMARY

