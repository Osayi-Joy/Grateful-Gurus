package com.osayijoy.rewardyourteacher.repository;

import com.osayijoy.rewardyourteacher.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}

