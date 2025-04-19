package dev.weigel.authlib.exception;

public class ExternalAuthenticationFailedException extends AuthLibException {
    public ExternalAuthenticationFailedException(String message) {
        super(message);
    }
}
