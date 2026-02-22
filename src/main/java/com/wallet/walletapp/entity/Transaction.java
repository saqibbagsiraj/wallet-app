package com.wallet.walletapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // LOAD, WITHDRAW, TRANSFER

    private double amount;

    private String fromMobile;
    private String toMobile;

    private LocalDateTime timestamp;

    public Transaction() {}

    public Transaction(String type, double amount,
                       String fromMobile, String toMobile) {
        this.type = type;
        this.amount = amount;
        this.fromMobile = fromMobile;
        this.toMobile = toMobile;
        this.timestamp = LocalDateTime.now();
    }

    // getters & setters
}