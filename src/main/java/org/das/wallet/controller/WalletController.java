package org.das.wallet.controller;

import org.das.wallet.domain.Wallet;
import org.das.wallet.dto.WalletBalanceResponse;
import org.das.wallet.dto.WalletOperationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.das.wallet.service.WalletService;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
public class WalletController {

    private static final Logger log = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Void> processOperation(
            @Valid @RequestBody WalletOperationRequest request
    ) {
        log.info("Post request wallet ={} operation", request);
        walletService.processOperation(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{WALLET_UUID}")
    public ResponseEntity<WalletBalanceResponse> getWalletBalance(
            @PathVariable("WALLET_UUID") UUID id
    ) {
        log.info("Get request wallet id={} operation", id);
        Wallet wallet = walletService.findById(id);
        WalletBalanceResponse response = toDto(wallet);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(response);
    }

    private WalletBalanceResponse toDto(Wallet wallet) {
        log.info("Call method toDto wallet ={} ", wallet);
        return new WalletBalanceResponse(
                wallet.id(),
                wallet.balance()
        );
    }
}
