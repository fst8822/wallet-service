package org.das.wallet.utils;

import org.das.wallet.entity.WalletEntity;
import org.das.wallet.exception.InsufficientFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class WalletValidation {

    private static final Logger log = LoggerFactory.getLogger(WalletValidation.class);


    public void validateSufficientBalance(WalletEntity walletEntity, BigDecimal amount) {
        log.info("Call method validateSufficientBalance with wallet={} and amount={}", walletEntity, amount);
        if (walletEntity == null) {
            log.error("Throw IllegalArgumentException Wallet not be null");
            throw new IllegalArgumentException("Wallet not be null");
        }
        if (walletEntity.getBalance().compareTo(amount) < 0) {
            log.error("Throw InsufficientFundsException balance is Insufficient");
            throw new InsufficientFundsException(
                    "Insufficient balance=%s for withdrawal, wallet id=%s"
                            .formatted(walletEntity.getBalance(), walletEntity.getId()));
        }
    }
}
