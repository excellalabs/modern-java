package com.excella.reactor.chapter01;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class Chapter01Exercises {

	List<Apple> apples = Arrays.asList(
			new Apple(80, "green"),
			new Apple(155, "green"),
			new Apple(120, "red")
	);

	public static List<Apple> filterByColor(List<Apple> inventory, String color) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if (color.equals(apple.getColor())) {
				result.add(apple);
			}
		}
		return result;
	}

	public static List<Apple> filterByWeight(List<Apple> inventory, long weight) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if (apple.getWeight() > weight) {
				result.add(apple);
			}
		}
		return result;
	}

	/// Tests
	@Test
	public void filter_apples_by_color() {
		var result = filterByColor(apples, "green");

		var wrongResults = new ArrayList<Apple>();
		for (Apple apple : result) {
			if (!apple.getColor().equals("green")) {
				wrongResults.add(apple);
			}
		}

		assertEquals(wrongResults.size(), 0);
	}

	@Test
	public void filter_apples_by_weight() {
		var result = filterByWeight(apples, 150);

		var wrongResults = new ArrayList<Apple>();
		for (Apple apple : result) {
			if (apple.getWeight() <= 150) {
				wrongResults.add(apple);
			}
		}

		assertEquals(wrongResults.size(), 0);
	}

}
