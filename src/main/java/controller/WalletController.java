package controller;

import domain.Wallet;
import dto.mapper.WalletBalanceResponse;
import dto.mapper.WalletOperationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.WalletService;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(name = "api/v1/")
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<Void> processOperation(
            @Valid @RequestBody WalletOperationRequest request
    ) {
        log.info("Post request wallet ={} operation", request);
        walletService.processOperation(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{WALLET_UUID}}")
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
