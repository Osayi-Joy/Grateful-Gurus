package com.decagon.rewardyourteacher.dto;

import com.decagon.rewardyourteacher.enums.SchoolType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
@NoArgsConstructor
public class SchoolDTO {
    private String name;
    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;
    private String address;
    private String city;
    private String state;
}
