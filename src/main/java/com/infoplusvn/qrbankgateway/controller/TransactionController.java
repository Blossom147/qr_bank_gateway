package com.infoplusvn.qrbankgateway.controller;

import com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO;
import com.infoplusvn.qrbankgateway.entity.BankEntity;
import com.infoplusvn.qrbankgateway.entity.TransactionActivityEntity;
import com.infoplusvn.qrbankgateway.entity.TransactionEntity;
import com.infoplusvn.qrbankgateway.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/infogw/qr/v1")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @GetMapping(value = "/transactions")
    public List<TransactionDTO> getAllTranscation(){
        return transactionService.getAllTransactions();
    }

    @GetMapping(value = "/transactions/{transaction_id}")
    public List<TransactionActivityEntity> getTransActivityByTransId(@PathVariable("transaction_id") Long transaction_id){
        return transactionService.getActivityByTransactionId(transaction_id);
    }
}
