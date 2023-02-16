package com.decagon.rewardyourteacher.services;

import com.decagon.rewardyourteacher.dto.TransactionHistoryDTO;
import com.decagon.rewardyourteacher.entity.Transaction;
import com.decagon.rewardyourteacher.entity.User;
import com.decagon.rewardyourteacher.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    List<TransactionHistoryDTO> transactionHistory(String email, int limit);
    Transaction saveTransaction(Transaction transaction);
    Transaction saveTransaction(User user, long senderId, BigDecimal amount, String description, TransactionType transactionType);

    void updateUserTransactionList(User user, long senderId, BigDecimal amount, String description, TransactionType transactionType);

    BigDecimal getTotalMoneySent(String email);
}
