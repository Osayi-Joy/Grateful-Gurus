package com.decagon.rewardyourteacher.services;

import com.decagon.rewardyourteacher.dto.PaymentResponse;
import com.decagon.rewardyourteacher.dto.SenderTransferDto;
import com.decagon.rewardyourteacher.exceptions.WalletNotFoundException;

public interface RewardService {
    PaymentResponse rewardTeacher(Long receiverId, SenderTransferDto senderTransferDto)
        throws WalletNotFoundException;
}
