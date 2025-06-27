package mapper;

import domain.Wallet;
import entity.WalletEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WalletMapper {

    public Wallet entityToDomain(WalletEntity entity) {
        log.info("Call method entityToDomain with entity={} ", entity);
        return new Wallet(
                entity.getId(),
                entity.getBalance()
        );
    }

    public WalletEntity domainToEntity(Wallet wallet) {
        log.info("Call method domainToEntity with wallet={} ", wallet);
        return new WalletEntity(
                wallet.id(),
                wallet.balance()
        );
    }
}
