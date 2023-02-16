package com.decagon.rewardyourteacher.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.decagon.rewardyourteacher.dto.PaymentResponse;
import com.decagon.rewardyourteacher.dto.SenderTransferDto;
import com.decagon.rewardyourteacher.services.RewardService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {RewardController.class})
@ExtendWith(SpringExtension.class)
class RewardControllerTest {
    @Autowired
    private RewardController rewardController;

    @MockBean
    private RewardService rewardService;


    @Test
    void testSendTeacherReward() throws Exception {
        when(rewardService.rewardTeacher(any(Long.class), any(SenderTransferDto.class)))
                .thenReturn(sendTeacherReward());

        SenderTransferDto senderTransferDto = new SenderTransferDto();
        senderTransferDto.setAmountToSend(BigDecimal.valueOf(42L));
        String content = (new ObjectMapper()).writeValueAsString(senderTransferDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/rewards/receiverId/{receiverId}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(rewardController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.message", is("Payment successful")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.currentBalance", is(100)))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    private PaymentResponse sendTeacherReward() {
        PaymentResponse response = new PaymentResponse();
        response.setMessage("Payment successful");
        response.setCurrentBalance(BigDecimal.valueOf(100));
        return response;
    }
}

