package dto.mapper;

import java.math.BigDecimal;

public record WalletBalanceResponse(
        BigDecimal amount
) {
}
