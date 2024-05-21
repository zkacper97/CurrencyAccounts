package org.currency.accounts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurrencyRate {

    @JsonProperty("no")
    private String exchangeRateNo;
    private LocalDate effectiveDate;
    @JsonProperty("mid")
    private double exchangeRate;
}
