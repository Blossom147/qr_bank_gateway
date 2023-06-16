package com.infoplusvn.qrbankgateway.dto.QR;

import com.infoplusvn.qrbankgateway.dto.common.Header.HeaderGW;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GenerateAdQR {
    @Valid
    private HeaderGW headerGW;

    @Valid
    private Data data;


    @lombok.Data
    public static class Data {

        @Valid
        private QrInfo qrInfo;

        @NotNull
        private String createdUser;

        private String channel;

    }

    @lombok.Data
    public static class QrInfo {

        private String adType;

        private String text;


    }
}