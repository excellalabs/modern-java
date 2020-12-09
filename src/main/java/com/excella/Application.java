package com.excella;

import com.excella.chap11.OptionalMain;
import com.excella.chap12DateTimeApi.DateAndTimeAPIHarness;
import com.excella.stocks.DslHarness;

public class Application {

    public static void main(String[] args) {
        //Micronaut.run(Application.class, args);

        ConsoleCode();
    }

    private static void ConsoleCode() {
        ParallelStreamHarness.run();
        DslHarness.run();

        OptionalMain optionalMain = new OptionalMain();
        optionalMain.run();

        DateAndTimeAPIHarness.run();


//        TempInfoSkaffold tempInfoSkaffold = new TempInfoSkaffold();
//        tempInfoSkaffold.run();
    }
}
