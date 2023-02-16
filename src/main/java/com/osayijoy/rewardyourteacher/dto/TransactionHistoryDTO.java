package com.decagon.rewardyourteacher.dto;

import com.decagon.rewardyourteacher.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionHistoryDTO {
    private Long id;
    private BigDecimal amount;
    private String message;
    private Long uui;
    private LocalDateTime localDateTime;
    private long userId;
    private TransactionType transactionType;
    private String elapsedTime;
    private long senderId;
}
