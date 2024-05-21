package org.currency.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.currency.accounts.client.CurrencyExchangeClient;
import org.currency.accounts.exception.custom.CurrencyExchangeFailedException;
import org.currency.accounts.model.CurrencyRate;
import org.currency.accounts.model.ExchangeCurrency;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRatesService {
    private final CurrencyExchangeClient currencyExchangeClient;

    public Double getActualRate() {
        ExchangeCurrency exchangeCurrency = currencyExchangeClient.getExchangeRates();

        validateExchangeCurrency(exchangeCurrency);

        return exchangeCurrency.getRates().stream()
                .map(CurrencyRate::getExchangeRate)
                .findFirst()
                .orElseThrow(CurrencyExchangeFailedException::new);
    }

    private void validateExchangeCurrency(ExchangeCurrency exchangeCurrency) {
        if (exchangeCurrency == null || exchangeCurrency.getRates() == null || exchangeCurrency.getRates().isEmpty()) {
            throw new CurrencyExchangeFailedException();
        }
    }
}
