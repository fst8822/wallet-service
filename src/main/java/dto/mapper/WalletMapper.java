package dto.mapper;

import domain.Wallet;
import domain.entity.WalletEntity;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public Wallet entityToDomain(WalletEntity entity) {
        return new Wallet(
                entity.getId(),
                entity.getOperation(),
                entity.getBalance(),
                entity.getCreatedAt()
        );
    }

    public WalletEntity domainToEntity(Wallet wallet) {
        return new WalletEntity(
                wallet.id(),
                wallet.operation(),
                wallet.balance(),
                wallet.createdAt());
    }
}
