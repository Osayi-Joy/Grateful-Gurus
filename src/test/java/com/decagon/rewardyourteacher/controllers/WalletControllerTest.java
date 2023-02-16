package com.decagon.rewardyourteacher.controllers;

import static org.hamcrest.Matchers.is;
import com.decagon.rewardyourteacher.dto.CurrentBalanceResponse;
import com.decagon.rewardyourteacher.entity.Wallet;
import com.decagon.rewardyourteacher.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@WithMockUser(value="abc", authorities = {"ROLE_STUDENT"})
public class WalletControllerTest {

   @MockBean
    private WalletService walletService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void currentUserWalletBalance() throws Exception {

        when(walletService.currentUserWalletBalance(any(String.class)))
                .thenReturn(getCurrentBalanceResponse());


        mockMvc.perform(get("/api/wallets/current-balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(true)))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.currentBalance", is(16000)));
    }
    private CurrentBalanceResponse getCurrentBalanceResponse(){

        int balance = 16000;
        BigDecimal userBalance = BigDecimal.valueOf(balance);

        Wallet wallet = new Wallet();
        wallet.setCurrentBalance(userBalance);
        CurrentBalanceResponse response = new CurrentBalanceResponse();
        response.setCurrentBalance(wallet.getCurrentBalance());
        return response;
    }
}