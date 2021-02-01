package com.excella.modernjava.tempinfo;

import java.util.concurrent.Flow.Publisher;

public class TempInfoClient {
    public static void runTempInfo() {
        getTemperatures("Washington, DC").subscribe(new TempSubscriber());
    }

    private static Publisher<TempInfo> getTemperatures(String town) {
        return subscriber -> subscriber.onSubscribe(
                new TempSubscription(subscriber, town));
    }
}
