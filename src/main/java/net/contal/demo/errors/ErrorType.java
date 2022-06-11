package net.contal.demo.errors;

import org.springframework.http.HttpStatus;

/**
 * @author Bo Li
 */
public enum ErrorType {
    /**
     * Unhandled or unrecoverable errors, a catch-all category
     */
    GENERAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Invalid request data, context or structure.
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST),

    /**
     * You arent allowed to do that, I'm dobbing
     */
    FORBIDDEN(HttpStatus.FORBIDDEN),

    /**
     * Invalid username/password
     */
    UNAUTHORISED(HttpStatus.UNAUTHORIZED),

    /**
     * Target not found
     */
    NOT_FOUND(HttpStatus.NOT_FOUND),

    /**
     * A unique constraint has been violated.
     */
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST),

    /**
     * JSR-303 related field validation error
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),

    ALREADY_EXISTS(HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED);

    private HttpStatus httpStatus;

    ErrorType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
