package com.excella.java.chapter02.quiz2dot1.solution;

import com.excella.java.domain.Apple;

@FunctionalInterface
public interface AppleFormatter {
    String accept(Apple a);
}
