package com.osayijoy.rewardyourteacher.controllers;

import com.osayijoy.rewardyourteacher.dto.APIResponse;
import com.osayijoy.rewardyourteacher.dto.PayStackPaymentRequest;
import com.osayijoy.rewardyourteacher.dto.PayStackResponse;
import com.osayijoy.rewardyourteacher.dto.PaystackVerifyRequestDTO;
import com.osayijoy.rewardyourteacher.services.PayStackService;
import com.osayijoy.rewardyourteacher.utils.VerifyTransactionResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor

@RequestMapping("api/payments")
public class PaymentController {
    private final PayStackService payStackService;
    @RequestMapping(value = "fund-wallet", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<?> deposit(Principal principal, @RequestBody PayStackPaymentRequest paymentRequest) {
        try {
            PayStackResponse paystackResponse = payStackService.initTransaction(principal, paymentRequest);
            return new ResponseEntity<>(new APIResponse<>(true, "success", paystackResponse), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "verify-payment", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<?> payStackResponse(@RequestBody PaystackVerifyRequestDTO paystackVerifyRequestDTO) {
        try {
            String paymentRef = paystackVerifyRequestDTO.getVerificationCode();
            VerifyTransactionResponse verifyPayment = payStackService.verifyPayment(paymentRef);
            return new ResponseEntity<>(
                    new APIResponse<>(true, "success", verifyPayment),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new APIResponse<>(false, ex.getMessage(), null),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
