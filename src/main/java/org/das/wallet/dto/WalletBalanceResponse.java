package org.das.wallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletBalanceResponse(

        UUID id,
        BigDecimal balance
) {
}
