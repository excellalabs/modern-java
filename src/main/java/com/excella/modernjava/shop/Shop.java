package com.excella.modernjava.shop;

import static com.excella.modernjava.shop.FakeLongRunningOp.delay;

public class Shop {
    public double getPrice(String product) {
        return calculatePrice(product);
    }

    private double calculatePrice(String product) {
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }
}
