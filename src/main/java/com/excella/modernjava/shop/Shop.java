package com.excella.modernjava.shop;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static com.excella.modernjava.shop.FakeLongRunningOp.delay;

public class Shop {
    /// ### 16.2.2: Error handling added manually for demonstration, then refactored to helper method below

    //    public Future<Double> getPriceAsync(String product) {
    //        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
    //        // 16.2.2
    //        new Thread( () -> {
    //            try {
    //                double price = calculatePrice(product);
    //                futurePrice.complete(price);
    //            } catch (Exception ex) {
    //                futurePrice.completeExceptionally(ex);
    //            }
    //        }).start();
    //        return futurePrice;
    //    }
    public Future<Double> getPriceAsync(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    private double calculatePrice(String product) {
        delay();
        var random = new Random();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }
}
