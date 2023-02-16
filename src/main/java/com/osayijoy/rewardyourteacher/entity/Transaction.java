package com.osayijoy.rewardyourteacher.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.osayijoy.rewardyourteacher.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal amount;
    private String message;
    private Long uui;
    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId" , referencedColumnName = "id")
    private User user;

    private long senderId;

}
