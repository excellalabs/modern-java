package com.excella.java.chapter03;

import java.util.Comparator;

import com.excella.java.domain.Apple;

public class AppleWeightComparator implements Comparator<Apple> {
    public int compare(Apple a1, Apple a2){
        return a1.getWeight() - a2.getWeight();
    }
}