package dto.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WalletBalanceResponse(

        UUID id,
        BigDecimal balance,
        LocalDateTime createdAt
) {
}
