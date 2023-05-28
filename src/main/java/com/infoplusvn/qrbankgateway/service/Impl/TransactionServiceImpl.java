package com.infoplusvn.qrbankgateway.service.Impl;

import com.infoplusvn.qrbankgateway.constant.CommonConstant;
import com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO;
import com.infoplusvn.qrbankgateway.dto.request.LookupIssuer.LookupIssuerRequestGW;
import com.infoplusvn.qrbankgateway.dto.request.Payment.PaymentRequestGW;
import com.infoplusvn.qrbankgateway.dto.response.LookupIssuer.LookupIssuerResponseGW;
import com.infoplusvn.qrbankgateway.entity.TransactionActivityEntity;
import com.infoplusvn.qrbankgateway.entity.TransactionEntity;
import com.infoplusvn.qrbankgateway.repo.TransactionActivityRepo;
import com.infoplusvn.qrbankgateway.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements com.infoplusvn.qrbankgateway.service.TransactionService {
    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionActivityRepo activityRepo;

    @Override
    public TransactionEntity createTransactionPayment(PaymentRequestGW paymentRequestGW,String type, String transStep, String transStepStatus, String transStepDesc) {
        TransactionEntity transaction = new TransactionEntity();

        transaction.setBankCode(paymentRequestGW.getHeader().getBkCd());

        transaction.setBrandCode(paymentRequestGW.getHeader().getBrCd());

        transaction.setTransDate(paymentRequestGW.getHeader().getTrnDt());

        transaction.setRefferenceNo(paymentRequestGW.getHeader().getRefNo());

        transaction.setChannel(paymentRequestGW.getData().getPayment().getChannel());

        transaction.setDirection(paymentRequestGW.getHeader().getDirection());

        transaction.setTransStep(transStep);

        transaction.setTransStepStatus(transStepStatus);

        transaction.setTransStepDesc(transStepDesc);

        transaction.setServiceCode("QR_PUSH");

        transaction.setSenderBank(paymentRequestGW.getHeader().getBkCd());

        transaction.setReceiverBank(paymentRequestGW.getData().getParticipant().getReceivingInstitutionId());

        transaction.setTransAmount(paymentRequestGW.getData().getAmount());

        transaction.setTransCcy(paymentRequestGW.getData().getCurrency());

        transaction.setDebitAcct(paymentRequestGW.getData().getRecipientAccount());

        transaction.setCreditAcct(paymentRequestGW.getData().getRecipientAccount());

        transaction.setReceivedDt(LocalDateTime.now());

        transaction.setType(type);

        transaction.setBillNumber(paymentRequestGW.getData().getOrder().getBillNumber());

        transaction.setOrganizationName(paymentRequestGW.getData().getRecipient().getFullName());

        transaction.setCountry(paymentRequestGW.getData().getPayment().getLocation());

        return transactionRepo.save(transaction);
    }



    @Override
    public TransactionEntity createTransactionLookupIssuer(LookupIssuerRequestGW lookupIssuerRequestGW, String type, String transStep, String transStepStatus, String transStepDesc) {
        TransactionEntity transaction = new TransactionEntity();

        transaction.setBankCode(lookupIssuerRequestGW.getHeaderGW().getBkCd());

        transaction.setBrandCode(lookupIssuerRequestGW.getHeaderGW().getBrCd());

//        transaction.setTransDate(lookupIssuerRequestGW.getHeaderGW().getTrnDt());

        transaction.setTransDate(CommonConstant.TRANSACTION_DATE);

//        transaction.setRefferenceNo(lookupIssuerRequestGW.getHeaderGW().getRefNo());

        transaction.setRefferenceNo(CommonConstant.REFERENCE_NUMBER);

        transaction.setChannel(lookupIssuerRequestGW.getData().getChannel());

        transaction.setDirection(lookupIssuerRequestGW.getHeaderGW().getDirection());

        transaction.setTransStep(transStep);

        transaction.setTransStepStatus(transStepStatus);

        transaction.setTransStepDesc(transStepDesc);

        transaction.setServiceCode("QR_PUSH");

        transaction.setSenderBank(lookupIssuerRequestGW.getHeaderGW().getBkCd());

        transaction.setReceiverBank(lookupIssuerRequestGW.getHeaderGW().getBkCd());
//
//        transaction.setTransAmount(lookupIssuerResponseGW.getData().getAmount());
//
        transaction.setTransCcy("VND");
//
//        transaction.setDebitAcct(paymentRequestGW.getData().getRecipientAccount());
//
//        transaction.setCreditAcct(paymentRequestGW.getData().getRecipientAccount());

        transaction.setReceivedDt(LocalDateTime.now());

        transaction.setType(type);

//        transaction.setBillNumber(lookupIssuerResponseGW.getData().getOrder().getBillNumber());
////
//        transaction.setOrganizationName(lookupIssuerResponseGW.getData().getRecipient().getFullName());
////
        transaction.setCountry("VN");

        return transactionRepo.save(transaction);
    }

    @Override
    public void updateSentDt(TransactionEntity transaction, LocalDateTime time) {
        transaction.setSentDt(time);
        transactionRepo.save(transaction);
    }

    @Override
    public void updateReceivedDt(TransactionEntity transaction, LocalDateTime time) {
        transaction.setReceivedDt(time);
        transactionRepo.save(transaction);
    }

    @Override
    public void updateTransStep(TransactionEntity transaction, String transStep, String transStepStatus, String transStepDesc) {
        transaction.setTransStep(transStep);

        transaction.setTransStepStatus(transStepStatus);

        transaction.setTransStepDesc(transStepDesc);

        transactionRepo.save(transaction);
    }

    @Override
    public void updateErrCodeDesc(TransactionEntity transaction, String errCode, String errDesc) {
        transaction.setErrorCode(errCode);

        transaction.setErrorDesc(errDesc);

        transactionRepo.save(transaction);
    }

    @Override
    public void createActivity(TransactionEntity transaction, String msgContent, String errCode, String errDesc, String activityStep, String activityStepStatus, String reqResgb) {
        TransactionActivityEntity activity = new TransactionActivityEntity();

        activity.setTransaction(transaction);

        activity.setMsgContent(msgContent);

        activity.setErrorCode(errCode);

        activity.setErrorDesc(errDesc);

        activity.setReqResgb(reqResgb);

        activity.setCreatedDt(LocalDateTime.now());

        activity.setActivityStep(activityStep);

        activity.setActivityStepStatus(activityStepStatus);

        activityRepo.save(activity);

    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepo.getAllTransaction();
    }

    @Override
    public List<TransactionActivityEntity> getAllActivity() {
        return activityRepo.findAll();
    }

    @Override
    public List<TransactionActivityEntity> getActivityByTransactionId(Long transactionId) {
        return activityRepo.findByTransactionId(transactionId);
    }
}