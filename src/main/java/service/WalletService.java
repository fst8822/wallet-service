package service;

import domain.OperationType;
import domain.Wallet;
import domain.entity.WalletEntity;
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


    @Transactional
    public void processOperation(WalletOperationRequest request) {
        Wallet foundWallet = findById(request.id());

        BigDecimal newBalance;
        if (request.operation()== OperationType.DEPOSIT) {
            newBalance = foundWallet.balance().add(request.amount());
            Wallet newWallet = new Wallet(
                    foundWallet.id(),
                    foundWallet.operation(),
                    newBalance,
                    foundWallet.createdAt()
            );
            WalletEntity EntityToSave = domainToEntity(newWallet);
            repository.save(EntityToSave);
            return;
        }
        if (request.operation()== OperationType.WITHDRAW) {
            if (foundWallet.balance().compareTo(request.amount()) < 0) {
                throw new IllegalArgumentException("Insufficient balance=% for withdrawal with id=%"
                        .formatted(request.amount(), request.id()));
            }
            newBalance = foundWallet.balance().subtract(request.amount());
            Wallet newWallet = new Wallet(
                    foundWallet.id(),
                    foundWallet.operation(),
                    newBalance,
                    foundWallet.createdAt()
            );
            WalletEntity EntityToSave = domainToEntity(newWallet);
            repository.save(EntityToSave);
        }
    }


    public Wallet findById(UUID id) {
        return  repository.findById(id)
                .map(this::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException("wallet not found with id=%s"
                        .formatted(id)));
    }

    private Wallet entityToDomain(WalletEntity entity) {
        return new Wallet(
                entity.getId(),
                entity.getOperation(),
                entity.getBalance(),
                entity.getCreatedAt()
        );
    }

    private WalletEntity domainToEntity(Wallet wallet) {
        return new WalletEntity(
                wallet.id(),
                wallet.operation(),
                wallet.balance(),
                wallet.createdAt());
    }
}