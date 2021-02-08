package com.excella.modernjava.tempinfo;

import java.util.concurrent.Flow.Publisher;

public class TempInfoClient {
    public static void runTempInfo() {
        // 1. Creates a new Publisher, of temperatures in New York and subscribes the TempSubscriber to it
        getTemperatures("Washington, DC").subscribe(new TempSubscriber());
    }

    private static Publisher<TempInfo> getTemperatures(String town) {
        // 2. Returns a Publisher that sends a TempSubscription to the Subscriber that subscribes
        return subscriber -> {
            TempProcessor processor = new TempProcessor();
            processor.subscribe(subscriber);
            processor.onSubscribe(new TempSubscription(processor, town));
        };
    }
}
