package org.das.wallet.utils;

import org.das.wallet.entity.WalletEntity;
import org.das.wallet.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WalletValidationTest {

    private WalletValidation walletValidation;
    private WalletEntity walletEntity;

    @BeforeEach
    void setUp() {
        walletValidation = new WalletValidation();
        walletEntity = new WalletEntity(UUID.randomUUID(), new BigDecimal("1000.00"));
    }

    @Test
    void validateSufficientBalance_NoException() {
        BigDecimal amountToWithdraw = new BigDecimal("500.00");
        assertDoesNotThrow(() -> walletValidation.validateSufficientBalance(walletEntity, amountToWithdraw));
    }

    @Test
    void validate_InsufficientBalance_ThrowsException() {
        BigDecimal withdrawAmount = new BigDecimal("1500.00");
        assertThrows(InsufficientFundsException.class,
                () -> walletValidation.validateSufficientBalance(walletEntity, withdrawAmount));
    }

    @Test
    void validateSufficientBalance_NullWallet_ThrowsException() {
        BigDecimal withdrawAmount = new BigDecimal("100.00");
        assertThrows(IllegalArgumentException.class,
                () -> walletValidation.validateSufficientBalance(null, withdrawAmount));
    }
}