package net.contal.demo.exceptions;

/**
 * Exception thrown when a request cannot be completed due to bad parameters or system state.
 * @author Bo Li
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
