package dev.lavalink.youtube.plugin.rest;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {
    public RestException(String message) {
        super(message);
    }

    public RestException(HttpStatus httpStatus, String msg) {
        super(msg);
    }

    public RestException(HttpStatus httpStatus, String msg, Throwable lastException) {
        super(msg);
    }
}
