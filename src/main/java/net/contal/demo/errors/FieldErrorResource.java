package net.contal.demo.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Bo Li
 */
@Getter
@AllArgsConstructor
public class FieldErrorResource {

    private String field;
    private String message;
}
