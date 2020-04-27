package com.excella.reactor.chapter01;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class Chapter01ExercisesCOMPLETED {

	List<Apple> apples = Arrays.asList(
			new Apple(80, "green"),
			new Apple(155, "green"),
			new Apple(120, "red")
	);

	public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if (p.test(apple)) {
				result.add(apple);
			}
		}
		return result;
	}

	/// Tests
	@Test
	public void exercise01_filter_apples_by_color() {
		var result = filterApples(apples, (Apple a ) -> a.getColor().equals("green"));

		var wrongResults = new ArrayList<Apple>();
		for (Apple apple : result) {
			if (!apple.getColor().equals("green")) {
				wrongResults.add(apple);
			}
		}

		assertEquals(wrongResults.size(), 0);
	}

	@Test
	public void exercise01_filter_apples_by_weight() {
		var result = filterApples(apples, (Apple a) -> a.getWeight() > 150);

		var wrongResults = new ArrayList<Apple>();
		for (Apple apple : result) {
			if (apple.getWeight() <= 150) {
				wrongResults.add(apple);
			}
		}

		assertEquals(wrongResults.size(), 0);
	}

}
