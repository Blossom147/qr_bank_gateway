package com.infoplusvn.qrbankgateway.dto.QR;

import com.infoplusvn.qrbankgateway.dto.common.Header.HeaderGW;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class DeCodeQRRequest {

    @Valid
    private HeaderGW headerGW;

    @Valid
    private Data data;


    @lombok.Data
    public static class Data {

        @NotBlank
        private String qrString;

        @NotBlank
        private String channel;
    }
}
