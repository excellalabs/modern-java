package com.excella.modernjava.shop;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

public class BestPriceFinder {
    List<Shop> shops = List.of(new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"));

    public List<String> findPrices(String product) {

        /// Exercise 16.3.2:
        ///     - the below commented code has to wait for all prices to return before the list can return, as it returns List<CompletableFuture<String>>
        ///     - Refactored betlow that to fix

        //        return shops.stream()
        //                .map(shop -> CompletableFuture.supplyAsync(
        //                        () -> String.format("%s price is %s", shop.getName(), shop.getPrice(product))))
        //                .collect(toList());

        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %s", shop.getName(), shop.getPrice(product))))
                .collect(toList());
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

}
