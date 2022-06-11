package net.contal.demo.exceptions;

/**
 * Exception thrown when a request cannot be completed due to bad parameters or system state.
 * @author Bo Li
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
