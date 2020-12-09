//package com.excella.java.chapter03.solution;
//
//import static java.util.Comparator.comparing;
//
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.List;
//
//import com.excella.java.chapter03.AppleWeightComparator;
//import com.excella.java.domain.Apple;
//
//import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//@MicronautTest
//public class SortingTest {
//
//    List<Apple> inventory = Arrays.asList(
//        new Apple(80, "green"),
//        new Apple(155, "green"),
//        new Apple(120, "red"),
//        new Apple(180, "red")
//    );
//
//    @Test
//    public void can_sort_apples_correctly() {
//        inventory.sort(new AppleWeightComparator());
//
//        assertThat(inventory).usingElementComparator(new AppleWeightComparator()).isSorted();
//    }
//
//    @Test
//    public void can_sort_apples_correctly_with_anonymous_class() {
//        var byWeight = new Comparator<Apple>() {
//            public int compare(Apple a1, Apple a2) {
//                return a1.getWeight() - a2.getWeight();
//            }
//        };
//
//        inventory.sort(byWeight);
//
//        assertThat(inventory).usingElementComparator(byWeight).isSorted();
//    }
//
//    @Test
//    public void can_sort_apples_correctly_with_lambda() {
//        Comparator<Apple> byWeight = (a1, a2) -> a1.getWeight() - a2.getWeight();
//
//        inventory.sort(byWeight);
//
//        assertThat(inventory).usingElementComparator(byWeight).isSorted();
//    }
//
//    @Test
//    public void can_sort_apples_correctly_with_lambda_comparing() {
//        Comparator<Apple> byWeight = comparing(apple -> apple.getWeight());
//
//        inventory.sort(byWeight);
//
//        assertThat(inventory).usingElementComparator(byWeight).isSorted();
//    }
//
//    @Test
//    public void can_sort_apples_correctly_including_method_reference() {
//
//        // List<Integer> numbers1 = Arrays.asList(1, 2, 3);
//        // List<Integer> numbers2 = Arrays.asList(3, 4);
//        // List<int[]> pairs =
//        //     numbers1.stream()
//        //             .flatMap(i ->
//        //                     numbers2.stream()
//        //                             .filter(j -> (i + j) % 3 == 0)
//        //                             .map(j -> new int[]{i, j})
//        //                     )
//        //             .collect(toList());
//
//        // System.out.println(numbers1);
//        // System.out.println(numbers2);
//
//
//
//        // var byWeight = comparing(Apple::getWeight);
//
//        // inventory.sort(byWeight);
//
//        // assertThat(inventory).usingElementComparator(byWeight).isSorted();
//
//        // List<String> names = menu.stream()
//        //         .filter(dish -> {
//        //                         System.out.println("filtering:" + dish.getName());
//        //                         return dish.getCalories() > 300;
//        //                     })                                                    1
//        //         .map(dish -> {
//        //                     System.out.println("mapping:" + dish.getName());
//        //                     return dish.getName();
//        //                 })                                                       2
//        //          .limit(3)
//        //         .collect(toList());
//
//        // System.out.println(names);
//    }
//}
