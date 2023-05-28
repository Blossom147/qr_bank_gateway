package com.infoplusvn.qrbankgateway.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoplusvn.qrbankgateway.constant.CommonConstant;
import com.infoplusvn.qrbankgateway.constant.ErrorDefination;
import com.infoplusvn.qrbankgateway.constant.PaymentConstant;
import com.infoplusvn.qrbankgateway.dto.common.Header.HeaderGW;
import com.infoplusvn.qrbankgateway.dto.request.Payment.PaymentRequestGW;
import com.infoplusvn.qrbankgateway.dto.request.Payment.PaymentRequestNapas;
import com.infoplusvn.qrbankgateway.dto.response.Payment.PaymentResonseNapas;
import com.infoplusvn.qrbankgateway.dto.response.Payment.PaymentResponseGW;
import com.infoplusvn.qrbankgateway.entity.TransactionEntity;
import com.infoplusvn.qrbankgateway.exception.ValidationHelper;
import com.infoplusvn.qrbankgateway.service.QRPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
public class QRPaymentServiceImpl implements QRPaymentService {

    @Autowired
    private TransactionServiceImpl transactionService;


    @Override
    public PaymentRequestNapas genMappingReqNAPAS(PaymentRequestGW request) {

        PaymentRequestNapas paymentRequestNAPAS = new PaymentRequestNapas();
        PaymentRequestNapas.Payload payload = new PaymentRequestNapas.Payload();
        PaymentRequestNapas.Payment payment = new PaymentRequestNapas.Payment();
        PaymentRequestNapas.Sender sender = new PaymentRequestNapas.Sender();
        PaymentRequestNapas.Participant participant = new PaymentRequestNapas.Participant();
        PaymentRequestNapas.Recipient recipient = new PaymentRequestNapas.Recipient();
        PaymentRequestNapas.Address senderAddress = new PaymentRequestNapas.Address();
        PaymentRequestNapas.Address recipientAddress = new PaymentRequestNapas.Address();
        PaymentRequestNapas.OrderInfo orderInfo = new PaymentRequestNapas.OrderInfo();

        //payment
        payment.setFunding_reference(request.getData().getFundingReference());
        payment.setType("QR_PUSH");
        payment.setGeneration_method("12");
        payment.setChannel(request.getData().getPayment().getChannel());
        payment.setDevice_id(request.getData().getPayment().getDeviceId());
        payment.setLocation(request.getData().getPayment().getLocation());
        payment.setTransaction_local_date_time(request.getData().getPayment().getLocationDateTime());
        payment.setInterbank_amount(request.getData().getPayment().getInterbankAmount());
        payment.setInterbank_currency(request.getData().getPayment().getInterbankCurrency());
        payment.setExchange_rate(request.getData().getPayment().getExchangeRate());
        payment.setPayment_reference(request.getHeader().getRefNo());
        payment.setTrace(request.getData().getPayment().getTrace());

        //senderAddress
        senderAddress.setLine1(request.getData().getSender().getAddress().getLine1());
        senderAddress.setLine2(request.getData().getSender().getAddress().getLine2());
        senderAddress.setCountry(request.getData().getSender().getCountry());
        senderAddress.setPhone(request.getData().getSender().getPhone());

        //sender
        sender.setFull_name(request.getData().getSender().getFullName());
        sender.setAddress(senderAddress);

        //participant
        participant.setReceiving_institution_id(request.getData().getParticipant().getReceivingInstitutionId());

        //recipientAddress
        recipientAddress.setLine1(request.getData().getRecipient().getAddress().getLine1());
        recipientAddress.setLine2(request.getData().getRecipient().getAddress().getLine2());

        //recipient
        recipient.setFull_name(request.getData().getRecipient().getFullName());
        recipient.setAddress(recipientAddress);

        //orderInfo
        orderInfo.setBill_number(request.getData().getOrder().getBillNumber());

        //payload
        payload.setPayment(payment);
        payload.setAmount(request.getData().getAmount());
        payload.setCurrency(request.getData().getCurrency());
        payload.setSender_account(request.getData().getSenderAccount());
        payload.setSender(sender);
        payload.setParticipant(participant);
        payload.setRecipient_account(request.getData().getRecipientAccount());
        payload.setRecipient(recipient);
        payload.setAdditional_message(request.getData().getAdditionMessage());
        payload.setOrder_info(orderInfo);


        paymentRequestNAPAS.setPayload(payload);

        return paymentRequestNAPAS;
    }

