package com.excella.reactor;


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
    }
}
