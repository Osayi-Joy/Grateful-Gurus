package com.decagon.rewardyourteacher.services;

import com.decagon.rewardyourteacher.dto.PayStackPaymentRequest;
import com.decagon.rewardyourteacher.dto.PayStackResponse;
import com.decagon.rewardyourteacher.utils.VerifyTransactionResponse;

import java.io.IOException;
import java.security.Principal;

public interface PayStackService {

    PayStackResponse initTransaction(Principal principal, PayStackPaymentRequest request) throws Exception;

    VerifyTransactionResponse verifyPayment(String reference) throws IOException;
}