    @Override
    public PaymentResponseGW genMappingResGW(PaymentResonseNapas responseNAPAS) {
        PaymentResponseGW paymentResponseGW = new PaymentResponseGW();

        PaymentResponseGW.Data data = new PaymentResponseGW.Data();
        PaymentResponseGW.Payment payment = new PaymentResponseGW.Payment();

        //payment
        payment.setTrace(responseNAPAS.getPayload().getPayment().getTrace());
        payment.setExchangeRate(responseNAPAS.getPayload().getPayment().getExchange_rate());
        payment.setAuthorizationCode(responseNAPAS.getPayload().getPayment().getAuthorization_code());
        payment.setReference(responseNAPAS.getPayload().getPayment().getReference());

        //data
        data.setResponseCode("00");
        data.setResponseDesc("Success");
        data.setPayment(payment);
        data.setAmount(responseNAPAS.getPayload().getAmount());
        data.setCurrency(responseNAPAS.getPayload().getCurrency());


        paymentResponseGW.setData(data);

        return paymentResponseGW;

    }

    @Override
    public PaymentResponseGW genPaymentResGW(PaymentRequestGW paymentRequestGW) throws JsonProcessingException {
        PaymentResponseGW paymentResponseGW = new PaymentResponseGW();
        HeaderGW header = paymentRequestGW.getHeader();
        header.setReqResGb("RES");
        paymentResponseGW.setHeader(header);



        ObjectMapper objectMapper = new ObjectMapper();
        String paymentRequestGWJson = objectMapper.writeValueAsString(paymentRequestGW);
        String paymentResponseGWJson = objectMapper.writeValueAsString(paymentResponseGW);


        log.info("----------------LUỒNG ĐI PAYMENT -----------------");

        try {
            TransactionEntity transaction = transactionService.createTransactionPayment(paymentRequestGW,"Payment", PaymentConstant.STEP_RECV, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);
            transactionService.createActivity(transaction, paymentRequestGWJson, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC, PaymentConstant.ACTIVITY_STEP_RECV_FROM_CORE, PaymentConstant.STEP_STATUS_SUCCESS_CODE,PaymentConstant.REQUEST);

            // Kiểm tra Request tồn tại
            if (!ValidationHelper.isValid(paymentRequestGW)) {

                //convert bản tin lỗi gửi cho core
                header.setErrCode(ErrorDefination.ERR_004.getErrCode());
                header.setErrDesc(ErrorDefination.ERR_004.getDesc() + ": " + ValidationHelper.fieldNames.get());
                paymentResponseGW.setHeader(header);

                //nếu bản tin sai định dạng thì cập nhật lại là nhận được bản tin sai với lỗi
                transactionService.updateTransStep(transaction, PaymentConstant.STEP_RECV, PaymentConstant.STEP_STATUS_ERROR_CODE, paymentResponseGW.getHeader().getErrDesc());
                log.info("STEP 1: RECV_FROM_CORE: " + transaction);

//                sentToIssuerBank(paymentResponseGW, header, transaction);
//                log.info("STEP 2: SEND_TO_CORE: " + transaction);

                //sent to core
                try {
                    RestTemplate restTemplate = new RestTemplate();


                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    // Tạo một đối tượng HttpEntity để đại diện cho toàn bộ yêu cầu POST
                    HttpEntity<PaymentResponseGW> requestDTO = new HttpEntity<>(paymentResponseGW, headers);

                    // Gọi API sử dụng phương thức POST và truyền vào body là đối tượng requestEntity
                    // InfoGW gửi bản tin chuẩn GW sang Issuer Bank
                    restTemplate.postForLocation(PaymentConstant.API_URL_SENT_TO_CORE, requestDTO);

                    //nếu gửi sang issuerBank thành công
                    transactionService.updateTransStep(transaction, "S", "00", "Success");
                    transactionService.updateErrCodeDesc(transaction, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc());
                    transactionService.updateSentDt(transaction, LocalDateTime.now());

                    transactionService.createActivity(transaction, paymentResponseGWJson, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc(), "SEND_TO_CORE", "00",PaymentConstant.RESPONSE);
                    log.info("STEP 2: SEND_TO_CORE: " + transaction);

                    return paymentResponseGW;
                } catch (Exception ex){

                    //nếu gửi sang issuerBank không thành công
                    header.setErrCode("068");
                    header.setErrDesc("System timeout");
                    paymentResponseGW.setHeader(header);

                    transactionService.updateTransStep(transaction, "S", "XX", "Sent error");
                    transactionService.updateErrCodeDesc(transaction, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc());
                    transactionService.updateSentDt(transaction, LocalDateTime.now());

                    transactionService.createActivity(transaction, paymentResponseGWJson, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc(), "SEND_TO_CORE", "XX",PaymentConstant.RESPONSE);
                    log.info("STEP 2: SEND_TO_CORE: " + transaction);
                    log.error("Lỗi: " + ex);
                    return paymentResponseGW;
                }
            }
            else {

                //nếu bản tin đúng thì cập nhật lại là nhận được được bản tin đúng
                //updateTransStep(transaction, "R", "00", "Success");
                log.info("STEP 1: RECV_FROM_CORE: " + transaction);

                //mapping dữ liệu từ reqGW -> reqNAPAS
                PaymentRequestNapas paymentRequestNAPAS = genMappingReqNAPAS(paymentRequestGW);

                transactionService.updateTransStep(transaction, PaymentConstant.STEP_SENT, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);
                transactionService.updateSentDt(transaction, LocalDateTime.now());

                //sent to NAPAS
                String paymentRequestNAPASJson = objectMapper.writeValueAsString(paymentRequestNAPAS);
                try {
                    RestTemplate restTemplate = new RestTemplate();

                    String apiUrl = PaymentConstant.API_URL_RESPONSE_NAPAS;

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    // Tạo một đối tượng HttpEntity để đại diện cho toàn bộ yêu cầu POST
                    HttpEntity<PaymentRequestNapas> requestDTO = new HttpEntity<>(paymentRequestNAPAS, headers);

                    // Gọi API sử dụng phương thức POST và truyền vào body là đối tượng requestEntity
                    // NAPAS gửi bản tin chuẩn NAPAS sang  Bank, sau đó nhận bản tin về theo chuẩn NAPAS
                    ResponseEntity<PaymentResonseNapas> responseEntity = restTemplate.postForEntity(apiUrl, requestDTO, PaymentResonseNapas.class);

                    //nếu sent thành công
                    transactionService.createActivity(transaction, paymentRequestNAPASJson, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC, PaymentConstant.ACTIVITY_STEP_SEND_TO_NAPAS, PaymentConstant.STEP_STATUS_SUCCESS_CODE,PaymentConstant.REQUEST);
                    log.info("STEP 2: SEND_TO_NAPAS: " + transaction);

                    // nếu nhận bản tin từ NAPAS với statusCode = 2xx (thành công)
                    if (responseEntity.getStatusCode().is2xxSuccessful()) {

                        PaymentResonseNapas paymentResponseNAPAS = responseEntity.getBody();

                        transactionService.updateTransStep(transaction, PaymentConstant.STEP_RECV, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);
                        transactionService.updateReceivedDt(transaction, LocalDateTime.now());
                        transactionService.updateErrCodeDesc(transaction, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);

                        String paymentResponseNAPASJson = objectMapper.writeValueAsString(paymentResponseNAPAS);
                        transactionService.createActivity(transaction, paymentResponseNAPASJson, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC, PaymentConstant.ACTIVITY_STEP_RECV_FROM_NAPAS, PaymentConstant.STEP_STATUS_SUCCESS_CODE,PaymentConstant.RESPONSE);
                        log.info("STEP 3: RECV_FROM_NAPAS: " + transaction);

                        //mapping dữ liệu từ ResponseNAPAS sang ResponseInfoGW
                        paymentResponseGW = genMappingResGW(paymentResponseNAPAS);
                        paymentResponseGW.setHeader(header);

//                        sentToIssuerBank(paymentResponseGW, header, transaction);
//                        log.info("STEP 4: SEND_TO_CORE: " + transaction);

                        // Send to core
                        try {

                            // Gửi sang issuerBank thành công

                            RestTemplate restTemplateCore = new RestTemplate();


                            HttpHeaders headersCore = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);

                            // Tạo một đối tượng HttpEntity để đại diện cho toàn bộ yêu cầu POST
                            HttpEntity<PaymentResponseGW> requestDTOCore = new HttpEntity<>(paymentResponseGW, headers);

                            // Gọi API sử dụng phương thức POST và truyền vào body là đối tượng requestEntity

                            // InfoGW gửi bản tin chuẩn GW sang Issuer Bank
                            restTemplate.postForLocation(PaymentConstant.API_URL_SENT_TO_CORE, requestDTO);

                            //nếu gửi sang issuerBank thành công
                            transactionService.updateTransStep(transaction, "S", "00", "Success");
                            transactionService.updateErrCodeDesc(transaction, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);
                            transactionService.updateSentDt(transaction, LocalDateTime.now());

                            transactionService.createActivity(transaction, paymentResponseGWJson, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc(), "SEND_TO_CORE", "00",PaymentConstant.RESPONSE);
                            log.info("STEP 4: SEND_TO_CORE: " + transaction);

                            return paymentResponseGW;
                        } catch (Exception ex){

                            // Gửi sang issuerBank không thành công
                            header.setErrCode(ErrorDefination.ERR_068.getErrCode());
                            header.setErrDesc(ErrorDefination.ERR_068.getDesc());
                            paymentResponseGW.setHeader(header);

                            transactionService.updateTransStep(transaction, "S", "XX", "Sent error");
                            transactionService.updateErrCodeDesc(transaction, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc());
                            transactionService.updateSentDt(transaction, LocalDateTime.now());

                            transactionService.createActivity(transaction, paymentResponseGWJson, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc(), "SEND_TO_CORE", "XX",PaymentConstant.RESPONSE);
                            log.info("STEP 4: SEND_TO_CORE: " + transaction);
                            log.error("Lỗi: " + ex);
                            return paymentResponseGW;
                        }
                    } else {
                        header.setErrCode(ErrorDefination.ERR_001.getErrCode());
                        header.setErrDesc(ErrorDefination.ERR_001.getDesc());
                        paymentResponseGW.setHeader(header);

                        transactionService.updateTransStep(transaction, PaymentConstant.STEP_RECV, PaymentConstant.STEP_STATUS_ERROR_CODE, PaymentConstant.RECV_ERR);
                        transactionService.updateReceivedDt(transaction, LocalDateTime.now());
                        transactionService.updateErrCodeDesc(transaction, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc());

                        transactionService.createActivity(transaction, null, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc(), PaymentConstant.ACTIVITY_STEP_RECV_FROM_NAPAS, PaymentConstant.STEP_STATUS_ERROR_CODE,PaymentConstant.RESPONSE);
                        log.info("STEP 3: RECV_FROM_NAPAS: " + transaction);

                        log.error("Lỗi: Gặp lỗi khi nhận bản tin về từ NAPAS");
                        return paymentResponseGW;
                    }
                } catch (Exception ex) {

                    //nếu không gửi được sang NAPAS
                    header.setErrCode(ErrorDefination.ERR_068.getErrCode());
                    header.setErrDesc(ErrorDefination.ERR_068.getDesc());
                    paymentResponseGW.setHeader(header);

                    transactionService.updateTransStep(transaction, PaymentConstant.STEP_SENT, PaymentConstant.STEP_STATUS_ERROR_CODE, PaymentConstant.STEP_STATUS_ERROR_DESC);
                    transactionService.updateSentDt(transaction, LocalDateTime.now());
                    transactionService.updateErrCodeDesc(transaction, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc());


                    transactionService.createActivity(transaction, paymentRequestNAPASJson, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc(), PaymentConstant.ACTIVITY_STEP_SEND_TO_NAPAS, PaymentConstant.STEP_STATUS_ERROR_CODE,PaymentConstant.REQUEST);
                    log.info("STEP 2: SEND_TO_NAPAS: " + transaction);

                    log.error("Lỗi: " + ex);
                    return paymentResponseGW;
                }
            }
        } catch (Exception ex) {
            log.info("STEP 1: ISSUER BANK -> InfoGW: " + paymentRequestGW);
            header.setErrCode(ErrorDefination.ERR_011.getErrCode());
            header.setErrDesc(ErrorDefination.ERR_011.getDesc());
            paymentResponseGW.setHeader(header);
            log.info("STEP 2: InfoGW -> ISSUER BANK: " + paymentResponseGW);
            log.error("Lỗi: " + ex);
            return paymentResponseGW;
        }
    }

    private void sentToIssuerBank(PaymentResponseGW paymentResponseGW, HeaderGW header, TransactionEntity transaction) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String paymentResponseGWJson = objectMapper.writeValueAsString(paymentResponseGW);
        try {
            RestTemplate restTemplate = new RestTemplate();

            String apiUrl = PaymentConstant.API_URL_SENT_TO_CORE;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Tạo một đối tượng HttpEntity để đại diện cho toàn bộ yêu cầu POST
//            HttpEntity<PaymentResponseGW> requestDTO = new HttpEntity<>(paymentResponseGW, headers);

            // Gọi API sử dụng phương thức POST và truyền vào body là đối tượng requestEntity
            //InfoGW gửi bản tin chuẩn GW sang Issuer Bank
//            restTemplate.postForLocation(apiUrl, requestDTO);

            //nếu gửi sang issuerBank thành công
            transactionService.updateTransStep(transaction, PaymentConstant.STEP_SENT, PaymentConstant.STEP_STATUS_SUCCESS_CODE, PaymentConstant.STEP_STATUS_SUCCESS_DESC);

            //dành cho trường hợp nhận được bản tin sai định dạng và gửi trả luôn cho core
            if (paymentResponseGW.getData() == null) {
                transactionService.updateErrCodeDesc(transaction, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc());
                transactionService.createActivity(transaction, paymentResponseGWJson, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc(), PaymentConstant.ACTIVITY_STEP_SEND_TO_CORE, PaymentConstant.STEP_STATUS_SUCCESS_CODE,PaymentConstant.RESPONSE);
            }
            //trường hợp nhận được bản tin đúng định dạng và trả về cho core ở step 4
            else {
                transactionService.updateErrCodeDesc(transaction, paymentResponseGW.getData().getResponseCode(), paymentResponseGW.getData().getResponseDesc());
                transactionService.createActivity(transaction, paymentResponseGWJson, paymentResponseGW.getData().getResponseCode(), paymentResponseGW.getData().getResponseDesc(), PaymentConstant.ACTIVITY_STEP_SEND_TO_CORE, PaymentConstant.STEP_STATUS_SUCCESS_CODE,PaymentConstant.RESPONSE);
            }
            transactionService.updateSentDt(transaction, LocalDateTime.now());



        } catch (Exception ex) {
            //nếu gửi sang issuerBank không thành công
            header.setErrCode(ErrorDefination.ERR_068.getErrCode());
            header.setErrDesc(ErrorDefination.ERR_068.getDesc());
            paymentResponseGW.setHeader(header);

            transactionService.updateTransStep(transaction, PaymentConstant.STEP_SENT, PaymentConstant.STEP_STATUS_ERROR_CODE, PaymentConstant.STEP_STATUS_ERROR_DESC);
            transactionService.updateErrCodeDesc(transaction, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc());
            transactionService.updateSentDt(transaction, LocalDateTime.now());

            transactionService.createActivity(transaction, paymentResponseGWJson, paymentResponseGW.getHeader().getErrCode(), paymentResponseGW.getHeader().getErrDesc(), PaymentConstant.ACTIVITY_STEP_SEND_TO_CORE, PaymentConstant.STEP_STATUS_ERROR_CODE,PaymentConstant.RESPONSE);

            log.error("Lỗi: " + ex);
        }
    }



}
