package service;

import domain.OperationType;
import domain.Wallet;
import domain.entity.WalletEntity;
import dto.mapper.WalletMapper;
import dto.mapper.WalletOperationRequest;
import exception.InsufficientFundsException;
import exception.WalletNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.WalletRepository;
import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repository;
    private final WalletMapper walletMapper;


    @Transactional
    public void processOperation(WalletOperationRequest request) {
        log.info("Call method processOperation with request={} ", request);
        validatePositiveAmount(request.amount());
        Wallet foundWallet = findById(request.id());

        if (request.operation() == OperationType.DEPOSIT) {
            log.info("Begin operation type with DEPOSIT");
            Wallet newWallet = deposit(foundWallet, request.amount());

            WalletEntity entityToSave = walletMapper.domainToEntity(newWallet);
            log.info("Begin operation save entity={} in DEPOSIT method", entityToSave);
            repository.save(entityToSave);
            log.info("End operation save entity with DEPOSIT");
            return;
        }
        if (request.operation()== OperationType.WITHDRAW) {
            log.info("Begin operation type with WITHDRAW");
            validateSufficientBalance(foundWallet, request.amount());

            Wallet newWallet = withdraw(foundWallet, request.amount());
            WalletEntity entityToSave = walletMapper.domainToEntity(newWallet);
            log.info("Begin operation save entity={} in WITHDRAW method", entityToSave);
            repository.save(entityToSave);
            log.info("End operation save entity with WITHDRAW");
        }
    }

    @Transactional(readOnly = true)
    public Wallet findById(UUID id) {
        log.info("Call method findById with id={}", id);
        return  repository.findById(id)
                .map(walletMapper::entityToDomain)
                .orElseThrow(() -> new WalletNotFoundException("wallet not found with id=%s".formatted(id)));
    }

    private Wallet deposit(Wallet walletToDeposit, BigDecimal amount) {
        log.info("Call method deposit with wallet={} and amount={}", walletToDeposit, amount);
        return walletToDeposit.deposit(amount);
    }

    private Wallet withdraw(Wallet walletToWithdraw, BigDecimal amount) {
        log.info("Call method withdraw with wallet={} and amount={}", walletToWithdraw, amount);
        return walletToWithdraw.withdraw(amount);
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