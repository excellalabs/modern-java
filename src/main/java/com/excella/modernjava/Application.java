package com.excella.modernjava;

import com.excella.modernjava.datetimeapi.DateAndTimeAPIHarness;
import com.excella.modernjava.optionals.OptionalMain;
import com.excella.modernjava.dsls.DslHarness;
import com.excella.modernjava.parallelstreams.ParallelStreamHarness;

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
    }
}
