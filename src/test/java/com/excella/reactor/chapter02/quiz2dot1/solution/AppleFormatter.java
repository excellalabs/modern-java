package com.excella.reactor.chapter02.quiz2dot1.solution;

import com.excella.reactor.domain.Apple;

@FunctionalInterface
public interface AppleFormatter {
    String accept(Apple a);
}
