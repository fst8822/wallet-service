package org.das.wallet.dto;

import org.das.wallet.domain.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record WalletOperationRequest(
        @NotNull(message = "Wallet ID cannot be null")
        UUID id,

        @NotNull(message = "Operation type cannot be null")
        OperationType operation,

        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "1", message = "Amount must > 0")
        @Digits(integer = 19, fraction = 2, message = "Amount must be digits")
        BigDecimal amount
) {
}
