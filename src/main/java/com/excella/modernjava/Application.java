package com.excella.modernjava;

import com.excella.modernjava.shop.ShopClient;

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

        ShopClient.run();
    }
}
