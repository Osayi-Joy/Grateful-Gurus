package com.decagon.rewardyourteacher.services;

import com.decagon.rewardyourteacher.dto.CurrentBalanceResponse;
import com.decagon.rewardyourteacher.dto.WalletRequest;
import com.decagon.rewardyourteacher.dto.WalletResponse;
import com.decagon.rewardyourteacher.entity.User;
import com.decagon.rewardyourteacher.entity.Wallet;
import com.decagon.rewardyourteacher.utils.VerifyTransactionResponse;
import java.util.List;

public interface WalletService {
    WalletResponse createOrUpdateWallet(WalletRequest walletRequest, String email);

    CurrentBalanceResponse currentUserWalletBalance(String email);

    void fundWallet(String email, VerifyTransactionResponse payStackResponse, String walletName, String description);

    WalletResponse getWallet(String email);

    List<WalletResponse> getAllWallet();

    Wallet createWallet(User user, String walletName, String description, WalletRequest... walletRequests);
}
