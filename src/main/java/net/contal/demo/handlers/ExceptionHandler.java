package net.contal.demo.handlers;

import net.contal.demo.errors.ErrorResource;
import net.contal.demo.errors.ErrorResourceCreator;
import net.contal.demo.errors.ErrorType;
import net.contal.demo.exceptions.BadRequestException;
import net.contal.demo.exceptions.NotFoundException;
import net.contal.demo.exceptions.TransactionalException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class that acts as a catch-all exceptions that aren't handled by more specific @ControllerAdvice components
 * or exception handlers
 * @author Bo Li
 */
@ControllerAdvice
@Order(-1)
public class ExceptionHandler {

    @Autowired
    private ErrorResourceCreator errorResourceCreator;

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    /**
     * Catch bad request exceptions.
     *
     * @param ex thrown exception.
     * @return ErrorResource DTO wrapped exception.
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            BadRequestException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResource ExceptionHandler(BadRequestException ex) {
        logger.error("REST Controller {}", ExceptionUtils.getRootCauseMessage(ex));
        return errorResourceCreator.createErrorResource(ErrorType.BAD_REQUEST, ex, false);
    }

    /**
     * Catch entity not found exceptions.
     *
     * @param ex thrown exception.
     * @return ErrorResource DTO wrapped exception.
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            NotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResource ExceptionHandler(NotFoundException ex) {
        logger.error("REST Controller {}", ExceptionUtils.getRootCauseMessage(ex));
        return errorResourceCreator.createErrorResource(ErrorType.NOT_FOUND, ex, false);
    }

    /**
     * Catch transaction exceptions.
     *
     * @param ex thrown exception.
     * @return ErrorResource DTO wrapped exception.
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {
            TransactionalException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResource ExceptionHandler(TransactionalException ex) {
        logger.error("REST Controller {}", ExceptionUtils.getRootCauseMessage(ex));
        return errorResourceCreator.createErrorResource(ErrorType.GENERAL_ERROR, ex, false);
    }
}
