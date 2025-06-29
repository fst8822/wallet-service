package org.das.wallet.service;

import org.das.wallet.domain.OperationType;
import org.das.wallet.domain.Wallet;
import org.das.wallet.dto.WalletUpdateRequest;
import org.das.wallet.entity.WalletEntity;
import org.das.wallet.mapper.WalletMapper;
import org.das.wallet.exception.InvalidOperationTypeException;
import org.das.wallet.exception.WalletNotFoundException;
import org.das.wallet.utils.WalletValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.das.wallet.repository.WalletRepository;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);
    private final WalletValidation walletValidation;
    private final WalletRepository repository;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletServiceImpl(
            WalletValidation walletValidation,
            WalletRepository repository,
            WalletMapper walletMapper
    ) {
        this.walletValidation = walletValidation;
        this.repository = repository;
        this.walletMapper = walletMapper;
    }

    @Override
    @Transactional
    public Wallet updateWalletBalance(WalletUpdateRequest request) {
        log.info("Call method processOperation with request={} ", request);
        WalletEntity foundWallet = repository.findByIdWithLock(request.id())
                .orElseThrow(() -> new WalletNotFoundException("wallet not found with id=%s".formatted(request.id())));
        if (request.operation() == OperationType.DEPOSIT) {
            log.info("Begin operation type with DEPOSIT");
            foundWallet.setBalance(foundWallet.getBalance().add(request.amount()));
        } else if (request.operation()== OperationType.WITHDRAW) {
            log.info("Begin operation type with WITHDRAW");
            walletValidation.validateSufficientBalance(foundWallet, request.amount());
            foundWallet.setBalance(foundWallet.getBalance().subtract(request.amount()));
        } else {
            log.error("Throw InvalidOperationTypeException={}", request.operation());
            throw new InvalidOperationTypeException("Invalid operation type");
        }
        return walletMapper.entityToDomain(repository.save(foundWallet));
    }

    @Override
    @Transactional
    public Wallet findById(UUID id) {
        return  repository.findByIdWithLock(id)
                .map(walletMapper::entityToDomain)
                .orElseThrow(() -> new WalletNotFoundException("wallet not found with id=%s".formatted(id)));
    }
}