package org.das.wallet.service;

import org.das.wallet.domain.Wallet;
import org.das.wallet.dto.WalletUpdateRequest;

import java.util.UUID;

public interface WalletService {

    Wallet updateWalletBalance(WalletUpdateRequest request);
    Wallet findById(UUID id);
}


