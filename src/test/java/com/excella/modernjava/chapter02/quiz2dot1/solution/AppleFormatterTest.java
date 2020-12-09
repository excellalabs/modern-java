package com.excella.modernjava.chapter02.quiz2dot1.solution;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import com.excella.modernjava.domain.Apple;

@MicronautTest
public class AppleFormatterTest {
    
    public String prettyPrintApple(List<Apple> inventory, AppleFormatter formatter) {
        StringBuilder output = new StringBuilder();
        for(Apple apple: inventory) {
            output.append(formatter.accept(apple) + "\n");
        }
        return output.toString();
    }

    List<Apple> inventory = Arrays.asList(
			new Apple(80, "green"),
			new Apple(155, "green"),
			new Apple(120, "red")
    );
    
    @Test
    public void apple_formatter_test() {
        var fancyOutput = prettyPrintApple(inventory, new AppleFancyFormatter());
        var simpleOutput = prettyPrintApple(inventory, new AppleSimpleFormatter());

        System.out.println("FANCY: " + fancyOutput + "\n");
        System.out.println("SIMPLE: " + simpleOutput);

        assertNotNull(fancyOutput);
    }

}

