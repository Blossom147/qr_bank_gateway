package com.infoplusvn.qrbankgateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infoplusvn.qrbankgateway.dto.request.LookupIssuer.LookupIssuerRequestGW;
import com.infoplusvn.qrbankgateway.dto.response.LookupIssuer.LookupIssuerResponseGW;
import com.infoplusvn.qrbankgateway.dto.response.LookupIssuer.LookupIssuerResponseNapas;
import com.infoplusvn.qrbankgateway.service.LookupIssuerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/infogw/qr/v1")
public class LookupIssuerController {

    @Autowired
    LookupIssuerService lookupIssuerService;


    @PostMapping(value = "/issuer/lookup")
    public LookupIssuerResponseGW ResponseNapas1(@RequestBody LookupIssuerRequestGW lookupIssuerRequestGW) throws UnsupportedEncodingException, JsonProcessingException {
        return lookupIssuerService.genLookupResNAPAS(lookupIssuerRequestGW);
    }
}

