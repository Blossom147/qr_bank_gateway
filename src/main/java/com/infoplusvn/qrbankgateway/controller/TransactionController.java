package com.infoplusvn.qrbankgateway.controller;

import com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO;
import com.infoplusvn.qrbankgateway.entity.TransactionActivityEntity;
import com.infoplusvn.qrbankgateway.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/infogw/qr/v1")
public class TransactionController {
    @Autowired

    TransactionService transactionService;

    @GetMapping(value = "/transactionSearch")
    public List<TransactionDTO> getTransaction(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        return transactionService.getTransactions(type, status, startDate, endDate);
    }

    @GetMapping(value = "/transactionCount")
    public Long countTransactions(){
        return transactionService.countTransactions();
    }

    @GetMapping(value = "/transactions")
    public List<TransactionDTO> getAllTranscation(){
        return transactionService.getAllTransactions();
    }

    @GetMapping(value = "/transactions/{transaction_id}")
    public List<TransactionActivityEntity> getTransActivityByTransId(@PathVariable("transaction_id") Long transaction_id){
        return transactionService.getActivityByTransactionId(transaction_id);
    }



}
