//package com.excella.java.common.reactor;
//
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
//
//import java.util.Optional;
//import java.util.concurrent.Callable;
//
//public class MonoUtils {
//    public static <T> Mono<T> fromCallable(Callable<T> supplier) {
//        return Mono.fromCallable(supplier).subscribeOn(Schedulers.elastic());
//    }
//
//    public static <T> Mono<T> fromCallableOpt(Callable<Optional<T>> supplier) {
//        return Mono.fromCallable(supplier).flatMap(Mono::justOrEmpty).subscribeOn(Schedulers.elastic());
//    }
//
//    public static <T> Flux<T> retrieveAsList(Callable<Iterable<T>> supplier) {
//        return Mono.fromCallable(supplier)
//                .flatMapMany(Flux::fromIterable)
//                .subscribeOn(Schedulers.elastic());
//    }
//}
