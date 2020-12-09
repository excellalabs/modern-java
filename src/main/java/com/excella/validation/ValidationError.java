package com.excella.validation;

import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ValidationError {
  private String messageKey;
  private Object[] messageParameters;

  public static ValidationError of(@NotNull String messageKey) {
    return new ValidationError(messageKey, null);
  }

  public static ValidationError of(@NotNull String messageKey, Object[] messageParameters) {
    return new ValidationError(messageKey, messageParameters);
  }
}
