package org.das.wallet.controller;

import org.das.wallet.domain.Wallet;
import org.das.wallet.dto.WalletBalanceResponse;
import org.das.wallet.dto.WalletOperationRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.das.wallet.service.WalletServiceImpl;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/wallet")
public class WalletController {

    private static final Logger log = LoggerFactory.getLogger(WalletController.class);
    private final WalletServiceImpl walletServiceImpl;

    @Autowired
    public WalletController(WalletServiceImpl walletServiceImpl) {
        this.walletServiceImpl = walletServiceImpl;
    }

    @PostMapping
    public ResponseEntity<WalletBalanceResponse> processOperation(
            @Valid @RequestBody WalletOperationRequest request
    ) {
        log.info("Post request wallet ={} operation", request);
        Wallet wallet = walletServiceImpl.processOperation(request);
        return ResponseEntity.ok().body(toDto(wallet));
    }

    @GetMapping("/{WALLET_UUID}")
    public ResponseEntity<WalletBalanceResponse> getWalletBalance(
            @PathVariable("WALLET_UUID") UUID id
    ) {
        Wallet wallet = walletServiceImpl.findById(id);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(toDto(wallet));
    }

    private WalletBalanceResponse toDto(Wallet wallet) {
        log.info("Call method toDto wallet ={} ", wallet);
        if (wallet == null) {
            log.error("Throw IllegalArgumentException Wallet not be null");
            throw new IllegalArgumentException("Wallet not be null");
        }
        return new WalletBalanceResponse(
                wallet.id(),
                wallet.balance()
        );
    }
}
