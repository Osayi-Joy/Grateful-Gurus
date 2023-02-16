package com.osayijoy.rewardyourteacher.dto;

import com.osayijoy.rewardyourteacher.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponseDTO {

    private Long id;
    private long userId;
    private String message;
    private String notificationBody;
    private TransactionType transactionType ;
    private LocalDateTime createdAt;
    private String Title;
    private String elapsedTime;
}
