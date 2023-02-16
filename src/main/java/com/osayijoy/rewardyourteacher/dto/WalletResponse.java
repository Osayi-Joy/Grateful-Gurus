package com.decagon.rewardyourteacher.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class WalletResponse {

    private String id;

    private String name;

    private String accountNumber;

    private String description;

    private Integer priority;

    private BigDecimal currentBalance;
}
