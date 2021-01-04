package com.excella.modernjava.shop;

public class BestPriceFinderClient {

    private static BestPriceFinder bestPriceFinder = new BestPriceFinder();

    public static void run() {
        long start = System.nanoTime();
        System.out.println("\n\nfindPrices: " + bestPriceFinder.findPrices("myPhone27S"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }
}
