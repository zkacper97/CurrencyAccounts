package org.currency.accounts.controller.mapper;

import org.currency.accounts.generated.model.CurrencyExchangeRequest;
import org.currency.accounts.model.Exchange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ExchangeMapper {

    @Mapping(target = "amount", expression = "java(exchangeRequest.getAmount().setScale(2, java.math.RoundingMode.HALF_UP))")
    Exchange map(CurrencyExchangeRequest exchangeRequest);
}