package com.infoplusvn.qrbankgateway.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoplusvn.qrbankgateway.constant.CommonConstant;
import com.infoplusvn.qrbankgateway.constant.ErrorDefination;
import com.infoplusvn.qrbankgateway.constant.LookupIssuerConstant;
import com.infoplusvn.qrbankgateway.constant.PaymentConstant;
import com.infoplusvn.qrbankgateway.dto.common.Header.HeaderGW;
import com.infoplusvn.qrbankgateway.dto.common.Header.HeaderNapas;
import com.infoplusvn.qrbankgateway.dto.request.LookupIssuer.LookupIssuerRequestGW;
import com.infoplusvn.qrbankgateway.dto.request.LookupIssuer.LookupIssuerRequestNapas;
import com.infoplusvn.qrbankgateway.dto.request.Payment.PaymentRequestNapas;
import com.infoplusvn.qrbankgateway.dto.response.LookupIssuer.LookupIssuerResponseGW;
import com.infoplusvn.qrbankgateway.dto.response.LookupIssuer.LookupIssuerResponseNapas;
import com.infoplusvn.qrbankgateway.dto.response.Payment.PaymentResonseNapas;
import com.infoplusvn.qrbankgateway.dto.response.Payment.PaymentResponseGW;
import com.infoplusvn.qrbankgateway.entity.TransactionEntity;
import com.infoplusvn.qrbankgateway.exception.ValidationHelper;
import com.infoplusvn.qrbankgateway.service.LookupIssuerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Slf4j
@Service
public class LookupIssuerServiceImpl implements LookupIssuerService {

    @Autowired
    private TransactionServiceImpl transactionService;

    @Override
    public LookupIssuerRequestNapas genMappingReqNAPAS(LookupIssuerRequestGW lookupIssuerRequestGW) {
        LookupIssuerRequestNapas lookupIssuerRequestNapas = new LookupIssuerRequestNapas();
        HeaderNapas headerNapas = new HeaderNapas();
        HeaderNapas.Requestor requestor = new HeaderNapas.Requestor();
        headerNapas.setRequestor(requestor);
        requestor.setId("ID01");
        requestor.setName("Nguyen Xuan Long");
        headerNapas.setReference_id("05050000011234567890ACB123456");
        headerNapas.setOperation(CommonConstant.REQ_GB);
        lookupIssuerRequestNapas.setHeaderNapas(headerNapas);

        LookupIssuerRequestNapas.Payload payload = new LookupIssuerRequestNapas.Payload();

        payload.setPayment_reference(CommonConstant.REFERENCE_NUMBER);
        payload.setQr_string(lookupIssuerRequestGW.getData().getQrString());

        lookupIssuerRequestNapas.setPayload(payload);


        return lookupIssuerRequestNapas;
    }

