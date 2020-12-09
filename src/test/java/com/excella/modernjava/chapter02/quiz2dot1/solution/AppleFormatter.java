package com.excella.modernjava.chapter02.quiz2dot1.solution;

import com.excella.modernjava.domain.Apple;

@FunctionalInterface
public interface AppleFormatter {
    String accept(Apple a);
}
