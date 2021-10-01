package dev.turgaycan.springboothttp2.controller.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper=false)
@Data
@RequiredArgsConstructor
public class CommonStringResponse extends ResponseStatus implements Serializable {
    private final String value;
}
