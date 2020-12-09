package com.excella.java;

import io.micronaut.runtime.Micronaut;

import com.excella.java.chap11.OptionalMain;
import com.excella.java.chap12DateTimeApi.DateAndTimeAPIHarness;
import com.excella.java.stocks.DslHarness;

// @SpringBootApplication
// public class ReactorApplication {

//     public static void main(final String[] args) throws Exception {
//         SpringApplication.run(ReactorApplication.class, args);
//     }
// }



//public class Application {
//
//	public static void main (String[] args)
//    {
//        //ParallelStreamHarness.run();
//
//        DslHarness.run();
//
//        OptionalMain optionalMain = new OptionalMain();
//        optionalMain.run();
//
//        DateAndTimeAPIHarness.run();
//    }
//}

public class Application {

    public static void main(String[] args) {
//        TempInfoSkaffold tempInfoSkaffold = new TempInfoSkaffold();
//        tempInfoSkaffold.run();

        Micronaut.run(Application.class, args);
    }
}
