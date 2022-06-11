package net.contal.demo.errors;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response payload for returning exception info to the client.
 * @author Bo Li
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResource {

    private ErrorType errorType;
    private String error;
}
