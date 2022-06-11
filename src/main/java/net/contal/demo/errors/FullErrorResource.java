package net.contal.demo.errors;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Bo Li
 */
@Getter
@Setter
public class FullErrorResource extends ErrorResource {

    private String errorDetail;

    public FullErrorResource(ErrorType errorType, String error, String errorDetail) {
        super(errorType, error);
        this.errorDetail = errorDetail;
    }
}
