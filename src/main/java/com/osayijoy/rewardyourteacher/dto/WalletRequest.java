package com.decagon.rewardyourteacher.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@ToString
public class WalletRequest {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 30)
    private String name;

    @Size(max = 100)
    private String description;

    @Min(1)
    @Max(3)
    private Integer priority;

}
