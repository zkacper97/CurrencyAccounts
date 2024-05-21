package org.currency.accounts.exception.custom;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String pesel) {
        super("User with PESEL " + pesel + " doesn't exist.");
    }
}
