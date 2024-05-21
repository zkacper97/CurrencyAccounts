package org.currency.accounts.repository.mapper;

import org.currency.accounts.generated.model.CreateAccountRequest;
import org.currency.accounts.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "balancePLN", source = "initialBalancePLN")
    Account map(CreateAccountRequest accountsRequest);
}