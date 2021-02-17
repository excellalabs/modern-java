package com.excella.modernjava.tempinfo.rxjava;

import com.excella.modernjava.tempinfo.TempInfo;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class TempInfoClient {
    public static void runTempInfoRxJava() {
        // 17.3.1
        //runTempInfoBasic();

        // More production-like, 17.14
        Observable<TempInfo> observable = getTemperature("New York");
        observable.blockingSubscribe(new TempObserver());

        Observable<TempInfo> observableCelsius = getTemperatureCelsius("New York");
        observableCelsius.blockingSubscribe(new TempObserver());
    }

    private static void runTempInfoBasic() {
        // Use this as the basis of another Observable that emits the temperature reported for a given town each second (17.3.1)
        Observable<Long> onePerSecond = Observable.interval(1, TimeUnit.MILLISECONDS);
        // Subscribe to onePerSec fetch and print the temperatures of the town of interest each second and be notified
        // NOTE: RxJava’s API are more flexible (have more overloaded variants) than the native Java 9 Flow API.
        //       I.E. You can subscribe to an Observable by passing a lambda expression with the signature of the onNext method and omitting the other 3 methods.
        //onePerSecond.subscribe(l -> System.out.println(TempInfo.fetch("New York")));
        // Use above in production, but for demoing, since the main method terminates immediately, you don't see any output
        // as the Observeable is executed on RxJava's computation thread pool, which is make of daemon threads
        onePerSecond.blockingSubscribe(x -> System.out.println(TempInfo.fetch("New York")));
    }

    /*
    Make more production grade:
        - Fix no error handling
        - emit temperatures once a second at most five times

    Achieve using a factory method named create that creates an Observable from a lambda,
    taking as an argument another Observer and returning void, as shown in the following listing.


    1 Creates an Observable from a function consuming an Observer
    2 An Observable emitting an infinite sequence of ascending longs, one per second
    3 Do something only if the consumed observer hasn’t been disposed yet (for a former error).
    4 If the temperature has been already emitted five times, completes the observer terminating the stream
    5 Otherwise, sends a temperature report to the Observer
    6 In case of error, notifies the Observer
     */
    public static Observable<TempInfo> getTemperature(String town) {
        return Observable.create(emitter ->
                Observable.interval(1, TimeUnit.SECONDS).subscribe(i -> {  // when subscribing, you pass the onNext
                        if (i >= 5) {
                            emitter.onComplete();
                        }
                        else {
                            try {
                                emitter.onNext(TempInfo.fetch(town)); // send temp to the Observer
                            }
                            catch (Exception e) {
                                emitter.onError(e);
                            }
                        }
                }));
    }

    private static Observable<TempInfo> getTemperatureCelsius(String town) {
        return getTemperature("New York")
                .map(temp -> new TempInfo(temp.getTown(),
                                    (temp.getTemp() - 32) * 5/9));
    }

}
