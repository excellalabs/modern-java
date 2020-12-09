package com.excella.modernjava.common.generators;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.excella.modernjava.common.generators.Constants.ALL_MY_CHARS;

public class LongStringGenerator extends Generator<String> {

  public static final int MIN_CAPACITY = 300;
  public static final int CAPACITY = 400;

  public LongStringGenerator() {
    super(String.class);
  }

  @Override
  public String generate(SourceOfRandomness random, GenerationStatus status) {
    var newString = new StringBuilder(CAPACITY);
    for (int i = MIN_CAPACITY; i < CAPACITY; i++) {
      int randomIndex = random.nextInt(ALL_MY_CHARS.length());
      newString.append(ALL_MY_CHARS.charAt(randomIndex));
    }
    return newString.toString();
  }
}
