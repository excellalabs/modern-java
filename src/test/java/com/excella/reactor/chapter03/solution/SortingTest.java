package com.excella.reactor.chapter03.solution;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.excella.reactor.chapter03.AppleComparator;
import com.excella.reactor.domain.Apple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SortingTest {

    List<Apple> inventory = Arrays.asList(
			new Apple(80, "green"),
			new Apple(155, "green"),
            new Apple(120, "red"),
            new Apple(180, "red")
    );
    
    @Test
    public void can_sort_apples_correctly() {
        inventory.sort(new AppleComparator());

        assertThat(inventory).usingElementComparator(new AppleComparator()).isSorted();
    }

    @Test
    public void can_sort_apples_correctly_with_anonymous_class() {
        var byWeight = new Comparator<Apple>() {
            public int compare(Apple a1, Apple a2) {
                return a1.getWeight() - a2.getWeight();
            }
        };

        inventory.sort(byWeight);

        assertThat(inventory).usingElementComparator(byWeight).isSorted();
    }

    @Test
    public void can_sort_apples_correctly_with_lambda() {
        Comparator<Apple> byWeight = (a1, a2) -> a1.getWeight() - a2.getWeight();

        inventory.sort(byWeight);

        assertThat(inventory).usingElementComparator(byWeight).isSorted();
    }

    @Test
    public void can_sort_apples_correctly_with_lambda_comparing() {
        Comparator<Apple> byWeight = comparing(apple -> apple.getWeight());
        
        inventory.sort(byWeight);

        assertThat(inventory).usingElementComparator(byWeight).isSorted();
    }

    @Test
    public void can_sort_apples_correctly_including_method_reference() {
        var byWeight = comparing(Apple::getWeight); 

        inventory.sort(byWeight);

        assertThat(inventory).usingElementComparator(byWeight).isSorted();
    }
}
