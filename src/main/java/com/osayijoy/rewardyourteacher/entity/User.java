package com.decagon.rewardyourteacher.entity;

import com.decagon.rewardyourteacher.enums.Gender;
import com.decagon.rewardyourteacher.enums.Status;
import com.decagon.rewardyourteacher.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@JsonDeserialize(builder = User.UserBuilder.class)
@Setter
@Getter
@DiscriminatorColumn(name = "user_type")
@Table(name = "users")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User {
    @Id
    @GeneratedValue
    private Long Id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String position;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String about;

    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime yearOfEmployment;
    private LocalDateTime yearOfResignation;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Transaction> transactionTypeList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Message> messageList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Notification> notificationList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    private String yearOfService;

    @ManyToMany
    @JoinColumn(name = "users_teacher", referencedColumnName = "id")
    private List<User> teacherList;

    @ManyToMany(mappedBy = "teacherList")
    private List<User>studentList;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "schoolId" , referencedColumnName = "id")
    private School school;


    @OneToOne(mappedBy = "user", cascade=CascadeType.ALL)
    Wallet wallet;
}
