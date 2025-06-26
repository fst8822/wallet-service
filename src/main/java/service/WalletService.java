package service;

import domain.OperationType;
import domain.Wallet;
import domain.entity.WalletEntity;
import dto.mapper.WalletMapper;
import dto.mapper.WalletOperationRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.WalletRepository;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repository;
    private final WalletMapper walletMapper;


    @Transactional
    public void processOperation(WalletOperationRequest request) {
        Wallet foundWallet = findById(request.id());

        BigDecimal newBalance;
        if (request.operation() == OperationType.DEPOSIT) {
            newBalance = foundWallet.balance().add(request.amount());
            Wallet newWallet = foundWallet.withBalance(newBalance);

            WalletEntity entityToSave = walletMapper.domainToEntity(newWallet);
            repository.save(entityToSave);
            return;
        }
        if (request.operation()== OperationType.WITHDRAW) {
            validateSufficientBalance(foundWallet, request.amount());

            newBalance = foundWallet.balance().subtract(request.amount());
            Wallet newWallet = foundWallet.withBalance(newBalance);

            WalletEntity entityToSave = walletMapper.domainToEntity(newWallet);
            repository.save(entityToSave);
        }
    }

    public Wallet findById(UUID id) {
        return  repository.findById(id)
                .map(walletMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException("wallet not found with id=%s".formatted(id)));
    }

    private void validateSufficientBalance(Wallet wallet, BigDecimal amount) {
        if (wallet.balance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient balance=%s for withdrawal, wallet id=%s"
                            .formatted(wallet.balance(), wallet.id()));
        }
    }
}