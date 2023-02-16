package com.osayijoy.rewardyourteacher.entity;

import com.osayijoy.rewardyourteacher.enums.SchoolType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    private String address;
    private String city;
    private String state;

//    @JsonManagedReference
    @OneToMany(mappedBy = "school")
    private List<User> users = new ArrayList<>();
}
