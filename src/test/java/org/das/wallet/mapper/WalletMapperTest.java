package org.das.wallet.mapper;

import org.das.wallet.domain.Wallet;
import org.das.wallet.entity.WalletEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WalletMapperTest {

    private WalletMapper walletMapper;
    private UUID walletId;
    private BigDecimal balance;

    @BeforeEach
    void setUp() {
        walletMapper = new WalletMapper();
        walletId = UUID.randomUUID();
        balance = new BigDecimal("1000.00");
    }


    @Test
    void shouldConvertEntityToDomainGetDomain() {
        WalletEntity entity = new WalletEntity(walletId, balance);
        Wallet domain = walletMapper.entityToDomain(entity);
        assertNotNull(domain);
        assertEquals(entity.getId(), domain.id());
        assertEquals(entity.getBalance(), domain.balance());
    }

    @Test
    void shouldConvertDomainToEntityGetEntity() {
        Wallet domain = new Wallet(walletId, balance);
        WalletEntity entity = walletMapper.domainToEntity(domain);
        assertNotNull(domain);
        assertEquals(domain.id(), entity.getId());
        assertEquals(domain.balance(), entity.getBalance());
    }

    @Test
    void shouldEntityNullWhenEntityToDomain_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> walletMapper.entityToDomain(null));
    }

    @Test
    void shouldEntityNullWhenDomainToEntity_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> walletMapper.domainToEntity(null));
    }
}