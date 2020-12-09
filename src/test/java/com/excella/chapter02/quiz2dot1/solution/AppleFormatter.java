package com.excella.chapter02.quiz2dot1.solution;

import com.excella.domain.Apple;

@FunctionalInterface
public interface AppleFormatter {
    String accept(Apple a);
}
