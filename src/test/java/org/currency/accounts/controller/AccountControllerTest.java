package org.currency.accounts.controller;


import org.currency.accounts.controller.mapper.UserAccountMapper;
import org.currency.accounts.generated.model.CreateAccountRequest;
import org.currency.accounts.generated.model.UserAccount;
import org.currency.accounts.model.Account;
import org.currency.accounts.service.AccountService;
import org.currency.accounts.utils.CurrencyAccountsHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.currency.accounts.utils.CurrencyAccountsHelper.ADULT_PESEL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private UserAccountMapper accountDtoMapper;

    @InjectMocks
    private AccountController accountController;

    @Test
    public void shouldReturnUserAccountSuccessfully() {
        // given
        Account account = CurrencyAccountsHelper.createAccount();
        UserAccount userAccount = CurrencyAccountsHelper.createUserAccount();
        when(accountService.getAccount(anyString())).thenReturn(account);
        when(accountDtoMapper.map(any(Account.class))).thenReturn(userAccount);

        // when
        ResponseEntity<UserAccount> response = accountController.getUserAccountByPesel(ADULT_PESEL);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userAccount, response.getBody());

        verify(accountService, times(1)).getAccount(ADULT_PESEL);
        verify(accountDtoMapper, times(1)).map(account);
    }

    @Test
    public void shouldCreateUserAccountSuccessfully() {
        // given
        CreateAccountRequest accountRequest = CurrencyAccountsHelper.createAccountRequest(ADULT_PESEL);

        // when
        ResponseEntity<Void> response = accountController.createUserAccount(accountRequest);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(accountService, times(1)).createUserAccount(accountRequest);
    }
}