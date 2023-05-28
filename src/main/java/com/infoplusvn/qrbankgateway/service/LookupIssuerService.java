package com.infoplusvn.qrbankgateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infoplusvn.qrbankgateway.dto.request.LookupIssuer.LookupIssuerRequestGW;
import com.infoplusvn.qrbankgateway.dto.request.LookupIssuer.LookupIssuerRequestNapas;
import com.infoplusvn.qrbankgateway.dto.response.LookupIssuer.LookupIssuerResponseGW;
import com.infoplusvn.qrbankgateway.dto.response.LookupIssuer.LookupIssuerResponseNapas;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface LookupIssuerService {

     LookupIssuerRequestNapas genMappingReqNAPAS(LookupIssuerRequestGW lookupIssuerRequestGW);

     LookupIssuerResponseGW genMappingResGW(LookupIssuerResponseNapas lookupIssuerResponseNapas);

     LookupIssuerResponseGW genLookupResNAPAS(LookupIssuerRequestGW lookupIssuerRequestGW) throws UnsupportedEncodingException, JsonProcessingException;
}
