package dev.joaov.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailExistsException extends ResponseStatusException {
    public EmailExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
