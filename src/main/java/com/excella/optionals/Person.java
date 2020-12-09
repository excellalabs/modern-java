package com.excella.optionals;

import java.util.Optional;

public class Person {

  private Optional<Car> car;
  private int age;

  public Optional<Car> getCar() {
    return car;
  }

  public void setCar(Optional<Car> car) {
    this.car = car;
  }

  public int getAge() {
    return age;
  }

}
