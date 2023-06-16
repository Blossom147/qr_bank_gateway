package com.infoplusvn.qrbankgateway.dto.QR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrCodeListDTO {
    private Long id;

    // Tạo bởi
    private String createdUser;

    //Loại bản tin
    private String qrType;

    // Nội dung
    private String text;

    // Ngày tạo
    private String trnDt;

    // Hình ảnh
    private String qrImage;


}
