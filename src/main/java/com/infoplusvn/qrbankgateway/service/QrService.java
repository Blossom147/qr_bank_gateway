package com.infoplusvn.qrbankgateway.service;

import com.google.zxing.WriterException;
import com.infoplusvn.qrbankgateway.dto.QR.DeCodeQRRequest;
import com.infoplusvn.qrbankgateway.dto.QR.GenerateAdQR;
import com.infoplusvn.qrbankgateway.dto.QR.GenerateQRRequest;
import com.infoplusvn.qrbankgateway.dto.QR.QrCodeDTORoleUser;
import com.infoplusvn.qrbankgateway.dto.response.DeCodeQRResponse;
import com.infoplusvn.qrbankgateway.dto.response.GenerateQRResponse;
import org.springframework.data.repository.query.Param;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface QrService {


     GenerateQRResponse genQRResponse(GenerateQRRequest qrRequest) throws IOException, WriterException ;

     DeCodeQRResponse parseQRString(DeCodeQRRequest deCodeQRRequest) throws UnsupportedEncodingException;

     GenerateQRResponse genAdQR(GenerateAdQR qrRequest) throws IOException, WriterException;

     Long countQRCode();

     List<QrCodeDTORoleUser> getQRByUsername(String username);

     List<QrCodeDTORoleUser> searchQR(@Param("qrType") String qrType,
                                      @Param("createdUser") String createdUser);

     void updateQrNameAndQrTypeById(@Param("qrName") String qrName, @Param("qrType") String qrType, @Param("id") Long id);


}
