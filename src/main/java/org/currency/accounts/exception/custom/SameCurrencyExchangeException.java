package org.currency.accounts.exception.custom;

public class SameCurrencyExchangeException extends RuntimeException {
    public SameCurrencyExchangeException() {
        super("Currencies to exchange should not be the same.");
    }
}
