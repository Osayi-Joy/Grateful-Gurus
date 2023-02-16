package com.decagon.rewardyourteacher.services.servicesImpl;

import com.decagon.rewardyourteacher.dto.TransactionHistoryDTO;
import com.decagon.rewardyourteacher.entity.Transaction;
import com.decagon.rewardyourteacher.entity.User;
import com.decagon.rewardyourteacher.enums.TransactionType;
import com.decagon.rewardyourteacher.repository.TransactionRepository;
import com.decagon.rewardyourteacher.repository.UserRepository;
import com.decagon.rewardyourteacher.services.TransactionService;
import com.decagon.rewardyourteacher.utils.EpochTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @PersistenceContext
    EntityManager entityManager;

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository){
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TransactionHistoryDTO> transactionHistory(String email, int limit) {
        User user = userRepository.findByEmail(email);

        List<Transaction> transactions = getTransactionHistory(user.getId(), limit);
        List<TransactionHistoryDTO> transactionHistories = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionHistoryDTO transactionHistoryDTO = new ModelMapper().map(transaction, TransactionHistoryDTO.class);
            transactionHistoryDTO.setUserId(transaction.getUser().getId());
            transactionHistoryDTO.setElapsedTime(EpochTime.getElapsedTime(transaction.getLocalDateTime()));
            transactionHistoryDTO.setSenderId(transaction.getSenderId());
            transactionHistories.add(transactionHistoryDTO);
        }
        return transactionHistories;
    }

    private List<Transaction> getTransactionHistory(long userId, int limit) {
        String builder = "select t from Transaction t where t.user.Id = :userId " +
                "order by t.localDateTime DESC";

        limit = limit > 0 ? limit : 1;
        TypedQuery<Transaction> query = entityManager.createQuery(builder, Transaction.class)
                .setMaxResults(limit);

        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction saveTransaction(User user, long senderId, BigDecimal amount, String description, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setSenderId(senderId);
        transaction.setAmount(amount);
        transaction.setMessage(description);
        transaction.setLocalDateTime(LocalDateTime.now());
        transaction.setTransactionType(transactionType);
        return saveTransaction(transaction);
    }

    @Override
    public void updateUserTransactionList(User user, long senderId, BigDecimal amount, String description, TransactionType transactionType){
        Transaction transaction = saveTransaction(user, senderId, amount, description, transactionType);

        List<Transaction> transactions = user.getTransactionTypeList();

        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
        user.setTransactionTypeList(transactions);
    }

    @Override
    public BigDecimal getTotalMoneySent(String email) {
        User user = userRepository.findByEmail(email);
        BigDecimal totalDebit = transactionRepository.totalDebit(user.getId());
        System.out.println("Total debit is " + totalDebit);
        return totalDebit == null ? BigDecimal.ZERO : totalDebit;
    }
}
