package com.excella.modernjava.shop;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

public class BestPriceFinder {
    List<Shop> shops = List.of(new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"));

    private final Executor executor = Executors.newFixedThreadPool(shops.size(), (Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    public List<String> findPrices(String product) {

        /// Exercise 16.3.2:
        ///     - the below commented code has to wait for all prices to return before the list can return, as it returns List<CompletableFuture<String>>
        ///     - Refactored betlow that to fix

        //        return shops.stream()
        //                .map(shop -> CompletableFuture.supplyAsync(
        //                        () -> String.format("%s price is %s", shop.getName(), shop.getPrice(product))))
        //                .collect(toList());

        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() ->
                             String.format("%s price is %s", shop.getName(), shop.getPrice(product)), executor))
                .collect(toList());
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    /*  ## 16.4.1
        Runtime:
        - 5 seconds required to query the five shops sequentially
        - plus 5 seconds consumed by the discount service in applying the discount code to the prices returned by the five shops
     */
    public List<String> findPricesSequential(String product) {
        return shops.stream()
                .map(shop -> shop.getPrice(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(toList());
    }

    /*  ## 16.4.3 Composing synchronous and asynchronous operations

     */
    public List<String> findPricesAsync(String product) {
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor)))
                .collect(toList());
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

}
