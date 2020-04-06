package com.excella.reactor.validation;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.internal.generator.NullAllowed;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class BasicInputValidationTests {

  @Test
  public void input_invalid_from_long_value_example_based() {
    final String input = StringUtils.repeat("Hello world", 200);

    var result = BasicInputValidation.textIsValidLength().apply(input);

    assertFalse(result.isValid());
    assertEquals(
        "Didn't fail validation as notValid",
        "input.invalidSize",
        result.getReasons().iterator().next().getMessageKey());
  }

  @Test
  public void input_invalid_from_short_value_example_based() {
    final String input = "";

    var result = BasicInputValidation.textIsValidLength().apply(input);

    assertFalse(result.isValid());
    assertEquals(
        "Didn't fail validation as notValid",
        "input.invalidSize",
        result.getReasons().iterator().next().getMessageKey());
  }

  @Test
  public void input_invalid_from_null_value_example_based() {
    final String input = null;

    var result = BasicInputValidation.textIsValidLength().apply(input);

    assertFalse(result.isValid());
    assertEquals(
        "Didn't fail validation as notValid",
        "input.invalidSize",
        result.getReasons().iterator().next().getMessageKey());
  }

  @Property
  public void input_invalid_from_invalid_value(@NullAllowed(probability = 1.0f) String input) {
    final String freeFormAnswer = input;

    var result = BasicInputValidation.textIsValidLength().apply(input);

    assertFalse(result.isValid());
    assertEquals(
        "Didn't fail validation as notValid",
        "input.invalidSize",
        result.getReasons().iterator().next().getMessageKey());
  }

}
