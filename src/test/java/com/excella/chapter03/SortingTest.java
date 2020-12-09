/*****************************************************************************

EXERCISE 3.7: Evolution of behavior parameterization

INSTRUCTIONS: 
- Use this test class along with the book's 3.7 exercise (it starts you off just before 3.7.2)
- Run the tests with: ./gradlew test --tests com.excella.reactor.chapter03.solution.*
- Try to do the following refactors to the apple ascending by weight sort without looking at the book or solution
- Create a new test for each of the following refactors:

1. Refactor test to use anonymous class (3.7.2)
2. Create new test and refactor to use lambda expressions (3.7.3)
    - Goal is this syntax: inventory.sort(comparing(apple -> apple.getWeight()));
3. Create a new test and apply method references (3.7.4)

*****************************************************************************/

package com.excella.chapter03;

import com.excella.domain.Apple;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;

@MicronautTest
public class SortingTest {

    List<Apple> inventory = Arrays.asList(
        new Apple(80, "green"),
        new Apple(155, "green"),
        new Apple(120, "red"),
        new Apple(180, "red")
    );

    @Test
    public void can_sort_apples_correctly() {
        inventory.sort(new AppleWeightComparator());

        //assertThat(inventory).usingElementComparator(new AppleWeightComparator()).isSorted();
    }
}
