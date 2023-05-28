package com.infoplusvn.qrbankgateway.service.Impl;

import com.infoplusvn.qrbankgateway.entity.BankEntity;
import com.infoplusvn.qrbankgateway.repo.BankRepo;
import com.infoplusvn.qrbankgateway.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BankServiceImpl implements BankService {

    @Autowired
    BankRepo bankRepo;

    @Override
    public List<BankEntity> getAllbank() {
        return  bankRepo.findAll();
    }

    @Override
    public BankEntity getBankNameByBin(String bin) {
        return bankRepo.findByBin(bin);
    }

    @Override
    public BankEntity getBinByShortName(String shortName) {
        return bankRepo.findByShortName(shortName);
    }


}
