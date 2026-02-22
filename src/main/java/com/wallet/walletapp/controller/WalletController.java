package com.wallet.walletapp.controller;

import com.wallet.walletapp.entity.Wallet;
import com.wallet.walletapp.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    // 1️⃣ Create Wallet (SAFE & PROFESSIONAL)
    @PostMapping
    public ResponseEntity<?> createWallet(
            @RequestParam String username,
            @RequestParam String mobile) {

        try {
            Wallet wallet = walletService.createWallet(username, mobile);
            return ResponseEntity.ok(wallet);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // 2️⃣ Get All Wallets
    @GetMapping
    public List<Wallet> getAllWallets() {
        return walletService.getAllWallets();
    }

    // 3️⃣ Load Money
    @PostMapping("/{id}/load")
    public ResponseEntity<?> loadMoney(
            @PathVariable Long id,
            @RequestParam double amount) {

        try {
            return ResponseEntity.ok(walletService.loadMoney(id, amount));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4️⃣ Withdraw Money
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdrawMoney(
            @PathVariable Long id,
            @RequestParam double amount) {

        try {
            return ResponseEntity.ok(walletService.withdrawMoney(id, amount));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWallet(@PathVariable Long id) {
        try {
            walletService.deleteWallet(id);
            return ResponseEntity.ok("Wallet deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}