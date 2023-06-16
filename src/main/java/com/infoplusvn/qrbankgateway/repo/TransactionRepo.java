package com.infoplusvn.qrbankgateway.repo;

import com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO;
import com.infoplusvn.qrbankgateway.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public interface TransactionRepo extends JpaRepository<TransactionEntity, Long> {
        @Query("SELECT new com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO(t.id,t.refferenceNo,t.direction,t.type,t.senderBank,t.organizationName,t.country,t.creditAcct,t.transAmount,t.transCcy,t.billNumber,t.receivedDt,t.errorCode) from TransactionEntity t ORDER BY t.id DESC")
        List<TransactionDTO> getAllTransaction();

//      @Query("SELECT new com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO(t.id,t.refferenceNo,t.direction,t.type,t.senderBank,t.organizationName,t.country,t.creditAcct,t.transAmount,t.transCcy,t.billNumber,t.receivedDt,t.errorCode) from TransactionEntity t WHERE t.type = :type AND t.transStepDesc = :status ORDER BY t.id DESC")
        @Query("SELECT new com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO(t.id, t.refferenceNo, t.direction, t.type, t.senderBank, t.organizationName, t.country, t.creditAcct, t.transAmount, t.transCcy, t.billNumber, t.receivedDt, t.errorCode) " +
                "FROM TransactionEntity t " +
                "WHERE (:type IS NULL OR t.type = :type) " +
                "AND (:status IS NULL OR t.transStepDesc = :status) " +
                "AND (:startDate IS NULL OR t.transDate >= STR_TO_DATE(:startDate, '%Y-%m-%d'))" +
                "AND (:endDate IS NULL OR t.transDate <= STR_TO_DATE(:endDate, '%Y-%m-%d'))")
        List<TransactionDTO> getTransaction(
                @Param("type") String type,
                @Param("status") String status,
                @Param("startDate")  LocalDate startDate,
                @Param("endDate") LocalDate endDate);


        @Query("SELECT COUNT(t) FROM TransactionEntity t")
        Long countTransactions();

}
