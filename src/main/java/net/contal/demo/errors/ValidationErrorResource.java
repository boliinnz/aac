package net.contal.demo.errors;

import java.util.ArrayList;
import java.util.List;

/**
 * Response payload for returning field validation errors to the client.
 *
 * @author Bo Li
 */
public class ValidationErrorResource extends ErrorResource {

    private List<FieldErrorResource> fieldErrors = new ArrayList<>();

    public ValidationErrorResource(ErrorType errorType, String error) {
        super(errorType, error);
    }

    public void addFieldError(String field, String message) {
        fieldErrors.add(new FieldErrorResource(field, message));
    }

    public List<FieldErrorResource> getFieldErrors() {
        return fieldErrors;
    }
}
