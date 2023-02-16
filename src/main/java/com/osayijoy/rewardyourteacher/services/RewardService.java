package com.osayijoy.rewardyourteacher.services;


import com.osayijoy.rewardyourteacher.dto.PaymentResponse;
import com.osayijoy.rewardyourteacher.dto.SenderTransferDto;
import com.osayijoy.rewardyourteacher.exceptions.WalletNotFoundException;

public interface RewardService {
    PaymentResponse rewardTeacher(Long receiverId, SenderTransferDto senderTransferDto)
        throws WalletNotFoundException;
}
