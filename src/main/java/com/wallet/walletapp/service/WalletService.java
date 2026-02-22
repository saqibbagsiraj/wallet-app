package com.wallet.walletapp.service;

import com.wallet.walletapp.entity.Wallet;
import com.wallet.walletapp.entity.Transaction;
import com.wallet.walletapp.exception.InsufficientBalanceException;
import com.wallet.walletapp.exception.WalletNotFoundException;
import com.wallet.walletapp.repository.TransactionRepository;
import com.wallet.walletapp.repository.WalletRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // 1️⃣ Create Wallet
    public Wallet createWallet(String username, String mobile) {

        if (walletRepository.findByMobile(mobile).isPresent()) {
            throw new IllegalArgumentException("Mobile number already exists");
        }

        Wallet wallet = new Wallet(username, mobile);
        return walletRepository.save(wallet);
    }

    // 2️⃣ Get All Wallets
    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    // 3️⃣ Load Money (ID based)
    public Wallet loadMoney(Long walletId, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        wallet.setBalance(wallet.getBalance() + amount);

        Wallet savedWallet = walletRepository.save(wallet);

        transactionRepository.save(
                new Transaction("LOAD", amount, null, wallet.getMobile())
        );

        return savedWallet;
    }

    // 4️⃣ Withdraw Money (ID based)
    public Wallet withdrawMoney(Long walletId, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        if (wallet.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance() - amount);

        Wallet savedWallet = walletRepository.save(wallet);

        transactionRepository.save(
                new Transaction("WITHDRAW", amount, wallet.getMobile(), null)
        );

        return savedWallet;
    }

    // 5️⃣ Transfer Money (MOBILE based)
    @Transactional
    public void transferMoney(String fromMobile, String toMobile, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Wallet fromWallet = walletRepository.findByMobile(fromMobile)
                .orElseThrow(() -> new WalletNotFoundException("Sender wallet not found"));

        Wallet toWallet = walletRepository.findByMobile(toMobile)
                .orElseThrow(() -> new WalletNotFoundException("Receiver wallet not found"));

        if (fromWallet.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Debit
        fromWallet.setBalance(fromWallet.getBalance() - amount);

        // Credit
        toWallet.setBalance(toWallet.getBalance() + amount);

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        transactionRepository.save(
                new Transaction("TRANSFER", amount, fromMobile, toMobile)
        );
    }
    public void deleteWallet(Long walletId) {

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        walletRepository.delete(wallet);
    }
}