package com.wallet.walletapp.repository;

import com.wallet.walletapp.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByMobile(String mobile);
}