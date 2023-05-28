package com.infoplusvn.qrbankgateway.service;

import com.google.zxing.WriterException;
import com.infoplusvn.qrbankgateway.dto.request.DeCodeQRRequest;
import com.infoplusvn.qrbankgateway.dto.request.GenerateAdQR;
import com.infoplusvn.qrbankgateway.dto.request.GenerateQRRequest;
import com.infoplusvn.qrbankgateway.dto.response.DeCodeQRResponse;
import com.infoplusvn.qrbankgateway.dto.response.GenerateQRResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface QrService {


     GenerateQRResponse genQRResponse(GenerateQRRequest qrRequest) throws IOException, WriterException ;

     DeCodeQRResponse parseQRString(DeCodeQRRequest deCodeQRRequest) throws UnsupportedEncodingException;

     GenerateQRResponse genAdQR(GenerateAdQR qrRequest) throws IOException, WriterException;


}
