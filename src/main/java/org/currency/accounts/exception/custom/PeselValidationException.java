package org.currency.accounts.exception.custom;

public class PeselValidationException extends RuntimeException {
    public PeselValidationException(String message) {
        super(message);
    }
}
