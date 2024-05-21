package org.currency.accounts.service;

import org.currency.accounts.client.CurrencyExchangeClient;
import org.currency.accounts.exception.custom.CurrencyExchangeFailedException;
import org.currency.accounts.model.CurrencyRate;
import org.currency.accounts.model.ExchangeCurrency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.currency.accounts.utils.CurrencyAccountsHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesServiceTest {

    @Mock
    private CurrencyExchangeClient currencyExchangeClient;

    @InjectMocks
    private ExchangeRatesService exchangeRatesService;

    @Test
    void shouldReturnActualRate() {
        // given
        mockCurrencyExchangeClientResponse();

        // when
        Double actualRate = exchangeRatesService.getActualRate();

        // then
        assertEquals(EXCHANGE_RATE, actualRate);
    }


    @Test
    void shouldThrowCurrencyExchangeFailedException() {
        // given
        mockEmptyCurrencyExchangeClientResponse();

        // when, then
        assertThrows(CurrencyExchangeFailedException.class,
                () -> exchangeRatesService.getActualRate());
    }

    private void mockCurrencyExchangeClientResponse() {
        when(currencyExchangeClient.getExchangeRates())
                .thenReturn(new ExchangeCurrency(CURRENCY_NAME, CURRENCY_CODE,
                        List.of(new CurrencyRate(EXCHANGE_RATE_NO, LocalDate.now(), EXCHANGE_RATE))));
    }

    private void mockEmptyCurrencyExchangeClientResponse() {
        when(currencyExchangeClient.getExchangeRates())
                .thenReturn(null);
    }
}