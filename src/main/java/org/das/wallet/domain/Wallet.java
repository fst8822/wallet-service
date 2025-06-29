package org.das.wallet.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record Wallet(
        UUID id,
        BigDecimal balance
) {
    public Wallet getNewWalletWithNewBalance(BigDecimal newBalance) {
        return new Wallet(id, newBalance);
    }
}
