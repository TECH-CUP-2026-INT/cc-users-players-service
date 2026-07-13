package co.edu.escuelaing.techcup.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a business rule is violated.
 * Results in a 400 Bad Request response.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}