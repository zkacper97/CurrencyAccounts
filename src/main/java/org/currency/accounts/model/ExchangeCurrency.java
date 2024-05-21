package org.currency.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExchangeCurrency {
    private String currency;
    private String code;
    private List<CurrencyRate> rates;
}
