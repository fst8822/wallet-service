package org.das.wallet.service;

import org.das.wallet.domain.OperationType;
import org.das.wallet.domain.Wallet;
import org.das.wallet.dto.WalletOperationRequest;
import org.das.wallet.entity.WalletEntity;
import org.das.wallet.mapper.WalletMapper;
import org.das.wallet.exception.InsufficientFundsException;
import org.das.wallet.exception.InvalidOperationTypeException;
import org.das.wallet.exception.WalletNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.das.wallet.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private static final Logger log = LoggerFactory.getLogger(WalletService.class);
    private final WalletRepository repository;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletService(WalletRepository repository, WalletMapper walletMapper) {
        this.repository = repository;
        this.walletMapper = walletMapper;
    }

    @Transactional
    public void processOperation(WalletOperationRequest request) {
        log.info("Call method processOperation with request={} ", request);
        Wallet foundWallet = findById(request.id());

        if (request.operation() == OperationType.DEPOSIT) {
            log.info("Begin operation type with DEPOSIT");
            foundWallet = deposit(foundWallet, request.amount());
        } else if (request.operation()== OperationType.WITHDRAW) {
            log.info("Begin operation type with WITHDRAW");
            foundWallet = withdraw(foundWallet, request.amount());
        } else {
            log.error("Throw InvalidOperationTypeException={}", request.operation());
            throw new InvalidOperationTypeException("Invalid operation type");
        }
        WalletEntity entityToSave = walletMapper.domainToEntity(foundWallet);

        log.info("Begin operation save entity={}", entityToSave);
        repository.save(entityToSave);
        log.info("End operation save entity");
    }

    @Transactional
    public Wallet findById(UUID id) {
        log.info("Call method findById with id={}", id);
        return  repository.findByIdWithLock(id)
                .map(walletMapper::entityToDomain)
                .orElseThrow(() -> new WalletNotFoundException("wallet not found with id=%s".formatted(id)));
    }

    private Wallet deposit(Wallet walletToDeposit, BigDecimal amount) {
        log.info("Call method deposit with wallet={} and amount={}", walletToDeposit, amount);
        validatePositiveAmount(amount);
        BigDecimal newAmount = walletToDeposit.balance().add(amount);
        return walletToDeposit.getNewWalletWithNewBalance(newAmount);
    }

    private Wallet withdraw(Wallet walletToWithdraw, BigDecimal amount) {
        validateSufficientBalance(walletToWithdraw, amount);
        validatePositiveAmount(amount);
        log.info("Call method withdraw with wallet={} and amount={}", walletToWithdraw, amount);
        BigDecimal newAmount = walletToWithdraw.balance().subtract(amount);
        return walletToWithdraw.getNewWalletWithNewBalance(newAmount);
    }

    private void validateSufficientBalance(Wallet wallet, BigDecimal amount) {
        log.info("Call method validateSufficientBalance with wallet={} and amount={}", wallet, amount);
        if (wallet.balance().compareTo(amount) < 0) {
            log.error("Throw InsufficientFundsException balance is Insufficient");
            throw new InsufficientFundsException(
                    "Insufficient balance=%s for withdrawal, wallet id=%s"
                            .formatted(wallet.balance(), wallet.id()));
        }
    }

    private void validatePositiveAmount(BigDecimal amount) {
        log.info("Call method validatePositiveAmount with amount={}", amount);
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Throw IllegalArgumentException because Amount is negative");
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}