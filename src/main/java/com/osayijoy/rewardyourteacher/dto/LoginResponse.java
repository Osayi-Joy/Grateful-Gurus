package com.osayijoy.rewardyourteacher.dto;

import com.osayijoy.rewardyourteacher.entity.School;
import com.osayijoy.rewardyourteacher.enums.Gender;
import com.osayijoy.rewardyourteacher.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private long userId;
    private String token;

    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private School school;
    private String about;
    private String profilePicture;
    private UserRole role;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime yearOfEmployment;
    private LocalDateTime yearOfResignation;
}
