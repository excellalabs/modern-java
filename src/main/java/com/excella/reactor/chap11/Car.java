package com.excella.reactor.chap11;

import java.util.Optional;

public class Car {

  public Optional<Insurance> insurance;

  public Optional<Insurance> getInsurance() {
    return insurance;
  }

}