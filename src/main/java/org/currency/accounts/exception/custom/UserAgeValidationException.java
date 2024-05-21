package org.currency.accounts.exception.custom;

public class UserAgeValidationException extends RuntimeException {
    public UserAgeValidationException(String pesel) {
        super("User with PESEL " + pesel + " is not an adult.");
    }
}
