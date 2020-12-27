package com.excella.modernjava.shop;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static com.excella.modernjava.shop.FakeLongRunningOp.delay;

public class Shop {
    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread( () -> {
            double price = calculatePrice(product);
            futurePrice.complete(price);
        }).start();
        return futurePrice;
    }

    private double calculatePrice(String product) {
        delay();
        var random = new Random();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }
}
