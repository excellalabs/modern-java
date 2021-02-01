package com.excella.modernjava.tempinfo;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
//import org.reactivestreams.Subscriber;

public class TempSubscription implements Subscription {
    private final Flow.Subscriber<? super TempInfo> subscriber;
    private final String town;

    public TempSubscription(Flow.Subscriber<? super TempInfo> subscriber,
                            String town) {
        this.subscriber = subscriber;
        this.town = town;
    }

    @Override
    public void request(long n) {
        for (long i = 0L; i < n; i++) {
            try{
                subscriber.onNext(TempInfo.fetch(town));
            }
            catch (Exception e) {
                subscriber.onError(e);
                break;
            }
        }
    }

    @Override
    public void cancel() {
        subscriber.onComplete();
    }
}

