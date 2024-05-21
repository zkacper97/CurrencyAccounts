package org.currency.accounts.service;

import org.currency.accounts.client.CurrencyExchangeClient;
import org.currency.accounts.exception.custom.CurrencyExchangeFailedException;
import org.currency.accounts.exception.custom.InsufficientFundsException;
import org.currency.accounts.exception.custom.SameCurrencyExchangeException;
import org.currency.accounts.exception.custom.UserNotFoundException;
import org.currency.accounts.model.Account;
import org.currency.accounts.model.CurrencyRate;
import org.currency.accounts.model.Exchange;
import org.currency.accounts.model.ExchangeCurrency;
import org.currency.accounts.repository.AccountRepository;
import org.currency.accounts.utils.CurrencyAccountsHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.currency.accounts.model.CurrencyCode.PLN;
import static org.currency.accounts.model.CurrencyCode.USD;
import static org.currency.accounts.utils.CurrencyAccountsHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyCodeExchangeServiceTest {

    @Mock
    private CurrencyExchangeClient currencyExchangeClient;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CurrencyExchangeService currencyExchangeService;

    @Test
    void shouldSuccessfullyExchangePLNToUSD() {
        // given
        Account userAccount = CurrencyAccountsHelper.createAccount();
        Exchange exchangeBody = CurrencyAccountsHelper.createExchange(AMOUNT, PLN, USD);

        mockGetAccountResponse(userAccount);
        mockCurrencyExchangeClientResponse();

        // when
        Account updatedAccount = currencyExchangeService.exchangeCurrency(exchangeBody);

        // then
        assertNotNull(updatedAccount);
        assertEquals(BigDecimal.valueOf(90000, 2), updatedAccount.getBalancePLN());
        assertEquals(BigDecimal.valueOf(102500, 2), updatedAccount.getBalanceUSD());
    }

    @Test
    void shouldSuccessfullyExchangeUSDToPLN() {
        // given
        Account userAccount = CurrencyAccountsHelper.createAccount();
        Exchange exchangeBody = CurrencyAccountsHelper.createExchange(AMOUNT, USD, PLN);

        mockGetAccountResponse(userAccount);
        mockCurrencyExchangeClientResponse();

        // when
        Account updatedAccount = currencyExchangeService.exchangeCurrency(exchangeBody);

        // then
        assertNotNull(updatedAccount);
        assertEquals(BigDecimal.valueOf(140000, 2), updatedAccount.getBalancePLN());
        assertEquals(BigDecimal.valueOf(90000, 2), updatedAccount.getBalanceUSD());
    }

    @Test
    void shouldThrowInsufficientFundsException() {
        // given
        Account userAccount = CurrencyAccountsHelper.createAccount();
        Exchange exchangeBody =
                CurrencyAccountsHelper.createExchange(BigDecimal.valueOf(2000), PLN, USD);

        mockGetAccountResponse(userAccount);
        mockCurrencyExchangeClientResponse();

        // when, then
        assertThrows(InsufficientFundsException.class,
                () -> currencyExchangeService.exchangeCurrency(exchangeBody));
    }

    @Test
    void shouldThrowCurrencyExchangeFailedException() {
        // given
        Account userAccount = CurrencyAccountsHelper.createAccount();
        Exchange exchangeBody =
                CurrencyAccountsHelper.createExchange(AMOUNT, PLN, USD);

        mockGetAccountResponse(userAccount);
        mockEmptyCurrencyExchangeClientResponse();

        // when, then
        assertThrows(CurrencyExchangeFailedException.class,
                () -> currencyExchangeService.exchangeCurrency(exchangeBody));
    }

    @Test
    void shouldThrowUserNotFoundException() {
        // given
        Exchange exchangeBody =
                CurrencyAccountsHelper.createExchange(AMOUNT, PLN, USD);

        mockEmptyGetAccountResponse();

        // when, then
        assertThrows(UserNotFoundException.class,
                () -> currencyExchangeService.exchangeCurrency(exchangeBody));
    }

    @Test
    void shouldThrowSameCurrencyExchangeException() {
        // given
        Account userAccount = CurrencyAccountsHelper.createAccount();
        Exchange exchangeBody =
                CurrencyAccountsHelper.createExchange(AMOUNT, USD, USD);

        mockGetAccountResponse(userAccount);

        // when, then
        assertThrows(SameCurrencyExchangeException.class,
                () -> currencyExchangeService.exchangeCurrency(exchangeBody));
    }

    private void mockGetAccountResponse(Account account) {
        when(accountRepository.findById(anyString())).thenReturn(Optional.ofNullable(account));
    }

    private void mockEmptyGetAccountResponse() {
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());
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