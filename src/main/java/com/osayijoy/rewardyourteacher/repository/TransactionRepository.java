package com.decagon.rewardyourteacher.repository;

import com.decagon.rewardyourteacher.entity.Transaction;
import com.decagon.rewardyourteacher.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionByUser(User user);

    @Query(value = "select sum(amount) as total from Transactions t where t.user_id = ?1 and t.transaction_type = 'DEBIT'", nativeQuery = true)
    BigDecimal totalDebit(long userId);

}
