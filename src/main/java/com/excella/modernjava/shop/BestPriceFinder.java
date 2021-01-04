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
        /// Exercise 16.3.1: compare total execution time when removing parallel
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %s",
                        shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

}
