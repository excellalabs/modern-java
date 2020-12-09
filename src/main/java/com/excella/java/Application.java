package com.excella.java;

import io.micronaut.runtime.Micronaut;

import com.excella.java.chap11.OptionalMain;
import com.excella.java.chap12DateTimeApi.DateAndTimeAPIHarness;
import com.excella.java.stocks.DslHarness;

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
