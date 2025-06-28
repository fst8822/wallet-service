package org.das.wallet.service;

import org.das.wallet.domain.Wallet;
import org.das.wallet.entity.WalletEntity;
import org.das.wallet.mapper.WalletMapper;
import org.das.wallet.repository.WalletRepository;
import org.das.wallet.utils.WalletValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletValidation walletValidation;

    @Mock
    private WalletRepository repository;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletServiceImpl walletService;

    private UUID walletId;
    private WalletEntity walletEntity;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        walletId = UUID.randomUUID();
        walletEntity = new WalletEntity(walletId, new BigDecimal("1000.00"));
        wallet = new Wallet(walletId, new BigDecimal("1000.00"));
    }


    @Test
    void shouldSuccessDepositProcessOperation() {
    }

    @Test
    void shouldNotSuccessDepositProcessOperation() {
    }

    @Test
    void shouldSuccessWithdrawProcessOperation() {
    }

    @Test
    void shouldNotSuccessWithdrawProcessOperation() {
    }

    @Test
    void shouldSuccessFindWhenWalletPresent() {
    }

    @Test
    void shouldReturnNotFoundWhenWalletNotPresent() {
    }
}