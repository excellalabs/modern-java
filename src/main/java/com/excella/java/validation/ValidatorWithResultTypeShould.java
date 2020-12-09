package com.excella.java.validation;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ValidatorWithResultTypeShould {

  public interface ValidationResult {

    static ValidationResult valid() {
      return ValidationSupport.valid();
    }

    static ValidationResult invalid(@NotNull ValidationError reason) {
      return new Invalid(reason);
    }

    static ValidationResult invalid(@NotNull String reason) {
      return new Invalid(ValidationError.of(reason));
    }

    static ValidationResult invalid(@NotNull String reason, Object[] args) {
      return new Invalid(ValidationError.of(reason, args));
    }

    boolean isValid();

    Set<ValidationError> getReasons();

    ValidationResult addReasons(Set<ValidationError> reasons);
  }

  private static final class Invalid implements ValidationResult {

    private final Set<ValidationError> reasons;

    Invalid(ValidationError reason) {
      this.reasons = new HashSet<>();
      reasons.add(reason);
    }

    Invalid(Set<ValidationError> reasons) {
      this.reasons = reasons;
    }

    public boolean isValid() {
      return false;
    }

    public Set<ValidationError> getReasons() {
      return reasons;
    }

    public Invalid addReasons(Set<ValidationError> newReasons) {
      reasons.addAll(newReasons);
      return new Invalid(reasons);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Invalid invalid = (Invalid) o;
      return Objects.equals(reasons, invalid.reasons);
    }

    @Override
    public int hashCode() {
      return Objects.hash(reasons);
    }

    @Override
    public String toString() {
      return "Invalid[" + "reasons='" + reasons + '\'' + ']';
    }
  }

  private static final class ValidationSupport {
    private static final ValidationResult valid =
        new ValidationResult() {
          public boolean isValid() {
            return true;
          }

          public Set<ValidationError> getReasons() {
            return new HashSet<>();
          }

          public Invalid addReasons(Set<ValidationError> newReasons) {
            return new Invalid(newReasons);
          }
        };

    static ValidationResult valid() {
      return valid;
    }
  }
}
