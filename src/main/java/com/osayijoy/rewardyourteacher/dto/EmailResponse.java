package com.decagon.rewardyourteacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class EmailResponse {

    private boolean status;
    private String message;

}
