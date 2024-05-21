package org.currency.accounts.utils;

import org.currency.accounts.client.CurrencyExchangeClient;
import org.currency.accounts.model.CurrencyRate;
import org.currency.accounts.model.ExchangeCurrency;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.currency.accounts.utils.CurrencyAccountsHelper.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public abstract class MockingForCurrencyApplication {

    @MockBean
    protected CurrencyExchangeClient currencyExchangeClient;

    protected void mockCurrencyExchangeClientResponse() {
        when(currencyExchangeClient.getExchangeRates())
                .thenReturn(new ExchangeCurrency(CURRENCY_NAME, CURRENCY_CODE,
                        List.of(new CurrencyRate(EXCHANGE_RATE_NO, LocalDate.now(), EXCHANGE_RATE))));
    }

    protected void mockEmptyCurrencyExchangeClientResponse() {
        when(currencyExchangeClient.getExchangeRates())
                .thenReturn(null);
    }
}
