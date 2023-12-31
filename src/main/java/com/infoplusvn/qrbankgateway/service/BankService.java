package com.infoplusvn.qrbankgateway.service;

import com.infoplusvn.qrbankgateway.entity.BankEntity;

import java.util.List;

public interface BankService {


     List<BankEntity> getAllbank();

     BankEntity getBankNameByBin(String bin);

     BankEntity getBinByShortName(String shortName);

     String getNameByBin(String bin);
}
