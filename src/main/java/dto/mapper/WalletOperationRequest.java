package dto.mapper;

import domain.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletOperationRequest(
        UUID id,
        OperationType operation,
        BigDecimal amount
) {
}
