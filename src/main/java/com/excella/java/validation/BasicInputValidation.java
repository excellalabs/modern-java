package com.excella.java.validation;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.excella.java.validation.ValidatorWithResultTypeShould.ValidationResult.invalid;
import static com.excella.java.validation.ValidatorWithResultTypeShould.ValidationResult.valid;

public interface BasicInputValidation extends Function<String, ValidatorWithResultTypeShould.ValidationResult> {

  static BasicInputValidation textIsValidLength() {
    return holds(
        submittedAnswerDto -> {
          if (isInvalidSize(submittedAnswerDto)) return false;
          return true;
        },
        "input.invalidSize");
  }

  private static boolean isInvalidSize(String answer) {
    return !isValidLength(answer, "^.{1,2000}$");
  }

  static BasicInputValidation holds(Predicate<String> p, String message) {
    return submission ->
        p.test(submission)
            ? valid()
            : invalid(message, new Object[] {submission});
  }

  static BasicInputValidation holds(Predicate<String> p, ValidatorWithResultTypeShould.ValidationResult invalidResult) {
    return submission -> p.test(submission) ? valid() : invalidResult;
  }

  default BasicInputValidation and(BasicInputValidation other) {
    return submission -> {
      final ValidatorWithResultTypeShould.ValidationResult result = this.apply(submission);
      final ValidatorWithResultTypeShould.ValidationResult otherResult = other.apply(submission);
      return result.isValid() ? otherResult : result.addReasons(otherResult.getReasons());
    };
  }

  default BasicInputValidation or(BasicInputValidation other) {
    return submission -> {
      final ValidatorWithResultTypeShould.ValidationResult result = this.apply(submission);
      return result.isValid() ? other.apply(submission) : result;
    };
  }

  static boolean isValidLength(String answer, String lengthExpression) {
    return answer != null && answer.matches(lengthExpression);
  }
}
