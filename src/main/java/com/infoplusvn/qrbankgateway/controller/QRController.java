package com.infoplusvn.qrbankgateway.controller;

import com.google.zxing.WriterException;
import com.infoplusvn.qrbankgateway.dto.QR.*;
import com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO;
import com.infoplusvn.qrbankgateway.dto.response.DataResponse;
import com.infoplusvn.qrbankgateway.dto.response.DeCodeQRResponse;
import com.infoplusvn.qrbankgateway.dto.response.GenerateQRResponse;
import com.infoplusvn.qrbankgateway.dto.user.EditAccountDTO;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleAdmin;
import com.infoplusvn.qrbankgateway.entity.AccountEntity;
import com.infoplusvn.qrbankgateway.entity.QRCodeEntity;
import com.infoplusvn.qrbankgateway.entity.UserEntity;
import com.infoplusvn.qrbankgateway.service.Impl.QrServiceImpl;
import com.infoplusvn.qrbankgateway.service.QrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
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
    @GetMapping(value = "/getQRByid/{id}")
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

    // Lấy QR theo tài khoản user
    @GetMapping(value = "/getQRByUsername/{username}")
    public List<QrCodeDTORoleUser> getQR(@PathVariable("username") String username){
        return qrService.getQRByUsername(username);
    }

    // Tìm kiếm QR
    @GetMapping(value = "/qrSearch")
    public List<QrCodeDTORoleUser> qrSearch(
            @RequestParam(value = "qrType", required = false) String qrType,
            @RequestParam(value = "username", required = false) String username) {

        return qrService.searchQR(qrType,username);
    }

    // Update QR
    @PutMapping(value = "/updateQR")
    public ResponseEntity<DataResponse> updateUser(@RequestBody ChangeQRNameRequest qrDTO) {
        try {
            QRCodeEntity getQRById = qrServiceImpl.findById(qrDTO.getId());

            if(getQRById == null) {
                return ResponseEntity.ok().body(new DataResponse().setStatus("500").setMessage("Không tìm thấy mã QR").setData(null));
            }
            else {
                QRCodeEntity qrCodeEntity = qrServiceImpl.updateQR(qrDTO);

                return ResponseEntity.ok().body(new DataResponse().setStatus("200").setMessage("Updated success"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse().setStatus("500").setMessage(ex.getMessage()).setData(null));
        }
    }
}
