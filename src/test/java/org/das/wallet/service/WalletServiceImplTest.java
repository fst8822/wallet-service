package org.das.wallet.service;

import org.das.wallet.domain.OperationType;
import org.das.wallet.domain.Wallet;
import org.das.wallet.dto.WalletUpdateRequest;
import org.das.wallet.entity.WalletEntity;
import org.das.wallet.exception.InvalidOperationTypeException;
import org.das.wallet.exception.WalletNotFoundException;
import org.das.wallet.mapper.WalletMapper;
import org.das.wallet.repository.WalletRepository;
import org.das.wallet.utils.WalletValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletValidation walletValidation;

    @Mock
    private WalletRepository repository;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletServiceImpl walletServiceImpl;

    private UUID walletId;


    @BeforeEach
    void setUp() {
        walletId = UUID.randomUUID();
    }

    @Test
    void shouldSuccessDepositUpdateWalletBalance() {
        WalletEntity walletEntity = new WalletEntity(walletId, new BigDecimal("1000.00"));
        Wallet updatedWallet = new Wallet(
                walletEntity.getId(), walletEntity.getBalance().add(walletEntity.getBalance())
        );
        WalletUpdateRequest request = new WalletUpdateRequest(
                walletId, OperationType.DEPOSIT, new BigDecimal("1000.00")
        );
        when(repository.findByIdWithLock(request.id())).thenReturn(Optional.of(walletEntity));
        when(repository.save(any(WalletEntity.class))).thenReturn(walletEntity);
        when(walletMapper.entityToDomain(walletEntity)).thenReturn(updatedWallet);
        Wallet res = walletServiceImpl.updateWalletBalance(request);

        Assertions.assertNotNull(res);
        Assertions.assertEquals(request.id(), res.id());
        Assertions.assertEquals(updatedWallet.balance(), res.balance());
    }

    @Test
    void WhenOperationTypeUNKNOWN_Then_ThrowsInvalidOperationTypeException() {
        WalletEntity walletEntity = new WalletEntity(walletId, new BigDecimal("1000.00"));
        WalletUpdateRequest request = new WalletUpdateRequest(
                walletId, OperationType.UNKNOWN, new BigDecimal("1000.00")
        );
        when(repository.findByIdWithLock(request.id())).thenReturn(Optional.of(walletEntity));
        assertThrows(InvalidOperationTypeException.class,
                () -> walletServiceImpl.updateWalletBalance(request));
    }

    @Test
    void shouldSuccessWithdrawUpdateWalletBalance() {
        WalletEntity walletEntity = new WalletEntity(walletId, new BigDecimal("1000.00"));
        Wallet updatedWallet = new Wallet(
                walletEntity.getId(), walletEntity.getBalance().subtract(walletEntity.getBalance())
        );
        WalletUpdateRequest request = new WalletUpdateRequest(
                walletId, OperationType.WITHDRAW, new BigDecimal("1000.00")
        );
        when(repository.findByIdWithLock(request.id())).thenReturn(Optional.of(walletEntity));
        when(repository.save(any(WalletEntity.class))).thenReturn(walletEntity);
        when(walletMapper.entityToDomain(walletEntity)).thenReturn(updatedWallet);
        doNothing().when(walletValidation).validateSufficientBalance(any(WalletEntity.class), any(BigDecimal.class));
        Wallet res = walletServiceImpl.updateWalletBalance(request);

        Assertions.assertNotNull(res);
        Assertions.assertEquals(request.id(), res.id());
        Assertions.assertEquals(updatedWallet.balance(), res.balance());
    }


    @Test
    void shouldSuccessFindWhenWalletPresent() {
        WalletEntity walletEntity = new WalletEntity(walletId, new BigDecimal("1000.00"));
        Wallet updatedWallet = new Wallet(
                walletEntity.getId(), walletEntity.getBalance()
        );
        when(repository.findByIdWithLock(walletId)).thenReturn(Optional.of(walletEntity));
        when(walletMapper.entityToDomain(walletEntity)).thenReturn(updatedWallet);
        Wallet res = walletServiceImpl.findById(walletId);

        Assertions.assertNotNull(res);
        Assertions.assertEquals(walletId, res.id());
        Assertions.assertEquals(walletEntity.getBalance(), res.balance());
    }

    @Test
    void WhenWalletNotPresent_Then_Throws_WalletNotFoundException() {
        when(repository.findByIdWithLock(walletId))
                .thenThrow(new WalletNotFoundException("wallet not found with id=%s".formatted(walletId)));
        assertThrows(WalletNotFoundException.class,
                () -> repository.findByIdWithLock(walletId));
    }
}