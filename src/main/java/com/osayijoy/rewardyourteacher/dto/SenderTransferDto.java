package com.decagon.rewardyourteacher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SenderTransferDto {
   private BigDecimal amountToSend;
}
