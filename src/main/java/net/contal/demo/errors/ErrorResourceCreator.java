package net.contal.demo.errors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Include the stack trace in an error resource
 * @author Bo Li
 */
@Component
public class ErrorResourceCreator {

    public ErrorResource createErrorResource(ErrorType errorType, String message, Exception exception, boolean isErrorStackTraceEnabled) {
        if (isErrorStackTraceEnabled) {
            return new FullErrorResource(errorType, message, prettyPrintStackTrace(exception));
        } else {
            return new ErrorResource(errorType, message);
        }
    }

    public ErrorResource createErrorResource(ErrorType errorType, Exception exception, boolean isErrorStackTraceEnabled) {
        return createErrorResource(errorType, ExceptionUtils.getRootCauseMessage(exception), exception, isErrorStackTraceEnabled);
    }

    public ValidationErrorResource createValidationErrorResource(MethodArgumentNotValidException exception) {
        ValidationErrorResource errorResource = new ValidationErrorResource(ErrorType.VALIDATION_ERROR, "Field validation errors");
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errorResource.addFieldError(fieldError.getField(), fieldError.getDefaultMessage()) );
        return errorResource;
    }

    private static String prettyPrintStackTrace(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
