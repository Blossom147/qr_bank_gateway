package com.infoplusvn.qrbankgateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infoplusvn.qrbankgateway.dto.common.Header.HeaderGW;
import com.infoplusvn.qrbankgateway.dto.request.Payment.PaymentRequestGW;
import com.infoplusvn.qrbankgateway.dto.request.Payment.PaymentRequestNapas;
import com.infoplusvn.qrbankgateway.dto.response.Payment.PaymentResponseGW;
import com.infoplusvn.qrbankgateway.dto.response.Payment.PaymentResonseNapas;
import com.infoplusvn.qrbankgateway.entity.TransactionActivityEntity;
import com.infoplusvn.qrbankgateway.entity.TransactionEntity;
import com.infoplusvn.qrbankgateway.exception.ValidationHelper;
import com.infoplusvn.qrbankgateway.repo.TransactionActivityRepo;
import com.infoplusvn.qrbankgateway.repo.TransactionRepo;
import com.infoplusvn.qrbankgateway.service.Impl.TransactionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public interface QRPaymentService {

     PaymentRequestNapas genMappingReqNAPAS(PaymentRequestGW request) ;

     PaymentResponseGW genMappingResGW(PaymentResonseNapas responseNAPAS);

     PaymentResponseGW genPaymentResGW(PaymentRequestGW paymentRequestGW) throws JsonProcessingException;
}