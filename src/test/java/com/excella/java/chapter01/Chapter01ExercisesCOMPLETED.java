package com.excella.java.chapter01;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.excella.java.domain.Apple;

@MicronautTest
public class Chapter01ExercisesCOMPLETED {

	List<Apple> apples = Arrays.asList(
			new Apple(80, "green"),
			new Apple(155, "green"),
			new Apple(120, "red")
	);

	public static List<Apple> filterApples(final List<Apple> inventory, final Predicate<Apple> p) {
		final List<Apple> result = new ArrayList<>();
		for (final Apple apple : inventory) {
			if (p.test(apple)) {
				result.add(apple);
			}
		}
		return result;
	}

	/// Tests
	@Test
	public void exercise01_filter_apples_by_color() {
		final var result = filterApples(apples, apple -> apple.getColor().equals("green"));

		final var wrongResults = new ArrayList<Apple>();
		for (final Apple apple : result) {
			if (!apple.getColor().equals("green")) {
				wrongResults.add(apple);
			}
		}

		assertEquals(wrongResults.size(), 0);
	}

	@Test
	public void exercise01_filter_apples_by_weight() {
		final var result = filterApples(apples, (Apple a) -> a.getWeight() > 150);

		final var wrongResults = new ArrayList<Apple>();
		for (final Apple apple : result) {
			if (apple.getWeight() <= 150) {
				wrongResults.add(apple);
			}
		}

		assertEquals(wrongResults.size(), 0);
	}

}
