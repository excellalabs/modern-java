package com.excella.modernjava.tempinfo;

import io.reactivex.rxjava3.core.Maybe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.Flow.Subscriber;
// You can swap out the Java Flow interface with reactivestreams:
// import org.reactivestreams.Subscriber;

public class TempSubscription implements Subscription {
    private final Subscriber<? super TempInfo> subscriber;
    private final String town;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TempSubscription(Subscriber<? super TempInfo> subscriber,
                            String town) {
        this.subscriber = subscriber;
        this.town = town;
    }

    @Override
    public void request(long n) {
        Maybe maybe;
        executor.submit(() -> { // Sends the next elements to the subscriber from a different thread, to prevent stackoverflow (see quiz 17.1)
            for (long i = 0L; i < n; i++) {
                try {
                    subscriber.onNext(TempInfo.fetch(town));
                } catch (Exception e) {
                    subscriber.onError(e);
                    break;
                }
            }
        });
    }

    @Override
    public void cancel() {
        subscriber.onComplete();
    }
}

