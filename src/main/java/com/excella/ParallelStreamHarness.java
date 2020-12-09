package com.excella;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

public class ParallelStreamHarness {

  public static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

  public static void run() {
    System.out.println("Iterative Sum done in: " + measurePerf(ParallelStreams::iterativeSum, 10_000_000L) + " msecs");
    System.out.println("Sequential Sum done in: " + measurePerf(ParallelStreams::sequentialSum, 10_000_000L) + " msecs");
    System.out.println("Parallel sum done in: " + measurePerf(ParallelStreams::parallelSum, 10_000_000L) + " msecs" );
    System.out.println("Range sum done in: " + measurePerf(ParallelStreams::rangedSum, 10_000_000L) + " msecs");
    System.out.println("Parallel range sum done in: " + measurePerf(ParallelStreams::parallelRangedSum, 10_000_000L) + " msecs" );
    System.out.println("SideEffect traditional sum done in: " + measurePerf(ParallelStreams::sideEffectSum, 10_000_000L) + " msecs" );
    // NOTE, INACCURATE because of a data race on every access to total
    System.out.println("SideEffect parallel sum done in: " + measurePerf(ParallelStreams::sideEffectParallelSum, 10_000_000L) + " msecs" );
    // WARNING: Trying to parallize an iterate operation can use up your memory
    //System.out.println("Parallel iterate forkJoinSum done in: " + measurePerf(ParallelStreams::parallelIterateSum, 10_000_000L) + " msecs" );

    // 7.2 Using fork/join framework directly    
    System.out.println("ForkJoin sum done in: " + measurePerf(ForkJoinSumCalculator::forkJoinSum, 10_000_000L) + " msecs" );

    // Custom Spliterator
    WordCount.run();

    
  }

  public static <T, R> long measurePerf(Function<T, R> f, T input) {
    long fastest = Long.MAX_VALUE;
    for (int i = 0; i < 10; i++) {
      long start = System.nanoTime();
      R result = f.apply(input);
      long duration = (System.nanoTime() - start) / 1_000_000;
      System.out.println("Result: " + result);
      if (duration < fastest) {
        fastest = duration;
      }
    }
    return fastest;
  }

}
