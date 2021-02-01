package com.excella.modernjava.tempinfo;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class TempSubscriber implements Subscriber<TempInfo> {
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        // Publisher calls back to this method with the Subscription object
        this.subscription = subscription;
        subscription.request(1); // send first request
    }

    @Override
    public void onNext(TempInfo tempInfo) {
        System.out.println(tempInfo);
        subscription.request(1); // make next request
    }

    @Override
    public void onError(Throwable t) {
        System.err.println(t.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Done!");
    }

}
