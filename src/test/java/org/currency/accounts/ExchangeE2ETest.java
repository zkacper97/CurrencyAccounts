package org.currency.accounts;

import org.currency.accounts.controller.ExchangeController;
import org.currency.accounts.exception.custom.CurrencyExchangeFailedException;
import org.currency.accounts.exception.custom.InsufficientFundsException;
import org.currency.accounts.exception.custom.SameCurrencyExchangeException;
import org.currency.accounts.exception.custom.UserNotFoundException;
import org.currency.accounts.generated.model.CurrencyExchangeRequest;
import org.currency.accounts.generated.model.UserAccount;
import org.currency.accounts.model.Account;
import org.currency.accounts.repository.AccountRepository;
import org.currency.accounts.utils.CurrencyAccountsHelper;
import org.currency.accounts.utils.MockingForCurrencyApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.currency.accounts.generated.model.Currency.PLN;
import static org.currency.accounts.generated.model.Currency.USD;
import static org.currency.accounts.utils.CurrencyAccountsHelper.*;
import static org.junit.jupiter.api.Assertions.*;


public class ExchangeE2ETest extends MockingForCurrencyApplication {

    @Autowired
    private ExchangeController exchangeController;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
    }

    @Test
    void shouldSuccessfullyExchangePLNToUSDWhenRequestIsCorrect() {
        // given
        Account account = CurrencyAccountsHelper.createAccount();
        CurrencyExchangeRequest exchangeRequest = CurrencyAccountsHelper.createCurrencyExchangeRequest(AMOUNT, PLN, USD);
        accountRepository.save(account);
        mockCurrencyExchangeClientResponse();

        // when
        ResponseEntity<UserAccount> exchangeResponse = exchangeController.exchangeCurrency(exchangeRequest);

        // then
        assertEquals(HttpStatus.OK, exchangeResponse.getStatusCode());
        UserAccount exchangeAccount = exchangeResponse.getBody();
        assertNotNull(exchangeAccount);
        assertAll("Exchange details",
                () -> assertEquals(ADULT_PESEL, exchangeAccount.getPesel()),
                () -> assertEquals(USER_FIRSTNAME, exchangeAccount.getFirstName()),
                () -> assertEquals(USER_LASTNAME, exchangeAccount.getLastName()),
                () -> assertEquals(BigDecimal.valueOf(90000, 2), exchangeAccount.getBalancePLN()),
                () -> assertEquals(BigDecimal.valueOf(102500, 2), exchangeAccount.getBalanceUSD()));
    }

    @Test
    void shouldSuccessfullyExchangeUSDToPLNWhenRequestIsCorrect() {
        // given
        Account account = createAccount();
        CurrencyExchangeRequest exchangeRequest = createCurrencyExchangeRequest(AMOUNT, USD, PLN);
        accountRepository.save(account);
        mockCurrencyExchangeClientResponse();

        // when
        ResponseEntity<UserAccount> exchangeResponse = exchangeController.exchangeCurrency(exchangeRequest);

        // then
        assertEquals(HttpStatus.OK, exchangeResponse.getStatusCode());
        UserAccount exchangeAccount = exchangeResponse.getBody();
        assertNotNull(exchangeAccount);
        assertAll("Exchange details",
                () -> assertEquals(ADULT_PESEL, exchangeAccount.getPesel()),
                () -> assertEquals(USER_FIRSTNAME, exchangeAccount.getFirstName()),
                () -> assertEquals(USER_LASTNAME, exchangeAccount.getLastName()),
                () -> assertEquals(BigDecimal.valueOf(140000, 2), exchangeAccount.getBalancePLN()),
                () -> assertEquals(BigDecimal.valueOf(90000, 2), exchangeAccount.getBalanceUSD()));
    }

    @Test
    void shouldThrowInsufficientFundsExceptionWhenUserHasInsufficientMoney() {
        // given
        Account account = CurrencyAccountsHelper.createAccount();
        CurrencyExchangeRequest exchangeRequest = CurrencyAccountsHelper.createCurrencyExchangeRequest(BigDecimal.valueOf(2000), PLN, USD);
        accountRepository.save(account);
        mockCurrencyExchangeClientResponse();

        // when, then
        assertThrows(InsufficientFundsException.class,
                () -> exchangeController.exchangeCurrency(exchangeRequest));
    }

    @Test
    void shouldThrowCurrencyExchangeFailedExceptionWhenExchangeClientResponseIsNull() {
        // given
        Account account = CurrencyAccountsHelper.createAccount();
        CurrencyExchangeRequest exchangeRequest = CurrencyAccountsHelper.createCurrencyExchangeRequest(AMOUNT, PLN, USD);
        accountRepository.save(account);
        mockEmptyCurrencyExchangeClientResponse();

        // when, then
        assertThrows(CurrencyExchangeFailedException.class,
                () -> exchangeController.exchangeCurrency(exchangeRequest));
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        // given
        CurrencyExchangeRequest exchangeRequest = CurrencyAccountsHelper.createCurrencyExchangeRequest(AMOUNT, PLN, USD);
        mockCurrencyExchangeClientResponse();

        // when, then
        assertThrows(UserNotFoundException.class,
                () -> exchangeController.exchangeCurrency(exchangeRequest));
    }

    @Test
    void shouldThrowSameCurrencyExchangeExceptionWhenRequestCurrenciesArePLN() {
        // given
        Account account = CurrencyAccountsHelper.createAccount();
        accountRepository.save(account);
        CurrencyExchangeRequest exchangeRequest = CurrencyAccountsHelper.createCurrencyExchangeRequest(AMOUNT, PLN, PLN);

        // when, then
        assertThrows(SameCurrencyExchangeException.class,
                () -> exchangeController.exchangeCurrency(exchangeRequest));
    }
}
