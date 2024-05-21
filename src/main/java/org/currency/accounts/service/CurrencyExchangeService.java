package org.currency.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.currency.accounts.client.CurrencyExchangeClient;
import org.currency.accounts.exception.custom.CurrencyExchangeFailedException;
import org.currency.accounts.exception.custom.InsufficientFundsException;
import org.currency.accounts.exception.custom.SameCurrencyExchangeException;
import org.currency.accounts.exception.custom.UserNotFoundException;
import org.currency.accounts.model.*;
import org.currency.accounts.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.currency.accounts.model.CurrencyCode.PLN;


@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_DOWN;

    private final CurrencyExchangeClient currencyExchangeClient;
    private final AccountRepository accountRepository;

    public Account exchangeCurrency(Exchange exchangeBody) {
        log.info("Exchange {} {} to {}", exchangeBody.getAmount(), exchangeBody.getFromCurrency(), exchangeBody.getToCurrency());
        Account userAccount = getUserAccount(exchangeBody);

        validateExchangeRequest(exchangeBody);

        BigDecimal availableBalanceFrom = getBalanceInCurrency(userAccount, exchangeBody.getFromCurrency());
        BigDecimal availableBalanceTo = getBalanceInCurrency(userAccount, exchangeBody.getToCurrency());

        double actualRate = getActualRate();
        log.info("Actual rate for dolar {}", actualRate);

        if (isExchangeAvailable(exchangeBody, availableBalanceFrom)) {
            log.info("Exchange {} {} to {} is available", exchangeBody.getAmount(), exchangeBody.getFromCurrency(), exchangeBody.getToCurrency());
            BigDecimal exchangedAmount = calculateExchangedAmount(exchangeBody.getAmount(), actualRate, exchangeBody.getFromCurrency());
            BigDecimal newBalanceFrom = availableBalanceFrom.subtract(exchangeBody.getAmount().setScale(SCALE, ROUNDING_MODE));
            BigDecimal newBalanceTo = availableBalanceTo.add(exchangedAmount.setScale(SCALE, ROUNDING_MODE));

            updateBalances(userAccount, exchangeBody.getFromCurrency(), newBalanceFrom, newBalanceTo);

            return userAccount;
        } else {
            throw new InsufficientFundsException(userAccount.getPesel());
        }
    }

    private Account getUserAccount(Exchange exchangeBody) {
        return accountRepository.findById(exchangeBody.getPesel())
                .orElseThrow(() -> new UserNotFoundException(exchangeBody.getPesel()));
    }

    private Double getActualRate() {
        ExchangeCurrency exchangeCurrency = currencyExchangeClient.getExchangeRates();

        validateExchangeCurrency(exchangeCurrency);

        return exchangeCurrency.getRates().stream()
                .map(CurrencyRate::getExchangeRate)
                .findFirst()
                .orElseThrow(CurrencyExchangeFailedException::new);
    }

    private BigDecimal calculateExchangedAmount(BigDecimal requestedAmount, double actualRate, CurrencyCode fromCurrencyCode) {
        return PLN.equals(fromCurrencyCode)
                ? requestedAmount.divide(BigDecimal.valueOf(actualRate), SCALE, ROUNDING_MODE)
                : requestedAmount.multiply(BigDecimal.valueOf(actualRate)).setScale(SCALE, ROUNDING_MODE);
    }

    private void updateBalances(Account userAccount, CurrencyCode fromCurrencyCode, BigDecimal newBalanceFrom, BigDecimal newBalanceTo) {
        if (PLN.equals(fromCurrencyCode)) {
            userAccount.setBalancePLN(newBalanceFrom);
            userAccount.setBalanceUSD(newBalanceTo);
        } else {
            userAccount.setBalanceUSD(newBalanceFrom);
            userAccount.setBalancePLN(newBalanceTo);
        }
        accountRepository.save(userAccount);
    }

    private void validateExchangeCurrency(ExchangeCurrency exchangeCurrency) {
        if (exchangeCurrency == null || exchangeCurrency.getRates() == null || exchangeCurrency.getRates().isEmpty()) {
            throw new CurrencyExchangeFailedException();
        }
    }

    private BigDecimal getBalanceInCurrency(Account userAccount, CurrencyCode currencyCode) {
        return PLN.equals(currencyCode)
                ? userAccount.getBalancePLN()
                : userAccount.getBalanceUSD();
    }

    private void validateExchangeRequest(Exchange exchangeBody) {
        if (exchangeBody.getFromCurrency().equals(exchangeBody.getToCurrency())) {
            throw new SameCurrencyExchangeException();
        }
    }

    private boolean isExchangeAvailable(Exchange exchangeBody, BigDecimal availableBalanceFrom) {
        return availableBalanceFrom.compareTo(exchangeBody.getAmount()) >= 0;
    }
}
