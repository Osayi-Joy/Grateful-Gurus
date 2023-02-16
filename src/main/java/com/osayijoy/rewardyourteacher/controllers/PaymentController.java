package com.decagon.rewardyourteacher.controllers;

import com.decagon.rewardyourteacher.dto.*;
import com.decagon.rewardyourteacher.services.PayStackService;
import com.decagon.rewardyourteacher.utils.VerifyTransactionResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
