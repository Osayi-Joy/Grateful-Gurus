package com.osayijoy.rewardyourteacher.dto;

import com.osayijoy.rewardyourteacher.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
public class StudentRequestDTO {
    private String firstName;
    private String lastName;
    private String password;
    private Gender gender;
    private String phoneNumber;
    private String profilePicture;
    private String about;
}
