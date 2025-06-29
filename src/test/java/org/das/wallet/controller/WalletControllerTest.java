package org.das.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.das.wallet.domain.OperationType;
import org.das.wallet.domain.Wallet;
import org.das.wallet.dto.WalletOperationRequest;
import org.das.wallet.exception.InvalidOperationTypeException;
import org.das.wallet.exception.WalletNotFoundException;
import org.das.wallet.service.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletServiceImpl walletService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID walletId;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
         walletId = UUID.randomUUID();
         wallet = new Wallet(walletId, new BigDecimal("1500.00"));
    }

    @Test
    void shouldSuccessDepositProcessOperationReturnOK() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest(
                walletId, OperationType.DEPOSIT, wallet.balance()
        );
        when(walletService.processOperation(request)).thenReturn(wallet);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wallet.id().toString()))
                .andExpect(jsonPath("$.balance").value("1500.0"));
    }

    @Test
    void shouldSuccessWithdrawProcessOperationReturnOK() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest(
                wallet.id(), OperationType.WITHDRAW, wallet.balance()
        );
        when(walletService.processOperation(request)).thenReturn(wallet);
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wallet.id().toString()))
                .andExpect(jsonPath("$.balance").value("1500.0"));
    }

    @Test
    void shouldNotSuccessWhenInvalidOperationTypeProcessOperation_ReturnBAD_REQUEST() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest(
                wallet.id(), OperationType.UNKNOWN, wallet.balance()
        );
        when(walletService.processOperation(request))
                .thenThrow(new InvalidOperationTypeException("Invalid operation type"));
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetWalletBalanceWhenWalletPresent() throws Exception {
        when(walletService.findById(walletId)).thenReturn(wallet);
        mockMvc.perform(get("/api/v1/{walletId}", walletId))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(wallet.id().toString()))
                .andExpect(jsonPath("$.balance").value("1500.0"));
    }

    @Test
    void shouldNotGetWalletBalanceWhenWalletNotPresentReturnNotFound() throws Exception {
        when(walletService.findById(walletId))
                .thenThrow(new WalletNotFoundException("wallet not found with id=%s".formatted(walletId)));
        mockMvc.perform(get("/api/v1/{walletId}", walletId))
                .andExpect(status().isNotFound());
    }
}