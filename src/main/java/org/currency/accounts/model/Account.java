package org.currency.accounts.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Account {
    @Id
    @Column(name = "pesel")
    private String pesel;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "balance_pln")
    private BigDecimal balancePLN = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);

    @Column(name = "balance_usd")
    private BigDecimal balanceUSD = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
}
