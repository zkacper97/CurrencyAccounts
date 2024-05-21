package org.currency.accounts.controller.mapper;

import org.currency.accounts.generated.model.UserAccount;
import org.currency.accounts.model.Account;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    UserAccount map(Account account);
}