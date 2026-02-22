package com.wallet.walletapp.controller;

import com.wallet.walletapp.dto.TransferRequest;
import com.wallet.walletapp.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/transfer")
    public String transferMoney(@RequestBody TransferRequest request) {

        walletService.transferMoney(
                request.getFromMobile(),
                request.getToMobile(),
                request.getAmount()
        );

        return "Transfer successful";
    }
}