package com.decagon.rewardyourteacher.dto;

import com.decagon.rewardyourteacher.entity.School;
import com.decagon.rewardyourteacher.enums.Gender;
import com.decagon.rewardyourteacher.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private School school;

    private String profilePicture;
    private UserRole role;
    private String about;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime yearOfEmployment;
    private LocalDateTime yearOfResignation;
}