    @Override
    public LookupIssuerResponseGW genMappingResGW(LookupIssuerResponseNapas lookupIssuerResponseNapas) {

        LookupIssuerResponseGW lookupIssuerResponseGW = new LookupIssuerResponseGW();
        HeaderGW headerGW = new HeaderGW();
        lookupIssuerResponseGW.setHeaderGW(headerGW);
        lookupIssuerResponseGW.getHeaderGW().setReqResGb(CommonConstant.RES_GB);

        LookupIssuerResponseGW.Data data = new LookupIssuerResponseGW.Data();
        lookupIssuerResponseGW.setData(data);

        LookupIssuerResponseGW.Data.Participant participant = new LookupIssuerResponseGW.Data.Participant();
        LookupIssuerResponseGW.Data.Recipient recipient = new LookupIssuerResponseGW.Data.Recipient();
        LookupIssuerResponseGW.Data.Order order = new LookupIssuerResponseGW.Data.Order();
        LookupIssuerResponseGW.Payment payment = new LookupIssuerResponseGW.Payment();
        LookupIssuerResponseGW.Data.Recipient.Address address = new LookupIssuerResponseGW.Data.Recipient.Address();

        lookupIssuerResponseGW.getData().setOrder(order);
        lookupIssuerResponseGW.getData().setPayment(payment);
        headerGW.setRefNo(CommonConstant.REFERENCE_NUMBER);
        headerGW.setBrCd(CommonConstant.BRAND_CODE);
        headerGW.setTrnDt(CommonConstant.TRANSACTION_DATE);
        headerGW.setDirection(CommonConstant.DIRECTION_INBOUND);
        headerGW.setBkCd(lookupIssuerResponseNapas.getPayload().getParticipant().getOriginating_institution_id());

        data.setRecipientAccount(CommonConstant.CARD_ACCEPTOR_CITY);
        data.setResponseCode(lookupIssuerResponseNapas.getResult().getCode());
        data.setResponseDesc(lookupIssuerResponseNapas.getResult().getDescription());

        data.getPayment().setGenerationMethod(lookupIssuerResponseNapas.getPayload().getPayment().getGeneration_method());
        data.getPayment().setIndicator(lookupIssuerResponseNapas.getPayload().getPayment().getIndicator());
        data.getPayment().setExchangeRate(lookupIssuerResponseNapas.getPayload().getPayment().getExchange_rate());
        data.getPayment().setFeeFixed(lookupIssuerResponseNapas.getPayload().getPayment().getFee_fixed());
        data.getPayment().setFeePercentage(lookupIssuerResponseNapas.getPayload().getPayment().getFee_percentage());


        data.setAmount(lookupIssuerResponseNapas.getPayload().getAmount());

        data.setCurrency(lookupIssuerResponseNapas.getPayload().getCurrency());

        lookupIssuerResponseGW.getData().setParticipant(participant);
        data.getParticipant().setReceivingInstitutionId(lookupIssuerResponseNapas.getPayload().getParticipant().getReceiving_institution_id());
        data.getParticipant().setMerchantId(lookupIssuerResponseNapas.getPayload().getParticipant().getMerchant_id());
        data.getParticipant().setMerchantCategoryCode(lookupIssuerResponseNapas.getPayload().getParticipant().getMerchant_category_code());
        data.getParticipant().setCardAcceptorId(lookupIssuerResponseNapas.getPayload().getParticipant().getCard_acceptor_id());
        data.getParticipant().setCardAcceptorName(lookupIssuerResponseNapas.getPayload().getParticipant().getCard_acceptor_name());
        data.getParticipant().setCardAcceptorCity(CommonConstant.CARD_ACCEPTOR_CITY);
        data.getParticipant().setCardAcceptorCountry(lookupIssuerResponseNapas.getPayload().getParticipant().getCard_acceptor_country());


        lookupIssuerResponseGW.getData().setRecipient(recipient);
        lookupIssuerResponseGW.getData().getRecipient().setAddress(address);
        data.getRecipient().setFullName(lookupIssuerResponseNapas.getPayload().getRecipient().getFull_name());
        data.getRecipient().setDob(lookupIssuerResponseNapas.getPayload().getRecipient().getDate_of_birth());
        data.getRecipient().getAddress().setLine1(lookupIssuerResponseNapas.getPayload().getRecipient().getAddress().getLine1());
        data.getRecipient().getAddress().setLine2(lookupIssuerResponseNapas.getPayload().getRecipient().getAddress().getLine2());
        data.getRecipient().getAddress().setCountry(lookupIssuerResponseNapas.getPayload().getRecipient().getAddress().getCountry());
        data.getRecipient().getAddress().setPhone(lookupIssuerResponseNapas.getPayload().getRecipient().getAddress().getPhone());

        data.getOrder().setReferenceLabel(lookupIssuerResponseNapas.getPayload().getPayment().getEnd_to_end_reference());
        data.getOrder().setBillNumber(lookupIssuerResponseNapas.getOrder_info().getBill_number());
        data.getOrder().setMobileNumber(lookupIssuerResponseNapas.getOrder_info().getMobile_number());
        data.getOrder().setStoreLable(lookupIssuerResponseNapas.getOrder_info().getStore_label());
        data.getOrder().setLoyaltyNumber(lookupIssuerResponseNapas.getOrder_info().getLoyalty_number());
        data.getOrder().setCustomerLabel(lookupIssuerResponseNapas.getOrder_info().getCustomer_label());
        data.getOrder().setTerminalLabel(lookupIssuerResponseNapas.getOrder_info().getTerminal_label());
        data.getOrder().setPurposeOfTrans(lookupIssuerResponseNapas.getOrder_info().getTransaction_purpose());




        return lookupIssuerResponseGW;
    }

