package com.excella.modernjava.chapter02.quiz2dot1.solution;

import com.excella.modernjava.domain.Apple;

public class AppleSimpleFormatter implements AppleFormatter {
    public String accept(final Apple apple) {
        return "An apple of " + apple.getWeight() + "g";
    }
}