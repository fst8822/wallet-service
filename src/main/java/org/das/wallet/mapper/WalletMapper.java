package org.das.wallet.mapper;

import org.das.wallet.domain.Wallet;
import org.das.wallet.entity.WalletEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    private static final Logger log = LoggerFactory.getLogger(WalletMapper.class);

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
