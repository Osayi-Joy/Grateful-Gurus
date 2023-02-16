package com.decagon.rewardyourteacher.dto;

import com.decagon.rewardyourteacher.entity.School;
import com.decagon.rewardyourteacher.enums.Gender;
import com.decagon.rewardyourteacher.enums.UserRole;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
