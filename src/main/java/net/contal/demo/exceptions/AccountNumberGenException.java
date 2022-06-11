package net.contal.demo.exceptions;

/**
 * Exception thrown when a request cannot be completed due to bad parameters or system state.
 * @author Bo Li
 */
public class AccountNumberGenException extends RuntimeException {

    public AccountNumberGenException(String message) {
        super(message);
    }

    public AccountNumberGenException(Throwable cause) {
        super(cause);
    }

    public AccountNumberGenException(String message, Throwable cause) {
        super(message, cause);
    }
}
