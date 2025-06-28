package org.das.wallet.utils;

import org.das.wallet.domain.Wallet;
import org.das.wallet.exception.InsufficientFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class WalletValidation {

    private static final Logger log = LoggerFactory.getLogger(WalletValidation.class);


    public void validateSufficientBalance(Wallet wallet, BigDecimal amount) {
        log.info("Call method validateSufficientBalance with wallet={} and amount={}", wallet, amount);
        if (wallet == null) {
            log.error("Throw IllegalArgumentException Wallet not be null");
            throw new IllegalArgumentException("Wallet not be null");
        }
        if (wallet.balance().compareTo(amount) < 0) {
            log.error("Throw InsufficientFundsException balance is Insufficient");
            throw new InsufficientFundsException(
                    "Insufficient balance=%s for withdrawal, wallet id=%s"
                            .formatted(wallet.balance(), wallet.id()));
        }
    }

    public void validatePositiveAmount(BigDecimal amount) {
        log.info("Call method validatePositiveAmount with amount={}", amount);
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Throw IllegalArgumentException because Amount is negative");
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
