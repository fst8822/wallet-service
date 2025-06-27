package repository;

import entity.WalletEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select we from WalletEntity we where we.id =:id")
    Optional<WalletEntity> findById(@Param("id") UUID id);
}
