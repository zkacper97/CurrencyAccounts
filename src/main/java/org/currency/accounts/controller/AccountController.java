package org.currency.accounts.controller;

import lombok.RequiredArgsConstructor;
import org.currency.accounts.controller.mapper.UserAccountMapper;
import org.currency.accounts.generated.api.AccountsApi;
import org.currency.accounts.generated.model.CreateAccountRequest;
import org.currency.accounts.generated.model.UserAccount;
import org.currency.accounts.model.Account;
import org.currency.accounts.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountsApi {

    private final AccountService accountService;
    private final UserAccountMapper accountDtoMapper;

    @Override
    public ResponseEntity<UserAccount> getUserAccountByPesel(String pesel) {
        Account account = accountService.getAccount(pesel);

        return ResponseEntity.ok().body(accountDtoMapper.map(account));
    }

    @Override
    public ResponseEntity<Void> createUserAccount(CreateAccountRequest body) {
        accountService.createUserAccount(body);

        return ResponseEntity.ok().build();
    }
}
