package org.das.wallet.service;

import org.das.wallet.domain.Wallet;
import org.das.wallet.dto.WalletOperationRequest;

import java.util.UUID;

public interface WalletService {

    void processOperation(WalletOperationRequest request);
    Wallet findById(UUID id);
}
