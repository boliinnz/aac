package net.contal.demo.exceptions;

/**
 * Exception thrown when a request cannot be completed due to bad database transaction.
 * @author Bo Li
 */
public class TransactionalException extends RuntimeException {
    public TransactionalException(String message) {
        super(message);
    }
    public TransactionalException(Throwable cause) {
        super(cause);
    }
    public TransactionalException(String message, Throwable cause) {
        super(message, cause);
    }
}
