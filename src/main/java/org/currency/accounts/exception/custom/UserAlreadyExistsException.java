package org.currency.accounts.exception.custom;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String pesel) {
        super("User with PESEL " + pesel + " already has an account.");
    }
}
