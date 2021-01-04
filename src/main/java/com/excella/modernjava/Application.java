package com.excella.modernjava;

import com.excella.modernjava.shop.AsyncShopClient;
import com.excella.modernjava.shop.BestPriceFinderClient;

public class Application {

    public static void main(String[] args) {
        //Micronaut.run(Application.class, args);

        ConsoleCode();
    }

    private static void ConsoleCode() {
        //        ParallelStreamHarness.run();
        //
        //        DslHarness.run();
        //
        //        OptionalMain optionalMain = new OptionalMain();
        //        optionalMain.run();
        //
        //        DateAndTimeAPIHarness.run();

        AsyncShopClient.run();

        BestPriceFinderClient.run();
    }
}
