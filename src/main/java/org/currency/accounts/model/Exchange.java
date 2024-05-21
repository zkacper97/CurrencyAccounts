package org.currency.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Exchange {
    private String pesel;
    private BigDecimal amount;
    private CurrencyCode fromCurrency;
    private CurrencyCode toCurrency;
}
