package com.infoplusvn.qrbankgateway.controller;

import com.google.zxing.WriterException;
import com.infoplusvn.qrbankgateway.dto.QR.DeCodeQRRequest;
import com.infoplusvn.qrbankgateway.dto.QR.GenerateAdQR;
import com.infoplusvn.qrbankgateway.dto.QR.GenerateQRRequest;
import com.infoplusvn.qrbankgateway.dto.QR.QrCodeListDTO;
import com.infoplusvn.qrbankgateway.dto.response.DeCodeQRResponse;
import com.infoplusvn.qrbankgateway.dto.response.GenerateQRResponse;
import com.infoplusvn.qrbankgateway.entity.QRCodeEntity;
import com.infoplusvn.qrbankgateway.service.Impl.QrServiceImpl;
import com.infoplusvn.qrbankgateway.service.QrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/infogw/qr/v1")
public class QRController {

    @Autowired
    QrService qrService;

    @Autowired
    QrServiceImpl qrServiceImpl;


    // Hàm đếm
    @GetMapping(value = "/qrCodeCount")
    public Long countTransactions(){
        return qrService.countQRCode();
    }

    // Lấy QR theo id
    @GetMapping(value = "/getByid/{id}")
    public QRCodeEntity getQR(@PathVariable("id") Long id){
        return qrServiceImpl.findById(id);
    }

    // Lấy ra tất cả QR
    @GetMapping(value = "/getAllQR")
    public List<QRCodeEntity> getAllQR(){
        return qrServiceImpl.getAllQRCodes();
    }

    // Tạo mã QR
    @PostMapping(value = "/genQR")
    public GenerateQRResponse generateQRCode(@RequestBody GenerateQRRequest generateQRRequest) throws IOException, WriterException {
        return qrService.genQRResponse(generateQRRequest);
    }

    // Đọc mã QR
    @PostMapping(value = "/readQR")
    public DeCodeQRResponse readQRCode(@RequestBody DeCodeQRRequest deCodeQRRequest) throws UnsupportedEncodingException {

        return qrService.parseQRString(deCodeQRRequest);
    }

    // Tạo QR quảng cáo
    @PostMapping(value = "/genAdQR", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GenerateQRResponse genAdQR(@RequestBody GenerateAdQR generateAdQR) throws IOException, WriterException {
        return qrService.genAdQR(generateAdQR);
    }

    // Xóa mã QR theo id
    @DeleteMapping(value = "/deleteQr/{id}")
    public void deleteQr(@PathVariable Long id){
        qrServiceImpl.deleteQR(id);
    }


}
