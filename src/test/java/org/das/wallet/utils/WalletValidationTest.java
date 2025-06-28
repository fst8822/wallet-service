package org.das.wallet.utils;

import org.das.wallet.domain.Wallet;
import org.das.wallet.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WalletValidationTest {

    private WalletValidation walletValidation;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        walletValidation = new WalletValidation();
        wallet = new Wallet(UUID.randomUUID(), new BigDecimal("1000.00"));
    }

    @Test
    void validateSufficientBalance_NoException() {
        BigDecimal amountToWithdraw = new BigDecimal("500.00");
        assertDoesNotThrow(() -> walletValidation.validateSufficientBalance(wallet, amountToWithdraw));
    }

    @Test
    void validate_InsufficientBalance_ThrowsException() {
        BigDecimal withdrawAmount = new BigDecimal("1500.00");
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class,
                () -> walletValidation.validateSufficientBalance(wallet, withdrawAmount));
        assertTrue(exception.getMessage().contains(wallet.id().toString()));
    }

    @Test
    void validateSufficientBalance_NullWallet_ThrowsException() {
        BigDecimal withdrawAmount = new BigDecimal("100.00");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> walletValidation.validateSufficientBalance(null, withdrawAmount));
    }

    @Test
    void validatePositiveAmount_NoException() {
        BigDecimal positiveAmount = new BigDecimal("100.00");
        assertDoesNotThrow(() -> walletValidation.validatePositiveAmount(positiveAmount));
    }

    @Test
    void validateZeroAmount_ThrowsException() {
        BigDecimal zeroAmount = BigDecimal.ZERO;
        assertThrows(IllegalArgumentException.class,
                () -> walletValidation.validatePositiveAmount(zeroAmount));
    }

    @Test
    void validateNegativeAmount_ThrowsException() {
        BigDecimal negativeAmount = new BigDecimal("-100.00");
        assertThrows(IllegalArgumentException.class,
                () -> walletValidation.validatePositiveAmount(negativeAmount));
    }

    @Test
    void validate_NullAmount_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> walletValidation.validatePositiveAmount(null));
    }
}