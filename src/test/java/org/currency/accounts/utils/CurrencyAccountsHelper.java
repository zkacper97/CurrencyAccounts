package org.currency.accounts.utils;

import org.currency.accounts.generated.model.CreateAccountRequest;
import org.currency.accounts.generated.model.Currency;
import org.currency.accounts.generated.model.CurrencyExchangeRequest;
import org.currency.accounts.generated.model.UserAccount;
import org.currency.accounts.model.Account;
import org.currency.accounts.model.CurrencyCode;
import org.currency.accounts.model.Exchange;

import java.math.BigDecimal;

public class CurrencyAccountsHelper {
    public static final String ADULT_PESEL = "80111285749";
    public static final String NOT_ADULT_PESEL = "12282173549";
    public static final String INVALID_PESEL = "XYZ123ABC456";
    public static final String USER_FIRSTNAME = "Jan";
    public static final String USER_LASTNAME = "Kowalski";
    public static final BigDecimal BALANCE_PLN = BigDecimal.valueOf(1000.00);
    public static final BigDecimal BALANCE_USD = BigDecimal.valueOf(1000.00);
    public static final BigDecimal AMOUNT = BigDecimal.valueOf(100);
    public static final double EXCHANGE_RATE = 4.00;
    public static final String EXCHANGE_RATE_NO = "097/A/NBP/2024";
    public static final String CURRENCY_NAME = "dolar amerykanski";
    public static final String CURRENCY_CODE = "USD";

    public static CreateAccountRequest createAccountRequest(String pesel) {
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setPesel(pesel);
        accountRequest.setFirstName(USER_FIRSTNAME);
        accountRequest.setLastName(USER_LASTNAME);
        accountRequest.setInitialBalancePLN(BALANCE_PLN);

        return accountRequest;
    }

    public static Account createAccount() {
        Account account = new Account();
        account.setPesel(ADULT_PESEL);
        account.setFirstName(USER_FIRSTNAME);
        account.setLastName(USER_LASTNAME);
        account.setBalancePLN(BALANCE_PLN);
        account.setBalanceUSD(BALANCE_USD);

        return account;
    }

    public static UserAccount createUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setPesel(ADULT_PESEL);
        userAccount.setFirstName(USER_FIRSTNAME);
        userAccount.setLastName(USER_LASTNAME);
        userAccount.setBalancePLN(BALANCE_PLN);
        userAccount.setBalanceUSD(BALANCE_USD);

        return userAccount;
    }

    public static Exchange createExchange(BigDecimal amount, CurrencyCode fromCurrencyCode, CurrencyCode toCurrencyCode) {
        Exchange exchange = new Exchange();
        exchange.setPesel(ADULT_PESEL);
        exchange.setAmount(amount);
        exchange.setFromCurrency(fromCurrencyCode);
        exchange.setToCurrency(toCurrencyCode);

        return exchange;
    }

    public static CurrencyExchangeRequest createCurrencyExchangeRequest(BigDecimal amount, Currency fromCurrency, Currency toCurrency) {
        CurrencyExchangeRequest exchangeRequest = new CurrencyExchangeRequest();
        exchangeRequest.setPesel(ADULT_PESEL);
        exchangeRequest.setAmount(amount);
        exchangeRequest.setFromCurrency(fromCurrency);
        exchangeRequest.setToCurrency(toCurrency);

        return exchangeRequest;
    }
}
