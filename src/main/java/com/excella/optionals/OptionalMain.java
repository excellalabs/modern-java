package com.excella.optionals;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OptionalMain {

  public void run() {
    PersonV1 personV1 = new PersonV1();
    System.out.println("Insurance name v1: " + getCarInsuranceNameNullSafeV1(personV1));
    System.out.println("Insurance name v2: " + getCarInsuranceNameNullSafeV2(personV1));

    Optional<Person> optionalPerson = Optional.of(
      new Person() {
        { setCar(Optional.of(
          new Car() {
            { insurance = Optional.of(new Insurance() { { name = "My insurance"; }}); }}
          )); 
        }
      }
    );
    System.out.println("getCarInsuranceName taking optional person: " + getCarInsuranceName(optionalPerson));    

    List<Person> people = List.of((optionalPerson.get()), optionalPerson.get());
    System.out.println("getCarInsuranceNames: " + getCarInsuranceNames(people));

    // 11.3.6. Combining two Optionals
    var optionalCar = Optional.of(new Car());
    System.out.println("getCarInsuranceName taking optional person: " + nullSafeFindCheapestInsurance(optionalPerson, optionalCar).get().name);
    
    // Careful using Optional.of?
    // Person person1 = null;
    // System.out.println("ofNullable: " + Optional.of(person1)); 
  }

  public String getCarInsuranceNameNullSafeV1(PersonV1 person) {
    if (person != null) {
      CarV1 car = person.getCar();
      if (car != null) {
        Insurance insurance = car.getInsurance();
        if (insurance != null) {
          return insurance.getName();
        }
      }
    }
    return "Unknown";
  }

  public String getCarInsuranceNameNullSafeV2(PersonV1 person) {
    if (person == null) {
      return "Unknown";
    }
    CarV1 car = person.getCar();
    if (car == null) {
      return "Unknown";
    }
    Insurance insurance = car.getInsurance();
    if (insurance == null) {
      return "Unknown";
    }
    return insurance.getName();
  }

  // Doesn't compile:
  // - In (1), we try to invoke map(Person::getCar) on an Optional<Person>.
  //   Switching to flatMap() solves this issue.
  // - Then in (2), we try to invoke map(Car::getInsurance) on an Optional<Car>.
  //   Switching to flatMap() solves this issue.
  // There is no need to further "flatMap" since Insurance::getName returns
  // a plain String.
  /*public String getCarInsuranceName(Person person) {
    Optional<Person> optPerson = Optional.of(person);
    Optional<String> name = optPerson.map(Person::getCar) // (1)
        .map(Car::getInsurance) // (2)
        .map(Insurance::getName);
    return name.orElse("Unknown");
  }*/

  public String getCarInsuranceName(Optional<Person> person) {
    return person.flatMap(Person::getCar)
        .flatMap(Car::getInsurance)
        .map(Insurance::getName)
        .orElse("Unknown");
  }

  public Set<String> getCarInsuranceNames(List<Person> persons) {
    return persons.stream()
        .map(Person::getCar)
        .map(optCar -> optCar.flatMap(Car::getInsurance))
        .map(optInsurance -> optInsurance.map(Insurance::getName))
        .flatMap(Optional::stream)
        .collect(toSet());
  }

  // Quiz 11.1: Rewrite to one statement 
  public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car) {
    if (person.isPresent() && car.isPresent()) {
      return Optional.of(findCheapestInsurance(person.get(), car.get()));
    } else {
      return Optional.empty();
    }
  }

//   public Optional<Insurance> nullSafeFindCheapestInsurance(
//                               Optional<Person> person, Optional<Car> car) {
//     return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
// }

  public Insurance findCheapestInsurance(Person person, Car car) {
    // queries services provided by the different insurance companies
    // compare all those data
    Insurance cheapestCompany = new Insurance();
    return cheapestCompany;
  }

  public String getCarInsuranceName(Optional<Person> person, int minAge) {
    return person.filter(p -> p.getAge() >= minAge)
     .flatMap(Person::getCar)
     .flatMap(Car::getInsurance)
     .map(Insurance::getName)
     .orElse("Unknown");
  }

}
