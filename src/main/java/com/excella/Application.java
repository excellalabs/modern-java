package com.excella;

import com.excella.optionals.OptionalMain;
import com.excella.dateTimeApi.DateAndTimeAPIHarness;
import com.excella.parallelStreams.ParallelStreamHarness;
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
    }
}
