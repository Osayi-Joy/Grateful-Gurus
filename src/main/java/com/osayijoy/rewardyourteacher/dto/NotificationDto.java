package com.osayijoy.rewardyourteacher.dto;

import com.osayijoy.rewardyourteacher.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDto {

    private String message;
    private LocalDateTime createdAt;
    private Transaction transaction;
}
