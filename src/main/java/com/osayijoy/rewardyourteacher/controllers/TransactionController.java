package com.decagon.rewardyourteacher.controllers;


import com.decagon.rewardyourteacher.dto.APIResponse;
import com.decagon.rewardyourteacher.dto.TransactionHistoryDTO;
import com.decagon.rewardyourteacher.entity.Transaction;
import com.decagon.rewardyourteacher.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")

public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;

    }

    @GetMapping("transaction-history")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<APIResponse<List<TransactionHistoryDTO>>> getTransactionHistory (
            final Principal principal,
            @RequestParam(value = "limit", defaultValue = "5") int limit
    ) {
        final String email = principal.getName();
        try {
            final List<TransactionHistoryDTO> transactions = transactionService.transactionHistory(email, limit);
            return ResponseEntity.ok(new APIResponse<>(true, "success", transactions));
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("total-debit")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<APIResponse<BigDecimal>> getTotalMoneySent (
            final Principal principal
    ) {
        final String email = principal.getName();
        try {
            final BigDecimal transactions = transactionService.getTotalMoneySent(email);
            return ResponseEntity.ok(new APIResponse<>(true, "success", transactions));
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
