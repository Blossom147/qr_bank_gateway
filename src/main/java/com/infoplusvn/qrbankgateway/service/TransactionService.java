package com.infoplusvn.qrbankgateway.service;

import com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO;
import com.infoplusvn.qrbankgateway.dto.request.LookupIssuer.LookupIssuerRequestGW;
import com.infoplusvn.qrbankgateway.dto.request.Payment.PaymentRequestGW;
import com.infoplusvn.qrbankgateway.dto.response.LookupIssuer.LookupIssuerResponseGW;
import com.infoplusvn.qrbankgateway.entity.TransactionActivityEntity;
import com.infoplusvn.qrbankgateway.entity.TransactionEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionEntity createTransactionPayment(PaymentRequestGW paymentRequestGW, String type, String transStep, String transStepStatus, String transStepDesc);

    TransactionEntity createTransactionLookupIssuer(LookupIssuerRequestGW lookupIssuerRequestGW, String type, String transStep, String transStepStatus, String transStepDesc);

    void updateSentDt(TransactionEntity transaction, LocalDateTime time);

    void updateReceivedDt(TransactionEntity transaction, LocalDateTime time);

    void updateTransStep(TransactionEntity transaction, String transStep, String transStepStatus, String transStepDesc);

    void updateErrCodeDesc(TransactionEntity transaction, String errCode, String errDesc);

    void createActivity(TransactionEntity transaction, String msgContent, String errCode, String errDesc, String activityStep, String activityStepStatus, String reqResgb);

    List<TransactionDTO> getAllTransactions();

    //List<TransactionEntity> getAllTransactions();

    List<TransactionActivityEntity> getAllActivity();

    List<TransactionActivityEntity> getActivityByTransactionId(Long transactionId);

}
