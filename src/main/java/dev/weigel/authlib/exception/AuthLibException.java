package dev.weigel.authlib.exception;

public class AuthLibException extends RuntimeException {
    public AuthLibException(String message) {
        super(message);
    }

    public AuthLibException(String message, Throwable cause) {
        super(message, cause);
    }
}
