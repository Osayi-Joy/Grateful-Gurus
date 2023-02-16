package com.decagon.rewardyourteacher.repository;

import com.decagon.rewardyourteacher.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}

