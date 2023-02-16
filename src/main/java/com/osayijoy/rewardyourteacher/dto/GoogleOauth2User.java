package com.decagon.rewardyourteacher.dto;

import lombok.Data;

@Data
public class GoogleOauth2User {
    private String firstName;
    private String lastName;
    private String email;
    private String displayPicture;
}
