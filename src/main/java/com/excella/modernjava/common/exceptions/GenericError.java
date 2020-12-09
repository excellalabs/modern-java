package com.excella.modernjava.common.exceptions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
@RequiredArgsConstructor(staticName = "of")
public class GenericError implements Serializable {
    @NonNull private Integer code;
    @NonNull private String message;
}