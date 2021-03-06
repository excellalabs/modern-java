package com.excella.modernjava.chapter02.quiz2dot1.solution;

import com.excella.modernjava.domain.Apple;

public class AppleFancyFormatter implements AppleFormatter {
    public String accept(Apple apple) {
        String characteristic = apple.getWeight() > 150 ? "heavy" : "light";
        return "A " + characteristic +
               " " + apple.getColor() +" apple";
    }
}