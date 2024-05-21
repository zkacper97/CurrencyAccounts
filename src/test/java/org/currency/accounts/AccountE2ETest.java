package org.currency.accounts;

import org.currency.accounts.controller.AccountController;
import org.currency.accounts.exception.custom.PeselValidationException;
import org.currency.accounts.exception.custom.UserAgeValidationException;
import org.currency.accounts.exception.custom.UserAlreadyExistsException;
import org.currency.accounts.exception.custom.UserNotFoundException;
import org.currency.accounts.generated.model.CreateAccountRequest;
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

import static org.currency.accounts.utils.CurrencyAccountsHelper.*;
import static org.junit.jupiter.api.Assertions.*;


public class AccountE2ETest extends MockingForCurrencyApplication {

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
    }

    @Test
    void shouldCreateUserAccountWhenAccountRequestIsCorrect() {
        // given
        CreateAccountRequest accountRequest = CurrencyAccountsHelper.createAccountRequest(ADULT_PESEL);

        // when
        ResponseEntity<Void> createdAccountResponse = accountController.createUserAccount(accountRequest);

        // then
        assertEquals(HttpStatus.OK, createdAccountResponse.getStatusCode());
    }

    @Test
    void shouldGetUserAccountWhenAccountExists() {
        // given
        Account account = CurrencyAccountsHelper.createAccount();
        accountRepository.save(account);

        // when
        ResponseEntity<UserAccount> existingAccountResponse = accountController.getUserAccountByPesel(ADULT_PESEL);

        // then
        assertEquals(HttpStatus.OK, existingAccountResponse.getStatusCode());
        UserAccount existingAccount = existingAccountResponse.getBody();
        assertNotNull(existingAccount);
        assertAll("Account details",
                () -> assertEquals(ADULT_PESEL, existingAccount.getPesel()),
                () -> assertEquals(USER_FIRSTNAME, existingAccount.getFirstName()),
                () -> assertEquals(USER_LASTNAME, existingAccount.getLastName()),
                () -> assertEquals(BigDecimal.valueOf(100000, 2), existingAccount.getBalancePLN()),
                () -> assertEquals(BigDecimal.valueOf(100000, 2), existingAccount.getBalanceUSD()));
    }

    @Test
    void shouldThrowUserAgeValidationExceptionWhenUserIsNotAdult() {
        // given
        CreateAccountRequest accountRequest = CurrencyAccountsHelper.createAccountRequest(NOT_ADULT_PESEL);

        // when, then
        assertThrows(UserAgeValidationException.class,
                () -> accountController.createUserAccount(accountRequest));
    }

    @Test
    void shouldThrowPeselValidationExceptionWhenUserIntroducedIncorrectPesel() {
        // given
        CreateAccountRequest accountRequest = CurrencyAccountsHelper.createAccountRequest(INVALID_PESEL);

        // when, then
        assertThrows(PeselValidationException.class,
                () -> accountController.createUserAccount(accountRequest));
    }

    @Test
    void shouldThrowUserAlreadyExistsException() {
        // given
        Account account = CurrencyAccountsHelper.createAccount();
        accountRepository.save(account);
        CreateAccountRequest accountRequest = CurrencyAccountsHelper.createAccountRequest(ADULT_PESEL);

        // when, then
        assertThrows(UserAlreadyExistsException.class,
                () -> accountController.createUserAccount(accountRequest));
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIsNotExist() {
        // given, when, then
        assertThrows(UserNotFoundException.class,
                () -> accountController.getUserAccountByPesel(ADULT_PESEL));
    }
}
