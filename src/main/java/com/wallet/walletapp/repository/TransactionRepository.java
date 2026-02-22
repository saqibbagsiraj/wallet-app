package com.wallet.walletapp.repository;

import com.wallet.walletapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromMobileOrToMobile(String fromMobile, String toMobile);
}