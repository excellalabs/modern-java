package com.excella.reactor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class ReactorApplicationTests {

	private static List<String> words = Arrays.asList(
        "the",
        "quick",
        "brown",
        "fox",
        "jumped",
        "over",
        "the",
        "lazy",
        "dog"
				);

	private static Set<Long> idsSet1 = Set.of(1L, 2L);

	private static Set<Long> idsSet3 = Set.of(1L, 2L, 3L);

	private static List<String> names = Arrays.asList("Adam", "Alexander", "John", "Tom");

	@Test
	public void stream_filter() {
		assertTrue(idsSet1.stream()
				.filter(x-> x == 2L).toArray().length == 1);
	}

	// EXERCISES 1.1: Pass code as parameters

	@Test
	public void Exercise_1_1_Predicate_Chain(){
		Predicate<String> startsWithAPredicate =  str -> str.startsWith("A");
		Predicate<String> shortLengthPredicate = str -> str.length() < 5;

		List<String> result = names.stream()
				.filter(startsWithAPredicate.and(shortLengthPredicate))
				.collect(Collectors.toList());

		assertEquals(1, result.size());
		assertTrue(result.contains("Adam"));
	}

	@Test
	public void Exercise_1_2_Predicate_Chain_with_And(){
		List<Predicate<String>> allPredicates = new ArrayList<Predicate<String>>();
		allPredicates.add(str -> str.startsWith("A"));
		allPredicates.add(str -> str.contains("d"));
		allPredicates.add(str -> str.length() > 4);

		List<String> result = names.stream()
				.filter(allPredicates.stream().reduce(x->true, Predicate::and))
				.collect(Collectors.toList());

		assertEquals(1, result.size());
		assertTrue(result.contains("Alexander"));
	}


//	@Test
//	public void Exercise_1__1_b_Operators_With_Projection() {
//		List<Integer> ints = Arrays.asList(1,2,3);
//
//		ints.stream().map(x-> x*2).collect();
//	}

	// TEST CASES
	// filter

//	public static List<Apple> filterGreenApples(List<Apple> inventory) {
//		List<Apple> result = new ArrayList<>();                           1
//		for (Apple apple: inventory){
//			if (GREEN.equals(apple.getColor())) {                         2
//				result.add(apple);
//			}
//		}
//		return result;
//	}



}

