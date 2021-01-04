package com.excella.modernjava.shop;

import java.util.Random;

import static com.excella.modernjava.shop.FakeLongRunningOp.delay;
import static com.excella.modernjava.shop.Util.format;

public class Shop {
    private final String name;
    private final Random random;

    public Shop(String name) {
        this.name = name;
        random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
    }

    public String getPrice(String product) {
        double price = calculatePrice(product);
        return name + ":" + price;
    }

    public double calculatePrice(String product) {
        delay();
        return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
    }

    public String getName() {
        return name;
    }
}
