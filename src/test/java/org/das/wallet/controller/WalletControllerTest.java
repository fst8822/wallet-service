package org.das.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.das.wallet.TestcontainersConf;
import org.das.wallet.domain.OperationType;
import org.das.wallet.dto.WalletUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = TestcontainersConf.class)
@AutoConfigureMockMvc
@Import(JacksonAutoConfiguration.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldSuccessDepositProcessOperationReturnOK() throws Exception {
        UUID walletId = UUID.fromString("f31d64fe-2c85-47bb-a007-9b44f36a5c7c");
        WalletUpdateRequest request = new WalletUpdateRequest(
                walletId, OperationType.DEPOSIT, new BigDecimal(1000)
        );
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.id().toString()))
                .andExpect(jsonPath("$.balance").value("3000.0"));
    }

    @Test
    void shouldSuccessWithdrawProcessOperationReturnOK() throws Exception {
        UUID walletId = UUID.fromString("9e07b2d2-799b-4b1e-8920-35f64d570cbe");
        WalletUpdateRequest request = new WalletUpdateRequest(
                walletId, OperationType.WITHDRAW, new BigDecimal(1000)
        );
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value("1500.0"));
    }

    @Test
    void shouldNotSuccessWhenInvalidOperationTypeProcessOperation_ReturnBAD_REQUEST() throws Exception {
        UUID walletId = UUID.fromString("9e07b2d2-799b-4b1e-8920-35f64d570cbe");
        WalletUpdateRequest request = new WalletUpdateRequest(
                walletId, OperationType.UNKNOWN, new BigDecimal(1000)
        );
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetWalletBalanceWhenWalletPresent() throws Exception {
        UUID walletId = UUID.fromString("7e07b2d2-799b-4b1e-8920-35f64d570cbd");
        mockMvc.perform(get("/api/v1/wallet/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value("3000.0"));
    }

    @Test
    void shouldNotGetWalletBalanceWhenWalletNotPresentReturnNotFound() throws Exception {
        UUID walletId = UUID.fromString("4e07b2d2-799b-4b1e-8920-35f64d570cbd");
        mockMvc.perform(get("/api/v1/wallet/{walletId}", walletId))
                .andExpect(status().isNotFound());
    }
}