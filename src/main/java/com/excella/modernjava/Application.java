package com.excella.modernjava;

import com.excella.modernjava.shop.AsyncShopClient;
import com.excella.modernjava.shop.BestPriceFinderClient;
import com.excella.modernjava.tempinfo.TempInfoClient;

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

        // Chapter 16 - Completeablefuture 
        //AsyncShopClient.run();
        //BestPriceFinderClient.run();
        
        // Chapter 17 - Reactive & RxJava
        //TempInfoClient.runTempInfo();
        com.excella.modernjava.tempinfo.rxjava.TempInfoClient.runTempInfoRxJava();
    }
}
