package com.osayijoy.rewardyourteacher.services;


import com.osayijoy.rewardyourteacher.dto.CurrentBalanceResponse;
import com.osayijoy.rewardyourteacher.dto.WalletRequest;
import com.osayijoy.rewardyourteacher.dto.WalletResponse;
import com.osayijoy.rewardyourteacher.entity.User;
import com.osayijoy.rewardyourteacher.entity.Wallet;
import com.osayijoy.rewardyourteacher.utils.VerifyTransactionResponse;

import java.util.List;

public interface WalletService {
    WalletResponse createOrUpdateWallet(WalletRequest walletRequest, String email);

    CurrentBalanceResponse currentUserWalletBalance(String email);

    void fundWallet(String email, VerifyTransactionResponse payStackResponse, String walletName, String description);

    WalletResponse getWallet(String email);

    List<WalletResponse> getAllWallet();

    Wallet createWallet(User user, String walletName, String description, WalletRequest... walletRequests);
}
