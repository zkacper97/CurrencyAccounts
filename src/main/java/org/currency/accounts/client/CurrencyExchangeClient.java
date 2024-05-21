package org.currency.accounts.client;

import org.currency.accounts.model.ExchangeCurrency;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "currency-exchange-service", url = "${configuration.exchange.service.url}")
public interface CurrencyExchangeClient {

    @GetMapping
    ExchangeCurrency getExchangeRates();
}
