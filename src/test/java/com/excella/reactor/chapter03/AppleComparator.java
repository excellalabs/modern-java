package com.excella.reactor.chapter03;

import java.util.Comparator;

import com.excella.reactor.domain.Apple;

public class AppleComparator implements Comparator<Apple> {
    public int compare(Apple a1, Apple a2){
        return a1.getWeight() - a2.getWeight();
    }
}