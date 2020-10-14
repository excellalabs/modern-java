package com.excella.reactor;


import javax.swing.text.html.Option;

import com.excella.reactor.chap11.OptionalMain;
import com.excella.reactor.chap12DateTimeApi.DateAndTimeAPIHarness;
import com.excella.reactor.stocks.DslHarness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class ReactorApplication {

//     public static void main(final String[] args) throws Exception {
//         SpringApplication.run(ReactorApplication.class, args);
//     }
// }

public class ReactorApplication {

	public static void main (String[] args)
    {	
        //ParallelStreamHarness.run();

        DslHarness.run();

        OptionalMain optionalMain = new OptionalMain();
        optionalMain.run();

        DateAndTimeAPIHarness.run();
    }
}
