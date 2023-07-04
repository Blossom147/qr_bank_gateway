package com.infoplusvn.qrbankgateway.dto.QR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeQRNameRequest {
    private Long Id;

    private String qrName;

    private String qrType;

    private String createdUser;
}