    @Override
    public LookupIssuerResponseGW genLookupResNAPAS(LookupIssuerRequestGW lookupIssuerRequestGW) throws JsonProcessingException {

        LookupIssuerResponseGW lookupIssuerResponseGW = new LookupIssuerResponseGW();
        HeaderGW header = lookupIssuerRequestGW.getHeaderGW();
        header.setReqResGb("RES");
        lookupIssuerResponseGW.setHeaderGW(header);



        ObjectMapper objectMapper = new ObjectMapper();
        String lookupRequestGWJson = objectMapper.writeValueAsString(lookupIssuerRequestGW);
        String lookupResponseGWJson = objectMapper.writeValueAsString(lookupIssuerResponseGW);

        log.info("----------------LUỒNG ĐI LOOKUP ISSUER -----------------");
        try {
            TransactionEntity transaction = transactionService.createTransactionLookupIssuer(lookupIssuerRequestGW ,"Lookup", PaymentConstant.STEP_RECV, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);
            transactionService.createActivity(transaction, lookupRequestGWJson, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC, PaymentConstant.ACTIVITY_STEP_RECV_FROM_CORE, PaymentConstant.STEP_STATUS_SUCCESS_CODE,PaymentConstant.REQUEST);
            // Kiểm tra Request tồn tại
            if (!ValidationHelper.isValid(lookupIssuerRequestGW)) {

                //convert bản tin lỗi gửi cho core
                header.setErrCode(ErrorDefination.ERR_004.getErrCode());
                header.setErrDesc(ErrorDefination.ERR_004.getDesc() + ": " + ValidationHelper.fieldNames.get());

                lookupIssuerResponseGW.setHeaderGW(header);

                //nếu bản tin sai định dạng thì cập nhật lại là nhận được bản tin sai với lỗi
                transactionService.updateTransStep(transaction, PaymentConstant.STEP_RECV, PaymentConstant.STEP_STATUS_ERROR_CODE, lookupIssuerResponseGW.getHeaderGW().getErrDesc());
                log.info("STEP 1: RECV_FROM_CORE: " + transaction);

                //sent to core
                try {
                    RestTemplate restTemplate = new RestTemplate();


                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    // Tạo một đối tượng HttpEntity để đại diện cho toàn bộ yêu cầu POST
                    HttpEntity<LookupIssuerResponseGW> requestDTO = new HttpEntity<>(lookupIssuerResponseGW, headers);

                    // Gọi API sử dụng phương thức POST và truyền vào body là đối tượng requestEntity
                    // InfoGW gửi bản tin chuẩn GW sang Issuer Bank
                    restTemplate.postForLocation(PaymentConstant.API_URL_SENT_TO_CORE, requestDTO);

                    //nếu gửi sang issuerBank thành công
                    transactionService.updateTransStep(transaction, "S", "00", "Success");
                    transactionService.updateErrCodeDesc(transaction, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc());
                    transactionService.updateSentDt(transaction, LocalDateTime.now());

                    transactionService.createActivity(transaction, lookupResponseGWJson, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc(), "SEND_TO_CORE", "00",PaymentConstant.REQUEST);
                    log.info("STEP 2: SEND_TO_CORE: " + transaction);

                    return lookupIssuerResponseGW;
                } catch (Exception ex){

                    //nếu gửi sang issuerBank không thành công
                    header.setErrCode("068");
                    header.setErrDesc("System timeout");
                    lookupIssuerResponseGW.setHeaderGW(header);

                    transactionService.updateTransStep(transaction, "S", "XX", "Sent error");
                    transactionService.updateErrCodeDesc(transaction, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc());
                    transactionService.updateSentDt(transaction, LocalDateTime.now());

                    transactionService.createActivity(transaction, lookupResponseGWJson, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc(), "SEND_TO_CORE", "XX",PaymentConstant.REQUEST);
                    log.info("STEP 2: SEND_TO_CORE: " + transaction);
                    log.error("Lỗi: " + ex);
                    return lookupIssuerResponseGW;
                }
            }
            else {
                //nếu bản tin đúng thì cập nhật lại là nhận được được bản tin đúng
                //updateTransStep(transaction, "R", "00", "Success");
                log.info("STEP 1: RECV_FROM_CORE: " + transaction);

                //mapping dữ liệu từ reqGW -> reqNAPAS
                LookupIssuerRequestNapas lookupIssuerRequestNapas = genMappingReqNAPAS(lookupIssuerRequestGW);

                transactionService.updateTransStep(transaction, PaymentConstant.STEP_SENT, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);
                transactionService.updateSentDt(transaction, LocalDateTime.now());

                //sent to NAPAS
                String lookupRequestNAPASJson = objectMapper.writeValueAsString(lookupIssuerRequestNapas);
                try {
                    RestTemplate restTemplate = new RestTemplate();

                    String apiUrl = LookupIssuerConstant.API_URL_RESPONSE_NAPAS;

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    // Tạo một đối tượng HttpEntity để đại diện cho toàn bộ yêu cầu POST
                    HttpEntity<LookupIssuerRequestNapas> requestDTO = new HttpEntity<>(lookupIssuerRequestNapas, headers);

                    // Gọi API sử dụng phương thức POST và truyền vào body là đối tượng requestEntity
                    //NAPAS gửi bản tin chuẩn NAPAS sang Ben Bank, sau đó nhận bản tin về theo chuẩn NAPAS
                    ResponseEntity<LookupIssuerResponseNapas> responseEntity = restTemplate.postForEntity(apiUrl, requestDTO, LookupIssuerResponseNapas.class);

                    //nếu sent thành công
                    transactionService.createActivity(transaction, lookupRequestNAPASJson, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC, PaymentConstant.ACTIVITY_STEP_SEND_TO_NAPAS, PaymentConstant.STEP_STATUS_SUCCESS_CODE,PaymentConstant.REQUEST);
                    log.info("STEP 2: SEND_TO_NAPAS: " + transaction);

                    // nếu nhận bản tin từ NAPAS với statusCode = 2xx (thành công)
                    if (responseEntity.getStatusCode().is2xxSuccessful()) {

                        LookupIssuerResponseNapas lookupIssuerResponseNapas = responseEntity.getBody();

                        transactionService.updateTransStep(transaction, PaymentConstant.STEP_RECV, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);
                        transactionService.updateReceivedDt(transaction, LocalDateTime.now());
                        transactionService.updateErrCodeDesc(transaction, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);

                        String lookupResponseNAPASJson = objectMapper.writeValueAsString(lookupIssuerResponseNapas);
                        transactionService.createActivity(transaction, lookupResponseNAPASJson, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC, PaymentConstant.ACTIVITY_STEP_RECV_FROM_NAPAS, PaymentConstant.STEP_STATUS_SUCCESS_CODE,PaymentConstant.RESPONSE);
                        log.info("STEP 3: RECV_FROM_NAPAS: " + transaction);

                        //mapping dữ liệu từ ResponseNAPAS sang ResponseInfoGW
                        lookupIssuerResponseGW = genMappingResGW(lookupIssuerResponseNapas);
                        header.setBkCd(lookupIssuerResponseGW.getHeaderGW().getBkCd());
                        lookupIssuerResponseGW.setHeaderGW(header);

                        // Send to core
                        try {

                            // Gửi sang issuerBank thành công

                            RestTemplate restTemplateCore = new RestTemplate();


                            HttpHeaders headersCore = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);

                            // Tạo một đối tượng HttpEntity để đại diện cho toàn bộ yêu cầu POST
                            HttpEntity<LookupIssuerResponseGW> requestDTOCore = new HttpEntity<>(lookupIssuerResponseGW, headers);

                            // Gọi API sử dụng phương thức POST và truyền vào body là đối tượng requestEntity

                            // InfoGW gửi bản tin chuẩn GW sang Issuer Bank
                            restTemplate.postForLocation(PaymentConstant.API_URL_SENT_TO_CORE, requestDTO);

                            //nếu gửi sang issuerBank thành công
                            transactionService.updateTransStep(transaction, "S", "00", "Success");
                            transactionService.updateErrCodeDesc(transaction, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);
                            transactionService.updateSentDt(transaction, LocalDateTime.now());

                            transactionService.createActivity(transaction, lookupResponseGWJson, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC, "SEND_TO_CORE", "00",PaymentConstant.RESPONSE);
                            transactionService.updateErrCodeDesc(transaction, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);

                            log.info("STEP 4: SEND_TO_CORE: " + transaction);

                            return lookupIssuerResponseGW;
                        } catch (Exception ex){

                            // Gửi sang issuerBank không thành công
                            header.setErrCode(ErrorDefination.ERR_068.getErrCode());
                            header.setErrDesc(ErrorDefination.ERR_068.getDesc());
                            lookupIssuerResponseGW.setHeaderGW(header);

                            transactionService.updateTransStep(transaction, "S", "XX", "Sent error");
                            transactionService.updateErrCodeDesc(transaction, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc());
                            transactionService.updateSentDt(transaction, LocalDateTime.now());

                            transactionService.createActivity(transaction, lookupResponseGWJson, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc(), "SEND_TO_CORE", "XX",PaymentConstant.RESPONSE);
                            log.info("STEP 4: SEND_TO_CORE: " + transaction);
                            log.error("Lỗi: " + ex);
                            return lookupIssuerResponseGW;
                        }
                    } else {
                        header.setErrCode(ErrorDefination.ERR_001.getErrCode());
                        header.setErrDesc(ErrorDefination.ERR_001.getDesc());
                        lookupIssuerResponseGW.setHeaderGW(header);

                        transactionService.updateTransStep(transaction, PaymentConstant.STEP_RECV, PaymentConstant.STEP_STATUS_ERROR_CODE, PaymentConstant.RECV_ERR);
                        transactionService.updateReceivedDt(transaction, LocalDateTime.now());
                        transactionService.updateErrCodeDesc(transaction, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc());

                        transactionService.createActivity(transaction, null, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc(), PaymentConstant.ACTIVITY_STEP_RECV_FROM_NAPAS, PaymentConstant.STEP_STATUS_ERROR_CODE,PaymentConstant.RESPONSE);
                        log.info("STEP 3: RECV_FROM_NAPAS: " + transaction);

                        log.error("Lỗi: Gặp lỗi khi nhận bản tin về từ NAPAS");
                        return lookupIssuerResponseGW;
                    }
                } catch (Exception ex) {

                    //nếu không gửi được sang NAPAS
                    header.setErrCode(ErrorDefination.ERR_068.getErrCode());
                    header.setErrDesc(ErrorDefination.ERR_068.getDesc());
                    lookupIssuerResponseGW.setHeaderGW(header);

                    transactionService.updateTransStep(transaction, PaymentConstant.STEP_SENT, PaymentConstant.STEP_STATUS_ERROR_CODE, PaymentConstant.STEP_STATUS_ERROR_DESC);
                    transactionService.updateSentDt(transaction, LocalDateTime.now());
                    transactionService.updateErrCodeDesc(transaction, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc());


                    transactionService.createActivity(transaction, lookupRequestNAPASJson, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc(), PaymentConstant.ACTIVITY_STEP_SEND_TO_NAPAS, PaymentConstant.STEP_STATUS_ERROR_CODE,PaymentConstant.REQUEST);
                    log.info("STEP 2: SEND_TO_NAPAS: " + transaction);

                    log.error("Lỗi: " + ex);
                    return lookupIssuerResponseGW;
                }
            }
        } catch (Exception ex) {
            log.info("STEP 1: ISSUER BANK -> InfoGW: " + lookupIssuerRequestGW);
            header.setErrCode(ErrorDefination.ERR_011.getErrCode());
            header.setErrDesc(ErrorDefination.ERR_011.getDesc());
            lookupIssuerResponseGW.setHeaderGW(header);
            log.info("STEP 2: InfoGW -> ISSUER BANK: " + lookupIssuerResponseGW);
            log.error("Lỗi: " + ex);
            return lookupIssuerResponseGW;
        }
    }

    private void sentToIssuerBank(LookupIssuerResponseGW lookupIssuerResponseGW, HeaderGW header, TransactionEntity transaction) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String lookupIssuerResponseGWJson = objectMapper.writeValueAsString(lookupIssuerResponseGW);
        try {
            RestTemplate restTemplate = new RestTemplate();

            String apiUrl = PaymentConstant.API_URL_SENT_TO_CORE;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Tạo một đối tượng HttpEntity để đại diện cho toàn bộ yêu cầu POST
            // HttpEntity<PaymentResponseGW> requestDTO = new HttpEntity<>(paymentResponseGW, headers);

            // Gọi API sử dụng phương thức POST và truyền vào body là đối tượng requestEntity
            // InfoGW gửi bản tin chuẩn GW sang Issuer Bank
            // restTemplate.postForLocation(apiUrl, requestDTO);

            // Nếu gửi sang issuerBank thành công
            transactionService.updateTransStep(transaction, PaymentConstant.STEP_SENT, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);

            // Dành cho trường hợp nhận được bản tin sai định dạng và gửi trả luôn cho core
            if (lookupIssuerResponseGW.getData() == null) {
                transactionService.updateErrCodeDesc(transaction, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc());
                transactionService.createActivity(transaction, lookupIssuerResponseGWJson, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc(), PaymentConstant.ACTIVITY_STEP_SEND_TO_CORE, PaymentConstant.STEP_STATUS_SUCCESS_CODE,PaymentConstant.RESPONSE);
            }
            // Trường hợp nhận được bản tin đúng định dạng và trả về cho core ở step 4
            else {
                transactionService.updateErrCodeDesc(transaction, lookupIssuerResponseGW.getData().getResponseCode(), lookupIssuerResponseGW.getData().getResponseDesc());
                transactionService.createActivity(transaction, lookupIssuerResponseGWJson, lookupIssuerResponseGW.getData().getResponseCode(), lookupIssuerResponseGW.getData().getResponseDesc(), PaymentConstant.ACTIVITY_STEP_SEND_TO_CORE, PaymentConstant.STEP_STATUS_SUCCESS_CODE,PaymentConstant.RESPONSE);
            }
            transactionService.updateSentDt(transaction, LocalDateTime.now());



        } catch (Exception ex) {
            //nếu gửi sang issuerBank không thành công
            header.setErrCode(ErrorDefination.ERR_068.getErrCode());
            header.setErrDesc(ErrorDefination.ERR_068.getDesc());
            lookupIssuerResponseGW.setHeaderGW(header);

            transactionService.updateTransStep(transaction, PaymentConstant.STEP_SENT, PaymentConstant.STEP_STATUS_ERROR_CODE, PaymentConstant.STEP_STATUS_ERROR_DESC);
            transactionService.updateErrCodeDesc(transaction, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc());
            transactionService.updateSentDt(transaction, LocalDateTime.now());

            transactionService.createActivity(transaction, lookupIssuerResponseGWJson, lookupIssuerResponseGW.getHeaderGW().getErrCode(), lookupIssuerResponseGW.getHeaderGW().getErrDesc(), PaymentConstant.ACTIVITY_STEP_SEND_TO_CORE, PaymentConstant.STEP_STATUS_ERROR_CODE,PaymentConstant.RESPONSE);

            log.error("Lỗi: " + ex);
        }
    }

}

