package com.osayijoy.rewardyourteacher.services;


import com.osayijoy.rewardyourteacher.dto.PayStackPaymentRequest;
import com.osayijoy.rewardyourteacher.dto.PayStackResponse;
import com.osayijoy.rewardyourteacher.utils.VerifyTransactionResponse;

import java.io.IOException;
import java.security.Principal;

public interface PayStackService {

    PayStackResponse initTransaction(Principal principal, PayStackPaymentRequest request) throws Exception;

    VerifyTransactionResponse verifyPayment(String reference) throws IOException;
}
