package com.decagon.rewardyourteacher.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CurrentBalanceResponse {
    private BigDecimal currentBalance;
}
