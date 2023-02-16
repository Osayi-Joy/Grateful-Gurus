package com.osayijoy.rewardyourteacher.dto;

import com.osayijoy.rewardyourteacher.entity.School;
import com.osayijoy.rewardyourteacher.enums.Gender;
import com.osayijoy.rewardyourteacher.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserResponseDTO {

    private Long Id;
    private String firstName;
    private String lastName;
    private String email;

    private Gender gender;
    private String about;

    private String profilePicture;
    private UserRole role;
    private String position;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime yearOfEmployment;
    private LocalDateTime yearOfResignation;

    private School school;
}
