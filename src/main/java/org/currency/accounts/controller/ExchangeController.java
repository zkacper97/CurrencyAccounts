package org.currency.accounts.controller;

import lombok.RequiredArgsConstructor;
import org.currency.accounts.controller.mapper.ExchangeMapper;
import org.currency.accounts.controller.mapper.UserAccountMapper;
import org.currency.accounts.generated.api.ExchangeApi;
import org.currency.accounts.generated.model.CurrencyExchangeRequest;
import org.currency.accounts.generated.model.UserAccount;
import org.currency.accounts.model.Account;
import org.currency.accounts.model.Exchange;
import org.currency.accounts.service.CurrencyExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExchangeController implements ExchangeApi {

    private final CurrencyExchangeService currencyExchangeService;
    private final UserAccountMapper accountDtoMapper;
    private final ExchangeMapper exchangeMapper;

    @Override
    public ResponseEntity<UserAccount> exchangeCurrency(CurrencyExchangeRequest exchangeRequest) {
        Exchange exchange = exchangeMapper.map(exchangeRequest);
        Account account = currencyExchangeService.exchangeCurrency(exchange);
        return ResponseEntity.ok().body(accountDtoMapper.map(account));
    }
}
