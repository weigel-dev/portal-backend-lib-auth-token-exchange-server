package dev.weigel.authlib.exception;

public class InvalidRefreshTokenException extends AuthLibException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
