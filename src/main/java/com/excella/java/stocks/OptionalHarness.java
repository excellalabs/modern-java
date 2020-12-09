package com.excella.java.stocks;

import java.util.Properties;

public class OptionalHarness {
    public void run() {

        Properties props = new Properties();
        props.setProperty("a", "5");
        props.setProperty("b", "true");
        props.setProperty("c", "-3");

        System.out.println(readDuration(props, "abc"));
    }

    public int readDuration(Properties props, String name) {
        String value = props.getProperty(name);
        if (value != null) {                            
            try {
                int i = Integer.parseInt(value);        
                if (i > 0) {                            
                    return i;
                }
            } catch (NumberFormatException nfe) { }
        }
        return 0;                                       
    }

    // Quiz 11.3
    // 
    // Reading duration from a property by using an Optional
    // Using the features of the Optional class and the utility method 
    // of listing 11.7, try to reimplement the imperative method of 
    // listing 11.8 with a single fluent statement:

}
