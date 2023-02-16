package com.decagon.rewardyourteacher.controllers;

import com.decagon.rewardyourteacher.dto.APIResponse;
import com.decagon.rewardyourteacher.dto.CurrentBalanceResponse;
import com.decagon.rewardyourteacher.dto.WalletRequest;
import com.decagon.rewardyourteacher.dto.WalletResponse;
import com.decagon.rewardyourteacher.services.WalletService;
import com.decagon.rewardyourteacher.utils.ErrorsMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.validation.BindingResult;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("create")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> createWallet(@Valid @RequestBody WalletRequest walletRequest, BindingResult result, Principal principal){
        Map<String, String> errorsMap = ErrorsMap.getErrors(result);
        if (errorsMap != null) {
            return new ResponseEntity<>(new APIResponse<>(false, "Failed to create wallet", errorsMap), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        try {
            WalletResponse walletSaved = walletService.createOrUpdateWallet(walletRequest, email);
            return new ResponseEntity<>(new APIResponse<>(true, "success", walletSaved), HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<APIResponse<List<?>>> getWallets() {
        try {
            List<WalletResponse> walletResponses = walletService.getAllWallet();
            return ResponseEntity.ok(new APIResponse<>(true, "success", walletResponses));
        } catch (UserDeniedAuthorizationException ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("my-wallet")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<APIResponse<WalletResponse>> getWallet(Principal principal){
        try {
            WalletResponse walletResponse = walletService.getWallet(principal.getName());
            return ResponseEntity.ok(new APIResponse<>(true, "success", walletResponse));
        } catch (UserDeniedAuthorizationException ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("current-balance")

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> currentUserWalletBalance(Principal principal){
        try {

            CurrentBalanceResponse currentBalanceResponse = walletService.currentUserWalletBalance(principal.getName());
            return ResponseEntity.ok(new APIResponse<>(true, "success", currentBalanceResponse));
        } catch (UserDeniedAuthorizationException ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.UNAUTHORIZED);
        }
    }

}
