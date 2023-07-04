package com.infoplusvn.qrbankgateway.dto.QR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrCodeDTORoleUser {
    private Long id;

    private String qrName;

    private String createdUser;

    private String qrType;

    //header
    private String trnDt;

    //qrInfoAD
    private String text;

    private String qrImage;

    private String qrThemeImage;

    private LocalDateTime updateOn;
}