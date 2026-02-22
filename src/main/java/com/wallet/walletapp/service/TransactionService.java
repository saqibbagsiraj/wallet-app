package com.wallet.walletapp.service;

import com.wallet.walletapp.entity.Transaction;
import com.wallet.walletapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Get transactions by mobile number
    public List<Transaction> getTransactionsByMobile(String mobile) {
        return transactionRepository.findByFromMobileOrToMobile(mobile, mobile);
    }
}