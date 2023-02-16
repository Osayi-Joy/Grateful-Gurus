package com.decagon.rewardyourteacher.entity;

import com.decagon.rewardyourteacher.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notification")
@Builder
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private String notificationBody;
    private TransactionType transactionType ;
    private LocalDateTime createdAt;
    private String Title;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId" , referencedColumnName = "id")
    private User user;

}
