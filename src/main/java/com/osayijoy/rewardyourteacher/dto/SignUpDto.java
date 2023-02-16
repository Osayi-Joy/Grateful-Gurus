package com.osayijoy.rewardyourteacher.dto;

import com.osayijoy.rewardyourteacher.enums.Gender;
import com.osayijoy.rewardyourteacher.enums.Status;
import lombok.*;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    @Size(min = 5)
    private String email;
    @NonNull
    @Size(min = 3)
    private String password;
    private String phoneNumber;
    private Gender gender;
    private String about;
    private String profilePicture;
    private String position;
    private Status status;
    private String yearOfService;
    private long schoolId;

    private LocalDateTime yearOfEmployment;
    private LocalDateTime yearOfResignation;


}
