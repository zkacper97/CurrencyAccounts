package org.currency.accounts.service;

import org.currency.accounts.exception.custom.PeselValidationException;
import org.currency.accounts.exception.custom.UserAgeValidationException;
import org.currency.accounts.exception.custom.UserAlreadyExistsException;
import org.currency.accounts.exception.custom.UserNotFoundException;
import org.currency.accounts.generated.model.CreateAccountRequest;
import org.currency.accounts.model.Account;
import org.currency.accounts.repository.AccountRepository;
import org.currency.accounts.repository.mapper.AccountMapper;
import org.currency.accounts.utils.CurrencyAccountsHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.currency.accounts.utils.CurrencyAccountsHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void shouldCreateAccountWhenUserRequestIsCorrect() {
        // given
        Account account = CurrencyAccountsHelper.createAccount();
        CreateAccountRequest accountRequest = CurrencyAccountsHelper.createAccountRequest(ADULT_PESEL);
        mockUserExistsResponse(false);
        mockMapAccountResponse(account);

        // when
        accountService.createUserAccount(accountRequest);

        // then
        verify(accountRepository).save(account);
    }

    @Test
    public void shouldThrowUserAlreadyExistsException() {
        // given
        CreateAccountRequest accountRequest = CurrencyAccountsHelper.createAccountRequest(ADULT_PESEL);
        mockUserExistsResponse(true);

        // when, then
        assertThrows(UserAlreadyExistsException.class,
                () -> accountService.createUserAccount(accountRequest));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void shouldThrowUserAgeValidationException() {
        // given
        CreateAccountRequest accountRequest = CurrencyAccountsHelper.createAccountRequest(NOT_ADULT_PESEL);
        accountRequest.setPesel(NOT_ADULT_PESEL);

        // when, then
        assertThrows(UserAgeValidationException.class,
                () -> accountService.createUserAccount(accountRequest));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void shouldThrowPeselValidationException() {
        // given
        CreateAccountRequest accountRequest = CurrencyAccountsHelper.createAccountRequest(INVALID_PESEL);

        // when, then
        assertThrows(PeselValidationException.class,
                () -> accountService.createUserAccount(accountRequest));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void shouldReturnUserAccountWhenAccountExists() {
        // given
        Account account = CurrencyAccountsHelper.createAccount();
        mockGetAccountResponse(account);

        // when
        Account foundAccount = accountService.getAccount(ADULT_PESEL);

        // then
        assertNotNull(foundAccount);
        assertAll("Account details",
                () -> assertEquals(ADULT_PESEL, foundAccount.getPesel()),
                () -> assertEquals(USER_FIRSTNAME, foundAccount.getFirstName()),
                () -> assertEquals(USER_LASTNAME, foundAccount.getLastName()),
                () -> assertEquals(BALANCE_PLN, foundAccount.getBalancePLN()),
                () -> assertEquals(BALANCE_USD, foundAccount.getBalanceUSD()));

        verify(accountRepository).findById(anyString());
    }

    @Test
    public void shouldThrowUserNotFoundException() {
        // given
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());

        // when, then
        assertThrows(UserNotFoundException.class,
                () -> accountService.getAccount(ADULT_PESEL));

        verify(accountRepository).findById(anyString());
    }

    private void mockUserExistsResponse(boolean exists) {
        when(accountRepository.existsById(anyString())).thenReturn(exists);
    }

    private void mockGetAccountResponse(Account account) {
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
    }

    private void mockMapAccountResponse(Account account) {
        when(accountMapper.map(any(CreateAccountRequest.class))).thenReturn(account);
    }

}