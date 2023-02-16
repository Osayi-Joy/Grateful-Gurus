package com.decagon.rewardyourteacher.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class APIResponse<T> {
    private Boolean status;
    private String message;
    private T data;

}
