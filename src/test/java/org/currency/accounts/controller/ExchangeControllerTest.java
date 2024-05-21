package org.currency.accounts.controller;

import org.currency.accounts.controller.mapper.ExchangeMapper;
import org.currency.accounts.controller.mapper.UserAccountMapper;
import org.currency.accounts.generated.model.Currency;
import org.currency.accounts.generated.model.CurrencyExchangeRequest;
import org.currency.accounts.generated.model.UserAccount;
import org.currency.accounts.model.Account;
import org.currency.accounts.model.Exchange;
import org.currency.accounts.service.CurrencyExchangeService;
import org.currency.accounts.utils.CurrencyAccountsHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.currency.accounts.model.CurrencyCode.PLN;
import static org.currency.accounts.model.CurrencyCode.USD;
import static org.currency.accounts.utils.CurrencyAccountsHelper.AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeControllerTest {

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @Mock
    private UserAccountMapper accountDtoMapper;

    @Mock
    private ExchangeMapper exchangeMapper;

    @InjectMocks
    private ExchangeController exchangeController;

    @Test
    public void shouldExchangeCurrenciesSuccessfully() {
        // given
        Exchange exchange = CurrencyAccountsHelper.createExchange(AMOUNT, PLN, USD);
        Account account = CurrencyAccountsHelper.createAccount();
        UserAccount userAccount = CurrencyAccountsHelper.createUserAccount();
        CurrencyExchangeRequest exchangeRequest = CurrencyAccountsHelper.createCurrencyExchangeRequest(AMOUNT, Currency.PLN, Currency.USD);
        when(exchangeMapper.map(any(CurrencyExchangeRequest.class))).thenReturn(exchange);
        when(currencyExchangeService.exchangeCurrency(any(Exchange.class)))
                .thenReturn(account);
        when(accountDtoMapper.map(any(Account.class))).thenReturn(userAccount);

        // when
        ResponseEntity<UserAccount> response = exchangeController.exchangeCurrency(exchangeRequest);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userAccount, response.getBody());

        verify(exchangeMapper, times(1)).map(exchangeRequest);
        verify(currencyExchangeService, times(1)).exchangeCurrency(exchange);
        verify(accountDtoMapper, times(1)).map(account);
    }
}