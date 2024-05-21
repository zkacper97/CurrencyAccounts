package org.currency.accounts.exception.custom;

public class CurrencyExchangeFailedException extends RuntimeException {
    public CurrencyExchangeFailedException() {
        super("Failed to fetch currency exchange rates.");
    }
}
