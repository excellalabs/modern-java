package com.excella.modernjava.shop;

import java.util.concurrent.Future;

public class ShopClient {
    public static void run() {
        AsyncShop shop = new AsyncShop();
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPrice("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime + " msecs");
        // Do some more tasks, like querying other shops, // while the price of the product is being calculated
        //doSomethingElse();
        try {
            double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");
    }

}
