package com.excella.reactor;

import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
* 
* Generating a stream of numbers n sized, evoling from sequential iterative, 
* through incorrect parallelization, to optimized parallel
*  
* INPUT: number to iterate to
*
* OPERATIONS: 
*    - sequential iterative
*    - sequential stream
*    - parallel stream, unoptimized
*    - ranged sequential stream
*    - ranaged (optimized) parallel stream
*    - sum using mutable list
*    - parallel sum using mutable list, side effects from data access race
*
* OUTPUT: numbers
* 
*/

public class ParallelStreams {

  public static long iterativeSum(long n) {
    long result = 0;
    for (long i = 0; i <= n; i++) {
      result += i;
    }
    return result;
  }

  public static long sequentialSum(long n) {
    return Stream.iterate(1L, i -> i + 1)
                 .limit(n)
                 .reduce(Long::sum).get();
  }

  public static long parallelSum(long n) {
    return Stream.iterate(1L, i -> i + 1)
                 .limit(n)
                 .parallel()
                 .reduce(Long::sum).get();
  }

  public static long rangedSum(long n) {
    return LongStream.rangeClosed(1, n)
                     .reduce(Long::sum).getAsLong();
  }

  public static long parallelRangedSum(long n) {
    return LongStream.rangeClosed(1, n)
                     .parallel()
                     .reduce(Long::sum).getAsLong();
  }

  // NOTE: this causes a memory leak
  public static long parallelIterateSum(long n) {
    return LongStream.iterate(1L, i -> i + 1)
                     .parallel()
                     .reduce(Long::sum).getAsLong();
  }

  public static long sideEffectSum(long n) {
    Accumulator accumulator = new Accumulator();
    LongStream.rangeClosed(1, n).forEach(accumulator::add);
    return accumulator.total;
  }

  public static long sideEffectParallelSum(long n) {
    Accumulator accumulator = new Accumulator();
    LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
    return accumulator.total;
  }

  public static class Accumulator {

    private long total = 0;

    public void add(long value) {
      total += value;
    }

  }

}