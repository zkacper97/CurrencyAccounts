package org.currency.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CurrencyCode {
    USD("USD"),
    PLN("PLN");

    @Getter
    private String currency;
}
