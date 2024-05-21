package org.currency.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.currency.accounts.exception.custom.PeselValidationException;
import org.currency.accounts.exception.custom.UserAgeValidationException;
import org.currency.accounts.exception.custom.UserAlreadyExistsException;
import org.currency.accounts.exception.custom.UserNotFoundException;
import org.currency.accounts.generated.model.CreateAccountRequest;
import org.currency.accounts.model.Account;
import org.currency.accounts.repository.AccountRepository;
import org.currency.accounts.repository.mapper.AccountMapper;
import org.springframework.stereotype.Service;
import pl.foltak.polishidnumbers.pesel.InvalidPeselException;
import pl.foltak.polishidnumbers.pesel.Pesel;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private static final int ADULT_AGE = 18;

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public void createUserAccount(CreateAccountRequest accountRequest) {
        log.info("Creating user account by PESEL {}", accountRequest.getPesel());
        Pesel pesel = getValidatedPesel(accountRequest.getPesel());

        validateUserAge(pesel.getBirthDate(), accountRequest.getPesel());
        validateUserExisting(accountRequest.getPesel());

        Account account = accountMapper.map(accountRequest);
        accountRepository.save(account);
    }

    public Account getAccount(String pesel) {
        log.info("Getting user account with PESEL {}", pesel);

        return accountRepository.findById(pesel)
                .orElseThrow(() -> new UserNotFoundException(pesel));
    }

    private boolean isAdult(LocalDate birthDate) {

        LocalDate now = LocalDate.now();
        LocalDate adultDate = birthDate.plusYears(ADULT_AGE);

        return !now.isBefore(adultDate);
    }

    private void validateUserAge(LocalDate birthDate, String pesel) {
        if (!isAdult(birthDate)) {
            throw new UserAgeValidationException(pesel);
        }
    }

    private Pesel getValidatedPesel(String pesel) {
        try {
            return new Pesel(pesel);
        } catch (InvalidPeselException e) {
            throw new PeselValidationException(e.getMessage());
        }
    }

    private void validateUserExisting(String pesel) {
        if (accountRepository.existsById(pesel)) {
            throw new UserAlreadyExistsException(pesel);
        }
    }
}
