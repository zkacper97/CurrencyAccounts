package org.currency.accounts.exception.custom;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String pesel) {
        super("Insufficient funds for user " + pesel + " to currency exchange.");
    }
}
