package com.infoplusvn.qrbankgateway.repo;

import com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO;
import com.infoplusvn.qrbankgateway.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepo extends JpaRepository<TransactionEntity, Long> {
    @Query("SELECT new com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO(t.transactionId,t.refferenceNo,t.direction,t.type,t.senderBank,t.organizationName,t.country,t.creditAcct,t.transAmount,t.transCcy,t.billNumber,t.receivedDt,t.errorCode) from TransactionEntity t ")
    List<TransactionDTO> getAllTransaction();
}
