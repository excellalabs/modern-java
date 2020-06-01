/*****************************************************************************

EXERCISE 3.7: Evolution of behavior parameterization

INSTRUCTIONS: 
- Use this test class (it starts you off just before 3.7.2)
- Run the tests with: ./gradlew test --tests com.excella.reactor.chapter03.solution.*
- Try to do the following refactors to the apple sort without looking at the book or solution
- Create a new test here for each version of the sort:

1. Refactor test to use anonymous class (3.7.2)
2. Create new test and refactor to use lambda expressions (3.7.3)
    - Goal is this syntax: inventory.sort(comparing(apple -> apple.getWeight()));
3. Create a new test and apply method references (3.7.4)

*****************************************************************************/

package com.excella.reactor.chapter03.solution;

import static java.util.Comparator.comparing;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.excella.reactor.domain.Apple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SortingTest {

    List<Apple> inventory = Arrays.asList(
			new Apple(80, "green"),
			new Apple(155, "green"),
			new Apple(120, "red")
	);

    @Test
    public void can_sort_apples_correctly_with_anonymous_class() {
        inventory.sort(new Comparator<Apple>() {
            public int compare(Apple a1, Apple a2) {
                return a1.getWeight() - a2.getWeight();
            }
        });

        assertEquals(inventory.get(1).getWeight(), 120);
    }

    @Test
    public void can_sort_apples_correctly_with_lambda() {
        inventory.sort((a1, a2) -> a1.getWeight() - a2.getWeight());

        assertEquals(inventory.get(1).getWeight(), 120);
    }

    @Test
    public void can_sort_apples_correctly_with_lambda_comparing() {
        inventory.sort(comparing(apple -> apple.getWeight()));

        assertEquals(inventory.get(1).getWeight(), 120);
    }

    @Test
    public void can_sort_apples_correctly_including_method_reference() {
        inventory.sort(comparing(Apple::getWeight));

        assertEquals(inventory.get(1).getWeight(), 120);
    }
}
