package org.das.wallet.controller;

import org.das.wallet.domain.Wallet;
import org.das.wallet.dto.WalletResponse;
import org.das.wallet.dto.WalletUpdateRequest;
import jakarta.validation.Valid;
import org.das.wallet.mapper.WalletMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.das.wallet.service.WalletServiceImpl;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/wallet")
public class WalletController {

    private static final Logger log = LoggerFactory.getLogger(WalletController.class);
    private final WalletServiceImpl walletServiceImpl;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletController(
            WalletServiceImpl walletServiceImpl,
            WalletMapper walletMapper
    ) {
        this.walletServiceImpl = walletServiceImpl;
        this.walletMapper = walletMapper;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> processOperation(
            @Valid @RequestBody WalletUpdateRequest request
    ) {
        log.info("Post request wallet ={} operation", request);
        Wallet wallet = walletServiceImpl.updateWalletBalance(request);
        return ResponseEntity.ok().body(walletMapper.toDto(wallet));
    }

    @GetMapping("/{WALLET_UUID}")
    public ResponseEntity<WalletResponse> getWallet(
            @PathVariable("WALLET_UUID") UUID id
    ) {
        Wallet wallet = walletServiceImpl.findById(id);
        return ResponseEntity.ok(walletMapper.toDto(wallet));
    }
}
